package com.codependent.niorest.controller;

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import com.codependent.niorest.dto.Data;
import com.codependent.niorest.service.DataService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Asynchronous data controller
 * @author JINGA4X
 */
@RestController
@Api(value="", description="Synchronous data controller")
public class AsyncRestController {

	@Autowired
	private DataService dataService;
	
	private Scheduler scheduler;
	
	@Autowired
	private TaskExecutor executor;
	
	@PostConstruct
	protected void initializeScheduler(){
		scheduler = Schedulers.from(executor);
	}
	
	@RequestMapping(value="/async/data", method=RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Gets data", notes="Gets data asynchronously")
	@ApiResponses(value={@ApiResponse(code=200, message="OK")})
	public Callable<List<Data>> getData(){
		return ( () -> {return dataService.loadData();} );
	}
	
	@RequestMapping(value="/observable/data", method=RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Gets data through Observable", notes="Gets data asynchronously through Observable")
	@ApiResponses(value={@ApiResponse(code=200, message="OK")})
	public DeferredResult<List<Data>> getDataObservable(){
		DeferredResult<List<Data>> dr = new DeferredResult<List<Data>>();
		Observable<List<Data>> dataObservable = dataService.loadDataObservable();
		dataObservable.subscribeOn(scheduler).subscribe( dr::setResult, dr::setErrorResult);
		return dr;
	}
	
	@RequestMapping(value="/observable2/data", method=RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Gets data through Observable returning Observable", notes="Gets data asynchronously through Observable returning Observable")
	@ApiResponses(value={@ApiResponse(code=200, message="OK")})
	public Observable<List<Data>> getDataObservable2(){
		Observable<List<Data>> dataObservable = dataService.loadDataObservable();
		return dataObservable.subscribeOn(scheduler);
	}
	
}
