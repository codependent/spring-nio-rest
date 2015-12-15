package com.codependent.niorest.service;

import java.util.List;

import rx.Observable;

import com.codependent.niorest.dto.Data;

public interface DataService {

	List<Data> loadData();
	Observable<List<Data>> loadDataObservable();
}