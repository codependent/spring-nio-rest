package com.codependent.niorest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import com.codependent.niorest.dto.Data;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

import rx.Observable;

@Service
public class DataServiceImpl implements DataService{

	@Override
	public List<Data> loadData() {
		return generateData(false);
	}

	@Override
	public Observable<List<Data>> loadDataObservable() {
		return Observable.fromCallable(() -> generateData(false));
	}
	
	@HystrixCommand(observableExecutionMode=ObservableExecutionMode.LAZY, fallbackMethod="loadDataHystrixFallback")
	@Override
	public Observable<List<Data>> loadDataHystrix() {
		double random = Math.random();
		return Observable.fromCallable(() -> generateData( random < 0.9 ? false : true ));
	}
	
	@HystrixCommand(observableExecutionMode=ObservableExecutionMode.LAZY, fallbackMethod="loadDataHystrixFallback")
	@Override
	public Future<List<Data>> loadDataHystrixAsync() {
		double random = Math.random();
		return new AsyncResult<List<Data>>() {
            @Override
            public List<Data> invoke() {
                return generateData( random < 0.9 ? false : true );
            }
        };
	}
	
	@SuppressWarnings("unused")
	private List<Data> loadDataHystrixFallback(){
		return new ArrayList<>();
	}
	
	private List<Data> generateData(boolean fail){
		if(fail){
			throw new RuntimeException("Counldn't generate data");
		}
		List<Data> dataList = new ArrayList<Data>();
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 20; i++) {
			Data data = new Data("key"+i, "value"+i);
			dataList.add(data);
		}
		return dataList;
	}

}
