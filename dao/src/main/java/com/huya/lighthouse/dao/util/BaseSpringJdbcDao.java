package com.huya.lighthouse.dao.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DB2SequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.huya.lighthouse.model.query.page.Page;
import com.huya.lighthouse.model.query.page.PageQuery;
import com.huya.lighthouse.model.query.page.Paginator;

/**
 * Spring的JDBC基类
 * 
 */
public abstract class BaseSpringJdbcDao implements InitializingBean {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected final Pattern orderByPattern = Pattern.compile("^(?i)(\\s*\\w+\\s+(asc|desc)\\s*)$");

	@Autowired
	protected SimpleJdbcTemplate simpleJdbcTemplate;
	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	protected SimpleJdbcTemplate slaveSimpleJdbcTemplate;
	@Autowired
	protected NamedParameterJdbcTemplate slaveNamedParameterJdbcTemplate;

	@Autowired
	protected DataSource masterDataSource;

	@Autowired
	protected DataSource slaveDataSource;

	@Override
	public final void afterPropertiesSet() throws IllegalArgumentException, BeanInitializationException {
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public SimpleJdbcTemplate getSlaveSimpleJdbcTemplate() {
		return slaveSimpleJdbcTemplate;
	}

	public NamedParameterJdbcTemplate getSlaveNamedParameterJdbcTemplate() {
		return slaveNamedParameterJdbcTemplate;
	}

	protected Class<?> getEntityClass() {
		throw new UnsupportedOperationException("not yet implements");
	}

	protected void checkSingleRowAffected(String sql, int rowsAffected) throws JdbcUpdateAffectedIncorrectNumberOfRowsException {
		checkRowAffected(sql, rowsAffected, 1);
	}

	/**
	 * 检查update调用的rowsAffected必须为正确的行数
	 * 
	 * @param sql
	 * @param rowsAffected
	 * @param requiredRowsAffected
	 * @throws JdbcUpdateAffectedIncorrectNumberOfRowsException
	 */
	public void checkRowAffected(String sql, int rowsAffected, int requiredRowsAffected) throws JdbcUpdateAffectedIncorrectNumberOfRowsException {
		if (requiredRowsAffected > 0 && rowsAffected != requiredRowsAffected) {
			throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(sql, requiredRowsAffected, rowsAffected);
		}
	}

	@SuppressWarnings("all")
	public Page pageQuery(String sql, PageQuery pageQuery, RowMapper rowMapper) {
		Map paramMap = new HashMap(describe(pageQuery));
		return pageQuery(sql, paramMap, queryTotalItems(sql, paramMap), pageQuery.getPageSize(), pageQuery.getPage(), rowMapper);
	}

	@SuppressWarnings("rawtypes")
	private Map describe(Object bean) {
		try {
			return PropertyUtils.describe(bean);
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
			return null;
		}
	}

	@SuppressWarnings("all")
	public Page pageQuery(String sql, Map paramMap, int pageSize, int pageNumber, RowMapper rowMapper) {
		return pageQuery(sql, paramMap, queryTotalItems(sql, paramMap), pageSize, pageNumber, rowMapper);
	}

	@SuppressWarnings("all")
	private Page pageQuery(String sql, Map paramMap, final int totalItems, int pageSize, int pageNumber, RowMapper rowMapper) {
		if (totalItems <= 0) {
			return new Page(new Paginator(pageNumber, pageSize, 0));
		}
		Paginator paginator = new Paginator(pageNumber, pageSize, totalItems);
		List list = pageQueryForList(sql, paramMap, paginator.getOffset(), pageSize, rowMapper);
		return new Page(list, paginator);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int queryTotalItems(String querySql, Map paramMap) {
		String removedOrderByQuery = "select count(*) from ( " + removeOrders(querySql) + " ) as c ";
		return getSlaveNamedParameterJdbcTemplate().queryForInt(removedOrderByQuery, new MapSqlParameterSource((Map) paramMap));
	}

	private String removeOrders(String sql) {
		Assert.hasText(sql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List pageQueryForList(String sql, Map paramMap, int startRow, int pageSize, RowMapper rowMapper) {
		paramMap.put("startRow", startRow);
		paramMap.put("pageSize", pageSize);
		if (startRow > 0) {
			sql = sql + " limit :startRow, :pageSize";
		} else {
			sql = sql + " limit :pageSize";
		}
		return (List) getSlaveNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}

	/**
	 * 适用sqlserver:identity,mysql:auto_increment 自动生成主键
	 */
	public int insertWithGeneratedKey(Object entity, String insertSql) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int affectedRows = getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity), keyHolder);
		setIdentifierProperty(entity, keyHolder.getKey().longValue());
		return affectedRows;
	}

	public Object insertWithGeneratedKeyReturnObject(Object entity, String insertSql) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity), keyHolder);
		setIdentifierProperty(entity, keyHolder.getKey().longValue());
		return entity;
	}

	public int insertWithIdentity(Object entity, String insertSql) {
		return insertWithGeneratedKey(entity, insertSql);
	}

	public int insertWithAutoIncrement(Object entity, String insertSql) {
		return insertWithIdentity(entity, insertSql);
	}

	public int insertWithSequence(Object entity, AbstractSequenceMaxValueIncrementer sequenceIncrementer, String insertSql) {
		Long id = sequenceIncrementer.nextLongValue();
		setIdentifierProperty(entity, id);
		return getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity));
	}

	public int insertWithDB2Sequence(Object entity, String sequenceName, String insertSql) {
		return insertWithSequence(entity, new DB2SequenceMaxValueIncrementer(masterDataSource, sequenceName), insertSql);
	}

	public int insertWithOracleSequence(Object entity, String sequenceName, String insertSql) {
		return insertWithSequence(entity, new OracleSequenceMaxValueIncrementer(masterDataSource, sequenceName), insertSql);
	}

	public int insertWithUUID(Object entity, String insertSql) {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		setIdentifierProperty(entity, uuid);
		return getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity));
	}

	/**
	 * 手工分配ID插入
	 * 
	 * @param entity
	 * @param insertSql
	 */
	public int insertWithAssigned(Object entity, String insertSql) {
		return getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity));
	}

	/**
	 * 得到主键对应的property
	 */
	protected String getIdentifierPropertyName() {
		throw new UnsupportedOperationException("not yet implements");
	}

	/**
	 * 设置实体的主键值
	 */
	public void setIdentifierProperty(Object entity, Object id) {
		try {
			BeanUtils.setProperty(entity, getIdentifierPropertyName(), id);
		} catch (Exception e) {
			throw new IllegalStateException("cannot set property value:" + id + " on entityClass:" + entity.getClass() + " by propertyName:" + getIdentifierPropertyName(), e);
		}
	}

	/**
	 * 得到实体的主键值
	 */
	public Object getIdentifierPropertyValue(Object entity) {
		try {
			return PropertyUtils.getProperty(entity, getIdentifierPropertyName());
		} catch (Exception e) {
			throw new IllegalStateException("cannot get property value on entityClass:" + entity.getClass() + " by propertyName:" + getIdentifierPropertyName(), e);
		}
	}

	protected boolean isNotEmpty(Object c) {
		return !isEmpty(c);
	}

	protected boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof String && StringUtils.isBlank((String) o)) {
			return true;
		}
		return false;
	}
	
	protected boolean isOKOrderBy(String orderBy) {
		Matcher m = orderByPattern.matcher(orderBy);
		if (m.find()) {
			return true;
		} else {
			log.error("oderBy error: " + orderBy);
			return false;
		}
	}
}
