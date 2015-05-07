package com.codependent.niorest;

import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringApplicationConfiguration(classes = SpringNioRestApplication.class)
@WebIntegrationTest("server.port:9090")
public class SpringNioRestApplicationTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@BeforeClass
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
    public void getSyncData() throws Exception{
        mockMvc.perform(get("/sync/data").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$",hasSize(20)));
    }
	
	@Test
    public void getAsyncData() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/async/data").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(request().asyncStarted())
            .andReturn();
        
        mvcResult.getAsyncResult();
        
        mockMvc.perform(asyncDispatch(mvcResult))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType("application/json;charset=UTF-8"))
        	.andExpect(jsonPath("$",hasSize(20)));
    }
}
