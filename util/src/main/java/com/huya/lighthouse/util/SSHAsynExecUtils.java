package com.huya.lighthouse.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.util.SSHUtils.SSHMsg;

public class SSHAsynExecUtils {

	protected static Logger logger = LoggerFactory.getLogger(SSHAsynExecUtils.class);

	public static void main(String[] args) throws Exception {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String user = args[2];
		String privateKey = args[3];
		String password = args[4];
		String program = args[5];
		String taskDir = args[6];
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String linuxUser = args[7];
		long logId = Long.parseLong(args[8]);

		try {
			boolean isMemoryOK = isMemoryOK(host, port, user, privateKey, password);
			boolean isDiskOK = isDiskOK(host, port, user, privateKey, password);
			double loadRate = getLoadRate(host, port, user, privateKey, password);
			if (!isMemoryOK || !isDiskOK || loadRate > 1) {
				throw new Exception("host is busy!");
			}
			mkdir(host, port, user, privateKey, password, taskDir);
			if (!isSubmitted(host, port, user, privateKey, password, taskDir)) {
				sendParamMap(host, port, user, privateKey, password, taskDir, paramMap);
				sendSynSh(host, port, user, privateKey, password, taskDir, program, linuxUser);
				sendAsynSh(host, port, user, privateKey, password, taskDir, logId);
				sendIsRunningSh(host, port, user, privateKey, password, taskDir);
				sendKillSh(host, port, user, privateKey, password, taskDir);
				submitAsynSh(host, port, user, privateKey, password, taskDir);
			}
			boolean isRunning = isRunning(host, port, user, privateKey, password, taskDir);
			while (isRunning) {
				Thread.sleep(5000l);
				kill(host, port, user, privateKey, password, taskDir);
			}

			boolean isSuccess = isSuccess(host, port, user, privateKey, password, taskDir);
			String contextResult = getContextResult(host, port, user, privateKey, password, taskDir);
			logger.info("isSuccess = " + isSuccess + ", contextResult" + contextResult);
			rmSubmittedPath(host, port, user, privateKey, password, taskDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String logPath = getLogPath(taskDir, logId);
		SSHUtils.sftpGet(host, port, linuxUser, privateKey, password, logPath, System.out);
	}

	public static boolean kill(String host, int port, String user, String privateKey, String password, String taskDir) {
		String cmd = "/bin/bash " + getKillPath(taskDir);
		String returnMsg = SSHUtils.execRetryMsg(host, port, user, privateKey, password, cmd);
		if (StringUtils.isBlank(returnMsg)) {
			return true;
		}
		return false;
	}

	public static boolean isSuccess(String host, int port, String user, String privateKey, String password, String taskDir) {
		String cmd = "head -n 1 " + getResultPath(taskDir) + " | cut -b1";
		String returnMsg = SSHUtils.execRetryMsg(host, port, user, privateKey, password, cmd);
		if (StringUtils.equals(returnMsg, "0\n")) {
			return true;
		}
		return false;
	}

	public static String getContextResult(String host, int port, String user, String privateKey, String password, String taskDir) {
		String cmd = "cat " + getContextPath(taskDir);
		return SSHUtils.execRetryMsg(host, port, user, privateKey, password, cmd);
	}

	public static boolean isRunning(String host, int port, String user, String privateKey, String password, String taskDir) {
		String cmd = "/bin/bash " + getIsRunningPath(taskDir);
		String isRunning = SSHUtils.execRetryMsg(host, port, user, privateKey, password, cmd);
		return StringUtils.equals(isRunning, "1\n");
	}

	public static void submitAsynSh(String host, int port, String user, String privateKey, String password, String taskDir) throws Exception {
		StringBuilder cmdBuilder = new StringBuilder("source /etc/profile && ");
		cmdBuilder.append("/bin/bash ").append(getAsynPath(taskDir));
		SSHMsg sshMsg = SSHUtils.execRetry(host, port, user, privateKey, password, cmdBuilder.toString());
		if (!sshMsg.isSuccess()) {
			throw new Exception(sshMsg.getErrorMsg());
		}
	}

	public static void mkdir(String host, int port, String user, String privateKey, String password, String taskDir) {
		String cmd = "mkdir -p -m 777 " + taskDir;
		SSHUtils.execRetry(host, port, user, privateKey, password, cmd);
	}

	public static boolean isSubmitted(String host, int port, String user, String privateKey, String password, String taskDir) {
		String cmd = "ls " + taskDir + " | grep " + getSubmittedFileName();
		String submitted = SSHUtils.execRetryMsg(host, port, user, privateKey, password, cmd);
		if (StringUtils.isBlank(submitted)) {
			return false;
		} else {
			return true;
		}
	}

	public static void rmSubmittedPath(String host, int port, String user, String privateKey, String password, String taskDir) {
		String cmd = "rm -fr " + getSubmittedPath(taskDir);
		SSHUtils.execRetry(host, port, user, privateKey, password, cmd);
	}

	public static void sendParamMap(String host, int port, String user, String privateKey, String password, String taskDir, Map<String, Object> paramMap) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(paramMap);
		oos.flush();
		byte[] objectByte = baos.toByteArray();
		InputStream inputStream = new ByteArrayInputStream(objectByte);
		String dst = getObjectPath(taskDir);
		SSHUtils.sftpPutRetry(host, port, user, privateKey, password, inputStream, dst, 40, 3000l);
	}

	public static void sendSynSh(String host, int port, String user, String privateKey, String password, String taskDir, String program, String linuxUser) throws Exception {
		StringBuilder synContent = new StringBuilder();
		synContent.append("#!/bin/bash\n");
		synContent.append("source /etc/profile\n");
		synContent.append("echo 1 > ").append(getSubmittedPath(taskDir)).append("\n");
		if (StringUtils.isNotBlank(linuxUser)) {
			synContent.append("sudo sudo -u ").append(linuxUser).append(" ");
		}
		synContent.append(program).append(" ").append(taskDir).append("\n");
		synContent.append("echo $? > " + getResultPath(taskDir) + "\n");
		SSHUtils.sftpPutContentRetry(host, port, user, privateKey, password, synContent.toString(), getSynPath(taskDir), 40, 3000l);
	}

	public static void sendAsynSh(String host, int port, String user, String privateKey, String password, String taskDir, long logId) throws Exception {
		StringBuilder asynContent = new StringBuilder();
		asynContent.append("#!/bin/bash\n");
		asynContent.append("source /etc/profile\n");
		asynContent.append("mkdir -p -m 777 ").append(getLogDir(taskDir)).append("\n");
		asynContent.append("echo '' > ").append(getContextPath(taskDir)).append("\n");
		asynContent.append("nohup /bin/bash ").append(getSynPath(taskDir)).append(" > ").append(getLogPath(taskDir, logId)).append(" 2>&1 &\n");
		asynContent.append("echo $! > " + getPidPath(taskDir) + "\n");
		SSHUtils.sftpPutContentRetry(host, port, user, privateKey, password, asynContent.toString(), getAsynPath(taskDir), 40, 3000l);
	}

	public static void sendIsRunningSh(String host, int port, String user, String privateKey, String password, String taskDir) throws Exception {
		StringBuilder isRunningContent = new StringBuilder();
		isRunningContent.append("#!/bin/bash\n");
		isRunningContent.append("source /etc/profile\n");
		isRunningContent.append("ps --no-heading -p `cat " + getPidPath(taskDir) + "` | wc -l\n");
		SSHUtils.sftpPutContentRetry(host, port, user, privateKey, password, isRunningContent.toString(), getIsRunningPath(taskDir), 40, 3000l);
	}

	public static void sendKillSh(String host, int port, String user, String privateKey, String password, String taskDir) throws Exception {
		StringBuilder killContent = new StringBuilder();
		killContent.append("#!/bin/bash\n");
		killContent.append("source /etc/profile\n");
		killContent.append("pid=`cat ").append(getPidPath(taskDir)).append("`\n");
		killContent.append("if [ -z \"$pid\" ];\n");
		killContent.append("then\n");
		killContent.append("  exit 0\n");
		killContent.append("fi\n");
		killContent.append("isExists=`ps --no-heading -p $pid | wc -l`\n");
		killContent.append("if [ $isExists -eq 0 ];\n");
		killContent.append("then\n");
		killContent.append("  exit 0\n");
		killContent.append("else\n");
		killContent.append("  echo KILL > " + getResultPath(taskDir) + "\n");
		killContent.append("  pstree -p $pid | awk -F\"[()]\" '{for(i=0;i<=NF;i++)if($i~/^[0-9]+/)print $i}' | xargs kill \n");
		killContent.append("  exit $?\n");
		killContent.append("fi\n");
		SSHUtils.sftpPutContentRetry(host, port, user, privateKey, password, killContent.toString(), getKillPath(taskDir), 40, 3000l);
	}

	public static double getLoadRate(String host, int port, String user, String privateKey, String password) {
		int i = 0;
		while (i < 3) {
			try {
				String cmd = "cat /proc/cpuinfo | grep 'processor' | wc -l    &&    uptime | sed 's/^.\\+average:\\ \\+\\([^,]*\\).*/\\1/g'";
				String loadStr = SSHUtils.execRetryMsg(host, port, user, privateKey, password, cmd);
				String[] loadStrArr = StringUtils.split(loadStr);
				int cpuCoreNum = Integer.parseInt(loadStrArr[0]);
				double curLoadVal = Double.parseDouble(loadStrArr[1]);
				return curLoadVal / cpuCoreNum;
			} catch (Exception e) {
				logger.error("getLoadVal: " + host, e);
				i++;
				SleepUtils.sleep(3000l);
			}
		}
		return Double.MAX_VALUE;
	}

	public static boolean isDiskOK(String host, int port, String user, String privateKey, String password) {
		int i = 0;
		while (i < 3) {
			try {
				String cmd = "df -h | awk '{print $5}' | sed '1d' | sed 's/%//g'";
				String dfRateStr = SSHUtils.execRetryMsg(host, port, user, privateKey, password, cmd);
				String[] dfRateStrArr = StringUtils.split(dfRateStr);
				for (String dfRateStrItem : dfRateStrArr) {
					int dfRate = Integer.parseInt(dfRateStrItem);
					if (dfRate > 90) {
						return false;
					}
				}
				return true;
			} catch (Exception e) {
				logger.error("isDfOK: " + host, e);
				i++;
				SleepUtils.sleep(3000l);
			}
		}
		return false;
	}

	public static boolean isMemoryOK(String host, int port, String user, String privateKey, String password) {
		double memUseRate = getMemoryUseRate(host, port, user, privateKey, password);
		if (memUseRate > 0.9) {
			return false;
		}
		return true;
	}

	public static double getMemoryUseRate(String host, int port, String user, String privateKey, String password) {
		int i = 0;
		while (i < 3) {
			try {
				String cmd = "free -m | sed -n '2p' | awk '{print $3/$2}'";
				String memUseRateStr = SSHUtils.execRetryMsg(host, port, user, privateKey, password, cmd);
				double memUseRate = Double.parseDouble(memUseRateStr);
				return memUseRate;
			} catch (Exception e) {
				logger.error("getMemoryRate: " + host, e);
				i++;
				SleepUtils.sleep(3000l);
			}
		}
		return 1;
	}
	
	public static void clean(String host, int port, String user, String privateKey, String password, String dir, int keepDays) {
		int i = 0;
		while (i < 3) {
			try {
				String cmd = "find " + dir + " -depth -type f -mtime +" + keepDays + " | xargs rm -fr";
				SSHMsg sshMsg = SSHUtils.execRetry(host, port, user, privateKey, password, cmd);
				if (StringUtils.isNotBlank(sshMsg.getStdMsg())) {	
					logger.info(sshMsg.getStdMsg());
				}
				if (StringUtils.isNotBlank(sshMsg.getErrorMsg())) {	
					logger.error(sshMsg.getErrorMsg());
				}
				return;
			} catch (Exception e) {
				logger.error("clean: " + host, e);
				i++;
				SleepUtils.sleep(3000l);
			}
		}
	}

	public static String getSynPath(String taskDir) {
		return taskDir + "/syn.sh";
	}

	public static String getAsynPath(String taskDir) {
		return taskDir + "/asyn.sh";
	}

	private static String getResultPath(String taskDir) {
		return taskDir + "/result";
	}

	public static String getPidPath(String taskDir) {
		return taskDir + "/pid";
	}

	public static String getObjectPath(String taskDir) {
		return taskDir + "/object";
	}

	public static String getContextPath(String taskDir) {
		return taskDir + "/context";
	}

	public static String getIsRunningPath(String taskDir) {
		return taskDir + "/isRunning.sh";
	}

	public static String getKillPath(String taskDir) {
		return taskDir + "/kill.sh";
	}

	public static String getLogDir(String taskDir) {
		return taskDir + "/logs/";
	}

	public static String getLogPath(String taskDir, long logId) {
		return getLogDir(taskDir) + logId + ".log";
	}

	private static String getSubmittedPath(String taskDir) {
		return taskDir + "/" + getSubmittedFileName();
	}

	public static String getSubmittedFileName() {
		return "submitted";
	}
}
