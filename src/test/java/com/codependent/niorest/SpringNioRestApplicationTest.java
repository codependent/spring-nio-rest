package com.codependent.niorest;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SpringBootTest(classes = SpringNioRestApplication.class)
public class SpringNioRestApplicationTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@BeforeClass
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
    public void getSyncData() throws Exception{
        mockMvc.perform(get("/sync/data").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$",hasSize(20)));
    }
	
	@Test
    public void getAsyncCallableData() throws Exception{
        performAsync("/callable/data");
	}
	
	@Test
    public void getAsyncDeferredData() throws Exception{
        performAsync("/deferred/data");
	}
	
	@Test
    public void getAsyncObservableData() throws Exception{
        performAsync("/observable/data");
	}
	
	@Test
    public void getAsyncObservableDeferredData() throws Exception{
        performAsync("/observable-deferred/data");
	}
	
	@Test
    public void getAsyncHystrixData() throws Exception{
        performAsync("/hystrix/data");
	}
	
	@Test
    public void getAsyncHystrixCallableData() throws Exception{
        performAsync("/hystrix-callable/data");
	}
	
	private void performAsync(String url) throws Exception{
		MvcResult mvcResult = mockMvc.perform(get(url).accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
	            .andExpect(request().asyncStarted())
	            .andReturn();
	        
	        mvcResult.getAsyncResult();
	        
	        mockMvc.perform(asyncDispatch(mvcResult))
	        	.andExpect(status().isOk())
	        	.andExpect(content().contentType("application/json;charset=UTF-8"))
	        	.andExpect(jsonPath("$",hasSize(20)));
	}
}
