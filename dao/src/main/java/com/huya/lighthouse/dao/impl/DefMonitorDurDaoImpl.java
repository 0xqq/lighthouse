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

import com.huya.lighthouse.dao.DefMonitorDurDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.def.DefMonitorDur;

/**
 * tableName: def_monitor_dur [DefMonitorDur] 的Dao操作
 * 
 */
@Repository("defMonitorDurDao")
public class DefMonitorDurDaoImpl extends BaseSpringJdbcDao implements DefMonitorDurDao {

	private RowMapper<DefMonitorDur> entityRowMapper = new BeanPropertyRowMapper<DefMonitorDur>(getEntityClass());

	static final private String COLUMNS = "task_id,dur_sec,remarks,is_valid,create_time,update_time,create_user,update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from def_monitor_dur";

	@Override
	public Class<DefMonitorDur> getEntityClass() {
		return DefMonitorDur.class;
	}

	public RowMapper<DefMonitorDur> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(DefMonitorDur entity) {
		String sql = getInsertSql();
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	private String getInsertSql() {
		String sql = "insert into def_monitor_dur (task_id,dur_sec,remarks,is_valid,create_time,update_time,create_user,update_user) values "
				+ " (:taskId,:durSec,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser)";
		return sql;
	}

	public int update(DefMonitorDur entity) {
		String sql = "update def_monitor_dur set "
				+ " remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser "
				+ " where  task_id = :taskId and dur_sec = :durSec ";
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(int taskId, int durSec) {
		String sql = "delete from def_monitor_dur where  task_id = ? and dur_sec = ? ";
		return getSimpleJdbcTemplate().update(sql, taskId, durSec);
	}

	public DefMonitorDur getById(int taskId, int durSec) {
		String sql = SELECT_FROM + " where  task_id = ? and dur_sec = ? ";
		return (DefMonitorDur) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, durSec));
	}

	@Override
	public List<DefMonitorDur> getByTaskId(Integer taskId, int isValid) {
		String sql = SELECT_FROM + " where  task_id = ? and is_valid = ?";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, isValid);
	}

	@Override
	public int[] batchInsert(List<DefMonitorDur> entityList) {
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(entityList.toArray());
		return getNamedParameterJdbcTemplate().batchUpdate(getInsertSql(), batchArgs);
	}

	@Override
	public int deleteById(Integer taskId) {
		String sql = "delete from def_monitor_dur where  task_id = ?";
		return getSimpleJdbcTemplate().update(sql, taskId);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		String sql = "update def_monitor_dur set is_valid=?, update_time=? where task_id=?";
		getSimpleJdbcTemplate().update(sql, isValid, new Date(), taskId);
	}

}
