package com.huya.lighthouse.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;

import com.huya.lighthouse.dao.InstanceTaskLogDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.instance.InstanceTaskLog;

/**
 * tableName: instance_task_log [InstanceTaskLog] 的Dao操作
 */
@Repository("instanceTaskLogDao")
public class InstanceTaskLogDaoImpl extends BaseSpringJdbcDao implements InstanceTaskLogDao {

	private RowMapper<InstanceTaskLog> entityRowMapper = new BeanPropertyRowMapper<InstanceTaskLog>(getEntityClass());

	static final private String COLUMNS = "task_id,task_date,instance_id,log_id,agent_host_run,log_path,content";
	static final private String SELECT_FROM = "select " + COLUMNS + " from instance_task_log";

	@Override
	public Class<InstanceTaskLog> getEntityClass() {
		return InstanceTaskLog.class;
	}

	@Override
	public String getIdentifierPropertyName() {
		return "taskId";
	}

	public RowMapper<InstanceTaskLog> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(InstanceTaskLog entity) {
		update(entity);
	}

	public int update(InstanceTaskLog entity) {
		StringBuilder sqlBuilder = new StringBuilder("insert into instance_task_log ");
		sqlBuilder.append(" (task_id,task_date,instance_id,log_id,agent_host_run,log_path,content) ");
		sqlBuilder.append(" values ");
		sqlBuilder.append(" (:taskId,:taskDate,:instanceId,:logId,:agentHostRun,:logPath,:content) ");
		sqlBuilder.append(" ON DUPLICATE KEY UPDATE ");
		sqlBuilder.append(" agent_host_run=:agentHostRun, log_path=:logPath, content=:content");
		return getNamedParameterJdbcTemplate().update(sqlBuilder.toString(), new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(int taskId, Date taskDate, String instanceId, long logId) {
		String sql = "delete from instance_task_log where  task_id = ? and task_date = ? and instance_id = ? and log_id = ? ";
		return getSimpleJdbcTemplate().update(sql, taskId, taskDate, instanceId, logId);
	}

	public InstanceTaskLog getById(int taskId, Date taskDate, String instanceId, long logId) {
		String sql = SELECT_FROM + " where  task_id = ? and task_date = ? and instance_id = ? and log_id = ? ";
		return (InstanceTaskLog) DataAccessUtils.singleResult(getSlaveSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, taskDate, instanceId, logId));
	}
	
	public List<InstanceTaskLog> getInstanceTaskLogs(int taskId, Date taskDate, String instanceId) {
		String sql = SELECT_FROM + " where  task_id = ? and task_date = ? and instance_id = ? order by log_id asc";
		return getSlaveSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, taskDate, instanceId);
	}
}
