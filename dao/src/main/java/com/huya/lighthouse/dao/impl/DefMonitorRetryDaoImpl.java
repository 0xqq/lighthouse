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

import com.huya.lighthouse.dao.DefMonitorRetryDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.def.DefMonitorRetry;

/**
 * tableName: def_monitor_retry [DefMonitorRetry] 的Dao操作
 * 
 */
@Repository("defMonitorRetryDao")
public class DefMonitorRetryDaoImpl extends BaseSpringJdbcDao implements DefMonitorRetryDao {

	private RowMapper<DefMonitorRetry> entityRowMapper = new BeanPropertyRowMapper<DefMonitorRetry>(getEntityClass());

	static final private String COLUMNS = "task_id,fail_retry_n,remarks,is_valid,create_time,update_time,create_user,update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from def_monitor_retry";

	@Override
	public Class<DefMonitorRetry> getEntityClass() {
		return DefMonitorRetry.class;
	}

	public RowMapper<DefMonitorRetry> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(DefMonitorRetry entity) {
		String sql = getInsertSql();
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	private String getInsertSql() {
		String sql = "insert into def_monitor_retry (task_id,fail_retry_n,remarks,is_valid,create_time,update_time,create_user,update_user) values "
				+ " (:taskId,:failRetryN,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser)";
		return sql;
	}

	public int update(DefMonitorRetry entity) {
		String sql = "update def_monitor_retry set "
				+ " remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser "
				+ " where task_id = :taskId and fail_retry_n = :failRetryN ";
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(int taskId, int failRetryN) {
		String sql = "delete from def_monitor_retry where task_id = ? and fail_retry_n = ? ";
		return getSimpleJdbcTemplate().update(sql, taskId, failRetryN);
	}

	public DefMonitorRetry getById(int taskId, int failRetryN) {
		String sql = SELECT_FROM + " where  task_id = ? and fail_retry_n = ? ";
		return (DefMonitorRetry) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, failRetryN));
	}

	@Override
	public List<DefMonitorRetry> getByTaskId(Integer taskId, int isValid) {
		String sql = SELECT_FROM + " where  task_id = ?  and is_valid = ?";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, isValid);
	}

	@Override
	public int[] batchInsert(List<DefMonitorRetry> entityList) {
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(entityList.toArray());
		return getNamedParameterJdbcTemplate().batchUpdate(getInsertSql(), batchArgs);
	}

	@Override
	public int deleteById(Integer taskId) {
		String sql = "delete from def_monitor_retry where task_id = ?";
		return getSimpleJdbcTemplate().update(sql, taskId);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		String sql = "update def_monitor_retry set is_valid=?, update_time=? where task_id=?";
		getSimpleJdbcTemplate().update(sql, isValid, new Date(), taskId);
	}

}
