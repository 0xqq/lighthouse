package com.huya.lighthouse.dao.impl;

import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;

import com.huya.lighthouse.dao.DefCatalogDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.def.DefCatalog;
import com.huya.lighthouse.model.query.DefCatalogQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * tableName: def_catalog [DefCatalog] 的Dao操作
 * 
 */
@Repository("defCatalogDao")
public class DefCatalogDaoImpl extends BaseSpringJdbcDao implements DefCatalogDao {

	private RowMapper<DefCatalog> entityRowMapper = new BeanPropertyRowMapper<DefCatalog>(getEntityClass());

	static final private String COLUMNS = "catalog_id,catalog_name,lessee_id,remarks,is_valid,create_time,update_time,create_user,update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from def_catalog";

	@Override
	public Class<DefCatalog> getEntityClass() {
		return DefCatalog.class;
	}

	@Override
	public String getIdentifierPropertyName() {
		return "catalogId";
	}

	public RowMapper<DefCatalog> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(DefCatalog entity) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("insert into def_catalog (catalog_id,catalog_name,lessee_id,remarks,is_valid,create_time,update_time,create_user,update_user) values ");
		sqlBuilder.append(" (:catalogId,:catalogName,:lesseeId,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser)");
		insertWithGeneratedKey(entity, sqlBuilder.toString());
	}

	public int update(DefCatalog entity) {
		String sql = "update def_catalog set "
				+ " catalog_name=:catalogName,lessee_id=:lesseeId,remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser "
				+ " where  catalog_id = :catalogId ";
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(int catalogId) {
		String sql = "delete from def_catalog where  catalog_id = ? ";
		return getSimpleJdbcTemplate().update(sql, catalogId);
	}

	public DefCatalog getById(int catalogId) {
		String sql = SELECT_FROM + " where  catalog_id = ? ";
		return (DefCatalog) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), catalogId));
	}
	
	@Override
	public List<DefCatalog> getAllValid() {
		String sql = SELECT_FROM + " where is_valid = 1";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper());
	}

	@SuppressWarnings("unchecked")
	public Page<DefCatalog> findPage(DefCatalogQuery query) {
		StringBuilder sql = new StringBuilder("select " + COLUMNS + " from def_catalog where 1=1 ");
		if (isNotEmpty(query.getCatalogId())) {
			sql.append(" and catalog_id = :catalogId ");
		}
		if (isNotEmpty(query.getCatalogName())) {
			sql.append(" and catalog_name = :catalogName ");
		}
		if (isNotEmpty(query.getLesseeId())) {
			sql.append(" and lessee_id = :lesseeId ");
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
		if (isNotEmpty(query.getOrderBy())) {
			sql.append(" order by :orderBy ");
		}
		return pageQuery(sql.toString(), query, getEntityRowMapper());
	}
}
