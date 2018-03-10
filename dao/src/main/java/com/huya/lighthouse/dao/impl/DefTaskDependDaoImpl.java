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

import com.huya.lighthouse.dao.DefTaskDependDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.def.DefTaskDepend;

/**
 * tableName: def_task_depend [DefTaskDepend] 的Dao操作
 * 
 */
@Repository("defTaskDependDao")
public class DefTaskDependDaoImpl extends BaseSpringJdbcDao implements DefTaskDependDao {

	private RowMapper<DefTaskDepend> entityRowMapper = new BeanPropertyRowMapper<DefTaskDepend>(getEntityClass());

	static final private String COLUMNS = "task_id,pre_task_id,depend_type,depend_n,is_same_agent_run,is_context_depend,remarks,is_valid,create_time,update_time,create_user,update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from def_task_depend";

	@Override
	public Class<DefTaskDepend> getEntityClass() {
		return DefTaskDepend.class;
	}

	@Override
	public String getIdentifierPropertyName() {
		return "taskId";
	}

	public RowMapper<DefTaskDepend> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(DefTaskDepend entity) {
		String sql = getInsertSql();
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	private String getInsertSql() {
		String sql = "insert into def_task_depend (task_id,pre_task_id,depend_type,depend_n,is_same_agent_run,is_context_depend,remarks,is_valid,create_time,update_time,create_user,update_user) values "
				+ " (:taskId,:preTaskId,:dependType,:dependN,:isSameAgentRun,:isContextDepend,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser)";
		return sql;
	}

	public int update(DefTaskDepend entity) {
		String sql = "update def_task_depend set "
				+ " depend_type=:dependType,depend_n=:dependN,is_same_agent_run=:isSameAgentRun,is_context_depend=:isContextDepend,remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser "
				+ " where task_id = :taskId and pre_task_id = :preTaskId ";
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(int taskId, int preTaskId) {
		String sql = "delete from def_task_depend where task_id = ? and pre_task_id = ? ";
		return getSimpleJdbcTemplate().update(sql, taskId, preTaskId);
	}

	public DefTaskDepend getById(int taskId, int preTaskId) {
		String sql = SELECT_FROM + " where task_id = ? and pre_task_id = ? ";
		return (DefTaskDepend) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, preTaskId));
	}

	@Override
	public List<DefTaskDepend> getByPreId(Integer preTaskId, int isValid) {
		String sql = SELECT_FROM + " where pre_task_id=? and is_valid=?";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), preTaskId, isValid);
	}

	@Override
	public List<DefTaskDepend> getByPostId(Integer taskId, int isValid) {
		String sql = SELECT_FROM + " where task_id=? and is_valid=?";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, isValid);
	}

	@Override
	public int[] batchInsert(List<DefTaskDepend> entityList) {
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(entityList.toArray());
		return getNamedParameterJdbcTemplate().batchUpdate(getInsertSql(), batchArgs);
	}

	@Override
	public int deleteById(Integer taskId) {
		String sql = "delete from def_task_depend where task_id = ?";
		return getSimpleJdbcTemplate().update(sql, taskId);
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		String sql = "update def_task_depend set is_valid=?, update_time=? where task_id=? or pre_task_id=?";
		getSimpleJdbcTemplate().update(sql, isValid, new Date(), taskId, taskId);
	}

}
