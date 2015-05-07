package com.codependent.niorest;

import java.util.List;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.codependent.niorest.SpringNioRestApplication;
import com.codependent.niorest.dto.LoadTestInfo;


@SpringApplicationConfiguration(classes = SpringNioRestApplication.class)
@WebIntegrationTest("server.port:9090")
public class SpringNioRestApplicationLoadTest extends AbstractTestNGSpringContextTests{

	private static final String BASE_URL = "http://localhost:9090";
	
	RestTemplate rt = new RestTemplate();
	
	LoadTestInfo syncTest = new LoadTestInfo();
	LoadTestInfo asyncTest = new LoadTestInfo();
	
	@BeforeSuite
	public void beforeSuite(){
	}
	
	@Test(invocationCount=200, threadPoolSize=10)
	public void syncLoadTest() {
		doRequest(BASE_URL+"/sync/data", syncTest);
	}
	
	@Test(invocationCount=200, threadPoolSize=10)
		public void asyncLoadTest() {
		doRequest(BASE_URL+"/async/data", asyncTest);
	}
	
	@SuppressWarnings("rawtypes")
	private void doRequest(String url, LoadTestInfo info){
		final Bool finished = new Bool(false);
		long t0 = System.currentTimeMillis();
		new Thread( 
			()->{
				ResponseEntity<List> response = rt.getForEntity(url, List.class);
				if(response.getStatusCode()==HttpStatus.INTERNAL_SERVER_ERROR){
					info.saveErrorRequest();
				}else{
					long t1 = System.currentTimeMillis();
					info.saveRequest((t1-t0));
				}
				finished.setValue(true);
			} 
		).start();
		while(!finished.isValue()){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@AfterSuite
	public void afterSuite(){
		if(syncTest.getNumberOfRequests()>0){
			System.out.printf("[SYNC] Result: #ofRequests %s - Average time %s mseg - minTime %s mseg - maxTime %s mseg - errors %s%n", 
				syncTest.getNumberOfRequests(), syncTest.getAverageTime(), syncTest.getMinTime(), syncTest.getMaxTime(), syncTest.getErrors());
		}
		if(asyncTest.getNumberOfRequests()>0){
			System.out.printf("[ASYNC] Result: #ofRequests %s - Average time %s mseg - minTime %s mseg - maxTime %s mseg - errors %s%n", 
				asyncTest.getNumberOfRequests(), asyncTest.getAverageTime(), asyncTest.getMinTime(), asyncTest.getMaxTime(), asyncTest.getErrors());
		}
	}
	
	private class Bool{
		private boolean value;
		public Bool(boolean value){
			this.value=value;
		}
		public boolean isValue() {
			return value;
		}
		public void setValue(boolean value) {
			this.value = value;
		}
	}

}
