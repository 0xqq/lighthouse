package com.huya.lighthouse.server.controller;

import com.huya.lighthouse.server.executor.InstanceTaskExecutor;
import com.huya.lighthouse.server.factory.TaskRunningFactory;
import com.huya.lighthouse.server.model.InstanceTaskKeyDetail;
import com.huya.lighthouse.server.model.TaskRunInfo;
import com.huya.lighthouse.util.DateUtils2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

	@RequestMapping("/printRunInfo.do")
	public void printRunInfo(HttpServletResponse response) throws Exception {
		logger.info("/printRunInfo.do");
		response.setContentType("text/plain;charset=UTF-8");
		StringBuilder strb = new StringBuilder();
		ConcurrentHashMap<Integer, TaskRunInfo> taskRunInfoMap = TaskRunningFactory.cloneMap();
		for (Map.Entry<Integer, TaskRunInfo> entry : taskRunInfoMap.entrySet()) {
			int taskId = entry.getKey();
			TaskRunInfo taskRunInfoTmp = entry.getValue();
			strb.append(taskId).append("\n");
			strb.append("    Run: ").append(taskRunInfoTmp.getRunningTaskMap()).append("\n");
			strb.append("    Wait: ");
			int i = 0;
			InstanceTaskExecutor tmp = taskRunInfoTmp.getWaitingTaskQueue().poll();
			while (tmp != null) {
				if (i>0) {
					strb.append(", ");
				}
				i++;
				InstanceTaskKeyDetail instanceTaskKeyDetail = tmp.getInstanceTaskKeyDetail();
				strb.append(DateUtils2.dateStr(instanceTaskKeyDetail.getTaskDate())).append("=").append(instanceTaskKeyDetail.getInstanceId());
				tmp = taskRunInfoTmp.getWaitingTaskQueue().poll();
			}
            strb.append("\n\n");
		}
		taskRunInfoMap = null;
		response.getWriter().print(strb.toString());
	}
}
