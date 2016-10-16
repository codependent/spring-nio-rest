package com.codependent.niorest.controller;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncManager;

import com.codependent.niorest.dto.Data;
import com.codependent.niorest.service.DataService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.schedulers.Schedulers;

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
	
	/**
	 * Callable usa el task executor de {@link WebAsyncManager}
	 * @return
	 */
	@RequestMapping(value="/callable/data", method=RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Gets data", notes="Gets data asynchronously")
	@ApiResponses(value={@ApiResponse(code=200, message="OK")})
	public Callable<List<Data>> getDataCallable(){
		return ( () -> {return dataService.loadData();} );
	}
	
	/**
	 * Con DeferredResult tienes que proporcionar tu propio executor, se asume que la tarea 
	 * es asíncrona 
	 * @return
	 */
	@RequestMapping(value="/deferred/data", method=RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Gets data", notes="Gets data asynchronously")
	@ApiResponses(value={@ApiResponse(code=200, message="OK")})
	public DeferredResult<List<Data>> getDataDeferredResult(){
		DeferredResult<List<Data>> dr = new DeferredResult<List<Data>>();
		Thread th = new Thread(() -> {
			List<Data> data = dataService.loadData();
			dr.setResult(data);
		},"MyThread");
		th.start();
		return dr;
	}
	
	@RequestMapping(value="/observable/data", method=RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Gets data through Observable", notes="Gets data asynchronously through Observable")
	@ApiResponses(value={@ApiResponse(code=200, message="OK")})
	public DeferredResult<List<Data>> getDataObservable(){
		DeferredResult<List<Data>> dr = new DeferredResult<List<Data>>();
		Observable<List<Data>> dataObservable = dataService.loadDataObservable();
		//XXX subscribeOn es necesario, si no se haría en el hilo http
		dataObservable.subscribeOn(scheduler).subscribe( 
				dr::setResult, 
				dr::setErrorResult);
		return dr;
	}
	
	@RequestMapping(value="/observable2/data", method=RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Gets data through Observable returning Observable", notes="Gets data asynchronously through Observable returning Observable")
	@ApiResponses(value={@ApiResponse(code=200, message="OK")})
	public Single<List<Data>> getDataObservable2(){
		Observable<List<Data>> dataObservable = dataService.loadDataObservable();
		//XXX subscribeOn es necesario, si no se haría en el hilo http
		return dataObservable.toSingle().subscribeOn(scheduler);
	}
	
	@RequestMapping(value="/hystrix/data", method=RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Gets data hystrix", notes="Gets data asynchronously with hystrix")
	@ApiResponses(value={@ApiResponse(code=200, message="OK")})
	public Single<List<Data>> getDataHystrix(){
		Observable<List<Data>> observable = dataService.loadDataHystrix();
		//XXX subscribeOn es necesario, si no se haría en el hilo http
		return observable.toSingle().subscribeOn(scheduler);
	}
	
	@RequestMapping(value="/hystrix2/data", method=RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Gets data hystrix", notes="Gets data asynchronously with hystrix")
	@ApiResponses(value={@ApiResponse(code=200, message="OK")})
	public Callable<List<Data>> getDataHystrixAsync() throws InterruptedException, ExecutionException{
		Future<List<Data>> future = dataService.loadDataHystrixAsync();
		Callable<List<Data>> callable =  () -> {
			return future.get();
		};
		return callable;
	}
	
}
