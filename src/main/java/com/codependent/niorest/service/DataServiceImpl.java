package com.codependent.niorest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codependent.niorest.dto.Data;

@Service
public class DataServiceImpl implements DataService{

	@Override
	public List<Data> loadData() {
		
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
