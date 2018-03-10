package com.huya.lighthouse.web.home.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckUrlFilter implements Filter {

	protected static Logger logger = (Logger) LoggerFactory.getLogger(CheckUrlFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest sRequest, ServletResponse sResponse, FilterChain filterChain) throws IOException, ServletException {
		filterChain.doFilter(sRequest, sResponse);
	}

	@Override
	public void destroy() {

	}

}
