package com.huya.lighthouse.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.huya.lighthouse.model.po.instance.InstanceTaskLog;


/**
 * [InstanceTaskLog] 的业务操作
 * 
 */
public interface InstanceTaskLogService {

	/** 
	 * 创建InstanceTaskLog
	 **/
	public InstanceTaskLog create(InstanceTaskLog instanceTaskLog);
	
	/** 
	 * 更新InstanceTaskLog
	 **/	
    public InstanceTaskLog update(InstanceTaskLog instanceTaskLog);
    
	/** 
	 * 删除InstanceTaskLog
	 **/
    public void removeById(int taskId, Date taskDate, String instanceId, long logId);
    
	/** 
	 * 根据ID得到InstanceTaskLog
	 **/    
    public InstanceTaskLog getById(int taskId, Date taskDate, String instanceId, long logId);
    
   
    public List<InstanceTaskLog> getInstanceTaskLogs(int taskId, Date taskDate, String instanceId);
    
    public void getLog(OutputStream outputStream, int taskId, Date taskDate, String instanceId) throws Exception;
}
