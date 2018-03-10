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

import com.huya.lighthouse.dao.DefMonitorBeginDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.def.DefMonitorBegin;

/**
 * tableName: def_monitor_begin [DefMonitorBegin] 的Dao操作
 * 
 */
@Repository("defMonitorBeginDao")
public class DefMonitorBeginDaoImpl extends BaseSpringJdbcDao implements DefMonitorBeginDao {

	private RowMapper<DefMonitorBegin> entityRowMapper = new BeanPropertyRowMapper<DefMonitorBegin>(getEntityClass());

	static final private String COLUMNS = "cron_exp,move_month,move_day,move_hour,move_minute,task_id,remarks,is_valid,create_time,update_time,create_user,update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from def_monitor_begin";

	@Override
	public Class<DefMonitorBegin> getEntityClass() {
		return DefMonitorBegin.class;
	}

	public RowMapper<DefMonitorBegin> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(DefMonitorBegin entity) {
		String sql = getInsertSql();
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	private String getInsertSql() {
		String sql = "insert into def_monitor_begin (cron_exp,move_month,move_day,move_hour,move_minute,task_id,remarks,is_valid,create_time,update_time,create_user,update_user) values "
				+ " (:cronExp,:moveMonth,:moveDay,:moveHour,:moveMinute,:taskId,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser)";
		return sql;
	}
	
	@Override
	public int[] batchInsert(List<DefMonitorBegin> entityList) {
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(entityList.toArray());
		return getNamedParameterJdbcTemplate().batchUpdate(getInsertSql(), batchArgs);
	}

	public int update(DefMonitorBegin entity) {
		String sql = "update def_monitor_begin set "
				+ " move_month=:moveMonth,move_day=:moveDay,move_hour=:moveHour,move_minute=:moveMinute,remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser "
				+ " where cron_exp = :cronExp and task_id = :taskId ";
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(String cronExp, int taskId) {
		String sql = "delete from def_monitor_begin where cron_exp = ? and task_id = ? ";
		return getSimpleJdbcTemplate().update(sql, cronExp, taskId);
	}

	public DefMonitorBegin getById(String cronExp, int taskId) {
		String sql = SELECT_FROM + " where  cron_exp = ? and task_id = ? ";
		return (DefMonitorBegin) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), cronExp, taskId));
	}

	@Override
	public List<String> getAllValidCronExp() {
		String sql = "select distinct cron_exp from def_monitor_begin where is_valid = 1";
		return getSimpleJdbcTemplate().getJdbcOperations().queryForList(sql, String.class);
	}

	@Override
	public int deleteById(Integer taskId) {
		String sql = "delete from def_monitor_begin where task_id = ? ";
		return getSimpleJdbcTemplate().update(sql, taskId);
	}

	@Override
	public List<DefMonitorBegin> getByTaskId(int taskId, int isValid) {
		String sql = SELECT_FROM + " where task_id = ? and is_valid = ?";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, isValid);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		String sql = "update def_monitor_begin set is_valid=?, update_time=? where task_id=?";
		getSimpleJdbcTemplate().update(sql, isValid, new Date(), taskId);
	}

	@Override
	public List<DefMonitorBegin> getByCronExp(String cronExp) {
		String sql = SELECT_FROM + " where cron_exp = ? and is_valid = 1";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), cronExp);
	}

}
