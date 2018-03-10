package com.huya.lighthouse.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;

import com.huya.lighthouse.dao.DefTaskDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.po.def.DefTask;
import com.huya.lighthouse.model.query.DefTaskQuery;
import com.huya.lighthouse.model.query.page.Page;

/**
 * tableName: def_task [DefTask] 的Dao操作
 * 
 */
@Repository("defTaskDao")
public class DefTaskDaoImpl extends BaseSpringJdbcDao implements DefTaskDao {

	private RowMapper<DefTask> entityRowMapper = new BeanPropertyRowMapper<DefTask>(getEntityClass());

	static final private String COLUMNS = "t1.task_id,t1.catalog_id,t1.task_name,t1.task_type,t1.task_plugin,t1.task_body,t1.exec_cron_exp,t1.agent_host_group,t1.linux_run_user,t1.queue_id,t1.priority,t1.max_run_num,t1.max_run_sec,t1.max_retry_num,t1.retry_interval,t1.is_ignore_error,t1.is_one_times,t1.offline_time,t1.remarks,t1.is_valid,t1.create_time,t1.update_time,t1.create_user,t1.update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from def_task t1";

	@Override
	public Class<DefTask> getEntityClass() {
		return DefTask.class;
	}

	@Override
	public String getIdentifierPropertyName() {
		return "taskId";
	}

	public RowMapper<DefTask> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(DefTask entity) {
		String sql = getInsertSql();
		insertWithGeneratedKey(entity, sql);
	}

	public String getInsertSql() {
		String sql = "insert into def_task "
				+ " (task_id,catalog_id,task_name,task_type,task_plugin,task_body,exec_cron_exp,agent_host_group,linux_run_user,queue_id,priority,max_run_num,max_run_sec,max_retry_num,retry_interval,is_ignore_error,is_one_times,offline_time,remarks,is_valid,create_time,update_time,create_user,update_user) "
				+ " values "
				+ " (:taskId,:catalogId,:taskName,:taskType,:taskPlugin,:taskBody,:execCronExp,:agentHostGroup,:linuxRunUser,:queueId,:priority,:maxRunNum,:maxRunSec,:maxRetryNum,:retryInterval,:isIgnoreError,:isOneTimes,:offlineTime,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser) "
				+ "ON DUPLICATE KEY UPDATE "
				+ "catalog_id=:catalogId,task_name=:taskName,task_type=:taskType,task_plugin=:taskPlugin,task_body=:taskBody,exec_cron_exp=:execCronExp,agent_host_group=:agentHostGroup,linux_run_user=:linuxRunUser,queue_id=:queueId,priority=:priority,max_run_num=:maxRunNum,max_run_sec=:maxRunSec,max_retry_num=:maxRetryNum,retry_interval=:retryInterval,is_ignore_error=:isIgnoreError,is_one_times=:isOneTimes,offline_time=:offlineTime,remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser";
		return sql;
	}

