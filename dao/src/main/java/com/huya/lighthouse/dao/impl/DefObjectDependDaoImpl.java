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

import com.huya.lighthouse.dao.DefObjectDependDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.def.DefObjectDepend;

/**
 * tableName: def_object_depend [DefObjectDepend] 的Dao操作
 * 
 */
@Repository("defObjectDependDao")
public class DefObjectDependDaoImpl extends BaseSpringJdbcDao implements DefObjectDependDao {

	private RowMapper<DefObjectDepend> entityRowMapper = new BeanPropertyRowMapper<DefObjectDepend>(getEntityClass());

	static final private String COLUMNS = "rw_flag,task_id,object_id,remarks,is_valid,create_time,update_time,create_user,update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from def_object_depend";

	@Override
	public Class<DefObjectDepend> getEntityClass() {
		return DefObjectDepend.class;
	}

	public RowMapper<DefObjectDepend> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(DefObjectDepend entity) {
		String sql = getInsertSql();
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	private String getInsertSql() {
		String sql = "insert into def_object_depend (rw_flag,task_id,object_id,remarks,is_valid,create_time,update_time,create_user,update_user) values "
				+ " (:rwFlag,:taskId,:objectId,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser)";
		return sql;
	}

	public int update(DefObjectDepend entity) {
		String sql = "update def_object_depend set remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser "
				+ " where rw_flag = :rwFlag and task_id = :taskId and object_id = :objectId ";
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(int rwFlag, int taskId, String objectId) {
		String sql = "delete from def_object_depend where  rw_flag = ? and task_id = ? and object_id = ? ";
		return getSimpleJdbcTemplate().update(sql, rwFlag, taskId, objectId);
	}

	public DefObjectDepend getById(int rwFlag, int taskId, String objectId) {
		String sql = SELECT_FROM + " where  rw_flag = ? and task_id = ? and object_id = ? ";
		return (DefObjectDepend) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), rwFlag, taskId, objectId));
	}

	@Override
	public int[] batchInsert(List<DefObjectDepend> entityList) {
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(entityList.toArray());
		return getNamedParameterJdbcTemplate().batchUpdate(getInsertSql(), batchArgs);
	}

	@Override
	public int deleteById(Integer taskId) {
		String sql = "delete from def_object_depend where task_id = ?";
		return getSimpleJdbcTemplate().update(sql, taskId);
	}

	@Override
	public List<DefObjectDepend> getByTaskId(int taskId, int isValid) {
		String sql = SELECT_FROM + " where task_id = ? and is_valid = ?";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, isValid);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		String sql = "update def_object_depend set is_valid=?, update_time=? where task_id=?";
		getSimpleJdbcTemplate().update(sql, isValid, new Date(), taskId);
	}

}
