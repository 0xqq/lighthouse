package com.huya.lighthouse.model.bo.def;

import org.apache.commons.lang.StringUtils;

import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.util.AssertUtils;

/**
 *
 */
public class DefTaskHttp extends AbstractBODefTask {

	private static final long serialVersionUID = -1647997243161670562L;

	/**
	 * url请求路径 要求：url请求执行的动作，成功返回0，即response.getWriter().print(0)。
	 * 并请开通对agentHostGroup机器的访问权限
	 */
	private String url;

	public DefTaskHttp() {
		this.setTaskType(TaskType.HTTP.name());
		this.setTaskPlugin("java -Xms128m -Xmx128m -cp $pluginDir/lighthouse-plugin-http-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.huya.lighthouse.plugin.http.HttpMain");
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void doAssert() throws Exception {
		super.doAssert();
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.url), "url cannot be blank!");
	}

	@Override
	public void doTrim() {
		super.doTrim();
		this.url = StringUtils.trim(this.url);
	}
}
