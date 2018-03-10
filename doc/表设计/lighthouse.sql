/*
SET GLOBAL TRANSACTION ISOLATION LEVEL READ COMMITTED;
SELECT @@global.tx_isolation;
SELECT @@session.tx_isolation;
SELECT @@tx_isolation;

CREATE DATABASE IF NOT EXISTS lighthouse DEFAULT CHARACTER SET utf8;
use lighthouse;
*/

/*==============================================================*/
/* Table: def_task                                              */
/*==============================================================*/
drop table if exists def_task;
create table def_task
(
   task_id              int not null auto_increment comment '任务id',
   catalog_id           int not null comment '所属项目id',
   task_name            varchar(100) not null comment '任务名称',
   task_type            varchar(20) not null comment '任务类型: 定时器, 虚拟任务...',
   task_plugin          text not null comment '任务执行插件',
   task_body            text comment '任务内容',
   exec_cron_exp        varchar(50) comment '执行频次quartz表达式',
   agent_host_group     varchar(50) comment '将要在哪个Agent组运行该任务',
   linux_run_user       varchar(50) comment 'linux运行用户',
   queue_id             int comment '所属运行队列',
   priority             int comment '优先级',
   max_run_num          int not null default 5 comment '同时运行的最大实例数',
   max_run_sec          int not null default 86400 comment '运行耗时到达该值，任务就kill掉',
   max_retry_num        int not null default 2 comment '任务失败最大重试次数',
   retry_interval       int not null default 120 comment '重试间隔(秒)',
   is_ignore_error      tinyint not null default 0 comment '最终失败是否可忽略, 1可忽略, 0不可忽略',
   is_one_times         tinyint not null default 0 comment '是否一次性任务, 1是, 0否',
   offline_time         datetime not null comment '下线时间',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (task_id),
   key(catalog_id)
) comment = '任务信息'
engine = InnoDB
default charset utf8
auto_increment = 10000;


/*==============================================================*/
/* Table: def_task_depend                                       */
/*==============================================================*/
drop table if exists def_task_depend;
create table def_task_depend
(
   task_id              int not null comment '任务id',
   pre_task_id          int not null comment '前置依赖的任务id',
   depend_type          varchar(20) not null comment '依赖类型: SAMELEVEL, SELF, SLIDE, HOUR, DAY, WEEK, MONTH, QUARTER',
   depend_n             int not null default 1 comment '依赖个数',
   is_same_agent_run    tinyint not null default 0 comment '是否有状态任务，需要在同个agent节点运行, 1是, 0否',
   is_context_depend    tinyint not null default 0 comment '是否依赖上下文, 1是, 0否',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (task_id, pre_task_id),
   key(pre_task_id)
) comment = '依赖关系'
engine = InnoDB
default charset utf8;


/*==============================================================*/
/* Table: instance_task                                         */
/*==============================================================*/
drop table if exists instance_task;
create table instance_task
(
   task_date            datetime not null comment '任务数据日期',
   task_id              int not null comment '任务id',
   instance_id          varchar(50) not null comment '实例id',
   catalog_id           int not null comment '所属项目id',
   task_name            varchar(100) not null comment '任务名称',
   task_type            varchar(20) not null comment '任务类型: 定时器, 虚拟任务...',
   task_plugin          text not null comment '任务执行插件',
   task_body            text comment '任务内容',
   exec_cron_exp        varchar(50) comment '执行频次quartz表达式',
   agent_host_group     varchar(50) comment '将要在哪个Agent组运行该任务',
   agent_host_run       varchar(50) comment '实际在哪个Agent运行该任务',
   agent_host_run_his   text comment '在哪些Agent运行过该任务，用分号分隔',
   linux_run_user       varchar(50) comment 'linux运行用户',
   queue_id             int comment '所属运行队列',
   priority             int comment '优先级',
   max_run_num          int not null default 3 comment '同时运行的最大实例数',
   max_run_sec          int not null default 86400 comment '运行耗时到达该值，任务就kill掉',
   max_retry_num        int not null default 2 comment '任务失败最大重试次数',
   retried_num          int comment '已经重试的次数',
   retry_interval       int not null default 120 comment '重试间隔(秒)',
   is_ignore_error      tinyint not null default 0 comment '最终失败是否可忽略, 1可忽略, 0不可忽略',
   is_one_times         tinyint not null default 0 comment '是否一次性任务, 1是, 0否',
   offline_time         datetime not null comment '下线时间',
   ready_time           datetime comment '准备好要运行的时间',
   start_time           datetime comment '开始时间',
   end_time             datetime comment '结束时间',
   status               varchar(20) comment '状态',
   is_self_run          tinyint not null default 0 comment '是否只运行自己,不触发后续依赖, 1是, 0否',
   is_force_run         tinyint not null default 0 comment '是否强制运行, 1是, 0否',
   context              text comment '上下文信息',
   schedule_log         text comment '初始化日志',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (task_date, task_id, instance_id),
   key (catalog_id),
   key (task_id),
   key (instance_id)
) comment = '任务实例'
engine = InnoDB
default charset utf8;


