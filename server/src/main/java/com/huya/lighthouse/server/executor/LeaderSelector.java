package com.huya.lighthouse.server.executor;

import java.util.UUID;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.server.factory.PropertiesFactory;
import com.huya.lighthouse.server.scheduler.QuartzScheduler;

public class LeaderSelector {

	private static Logger logger = LoggerFactory.getLogger(LeaderSelector.class);

	public static boolean isLeader = false;
	private static boolean isNoZK = true;

	public static void select() throws Exception {
		if (isNoZK) {
			isLeader = true;
			SceneRestorer.restore();
			return;
		}
		
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString(PropertiesFactory.zkConn).retryPolicy(new RetryNTimes(10, 1000)).connectionTimeoutMs(60000).build();
		client.start();

		LeaderLatch leaderLatch = new LeaderLatch(client, PropertiesFactory.zkPath, UUID.randomUUID().toString());
		leaderLatch.addListener(new LeaderLatchListener() {

			@Override
			public void isLeader() {
				logger.info("I am from notLeader to leader");
				isLeader = true;
				try {
					SceneRestorer.restore();
				} catch (Exception e) {
					logger.error("Scene restore error", e);
				}
			}

			@Override
			public void notLeader() {
				logger.info("I am from leader to notLeader");
				isLeader = false;
				try {
					QuartzScheduler.shutdownAll();
				} catch (Exception e) {
					logger.error("QuartzScheduler.shutdownAll error", e);
				}
			}
		});
		leaderLatch.start();
	}
	
	public static void checkIsLeader() throws Exception {
		if (!isLeader) {
			throw new Exception("server leader is switching, please wait 1 miniute!");
		}
	}
}
