package com.josesa.niorest.dto;

import java.util.ArrayList;
import java.util.List;

public class LoadTestInfo {

	private long minTime = 0;
	private long maxTime = 0;
	private int errors = 0;
	private List<Long> requests = new ArrayList<Long>();
	
	public void saveRequest(long time){
		requests.add(time);
		if(minTime>time){
			minTime = time;
		}
		if(maxTime<time){
			maxTime = time;
		}
	}
	
	public void saveErrorRequest(){
		errors++;
	}
	
	public int getAverageTime() {
		int total = 0;
		for (Long reqTime : requests) {
			total+=reqTime;
		}
		return total/requests.size();
	}
	
	public int getNumberOfRequests() {
		return requests.size();
	}
	
	public long getMaxTime() {
		return maxTime;
	}
	
	public long getMinTime() {
		return minTime;
	}
	
	public int getErrors() {
		return errors;
	}
}