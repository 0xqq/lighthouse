package com.huya.lighthouse.dao.impl;

import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;

import com.huya.lighthouse.dao.DefTaskParamDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.def.DefTaskParam;
import com.huya.lighthouse.model.query.DefTaskParamQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * tableName: def_task_param [DefTaskParam] 的Dao操作
 * 
 */
@Repository("defTaskParamDao")
public class DefTaskParamDaoImpl extends BaseSpringJdbcDao implements DefTaskParamDao {

	private RowMapper<DefTaskParam> entityRowMapper = new BeanPropertyRowMapper<DefTaskParam>(getEntityClass());

	static final private String COLUMNS = "catalog_id,param_code,param_type,param_value,is_password,remarks,is_valid,create_time,update_time,create_user,update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from def_task_param";

	@Override
	public Class<DefTaskParam> getEntityClass() {
		return DefTaskParam.class;
	}

	public RowMapper<DefTaskParam> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(DefTaskParam entity) {
		String sql = "insert into def_task_param (catalog_id,param_code,param_type,param_value,is_password,remarks,is_valid,create_time,update_time,create_user,update_user) values "
				+ " (:catalogId,:paramCode,:paramType,:paramValue,:isPassword,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int update(DefTaskParam entity) {
		String sql = "update def_task_param set "
				+ " param_type=:paramType,param_value=:paramValue,is_password=:isPassword,remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser "
				+ " where catalog_id=:catalogId and param_code = :paramCode ";
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(Integer catalogId, String paramCode) {
		String sql = "delete from def_task_param where catalog_id=? and param_code = ? ";
		return getSimpleJdbcTemplate().update(sql, catalogId, paramCode);
	}

	public DefTaskParam getById(Integer catalogId, String paramCode) {
		String sql = SELECT_FROM + " where catalog_id=? and param_code = ? ";
		return (DefTaskParam) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), catalogId, paramCode));
	}

	@Override
	public List<DefTaskParam> getAllValid() {
		String sql = SELECT_FROM + " where is_valid = 1";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper());
	}

	@SuppressWarnings("unchecked")
	public Page<DefTaskParam> findPage(DefTaskParamQuery query) {
		DefTaskParamQuery queryClone = null;
		try {
			queryClone = (DefTaskParamQuery) SerializationUtils.clone(query);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		StringBuilder sql = new StringBuilder("select " + COLUMNS + " from def_task_param where 1=1 ");
		if (isNotEmpty(query.getCatalogId())) {
			sql.append(" and catalog_id = :catalogId ");
		}
		if (isNotEmpty(query.getParamCode())) {
			queryClone.setParamCode("%" + query.getParamCode() + "%");
			sql.append(" and param_code like :paramCode ");
		}
		if (isNotEmpty(query.getParamType())) {
			sql.append(" and param_type = :paramType ");
		}
		if (isNotEmpty(query.getParamValue())) {
			queryClone.setParamValue("%" + query.getParamValue() + "%");
			sql.append(" and param_value like :paramValue ");
		}
		if (isNotEmpty(query.getIsPassword())) {
			sql.append(" and is_password = :isPassword ");
		}
		if (isNotEmpty(query.getRemarks())) {
			sql.append(" and remarks = :remarks ");
		}
		if (isNotEmpty(query.getIsValid())) {
			sql.append(" and is_valid = :isValid ");
		}
		if (isNotEmpty(query.getCreateTimeBegin())) {
			sql.append(" and create_time >= :createTimeBegin ");
		}
		if (isNotEmpty(query.getCreateTimeEnd())) {
			sql.append(" and create_time <= :createTimeEnd ");
		}
		if (isNotEmpty(query.getUpdateTimeBegin())) {
			sql.append(" and update_time >= :updateTimeBegin ");
		}
		if (isNotEmpty(query.getUpdateTimeEnd())) {
			sql.append(" and update_time <= :updateTimeEnd ");
		}
		if (isNotEmpty(query.getCreateUser())) {
			sql.append(" and create_user = :createUser ");
		}
		if (isNotEmpty(query.getUpdateUser())) {
			sql.append(" and update_user = :updateUser ");
		}
		if (isNotEmpty(query.getOrderBy()) && isOKOrderBy(query.getOrderBy())) {
			sql.append(" order by ").append(query.getOrderBy());
		}
		return pageQuery(sql.toString(), queryClone, getEntityRowMapper());
	}

}
