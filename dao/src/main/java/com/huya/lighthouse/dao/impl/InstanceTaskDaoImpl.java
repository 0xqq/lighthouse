package com.huya.lighthouse.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.huya.lighthouse.dao.InstanceTaskDao;
import com.huya.lighthouse.dao.util.BaseSpringJdbcDao;
import com.huya.lighthouse.model.bo.misc.InstanceStatus;
import com.huya.lighthouse.model.bo.misc.StatusCnt;
import com.huya.lighthouse.model.bo.misc.StatusCntCatalog;
import com.huya.lighthouse.model.bo.misc.StatusCntCatalogDay;
import com.huya.lighthouse.model.po.instance.InstanceTask;
import com.huya.lighthouse.model.query.InstanceTaskQuery;
import com.huya.lighthouse.model.query.misc.StatusCntCatalogDayQuery;
import com.huya.lighthouse.model.query.misc.StatusCntQuery;
import com.huya.lighthouse.model.query.page.Page;
import com.huya.lighthouse.util.DateUtils2;

/**
 * tableName: instance_task [InstanceTask] 的Dao操作
 * 
 */
@Repository("instanceTaskDao")
public class InstanceTaskDaoImpl extends BaseSpringJdbcDao implements InstanceTaskDao {

	private RowMapper<InstanceTask> entityRowMapper = new BeanPropertyRowMapper<InstanceTask>(getEntityClass());
	private RowMapper<InstanceStatus> instanceStatusRowMapper = new BeanPropertyRowMapper<InstanceStatus>(InstanceStatus.class);
	private RowMapper<StatusCntCatalogDay> statusCntCatalogDayRowMapper = new BeanPropertyRowMapper<StatusCntCatalogDay>(StatusCntCatalogDay.class);
	private RowMapper<StatusCntCatalog> statusCntCatalogRowMapper = new BeanPropertyRowMapper<StatusCntCatalog>(StatusCntCatalog.class);
	private RowMapper<StatusCnt> statusCntRowMapper = new BeanPropertyRowMapper<StatusCnt>(StatusCnt.class);

	static final private String COLUMNS = "t1.instance_id,t1.task_id,t1.task_date,t1.catalog_id,t1.task_name,t1.task_type,t1.task_plugin,t1.task_body,t1.exec_cron_exp,t1.agent_host_group,t1.agent_host_run,t1.agent_host_run_his,t1.linux_run_user,t1.queue_id,t1.priority,t1.max_run_num,t1.max_run_sec,t1.max_retry_num,t1.retried_num,t1.retry_interval,t1.is_ignore_error,t1.is_one_times,t1.offline_time,t1.ready_time,t1.start_time,t1.end_time,t1.status,t1.is_self_run,t1.is_force_run,t1.context,t1.schedule_log,t1.remarks,t1.is_valid,t1.create_time,t1.update_time,t1.create_user,t1.update_user";
	static final private String SELECT_FROM = "select " + COLUMNS + " from instance_task t1  ";

	@Override
	public Class<InstanceTask> getEntityClass() {
		return InstanceTask.class;
	}

	public RowMapper<InstanceTask> getEntityRowMapper() {
		return entityRowMapper;
	}

	public void insert(InstanceTask entity) {
		update(entity);
	}

	@Override
	public int[] batchInsert(List<InstanceTask> entityList) {
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(entityList.toArray());
		return getNamedParameterJdbcTemplate().batchUpdate(getInsertSql(), batchArgs);
	}

	public int update(InstanceTask entity) {
		String sql = getInsertSql();
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}

