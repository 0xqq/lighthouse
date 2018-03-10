package com.huya.lighthouse.server.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huya.lighthouse.server.executor.LeaderSelector;

/**
 * 
 * 
 */
@Controller
public class LeaderController {

	protected static Logger logger = LoggerFactory.getLogger(LeaderController.class);

	@RequestMapping("/isLeader.do")
	public void isLeader(HttpServletResponse response) throws Exception {
		response.getWriter().print("" + LeaderSelector.isLeader);
	}

}