	public int update(DefTask entity) {
		String sql = getInsertSql();
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	public int deleteById(int taskId) {
		String sql = "delete from def_task where  task_id = ? ";
		return getSimpleJdbcTemplate().update(sql, taskId);
	}

	public DefTask getById(int taskId) {
		String sql = SELECT_FROM + " where task_id = ? ";
		return (DefTask) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId));
	}

	@Override
	public List<DefTask> getByDependPreId(Integer preTaskId, Integer isValid) {
		String sql = SELECT_FROM + " join def_task_depend t2 on t2.task_id=t1.task_id where t2.pre_task_id=? and t1.is_valid=1 and t1.offline_time>?";
		if (isValid==null || isValid==0) {
			sql = SELECT_FROM + " join def_task_depend t2 on t2.task_id=t1.task_id where t2.pre_task_id=? and (t1.is_valid=0 or t1.offline_time<=?)";
		}
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), preTaskId, new Date());
	}

	@Override
	public List<DefTask> getByDependPostId(Integer taskId, Integer isValid) {
		String sql = SELECT_FROM + " join def_task_depend t2 on t2.pre_task_id=t1.task_id where t2.task_id=? and t1.is_valid=1 and t1.offline_time>?";
		if (isValid==null || isValid==0) {
			sql = SELECT_FROM + " join def_task_depend t2 on t2.pre_task_id=t1.task_id where t2.task_id=? and (t1.is_valid=0 or t1.offline_time<=?)";
		}
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, new Date());
	}

	@Override
	public List<DefTask> getAllValidCronTask() {
		String sql = SELECT_FROM + " where task_type='CRON' and is_valid=1 and t1.offline_time>?";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), new Date());
	}

	@Override
	public List<DefTask> getForInitByCatalogId(Integer catalogId) {
		String sql = SELECT_FROM + " where t1.catalog_id=? and t1.is_valid=1 and t1.offline_time>? and EXISTS (SELECT 1 FROM def_task_depend t2 where t2.is_valid=1 and t2.task_id=t1.task_id)";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), catalogId, new Date());
	}

	@Override
	public void updateIsValidById(int taskId, int isValid) {
		String sql = "update def_task set is_valid=?, update_time=? where task_id=? ";
		getSimpleJdbcTemplate().update(sql, isValid, new Date(), taskId);
	}

	@Override
	public List<DefTask> getByMonitorCronExp(String cronExp) {
		String sql = SELECT_FROM + " where t1.is_valid=1 and t1.offline_time>? and EXISTS (SELECT 1 FROM def_monitor_begin t2 where t2.cron_exp=? and t2.is_valid=1 and t2.task_id=t1.task_id)";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), new Date(), cronExp);
	}

	@Override
	public List<DefTask> getByOfflineTime(Date offlineTime) {
		String sql = SELECT_FROM + " where t1.is_valid=1 and t1.offline_time<=? and t1.is_one_times=0";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), offlineTime);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<DefTask> findPage(DefTaskQuery query) {
		DefTaskQuery queryClone = null;
		try {
			queryClone = (DefTaskQuery) SerializationUtils.clone(query);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		StringBuilder sql = new StringBuilder(SELECT_FROM).append(" where 1=1 ");
		if (isNotEmpty(query.getTaskId())) {
			sql.append(" and task_id = :taskId ");
		}
		if (isNotEmpty(query.getCatalogId()) && (query.getCatalogIdList() == null || query.getCatalogIdList().size() == 0)) {
			sql.append(" and catalog_id = :catalogId ");
		}
		if (query.getCatalogIdList() != null && query.getCatalogIdList().size() > 0) {
			sql.append(" and catalog_id in ( ");
			int i = 0;
			for (int catalogId : query.getCatalogIdList()) {
				if (i == 0) {
					sql.append(catalogId);
				} else {
					sql.append(",").append(catalogId);
				}
				i++;
			}
			sql.append(") ");
		}
		if (isNotEmpty(query.getTaskName())) {
			queryClone.setTaskName("%" + query.getTaskName() + "%");
			sql.append(" and task_name like :taskName ");
		}
		if (isNotEmpty(query.getTaskType())) {
			sql.append(" and task_type = :taskType ");
		}
		if (isNotEmpty(query.getTaskPlugin())) {
			sql.append(" and task_plugin = :taskPlugin ");
		}
		if (isNotEmpty(query.getTaskBody())) {
			queryClone.setTaskBody("%" + query.getTaskBody() + "%");
			sql.append(" and (task_body like :taskBody or task_name like :taskBody or task_id like :taskBody or remarks like :taskBody) ");
		}
		if (isNotEmpty(query.getExecCronExp())) {
			sql.append(" and exec_cron_exp = :execCronExp ");
		}
		if (isNotEmpty(query.getAgentHostGroup())) {
			sql.append(" and agent_host_group = :agentHostGroup ");
		}
		if (isNotEmpty(query.getLinuxRunUser())) {
			sql.append(" and linux_run_user = :linuxRunUser ");
		}
		if (isNotEmpty(query.getQueueId())) {
			sql.append(" and queue_id = :queueId ");
		}
		if (isNotEmpty(query.getPriority())) {
			sql.append(" and priority = :priority ");
		}
		if (isNotEmpty(query.getMaxRunNum())) {
			sql.append(" and max_run_num = :maxRunNum ");
		}
		if (isNotEmpty(query.getMaxRunSec())) {
			sql.append(" and max_run_sec = :maxRunSec ");
		}
		if (isNotEmpty(query.getMaxRetryNum())) {
			sql.append(" and max_retry_num = :maxRetryNum ");
		}
		if (isNotEmpty(query.getRetryInterval())) {
			sql.append(" and retry_interval = :retryInterval ");
		}
		if (isNotEmpty(query.getIsIgnoreError())) {
			sql.append(" and is_ignore_error = :isIgnoreError ");
		}
		if (isNotEmpty(query.getIsOneTimes())) {
			sql.append(" and is_one_times = :isOneTimes ");
		}
		if (isNotEmpty(query.getOfflineTimeBegin())) {
			sql.append(" and offline_time >= :offlineTimeBegin ");
		}
		if (isNotEmpty(query.getOfflineTimeEnd())) {
			sql.append(" and offline_time <= :offlineTimeEnd ");
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
			sql.append(" and (create_user = :createUser or update_user = :createUser) ");
		}
		if (isNotEmpty(query.getUpdateUser())) {
			sql.append(" and (update_user = :updateUser or create_user = :updateUser) ");
		}
		if (isNotEmpty(query.getOrderBy()) && isOKOrderBy(query.getOrderBy())) {
			sql.append(" order by ").append(query.getOrderBy());
		}
		return pageQuery(sql.toString(), queryClone, getEntityRowMapper());
	}

	@Override
	public List<DefTask> getOfflineValid() {
		String sql = SELECT_FROM + " where t1.is_valid=1 and t1.offline_time<=?";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), new Date());
	}

}