	private String getInsertSql() {
		String sql = "insert into instance_task "
				+ "(instance_id,task_id,task_date,catalog_id,task_name,task_type,task_plugin,task_body,exec_cron_exp,agent_host_group,agent_host_run,agent_host_run_his,linux_run_user,queue_id,priority,max_run_num,max_run_sec,max_retry_num,retried_num,retry_interval,is_ignore_error,is_one_times,offline_time,ready_time,start_time,end_time,status,is_self_run,is_force_run,context,schedule_log,remarks,is_valid,create_time,update_time,create_user,update_user) "
				+ "values "
				+ "(:instanceId,:taskId,:taskDate,:catalogId,:taskName,:taskType,:taskPlugin,:taskBody,:execCronExp,:agentHostGroup,:agentHostRun,:agentHostRunHis,:linuxRunUser,:queueId,:priority,:maxRunNum,:maxRunSec,:maxRetryNum,:retriedNum,:retryInterval,:isIgnoreError,:isOneTimes,:offlineTime,:readyTime,:startTime,:endTime,:status,:isSelfRun,:isForceRun,:context,:scheduleLog,:remarks,:isValid,:createTime,:updateTime,:createUser,:updateUser) "
				+ "ON DUPLICATE KEY UPDATE "
				+ "catalog_id=:catalogId,task_name=:taskName,task_type=:taskType,task_plugin=:taskPlugin,task_body=:taskBody,exec_cron_exp=:execCronExp,agent_host_group=:agentHostGroup,agent_host_run=:agentHostRun,agent_host_run_his=:agentHostRunHis,linux_run_user=:linuxRunUser,queue_id=:queueId,priority=:priority,max_run_num=:maxRunNum,max_run_sec=:maxRunSec,max_retry_num=:maxRetryNum,retried_num=:retriedNum,retry_interval=:retryInterval,is_ignore_error=:isIgnoreError,is_one_times=:isOneTimes,offline_time=:offlineTime,ready_time=:readyTime,start_time=:startTime,end_time=:endTime,status=:status,is_self_run=:isSelfRun,is_force_run=:isForceRun,context=:context,schedule_log=:scheduleLog,remarks=:remarks,is_valid=:isValid,create_time=:createTime,update_time=:updateTime,create_user=:createUser,update_user=:updateUser";
		return sql;
	}

	public int deleteById(int taskId, Date taskDate, String instanceId) {
		String sql = "delete from instance_task where task_id=? and task_date=? and instance_id=?";
		return getSimpleJdbcTemplate().update(sql, taskId, taskDate, instanceId);
	}

