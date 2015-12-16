package com.codependent.niorest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import rx.Observable;

import com.codependent.niorest.dto.Data;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;

@Service
public class DataServiceImpl implements DataService{

	@Override
	public List<Data> loadData() {
		return generateData();
	}

	@Override
	public Observable<List<Data>> loadDataObservable() {
		return Observable.just(generateData());
		/*return Observable.create( s -> {
			List<Data> dataList = generateData();
			s.onNext(dataList);
			s.onCompleted();
		});*/
	}
	
	@HystrixCommand
	@Override
	public Observable<List<Data>> loadDataHystrix() {
		return new ObservableResult<List<Data>>() {
			@Override
			public List<Data> invoke() {
				List<Data> dataList = generateData();
				return dataList;
			}
		};
	}
	
	private List<Data> generateData(){
		List<Data> dataList = new ArrayList<Data>();
		for (int i = 0; i < 20; i++) {
			Data data = new Data("key"+i, "value"+i);
			dataList.add(data);
		}
		//Processing time simulation
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return dataList;
	}

}
