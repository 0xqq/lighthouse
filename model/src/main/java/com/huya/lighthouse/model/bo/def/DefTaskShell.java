package com.huya.lighthouse.model.bo.def;

import org.apache.commons.lang.StringUtils;

import com.huya.lighthouse.model.type.TaskType;
import com.huya.lighthouse.util.AssertUtils;

/**
 * 
 *
 */
public class DefTaskShell extends AbstractBODefTask {

	private static final long serialVersionUID = 126953786394600352L;

	private String script;

	public DefTaskShell() {
		this.setTaskType(TaskType.SHELL.name());
		this.setTaskPlugin("java -Xms128m -Xmx128m -cp $pluginDir/lighthouse-plugin-shell-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.huya.lighthouse.plugin.shell.ShellMain");
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public void doAssert() throws Exception {
		super.doAssert();
		AssertUtils.assertTrue(StringUtils.isNotBlank(this.script), "script cannot be blank!");
	}

	@Override
	public void doTrim() {
		super.doTrim();
		this.script = StringUtils.trim(this.script);
	}
}