	public InstanceTask getById(Integer taskId, Date taskDate, String instanceId) {
		String sql = SELECT_FROM + " where task_id=? and task_date=? and instance_id=?";
		return (InstanceTask) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, taskDate, instanceId));
	}

	@Override
	public InstanceTask getValidInstance(Integer taskId, Date taskDate) {
		String sql = SELECT_FROM + " where task_id=? and task_date=? and is_valid=1 order by create_time desc limit 1";
		return (InstanceTask) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, taskDate));
	}

	@Override
	public boolean isSuccessPreInstance(Integer taskId, Date taskDate) {
		String sql = "select count(1) - COUNT(IF(t2.status='SUCCESS' or (t2.status='FAIL' and t2.is_ignore_error=1 and t2.retried_num>=t2.max_retry_num), 1, NULL)) no_Success_num "
				+ "from instance_task_depend t1 left join instance_task t2 on t2.task_id=t1.pre_task_id and t2.task_date=t1.pre_task_date and t2.is_valid=1 "
				+ "where t1.task_id=? and t1.task_date=? and t1.is_valid=1";
		int result = getSimpleJdbcTemplate().queryForInt(sql, taskId, taskDate);
		if (result != 0) {
			return false;
		}
		return true;
	}

	@Override
	public int updateInvalid(Integer taskId, Date taskDate) {
		String sql = "update instance_task set is_valid = 0 where task_id = ? and task_date = ?";
		return getSimpleJdbcTemplate().update(sql, taskId, taskDate);
	}

	@Override
	public InstanceTask getUnDoneTask(Integer taskId, Date taskDate) {
		String sql = SELECT_FROM + " where task_id=? and task_date=? and is_valid=1 and "
				+ "(status='INIT' or status='READY' or status='RUNNING' or (status='FAIL' and retried_num<max_retry_num)) limit 1";
		return (InstanceTask) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, taskDate));
	}

	@Override
	public List<InstanceTask> getValidInitInstance(Integer taskId) {
		String sql = SELECT_FROM + " where task_id=? and status='INIT' and is_valid=1";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId);
	}

	@Override
	public InstanceTask getSameAgentRun(Integer taskId, Date taskDate) {
		String sql = SELECT_FROM + " join instance_task_depend t2 on t2.pre_task_id=t1.task_id and t2.pre_task_date=t1.task_date "
				+ "where t2.task_id=? and t2.task_date=? and t2.is_valid=1 and t1.is_valid=1 and t2.is_same_agent_run=1 and t1.agent_host_run IS NOT NULL and t1.agent_host_run!='' order by t2.pre_task_date DESC limit 1";
		return (InstanceTask) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, taskDate));
	}

	@Override
	public InstanceTask getLastValidInstance(Integer taskId) {
		String sql = SELECT_FROM + " where task_id=? and is_valid=1 order by task_date DESC limit 1";
		return (InstanceTask) DataAccessUtils.singleResult(getSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId));
	}

	@Override
	public List<InstanceTask> getAllValidUnDoneInstance() {
		String sql = SELECT_FROM
				+ " where is_valid=1 and (status='INIT' or status='READY' or status='RUNNING' or (status='FAIL' and retried_num<max_retry_num)) and create_time>=date_sub(curdate(),interval 7 day) ORDER BY IF(status='RUNNING', 0, IF(status='READY', 1, 2)), task_date DESC, priority";
		return getSimpleJdbcTemplate().query(sql, getEntityRowMapper());
	}

	@Override
	public List<String> getPreContexts(Integer taskId, Date taskDate, String instanceId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t1.context ");
		sql.append("from instance_task t1 ");
		sql.append("join instance_task_depend t2 on t2.pre_task_id=t1.task_id and t2.pre_task_date=t1.task_date ");
		sql.append("where t1.is_valid=1 and t2.is_valid=1 and t2.is_context_depend=1 and t1.context IS NOT NULL and t2.task_id=? and t2.task_date=? and t2.instance_id=?");
		return getSimpleJdbcTemplate().getJdbcOperations().queryForList(sql.toString(), String.class, taskId, taskDate, instanceId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<InstanceTask> findPage(InstanceTaskQuery query) {
		InstanceTaskQuery queryClone = null;
		try {
			queryClone = (InstanceTaskQuery) SerializationUtils.clone(query);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		StringBuilder sql = new StringBuilder(SELECT_FROM).append(" where 1=1 ");
		sql.append(" and !(is_valid=0 and status='KILL' and start_time=end_time) ");
		if(isNotEmpty(query.getTaskDateBegin())) {
		    sql.append(" and task_date >= :taskDateBegin ");
		}
		if(isNotEmpty(query.getTaskDateEnd())) {
            sql.append(" and task_date <= :taskDateEnd ");
        }
		if(isNotEmpty(query.getTaskId())) {
            sql.append(" and task_id = :taskId ");
        }
		if(isNotEmpty(query.getInstanceId())) {
            sql.append(" and instance_id = :instanceId ");
        }
		if(isNotEmpty(query.getCatalogId())) {
            sql.append(" and catalog_id = :catalogId ");
        }
		if (isNotEmpty(query.getTaskName())) {
			queryClone.setTaskName("%" + query.getTaskName() + "%");
			sql.append(" and task_name like :taskName ");
		}
		if(isNotEmpty(query.getTaskType())) {
            sql.append(" and task_type = :taskType ");
        }
		if(isNotEmpty(query.getTaskPlugin())) {
            sql.append(" and task_plugin = :taskPlugin ");
        }
		if (isNotEmpty(query.getTaskBody())) {
			queryClone.setTaskBody("%" + query.getTaskBody() + "%");
			sql.append(" and (task_body like :taskBody or task_name like :taskBody or task_id like :taskBody or remarks like :taskBody) ");
		}
		if(isNotEmpty(query.getExecCronExp())) {
            sql.append(" and exec_cron_exp = :execCronExp ");
        }
		if(isNotEmpty(query.getAgentHostGroup())) {
            sql.append(" and agent_host_group = :agentHostGroup ");
        }
		if(isNotEmpty(query.getAgentHostRun())) {
            sql.append(" and agent_host_run = :agentHostRun ");
        }
		if(isNotEmpty(query.getAgentHostRunHis())) {
            sql.append(" and agent_host_run_his = :agentHostRunHis ");
        }
		if(isNotEmpty(query.getLinuxRunUser())) {
            sql.append(" and linux_run_user = :linuxRunUser ");
        }
		if(isNotEmpty(query.getQueueId())) {
            sql.append(" and queue_id = :queueId ");
        }
		if(isNotEmpty(query.getPriority())) {
            sql.append(" and priority = :priority ");
        }
		if(isNotEmpty(query.getMaxRunNum())) {
            sql.append(" and max_run_num = :maxRunNum ");
        }
		if(isNotEmpty(query.getMaxRunSec())) {
            sql.append(" and max_run_sec = :maxRunSec ");
        }
		if(isNotEmpty(query.getMaxRetryNum())) {
            sql.append(" and max_retry_num = :maxRetryNum ");
        }
		if(isNotEmpty(query.getRetriedNum())) {
            sql.append(" and retried_num = :retriedNum ");
        }
		if(isNotEmpty(query.getRetryInterval())) {
            sql.append(" and retry_interval = :retryInterval ");
        }
		if(isNotEmpty(query.getIsIgnoreError())) {
            sql.append(" and is_ignore_error = :isIgnoreError ");
        }
		if(isNotEmpty(query.getIsOneTimes())) {
            sql.append(" and is_one_times = :isOneTimes ");
        }
		if(isNotEmpty(query.getOfflineTimeBegin())) {
		    sql.append(" and offline_time >= :offlineTimeBegin ");
		}
		if(isNotEmpty(query.getOfflineTimeEnd())) {
            sql.append(" and offline_time <= :offlineTimeEnd ");
        }
		if(isNotEmpty(query.getReadyTimeBegin())) {
		    sql.append(" and ready_time >= :readyTimeBegin ");
		}
		if(isNotEmpty(query.getReadyTimeEnd())) {
            sql.append(" and ready_time <= :readyTimeEnd ");
        }
		if(isNotEmpty(query.getStartTimeBegin())) {
		    sql.append(" and start_time >= :startTimeBegin ");
		}
		if(isNotEmpty(query.getStartTimeEnd())) {
            sql.append(" and start_time <= :startTimeEnd ");
        }
		if(isNotEmpty(query.getEndTimeBegin())) {
		    sql.append(" and end_time >= :endTimeBegin ");
		}
		if(isNotEmpty(query.getEndTimeEnd())) {
            sql.append(" and end_time <= :endTimeEnd ");
        }
		if(isNotEmpty(query.getStatus())) {
            sql.append(" and status = :status ");
        }
		if(isNotEmpty(query.getIsSelfRun())) {
            sql.append(" and is_self_run = :isSelfRun ");
        }
		if(isNotEmpty(query.getIsForceRun())) {
            sql.append(" and is_force_run = :isForceRun ");
        }
		if(isNotEmpty(query.getContext())) {
            sql.append(" and context = :context ");
        }
		if(isNotEmpty(query.getScheduleLog())) {
            sql.append(" and schedule_log = :scheduleLog ");
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
            sql.append(" and (create_user = :createUser or update_user = :createUser) ");
        }
		if(isNotEmpty(query.getUpdateUser())) {
            sql.append(" and (update_user = :updateUser or create_user = :updateUser) ");
        }
		if (isNotEmpty(query.getOrderBy()) && isOKOrderBy(query.getOrderBy())) {
			sql.append(" order by ").append(query.getOrderBy());
		}
		return pageQuery(sql.toString(), queryClone, getEntityRowMapper());
	}

	@Override
	public StatusCnt getCurStatusCountAll(List<Integer> catalogIds) {
		Date date = DateUtils2.clear2HourLevel(new Date());
		Date yesterday = DateUtils.addDays(date, -1);
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select count(1) all_cnt, ");
		sqlBuilder.append("  count(IF(status='INIT', 1, null)) init_cnt, ");
		sqlBuilder.append("  count(IF(status='READY', 1, null)) ready_cnt, ");
		sqlBuilder.append("  count(IF(status='RUNNING', 1, null)) running_cnt, ");
		sqlBuilder.append("  count(IF(status='SUCCESS', 1, null)) success_cnt, ");
		sqlBuilder.append("  count(IF(status='FAIL', 1, null)) fail_cnt, ");
		sqlBuilder.append("  count(IF(status='KILL', 1, null)) kill_cnt ");
		sqlBuilder.append("from instance_task ");
		sqlBuilder.append("where is_valid=1 and (update_time>=? or status IN ('READY', 'RUNNING') or (status='INIT' and update_time>=?))");
		if (catalogIds != null && catalogIds.size() > 0) {
			String concat = "";
			for (int i=0; i<catalogIds.size(); i++) {
				if (i == 0) {
					concat = catalogIds.get(i).toString();
				}
				concat += "," + catalogIds.get(i).toString();
			}
			sqlBuilder.append("  and catalog_id in (").append(concat).append(")");
		}
		return (StatusCnt) DataAccessUtils.singleResult(getSlaveSimpleJdbcTemplate().query(sqlBuilder.toString(), statusCntRowMapper, date, yesterday));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<InstanceTask> pageQueryCurInstances(StatusCntQuery query) {
		StatusCntQuery queryClone = null;
		try {
			queryClone = (StatusCntQuery) SerializationUtils.clone(query);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		String yesterday = DateFormatUtils.format(DateUtils.addDays(new Date(), -1), "yyyy-MM-dd");
		StringBuilder sqlBuilder = new StringBuilder(SELECT_FROM);
		sqlBuilder.append("where is_valid=1 and (update_time>='").append(date).append("' or status IN ('READY', 'RUNNING') or (status='INIT' and update_time>='").append(yesterday).append("'))");
		if (isNotEmpty(queryClone.getCatalogId())) {
			sqlBuilder.append(" and catalog_id=:catalogId");
		}
		if (isNotEmpty(queryClone.getStatus())) {
			sqlBuilder.append(" and status=:status");
		}
		if (isNotEmpty(queryClone.getTaskBody())) {
			queryClone.setTaskBody("%" + query.getTaskBody() + "%");
			sqlBuilder.append(" and (task_body like :taskBody or task_name like :taskBody or task_id like :taskBody)");
		}
		return pageQuery(sqlBuilder.toString(), queryClone, getEntityRowMapper());
	}
	
	@Override
	public List<StatusCntCatalog> getCurStatusCountCatalog(List<Integer> catalogIds) {
		Date date = DateUtils2.clear2HourLevel(new Date());
		Date yesterday = DateUtils.addDays(date, -1);
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select catalog_id, count(1) all_cnt, ");
		sqlBuilder.append("  count(IF(status='INIT', 1, null)) init_cnt, ");
		sqlBuilder.append("  count(IF(status='READY', 1, null)) ready_cnt, ");
		sqlBuilder.append("  count(IF(status='RUNNING', 1, null)) running_cnt, ");
		sqlBuilder.append("  count(IF(status='SUCCESS', 1, null)) success_cnt, ");
		sqlBuilder.append("  count(IF(status='FAIL', 1, null)) fail_cnt, ");
		sqlBuilder.append("  count(IF(status='KILL', 1, null)) kill_cnt ");
		sqlBuilder.append("from instance_task ");
		sqlBuilder.append("where is_valid=1 and (update_time>=? or status IN ('READY', 'RUNNING') or (status='INIT' and update_time>=?)) ");
		if (catalogIds != null && catalogIds.size() > 0) {
			String concat = "";
			for (int i=0; i<catalogIds.size(); i++) {
				if (i == 0) {
					concat = catalogIds.get(i).toString();
				}
				concat += "," + catalogIds.get(i).toString();
			}
			sqlBuilder.append("  and catalog_id in (").append(concat).append(")");
		}
		sqlBuilder.append("group by catalog_id ");
		sqlBuilder.append("order by catalog_id");
		return getSlaveSimpleJdbcTemplate().query(sqlBuilder.toString(), statusCntCatalogRowMapper, date, yesterday);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<StatusCntCatalogDay> pageQueryStatusCountCatalogDay(StatusCntCatalogDayQuery query) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select catalog_id, task_day, count(1) all_cnt, ");
		sqlBuilder.append("  count(IF(status='INIT', 1, null)) init_cnt, ");
		sqlBuilder.append("  count(IF(status='READY', 1, null)) ready_cnt, ");
		sqlBuilder.append("  count(IF(status='RUNNING', 1, null)) running_cnt, ");
		sqlBuilder.append("  count(IF(status='SUCCESS', 1, null)) success_cnt, ");
		sqlBuilder.append("  count(IF(status='FAIL', 1, null)) fail_cnt, ");
		sqlBuilder.append("  count(IF(status='KILL', 1, null)) kill_cnt ");
		sqlBuilder.append("from ( ");
		sqlBuilder.append("  select DATE_FORMAT(task_date,'%Y-%m-%d') task_day, t1.* ");
		sqlBuilder.append("  from instance_task t1 ");
		sqlBuilder.append("  where is_valid=1 and task_date>=:taskDateBegin and task_date<=:taskDateEnd ");
		if (query.getCatalogIds() != null && query.getCatalogIds().size() > 0) {
			String concat = "";
			for (int i=0; i<query.getCatalogIds().size(); i++) {
				if (i == 0) {
					concat = query.getCatalogIds().get(i).toString();
				}
				concat += "," + query.getCatalogIds().get(i).toString();
			}
			sqlBuilder.append("    and catalog_id in (").append(concat).append(") ");
		}
		sqlBuilder.append(") t2 ");
		sqlBuilder.append("group by catalog_id, task_day ");
		sqlBuilder.append("order by task_day DESC, catalog_id");
		return pageQuery(sqlBuilder.toString(), query, statusCntCatalogDayRowMapper);
	}

	@Override
	public List<InstanceStatus> getInstanceStatuss(int taskId, Date startTaskDate, Date endTaskDate) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select task_date, status ");
		sqlBuilder.append("from instance_task ");
		sqlBuilder.append("where task_id=? and is_valid=1 and task_date>=? and task_date<=?");
		return getSlaveSimpleJdbcTemplate().query(sqlBuilder.toString(), instanceStatusRowMapper, taskId, startTaskDate, endTaskDate);
	}

	@Override
	public List<InstanceTask> getInstancesById(int taskId, Date taskDate) {
		String sql = SELECT_FROM + " where task_id=? and task_date=?";
		return getSlaveSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, taskDate);
	}

	@Override
	public List<InstanceTask> getByDependPreId(Integer taskId, Date taskDate) {
		String sql = "select t2.* from instance_task_depend t1 left join instance_task t2 on t2.task_id=t1.task_id and t2.task_date=t1.task_date and t2.instance_id=t1.instance_id where t1.pre_task_id=? and t1.pre_task_date=? and t1.is_valid=1";
		return getSlaveSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, taskDate);
	}

	@Override
	public List<InstanceTask> getByDependPostId(Integer taskId, Date taskDate) {
		String sql = "select t2.* from instance_task_depend t1 left join instance_task t2 on t2.task_id=t1.pre_task_id and t2.task_date=t1.pre_task_date and t2.instance_id=t1.instance_id where t1.task_id=? and t1.task_date=? and t1.is_valid=1";
		return getSlaveSimpleJdbcTemplate().query(sql, getEntityRowMapper(), taskId, taskDate);
	}
}
