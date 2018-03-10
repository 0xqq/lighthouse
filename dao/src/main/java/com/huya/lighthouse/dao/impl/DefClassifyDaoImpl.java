/*
 * Copyright [duowan.com]
 * Web Site: http://www.duowan.com
 * Since 2005 - 2017
 */

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

import com.huya.lighthouse.dao.DefClassifyDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.def.DefClassify;

/**
 * tableName: def_classify [DefClassify] 的Dao操作
 * 
 */
@Repository("defClassifyDao")
public class DefClassifyDaoImpl extends BaseSpringJdbcDao implements DefClassifyDao {

	private RowMapper<DefClassify> entityRowMapper = new BeanPropertyRowMapper<DefClassify>(getEntityClass());

	static final private String COLUMNS = "classify_type,classify_code,task_id,remarks,is_valid,create_time,update_time,create_user,update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from def_classify";

	@Override
	public Class<DefClassify> getEntityClass() {
		return DefClassify.class;
	}

	@Override
	public String getIdentifierPropertyName() {
		return "classifyType";
	}

	public RowMapper<DefClassify> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(DefClassify entity) {
		getNamedParameterJdbcTemplate().update(getInsertSql(), new BeanPropertySqlParameterSource(entity));
	}

	public int update(DefClassify entity) {
		String sql = "update def_classify set " + " remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser "
				+ " where  classify_type = :classifyType and classify_code = :classifyCode and task_id = :taskId ";
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(String classifyType, String classifyCode, int taskId) {
		String sql = "delete from def_classify where  classify_type = ? and classify_code = ? and task_id = ? ";
		return getSimpleJdbcTemplate().update(sql, classifyType, classifyCode, taskId);
	}

	public DefClassify getById(String classifyType, String classifyCode, int taskId) {
		String sql = SELECT_FROM + " where  classify_type = ? and classify_code = ? and task_id = ? ";
		return (DefClassify) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), classifyType, classifyCode, taskId));
	}

	private String getInsertSql() {
		String sql = "insert into def_classify (classify_type,classify_code,task_id,remarks,is_valid,create_time,update_time,create_user,update_user) values "
				+ " (:classifyType,:classifyCode,:taskId,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser)";
		return sql;
	}
	
	@Override
	public int[] batchInsert(List<DefClassify> entityList) {
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(entityList.toArray());
		return getNamedParameterJdbcTemplate().batchUpdate(getInsertSql(), batchArgs);
	}

	@Override
	public int deleteByTaskId(Integer taskId) {
		String sql = "delete from def_classify where task_id = ? ";
		return getSimpleJdbcTemplate().update(sql, taskId);
	}

	@Override
	public List<DefClassify> getByTaskId(Integer taskId, int isValid) {
		String sql = SELECT_FROM + " where task_id = ? and is_valid=?";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, isValid);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		String sql = "update def_classify set is_valid=?, update_time=? where task_id=?";
		getSimpleJdbcTemplate().update(sql, isValid, new Date(), taskId);
	}
}
