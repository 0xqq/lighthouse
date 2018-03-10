package com.huya.lighthouse.dao.impl;

import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;

import com.huya.lighthouse.dao.DefLesseeDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.def.DefLessee;

/**
 * tableName: def_lessee [DefLessee] 的Dao操作
 * 
 */
@Repository("defLesseeDao")
public class DefLesseeDaoImpl extends BaseSpringJdbcDao implements DefLesseeDao {

	private RowMapper<DefLessee> entityRowMapper = new BeanPropertyRowMapper<DefLessee>(getEntityClass());

	static final private String COLUMNS = "t1.lessee_id,t1.lessee_name,t1.alert_url,t1.password,t1.trigger_urls,t1.remarks,t1.is_valid,t1.create_time,t1.update_time,t1.create_user,t1.update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from def_lessee t1";

	@Override
	public Class<DefLessee> getEntityClass() {
		return DefLessee.class;
	}

	@Override
	public String getIdentifierPropertyName() {
		return "lesseeId";
	}

	public RowMapper<DefLessee> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(DefLessee entity) {
		String sql = "insert into def_lessee (lessee_id,lessee_name,alert_url,password,trigger_urls,remarks,is_valid,create_time,update_time,create_user,update_user) values "
				+ " (:lesseeId,:lesseeName,:alertUrl,:password,:triggerUrls,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser)";
		insertWithGeneratedKey(entity, sql);
	}

	public int update(DefLessee entity) {
		String sql = "update def_lessee set "
				+ " lessee_name=:lesseeName,alert_url=:alertUrl,password=:password,trigger_urls=:triggerUrls,remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser "
				+ " where  lessee_id = :lesseeId ";
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(int lesseeId) {
		String sql = "delete from def_lessee where  lessee_id = ? ";
		return getSimpleJdbcTemplate().update(sql, lesseeId);
	}

	public DefLessee getById(int lesseeId) {
		String sql = SELECT_FROM + " where  lessee_id = ? ";
		return (DefLessee) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), lesseeId));
	}

	@Override
	public List<DefLessee> getAllValid() {
		String sql = SELECT_FROM + " where is_valid = 1 ";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper());
	}

	@Override
	public DefLessee getByCatalogId(Integer catalogId) {
		String sql = SELECT_FROM + " where t1.is_valid=1 and exists (select 1 from def_catalog t2 where t2.catalog_id=? and t2.lessee_id=t1.lessee_id)";
		return (DefLessee) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), catalogId));
	}
}
