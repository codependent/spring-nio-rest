package com.codependent.niorest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import rx.Observable;

import com.codependent.niorest.dto.Data;

@Service
public class DataServiceImpl implements DataService{

	@Override
	public List<Data> loadData() {
		return generateData();
	}

	@Override
	public Observable<List<Data>> loadDataObservable() {
		return Observable.create( s -> {
			List<Data> dataList = generateData();
			s.onNext(dataList);
			s.onCompleted();
		});
	}
	
	private List<Data> generateData(){
		List<Data> dataList = new ArrayList<Data>();
		for (int i = 0; i < 20; i++) {
			Data data = new Data("key"+i, "value"+i);
			dataList.add(data);
		}
		//Processing time simulation
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return dataList;
	}

}