/*==============================================================*/
/* Table: instance_depend                                       */
/*==============================================================*/
drop table if exists instance_task_depend;
create table instance_task_depend
(
   task_id              int not null comment '任务id',
   task_date            datetime not null comment '任务的数据日期',
   instance_id          varchar(50) not null comment '实例id',
   pre_task_id          int not null comment '前置依赖的任务id',
   pre_task_date        datetime not null comment '前置依赖的任务的数据日期',
   depend_type          varchar(20) not null comment '依赖类型',
   depend_n             int not null default 1 comment '依赖个数',
   is_same_agent_run    tinyint not null default 0 comment '是否有状态任务，需要在同个agent节点运行, 1是, 0否',
   is_context_depend    tinyint not null default 0 comment '是否依赖上下文, 1是, 0否',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (task_date, task_id, instance_id, pre_task_id, pre_task_date),
   key (pre_task_id, pre_task_date)
) comment = '依赖关系实例'
engine = InnoDB
default charset utf8;


/*==============================================================*/
/* Table: instance_task_log                                     */
/*==============================================================*/
drop table if exists instance_task_log;
create table instance_task_log
(
   task_id              int not null comment '任务id',
   task_date            datetime not null comment '任务的数据日期',
   instance_id          varchar(50) not null comment '实例id',
   log_id          		bigint not null comment '日志id',
   agent_host_run       varchar(50) comment '实际在哪个Agent运行该任务',
   log_path        		varchar(200) comment '日志路径',
   content		        text comment '日志内容',
   primary key (task_id, task_date, instance_id, log_id)
) comment = '任务日志'
engine = InnoDB
default charset utf8;


/*==============================================================*/
/* Table: def_task_param                                        */
/*==============================================================*/
drop table if exists def_task_param;
create table def_task_param
(
   catalog_id           int not null comment '所属项目id',
   param_code           varchar(50) not null comment '参数名',
   param_type           varchar(50) not null comment '参数类型',
   param_value          varchar(300) comment '参数值',
   is_password          tinyint not null default 0 comment '是否为密码类型，1是，0否',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (catalog_id, param_code),
   key (param_code)
) comment = '任务自定义参数'
engine = InnoDB
default charset utf8;


/*==============================================================*/
/* Table: def_catalog                                           */
/*==============================================================*/
drop table if exists def_catalog;
create table def_catalog
(
   catalog_id           int not null auto_increment comment 'id',
   catalog_name         varchar(20) not null comment '名称',
   lessee_id			int not null comment '租户id',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (catalog_id)
) comment = 'catalog信息'
engine = InnoDB
default charset utf8
auto_increment = 10000;


/*==============================================================*/
/* Table: def_lessee                                           */
/*==============================================================*/
drop table if exists def_lessee;
create table def_lessee
(
   lessee_id            int not null auto_increment comment 'id',
   lessee_name          varchar(20) not null comment '名称',
   alert_url            text comment '报警url接口',
   trigger_urls         text comment '任务运行完，触发外部URL列表，英文逗号分隔',
   password             varchar(50) comment '报警url接口，做加密用的密钥',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (lessee_id)
) comment = '租户信息'
engine = InnoDB
default charset utf8
auto_increment = 10000;


