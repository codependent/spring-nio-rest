package com.codependent.niorest.controller;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	
	/**
	 * Returns {@link List<Data>}
	 */
	@RequestMapping(value="/async/data", method=RequestMethod.GET, produces="application/json")
	@ApiOperation(value = "Gets data", notes="Gets data asynchronously")
	@ApiResponses(value={@ApiResponse(code=200, message="OK")})
	public Callable<List<Data>> getData(){
		return ( () -> {return dataService.loadData();} );
	}
	
}
