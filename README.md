灯塔离线数据开发平台
===================

一、简介
---------------------
<pre>
一个任务排队调度系统。
简单、集群化、高可用、高扩展性
</pre>

二、架构
---------------------
![image](https://github.com/meteorchenwu/meteor/blob/master/doc/ppt/meteor%E6%9E%B6%E6%9E%841.jpg)

三、产品示意图
---------------------
![image](https://github.com/meteorchenwu/meteor/blob/master/doc/ppt/meteor%E6%9E%B6%E6%9E%841.jpg)

四、代码入口
<pre>
目前前台页面暂时没有公布出来，前台web-home的操作入口代码：

任务的增删改查：com.huya.lighthouse.service.impl.BODefTaskServiceImpl

任务人工操作入口：com.huya.lighthouse.service.impl.TaskActionServiceImpl

引擎入口：com.huya.lighthouse.server。TaskAction
</pre>