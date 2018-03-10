package com.huya.lighthouse.dao;

import java.util.Date;
import java.util.List;

import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.query.DefTaskQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * tableName: def_task [DefTask] 的Dao操作
 * 
 */
public interface DefTaskDao {

	public void insert(DefTask entity);

	public int update(DefTask entity);

	public int deleteById(int taskId);

	public DefTask getById(int taskId);

	public List<DefTask> getByDependPreId(Integer preTaskId, Integer isValid);

	public List<DefTask> getByDependPostId(Integer taskId, Integer isValid);

	public List<DefTask> getAllValidCronTask();

	public List<DefTask> getForInitByCatalogId(Integer catalogId);

	public void updateIsValidById(int taskId, int isValid);

	public List<DefTask> getByMonitorCronExp(String cronExp);

	public List<DefTask> getByOfflineTime(Date offlineTime);

	public Page<DefTask> findPage(DefTaskQuery query);

	public List<DefTask> getOfflineValid();

}
