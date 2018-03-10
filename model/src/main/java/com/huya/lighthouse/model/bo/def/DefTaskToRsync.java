package com.huya.lighthouse.model.bo.def;

import org.apache.commons.lang.StringUtils;

import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.util.AssertUtils;

/**
 * 
 * 
 */
public class DefTaskToRsync extends AbstractBODefTask {

	private static final long serialVersionUID = -6179524733784745704L;

	private String rsyncIp;

	private int rsyncPort = 873;

	private String rsyncUserName;

	private String rsyncPassword;

	private String rsyncModuleName;

	private String rsyncPathUnderModule;

	private String dataPaths;

	public DefTaskToRsync() {
		this.setTaskType(TaskType.TORSYNC.name());
		this.setTaskPlugin("java -Xms512m -Xmx512m -cp $pluginDir/lighthouse-plugin-toRsync-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.huya.lighthouse.plugin.toRsync.ToRsyncMain");
	}

	public String getRsyncIp() {
		return rsyncIp;
	}

	public void setRsyncIp(String rsyncIp) {
		this.rsyncIp = rsyncIp;
	}

	public int getRsyncPort() {
		return rsyncPort;
	}

	public void setRsyncPort(int rsyncPort) {
		this.rsyncPort = rsyncPort;
	}

	public String getRsyncUserName() {
		return rsyncUserName;
	}

	public void setRsyncUserName(String rsyncUserName) {
		this.rsyncUserName = rsyncUserName;
	}

	public String getRsyncPassword() {
		return rsyncPassword;
	}

	public void setRsyncPassword(String rsyncPassword) {
		this.rsyncPassword = rsyncPassword;
	}

	public String getRsyncModuleName() {
		return rsyncModuleName;
	}

	public void setRsyncModuleName(String rsyncModuleName) {
		this.rsyncModuleName = rsyncModuleName;
	}

	public String getRsyncPathUnderModule() {
		return rsyncPathUnderModule;
	}

	public void setRsyncPathUnderModule(String rsyncPathUnderModule) {
		this.rsyncPathUnderModule = rsyncPathUnderModule;
	}

	public String getDataPaths() {
		return dataPaths;
	}

	public void setDataPaths(String dataPaths) {
		this.dataPaths = dataPaths;
	}

	@Override
	public void doAssert() throws Exception {
		super.doAssert();
		AssertUtils.assertTrue(StringUtils.isNotBlank(rsyncIp), "rsyncIp cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(rsyncUserName), "rsyncUserName cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(rsyncPassword), "rsyncPassword cannot be blank!");
		AssertUtils.assertTrue(StringUtils.isNotBlank(rsyncModuleName), "rsyncModuleName cannot be blank!");

	}

	@Override
	public void doTrim() {
		super.doTrim();
		this.rsyncIp = StringUtils.trim(this.rsyncIp);
		this.rsyncUserName = StringUtils.trim(this.rsyncUserName);
		this.rsyncPassword = StringUtils.trim(this.rsyncPassword);
		this.rsyncModuleName = StringUtils.trim(this.rsyncModuleName);
		this.rsyncPathUnderModule = StringUtils.trim(this.rsyncPathUnderModule);
		this.dataPaths = StringUtils.trim(this.dataPaths);
	}
}
