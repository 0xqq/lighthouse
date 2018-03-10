package com.huya.lighthouse.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;

import com.huya.lighthouse.dao.DefAgentGroupDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.def.DefAgentGroup;
import com.huya.lighthouse.model.query.DefAgentGroupQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * tableName: def_agent_group [DefAgentGroup] 的Dao操作
 * 
 */
@Repository("defAgentGroupDao")
public class DefAgentGroupDaoImpl extends BaseSpringJdbcDao implements DefAgentGroupDao {

	private RowMapper<DefAgentGroup> entityRowMapper = new BeanPropertyRowMapper<DefAgentGroup>(getEntityClass());

	static final private String COLUMNS = "agent_host_group,agent_host,agent_port,agent_user,agent_private_key,agent_password,work_base_dir,remarks,is_valid,create_time,update_time,create_user,update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from def_agent_group";

	@Override
	public Class<DefAgentGroup> getEntityClass() {
		return DefAgentGroup.class;
	}

	@Override
	public String getIdentifierPropertyName() {
		return "agentHostGroup";
	}

	public RowMapper<DefAgentGroup> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(DefAgentGroup entity) {
		String sql = "insert into def_agent_group "
				+ " (agent_host_group,agent_host,agent_port,agent_user,agent_private_key,agent_password,work_base_dir,remarks,is_valid,create_time,update_time,create_user,update_user) "
				+ " values "
				+ " (:agentHostGroup,:agentHost,:agentPort,:agentUser,:agentPrivateKey,:agentPassword,:workBaseDir,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int update(DefAgentGroup entity) {
		String sql = "update def_agent_group set agent_port=:agentPort,agent_user=:agentUser,agent_private_key=:agentPrivateKey,agent_password=:agentPassword,work_base_dir=:workBaseDir,remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser "
				+ " where  agent_host_group = :agentHostGroup and agent_host = :agentHost ";
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(String agentHostGroup, String agentHost) {
		String sql = "delete from def_agent_group where  agent_host_group = ? and agent_host = ? ";
		return getSimpleJdbcTemplate().update(sql, agentHostGroup, agentHost);
	}

	public DefAgentGroup getById(String agentHostGroup, String agentHost) {
		String sql = SELECT_FROM + " where  agent_host_group = ? and agent_host = ? ";
		return (DefAgentGroup) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), agentHostGroup, agentHost));
	}

	@Override
	public List<DefAgentGroup> getAllValid() {
		String sql = SELECT_FROM + " where is_valid = 1 order by update_time ASC";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return getNamedParameterJdbcTemplate().query(sql, paramMap, getEntityRowMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<DefAgentGroup> findPage(DefAgentGroupQuery query) {
		StringBuilder sql = new StringBuilder("select "+ COLUMNS + " from def_agent_group where 1=1 ");
		if(isNotEmpty(query.getAgentHostGroup())) {
            sql.append(" and agent_host_group = :agentHostGroup ");
        }
		if(isNotEmpty(query.getAgentHost())) {
            sql.append(" and agent_host = :agentHost ");
        }
		if(isNotEmpty(query.getAgentPort())) {
            sql.append(" and agent_port = :agentPort ");
        }
		if(isNotEmpty(query.getAgentUser())) {
            sql.append(" and agent_user = :agentUser ");
        }
		if(isNotEmpty(query.getAgentPrivateKey())) {
            sql.append(" and agent_private_key = :agentPrivateKey ");
        }
		if(isNotEmpty(query.getWorkBaseDir())) {
            sql.append(" and work_base_dir = :workBaseDir ");
        }
		if(isNotEmpty(query.getRemarks())) {
            sql.append(" and remarks = :remarks ");
        }
		if(isNotEmpty(query.getIsValid())) {
            sql.append(" and is_valid = :isValid ");
        }
		if(isNotEmpty(query.getCreateTimeBegin())) {
		    sql.append(" and create_time >= :createTimeBegin ");
		}
		if(isNotEmpty(query.getCreateTimeEnd())) {
            sql.append(" and create_time <= :createTimeEnd ");
        }
		if(isNotEmpty(query.getUpdateTimeBegin())) {
		    sql.append(" and update_time >= :updateTimeBegin ");
		}
		if(isNotEmpty(query.getUpdateTimeEnd())) {
            sql.append(" and update_time <= :updateTimeEnd ");
        }
		if(isNotEmpty(query.getCreateUser())) {
            sql.append(" and create_user = :createUser ");
        }
		if(isNotEmpty(query.getUpdateUser())) {
            sql.append(" and update_user = :updateUser ");
        }
		if (isNotEmpty(query.getOrderBy())) {
			sql.append(" order by :orderBy ");
		}
		return pageQuery(sql.toString(), query, getEntityRowMapper());	
	}
}
