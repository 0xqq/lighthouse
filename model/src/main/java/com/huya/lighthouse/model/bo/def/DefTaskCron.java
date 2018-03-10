package com.huya.lighthouse.model.bo.def;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;

import com.huya.lighthouse.model.type.TaskType;

/**
 *
 */
public class DefTaskCron extends AbstractBODefTask {

	private static final long serialVersionUID = 8012690426989220016L;

	private String cronExp;

	private int moveMinute = 0;

	private int moveHour = 0;

	private int moveDay = 0;

	private int moveMonth = 0;

	public DefTaskCron() {
		this.setTaskType(TaskType.CRON.name());
		this.setTaskPlugin("none");
	}

	public String getCronExp() {
		return cronExp;
	}

	public void setCronExp(String cronExp) {
		this.cronExp = cronExp;
	}

	public int getMoveMinute() {
		return moveMinute;
	}

	public void setMoveMinute(int moveMinute) {
		this.moveMinute = moveMinute;
	}

	public int getMoveHour() {
		return moveHour;
	}

	public void setMoveHour(int moveHour) {
		this.moveHour = moveHour;
	}

	public int getMoveDay() {
		return moveDay;
	}

	public void setMoveDay(int moveDay) {
		this.moveDay = moveDay;
	}

	public int getMoveMonth() {
		return moveMonth;
	}

	public void setMoveMonth(int moveMonth) {
		this.moveMonth = moveMonth;
	}

	@Override
	public void doAssert() throws Exception {
		super.doAssert();
		new CronExpression(this.cronExp);
	}

	@Override
	public void doTrim() {
		super.doTrim();
		this.cronExp = StringUtils.trim(this.cronExp);
	}
}
