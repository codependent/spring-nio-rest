package com.codependent.niorest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class CorsFilter implements Filter{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${cors.allowed.origins}")
	private String corsAllowedOrigins;

	public CorsFilter() {}

	public void init(FilterConfig fConfig) throws ServletException {
		WebApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(fConfig.getServletContext());
		ac.getAutowireCapableBeanFactory().autowireBean(this);
	}
    
	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest hRequest = (HttpServletRequest)request;
		HttpServletResponse hResponse = (HttpServletResponse)response;
		
		String remoteHost = hRequest.getHeader("Origin");
		logger.debug("### rest request from remote host[{}]",remoteHost);

		if(remoteHost!=null && (corsAllowedOrigins.contains(remoteHost) || corsAllowedOrigins.equals("*"))){
			logger.debug("### adding Access Control Headers for {} ###",remoteHost);
			hResponse.setHeader("Access-Control-Allow-Origin", remoteHost);
			//hResponse.setHeader("Access-Control-Allow-Credentials", "true");
			hResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,Accept");
		}
		chain.doFilter(request, response);
	}
	
}
