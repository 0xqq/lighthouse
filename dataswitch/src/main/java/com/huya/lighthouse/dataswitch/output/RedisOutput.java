package com.huya.lighthouse.dataswitch.output;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mvel2.MVEL;
import org.springframework.util.Assert;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

public class RedisOutput implements Output {

	private JedisPool jedisPool;
	private String script;
	// private String beforeScript;
	// private String afterScript;
	private Map context;

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Map getContext() {
		return context;
	}

	public void setContext(Map context) {
		this.context = context;
	}

	@Override
	public void write(List<Object> rows) {
		try {
			batchRedis(jedisPool, rows, context, script);
		} catch (Exception e) {
			throw new RuntimeException("redis error,script:" + script, e);
		}
	}

	@Override
	public void close() {
		jedisPool.destroy();
	}

	public static void batchRedis(JedisPool jedisPool, final List<Object> datas, final Map globalContext, final String script) throws Exception {
		Assert.hasText(script, "script must be not empty");
		Assert.notNull(jedisPool, "jedisPool must be not null");
		String newExpr = String.format("foreach(row : datas) { var redis = redis; %s}", script);
		final Serializable expr = MVEL.compileExpression(newExpr);
		Jedis jedis = jedisPool.getResource();
		try {
			Transaction tran = jedis.multi();
			Map vars = new HashMap();
			if (globalContext != null) {
				vars.putAll(globalContext);
			}
			vars.put("datas", datas);
			vars.put("redis", tran);
			MVEL.executeExpression(expr, vars);
			tran.exec();
		} finally {
			jedis.close();
		}
	}

}
