package com.codependent.niorest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.rx.RxJavaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.codependent.niorest.filter.CorsFilter;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableCircuitBreaker
@Import(RxJavaAutoConfiguration.class)
@SpringBootApplication
@EnableSwagger2
public class SpringNioRestApplication {
	
	@Bean
	public Docket nioApi() {
		return new Docket(DocumentationType.SWAGGER_2)
		//.groupName("full-spring-nio-api")
        .apiInfo(apiInfo())
        .useDefaultResponseMessages(false)
        .select()
        .build();
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
	
	@Bean
	public ThreadPoolTaskExecutor executor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadGroupName("MyThreadGroup");
		executor.setThreadNamePrefix("MyThreadNamePrefix");
		executor.setCorePoolSize(5000);
		return executor;
	}
	
	@Bean
	public ServletRegistrationBean hystrixMetricsStreamServlet(){
		ServletRegistrationBean srb = new ServletRegistrationBean(new HystrixMetricsStreamServlet(), "/hystrix.stream");
		return srb;
	}
	
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring NIO Rest API")
                .description("A couple of services to test Java NIO Performance")
                .termsOfServiceUrl("http://some.io")
                .contact("codependent")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/codependent/spring-nio-rest/LICENSE")
                .version("2.0")
                .build();
    }

	public static void main(String[] args) {
		SpringApplication.run(SpringNioRestApplication.class, args);
	}
}
