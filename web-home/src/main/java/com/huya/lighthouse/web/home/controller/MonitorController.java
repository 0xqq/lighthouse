package com.huya.lighthouse.web.home.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * 
 */
@Controller
public class MonitorController {

	private static Logger logger = LoggerFactory.getLogger(MonitorController.class);

	@RequestMapping("/monitor/monitor.do")
	public void monitor(HttpServletResponse response) throws Exception {
		logger.info("/monitor/monitor.do");
		response.getWriter().print("200");
	}

}
