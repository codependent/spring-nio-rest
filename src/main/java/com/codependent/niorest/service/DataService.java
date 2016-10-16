package com.codependent.niorest.service;

import java.util.List;
import java.util.concurrent.Future;

import com.codependent.niorest.dto.Data;

import rx.Observable;

public interface DataService {

	List<Data> loadData();
	Observable<List<Data>> loadDataHystrix();
	Future<List<Data>> loadDataHystrixAsync();
	Observable<List<Data>> loadDataObservable();
	
	
}