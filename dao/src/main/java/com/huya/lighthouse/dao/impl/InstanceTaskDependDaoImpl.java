package com.huya.lighthouse.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.huya.lighthouse.dao.InstanceTaskDependDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.instance.InstanceTaskDepend;

/**
 * tableName: instance_task_depend [InstanceTaskDepend] 的Dao操作
 * 
 */
@Repository("instanceTaskDependDao")
public class InstanceTaskDependDaoImpl extends BaseSpringJdbcDao implements InstanceTaskDependDao {

	private RowMapper<InstanceTaskDepend> entityRowMapper = new BeanPropertyRowMapper<InstanceTaskDepend>(getEntityClass());

	static final private String COLUMNS = "task_id,task_date,instance_id,pre_task_id,pre_task_date,depend_type,depend_n,is_same_agent_run,is_context_depend,remarks,is_valid,create_time,update_time,create_user,update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from instance_task_depend";

	@Override
	public Class<InstanceTaskDepend> getEntityClass() {
		return InstanceTaskDepend.class;
	}

	public RowMapper<InstanceTaskDepend> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(InstanceTaskDepend entity) {
		update(entity);
	}
	
	@Override
	public int[] batchInsert(List<InstanceTaskDepend> entityList) {
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(entityList.toArray());
		return getNamedParameterJdbcTemplate().batchUpdate(getInsertSql(), batchArgs);
	}

	public int update(InstanceTaskDepend entity) {
		String sql = getInsertSql();
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	private String getInsertSql() {
		String sql = "insert into instance_task_depend (task_id,task_date,instance_id,pre_task_id,pre_task_date,depend_type,depend_n,is_same_agent_run,is_context_depend,remarks,is_valid,create_time,update_time,create_user,update_user) values "
				+ " (:taskId,:taskDate,:instanceId,:preTaskId,:preTaskDate,:dependType,:dependN,:isSameAgentRun,:isContextDepend,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser) "
				+ "ON DUPLICATE KEY UPDATE "
				+ "depend_type=:dependType,depend_n=:dependN,is_same_agent_run=:isSameAgentRun,is_context_depend=:isContextDepend,remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser";
		return sql;
	}

	public int deleteById(int taskId, Date taskDate, String instanceId, int preTaskId, Date preTaskDate) {
		String sql = "delete from instance_task_depend where  task_id = ? and task_date = ? and instance_id = ? and pre_task_id = ? and pre_task_date = ? ";
		return getSimpleJdbcTemplate().update(sql, taskId, taskDate, instanceId, preTaskId, preTaskDate);
	}

	public InstanceTaskDepend getById(int taskId, Date taskDate, String instanceId, int preTaskId, Date preTaskDate) {
		String sql = SELECT_FROM + " where  task_id = ? and task_date = ? and instance_id = ? and pre_task_id = ? and pre_task_date = ? ";
		return (InstanceTaskDepend) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, taskDate, instanceId, preTaskId, preTaskDate));
	}

	@Override
	public int updateInvalid(Integer taskId, Date taskDate) {
		String sql = "update instance_task_depend set is_valid = 0 where task_id = ? and task_date = ?";
		return getSimpleJdbcTemplate().update(sql, taskId, taskDate);
	}

	@Override
	public List<InstanceTaskDepend> getByPostId(int taskId, Date taskDate, String instanceId) {
		String sql = SELECT_FROM + " where  task_id = ? and task_date = ? and instance_id = ?";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, taskDate, instanceId);
	}

	@Override
	public List<InstanceTaskDepend> getByPreId(int preTaskId, Date preTaskDate, String instanceId) {
		String sql = SELECT_FROM + " where pre_task_id = ? and pre_task_date = ? and instance_id = ?";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), instanceId, preTaskId, preTaskDate);
	}


}
