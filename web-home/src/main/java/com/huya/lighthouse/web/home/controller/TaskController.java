package com.huya.lighthouse.web.home.controller;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huya.lighthouse.service.InstanceTaskService;
import com.huya.lighthouse.util.DateUtils2;

/**
 * 
 * 
 */
@Controller
public class TaskController {

	protected static Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private InstanceTaskService instanceTaskService;

	@RequestMapping("/getLastStatus.do")
	public void getLastStatus(HttpServletResponse response, Integer taskId, String taskDate) throws Exception {
		Date date = DateUtils.parseDate(taskDate, DateUtils2.parsePatterns);
		response.setContentType("text/plain;charset=UTF-8");
		String status = instanceTaskService.getLastStatus(taskId, date);
		response.getWriter().print(status);
	}
}
