package com.huya.lighthouse.dao.impl;

import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;

import com.huya.lighthouse.dao.DefQueueDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.def.DefQueue;
import com.huya.lighthouse.model.query.DefQueueQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * tableName: def_queue [DefQueue] 的Dao操作
 * 
 */
@Repository("defQueueDao")
public class DefQueueDaoImpl extends BaseSpringJdbcDao implements DefQueueDao {

	private RowMapper<DefQueue> entityRowMapper = new BeanPropertyRowMapper<DefQueue>(getEntityClass());

	static final private String COLUMNS = "queue_id,queue_name,queue_size,remarks,is_valid,create_time,update_time,create_user,update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from def_queue";

	@Override
	public Class<DefQueue> getEntityClass() {
		return DefQueue.class;
	}

	@Override
	public String getIdentifierPropertyName() {
		return "queueId";
	}

	public RowMapper<DefQueue> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(DefQueue entity) {
		String sql = "insert into def_queue (queue_id,queue_name,queue_size,remarks,is_valid,create_time,update_time,create_user,update_user) values "
				+ " (:queueId,:queueName,:queueSize,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser)";
		insertWithGeneratedKey(entity, sql);
	}

	public int update(DefQueue entity) {
		String sql = "update def_queue set "
				+ " queue_name=:queueName,queue_size=:queueSize,remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser "
				+ " where  queue_id = :queueId ";
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(int queueId) {
		String sql = "delete from def_queue where  queue_id = ? ";
		return getSimpleJdbcTemplate().update(sql, queueId);
	}

	public DefQueue getById(int queueId) {
		String sql = SELECT_FROM + " where  queue_id = ? ";
		return (DefQueue) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), queueId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<DefQueue> findPage(DefQueueQuery query) {
		StringBuilder sql = new StringBuilder("select " + COLUMNS + " from def_queue where 1=1 ");
		if (isNotEmpty(query.getQueueId())) {
			sql.append(" and queue_id = :queueId ");
		}
		if (isNotEmpty(query.getQueueName())) {
			sql.append(" and queue_name = :queueName ");
		}
		if (isNotEmpty(query.getQueueSize())) {
			sql.append(" and queue_size = :queueSize ");
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
		if (isNotEmpty(query.getOrderBy())) {
			sql.append(" order by :orderBy ");
		}
		return pageQuery(sql.toString(), query, getEntityRowMapper());
	}

	@Override
	public List<DefQueue> getAllValid() {
		String sql = SELECT_FROM + " where is_valid = 1 ORDER BY IF(queue_id=7, 0, queue_id) ASC ";
		return getSlaveSimpleJdbcTemplate().query(sql, getEntityRowMapper());
	}

}
