package com.josesa.niorest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josesa.niorest.dto.Data;
import com.josesa.niorest.service.DataService;

@RestController
public class SyncRestController {

	@Autowired
	private DataService dataService;
	
	@RequestMapping("/sync/data")
	public List<Data> getData(){
		return dataService.loadData();
	}
	
}
