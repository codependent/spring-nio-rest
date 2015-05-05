package com.codependent.niorest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import com.codependent.niorest.filter.CorsFilter;

@SpringBootApplication
@EnableSwagger2
public class SpringNioRestApplication {
	
	@Bean
	public Docket petApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().build();
	}
	
	@Bean
	public FilterRegistrationBean registerCorsFilter(){
		CorsFilter cf = new CorsFilter();
		List<String> urlPatterns = new ArrayList<String>();
	    urlPatterns.add("/*");
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(cf);
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setOrder(1);
		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringNioRestApplication.class, args);
	}
}