/*==============================================================*/
/* Table: def_classify                                          */
/*==============================================================*/
drop table if exists def_classify;
create table def_classify
(
   classify_type        varchar(20) not null comment '分类类别',
   classify_code        varchar(200) not null comment '分类代码',
   task_id          	int not null comment '任务id',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (classify_type, classify_code, task_id)
) comment = '分类信息'
engine = InnoDB
default charset utf8
auto_increment = 10000;


/*==============================================================*/
/* Table: def_object_depend                                     */
/*==============================================================*/
drop table if exists def_object_depend;
create table def_object_depend
(
   rw_flag              tinyint not null default 1 comment '1读，0写',
   task_id              int not null comment '任务id',
   object_id            varchar(200) not null comment '对象ID',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (rw_flag, task_id, object_id),
   key(object_id)
) comment = '读写对象依赖关系'
engine = InnoDB
default charset utf8;


/*==============================================================*/
/* Table: def_agent_group                                       */
/*==============================================================*/
drop table if exists def_agent_group;
create table def_agent_group
(
   agent_host_group     varchar(50) not null comment '执行机所属的组',
   agent_host           varchar(50) not null comment '执行机域名',
   agent_port           int not null default 9022 comment '执行机端口',
   agent_user           varchar(50) not null comment '执行机用户名',
   agent_private_key    varchar(200) not null comment '执行机私钥路径',
   agent_password       varchar(200) not null comment '执行机私钥路径',
   work_base_dir    	varchar(200) not null comment '工作基础目录，插件和任务日志都在这路径下',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (agent_host_group, agent_host)
) comment = '执行机组信息'
engine = InnoDB
default charset utf8;


/*==============================================================*/
/* Table: def_queue                                             */
/*==============================================================*/
drop table if exists def_queue;
create table def_queue
(
   queue_id             int not null auto_increment comment '队列id',
   queue_name           varchar(50) not null comment '队列名称',
   queue_size           int not null default 1 comment '队列允许最大同时执行数',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (queue_id)
) comment = '队列信息'
engine = InnoDB
default charset utf8;


/*==============================================================*/
/* Table: def_monitor_begin                                     */
/*==============================================================*/
drop table if exists def_monitor_begin;
create table def_monitor_begin
(
   cron_exp             varchar(50) not null comment 'cron表达式',
   task_id				int not null comment '任务id',
   move_month			int not null default 0 comment '数据日期=偏移月',
   move_day             int not null default 0 comment '数据日期=偏移天',
   move_hour            int not null default 0 comment '数据日期=偏移小时',
   move_minute          int not null default 0 comment '数据日期=偏移分钟',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (cron_exp, task_id)
) comment = '定时器触发那一刻，相应数据日期有没被执行'
engine = InnoDB
default charset utf8;


/*==============================================================*/
/* Table: def_monitor_dur                                       */
/*==============================================================*/
drop table if exists def_monitor_dur;
create table def_monitor_dur
(
   task_id				int not null comment '任务id',
   dur_sec              int not null comment '耗时(秒)，任务运行耗时达到该值就报警',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (task_id, dur_sec)
) comment = '运行耗时监控'
engine = InnoDB
default charset utf8;


/*==============================================================*/
/* Table: def_monitor_retry                                     */
/*==============================================================*/
drop table if exists def_monitor_retry;
create table def_monitor_retry
(
   task_id				int not null comment '任务id',
   fail_retry_n         int not null comment '第n次失败后报警',
   remarks              text comment '备注',
   is_valid             tinyint not null default 1 comment '是否还可用，1可用，0已下线不可用',
   create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime comment '更新时间',
   create_user          varchar(20) comment '创建用户',
   update_user          varchar(20) comment '更新用户',
   primary key (task_id, fail_retry_n)
) comment = '第n次失败后报警'
engine = InnoDB
default charset utf8;

