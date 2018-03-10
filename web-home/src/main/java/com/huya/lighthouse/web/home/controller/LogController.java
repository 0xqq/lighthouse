package com.huya.lighthouse.web.home.controller;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huya.lighthouse.service.BOInstanceTaskService;
import com.huya.lighthouse.util.DateUtils2;

/**
 * 
 * 
 */
@Controller
public class LogController {

	protected static Logger logger = LoggerFactory.getLogger(LogController.class);

	@Autowired
	private BOInstanceTaskService bOInstanceTaskService;

	@RequestMapping("/getLog.do")
	public void getLog(HttpServletResponse response, Integer taskId, String taskDate, String instanceId) throws Exception {
		Date date = DateUtils.parseDate(taskDate, DateUtils2.parsePatterns);
		response.setContentType("text/plain;charset=UTF-8");
		bOInstanceTaskService.getLog(response.getOutputStream(), taskId, date, instanceId);
	}

	@RequestMapping("/getLastLog.do")
	public void getLastLog(HttpServletResponse response, Integer taskId, String taskDate) throws Exception {
		Date date = DateUtils.parseDate(taskDate, DateUtils2.parsePatterns);
		response.setContentType("text/plain;charset=UTF-8");
		bOInstanceTaskService.getLastLog(response.getOutputStream(), taskId, date);
	}
}
