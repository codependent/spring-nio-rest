package com.codependent.niorest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import rx.Observable;

import com.codependent.niorest.dto.Data;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class DataServiceImpl implements DataService{

	@Override
	public List<Data> loadData() {
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return generateData();
	}

	@Override
	public Observable<List<Data>> loadDataObservable() {
		return Observable.just(generateData()).delay(400, TimeUnit.MILLISECONDS);
	}
	
	@HystrixCommand
	@Override
	public Observable<List<Data>> loadDataHystrix() {
		return Observable.just(generateData());
	}
	
	private List<Data> generateData(){
		List<Data> dataList = new ArrayList<Data>();
		for (int i = 0; i < 20; i++) {
			Data data = new Data("key"+i, "value"+i);
			dataList.add(data);
		}
		return dataList;
	}

}
