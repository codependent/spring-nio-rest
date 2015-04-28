package com.josesa.niorest.controller;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josesa.niorest.dto.Data;
import com.josesa.niorest.service.DataService;

@RestController
public class AsyncRestController {

	@Autowired
	private DataService dataService;
	
	@RequestMapping("/async/data")
	public Callable<List<Data>> getData(){
		return ( () -> {return dataService.loadData();} );
	}
	
}
