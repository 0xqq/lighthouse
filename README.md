灯塔离线数据开发平台
===================

一、简介
---------------------
<pre>
一个任务排队调度系统

简单、集群化、高可用、高扩展性

借用jsch框架，带密码ssh拉起远程机器任务

交流Q群：570178360
</pre>

二、架构
---------------------
![image](https://github.com/meteorchenwu/lighthouse/blob/master/doc/%E6%9E%B6%E6%9E%84/lighthouse%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

三、产品示意图
---------------------
![image](https://github.com/meteorchenwu/lighthouse/blob/master/doc/%E9%A1%B5%E9%9D%A2%E5%8E%9F%E5%9E%8B/%E6%A6%82%E5%86%B5.jpg)

四、代码入口
---------------------
<pre>
目前前台页面暂时没有公布出来，关键代码入口如下：

任务的增删改查：com.huya.lighthouse.service.impl.BODefTaskServiceImpl

任务人工操作入口：com.huya.lighthouse.service.impl.TaskActionServiceImpl

引擎入口：com.huya.lighthouse.server.TaskAction

带密码ssh生成类：com.huya.lighthouse.util.KeyGen
</pre>