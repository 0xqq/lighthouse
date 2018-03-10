package com.huya.lighthouse.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * remote ssh
 * 
 */
public class SSHUtils {

	private static Logger logger = LoggerFactory.getLogger(SSHUtils.class);

	public static void main(String[] args) throws Exception {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String user = args[2];
		String privateKey = args[3];
		String password = args[4];
		String command = args[5];
		execRetry(host, port, user, privateKey, password, command);
	}
	
	public static String execRetryMsg(String host, int port, String user, String privateKey, String password, String command) {
		SSHMsg sshMsg = execRetry(host, port, user, privateKey, password, command);
		return sshMsg.getStdMsg();
	}
	
	public static SSHMsg execRetry(String host, int port, String user, String privateKey, String password, String command) {
		return execRetry(host, port, user, privateKey, password, command, 40, 3000);
	}

	public static SSHMsg execRetry(String host, int port, String user, String privateKey, String password, String command, int retryTimes, long retrySleepMillis) {
		Exception exception = null;
		for (int i = 0; i < retryTimes; i++) {
			try {
				return exec(host, port, user, privateKey, password, command);
			} catch (Exception e) {
				exception = e;
				logger.error(String.format("%s -> %s:%s, %s", i, host, port, command), e);
				SleepUtils.sleep(retrySleepMillis);
			}
		}
		throw new RuntimeException(host + ", " + command, exception);
	}

	public static SSHMsg exec(String host, int port, String user, String privateKey, String password, String command) throws Exception {
		JSch jsch = new JSch();
		Session session = null;
		ChannelExec channel = null;
		InputStream inputStream = null;
		ByteArrayOutputStream errorBaos = new ByteArrayOutputStream();
		ByteArrayOutputStream stdBaos = new ByteArrayOutputStream();
		try {
			jsch.addIdentity(privateKey, password);
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(command);
			channel.setInputStream(null);
			channel.setErrStream(errorBaos);
			channel.setOutputStream(stdBaos);
			channel.connect();
			while (!channel.isClosed()) {
				SleepUtils.sleep(200l);
			}
			SSHMsg sshMsg = new SSHMsg(stdBaos.toString(), errorBaos.toString(), channel.getExitStatus());
			return sshMsg;
		} finally {
			IOUtils.closeQuietly(errorBaos);
			IOUtils.closeQuietly(inputStream);
			closeQuietly(session, channel);
		}
	}

	public static void sftpPutRetry(String host, int port, String user, String privateKey, String password, String src, String dst, int retryTimes, long retrySleepMillis) throws Exception {
		Exception exception = null;
		for (int i = 0; i < retryTimes; i++) {
			try {
				sftpPut(host, port, user, privateKey, password, src, dst);
				return;
			} catch (Exception e) {
				exception = e;
				logger.error(String.format("%s -> %s:%s, %s, %s", i, host, port, src, dst), e);
				SleepUtils.sleep(retrySleepMillis);
			}
		}
		if (exception != null) {
			throw exception;
		}
	}
	
	public static void sftpPut(String host, int port, String user, String privateKey, String password, String src, String dst) throws Exception {
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp channel = null;
		try {
			jsch.addIdentity(privateKey, password);
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			channel.put(src, dst);
		} finally {
			closeQuietly(session, channel);
		}
	}
	
	public static void sftpPutRetry(String host, int port, String user, String privateKey, String password, InputStream inputStream, String dst, int retryTimes, long retrySleepMillis) throws Exception {
		Exception exception = null;
		for (int i = 0; i < retryTimes; i++) {
			try {
				sftpPut(host, port, user, privateKey, password, inputStream, dst);
				return;
			} catch (Exception e) {
				exception = e;
				logger.error(String.format("%s -> %s:%s, %s", i, host, port, dst), e);
				SleepUtils.sleep(retrySleepMillis);
			}
		}
		if (exception != null) {
			throw exception;
		}
	}

	public static void sftpPut(String host, int port, String user, String privateKey, String password, InputStream inputStream, String dst) throws Exception {
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp channel = null;
		try {
			jsch.addIdentity(privateKey, password);
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			channel.put(inputStream, dst);
		} finally {
			closeQuietly(session, channel);
		}
	}
	
	public static void sftpPutContentRetry(String host, int port, String user, String privateKey, String password, String content, String dst, int retryTimes, long retrySleepMillis) throws Exception {
		Exception exception = null;
		for (int i = 0; i < retryTimes; i++) {
			try {
				sftpPutContent(host, port, user, privateKey, password, content, dst);
				return;
			} catch (Exception e) {
				exception = e;
				logger.error(String.format("%s -> %s:%s, %s", i, host, port, dst), e);
				SleepUtils.sleep(retrySleepMillis);
			}
		}
		if (exception != null) {
			throw exception;
		}
	}

	public static void sftpPutContent(String host, int port, String user, String privateKey, String password, String content, String dst) throws Exception {
		InputStream inputStream = new ByteArrayInputStream(content.getBytes());
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp channel = null;
		try {
			jsch.addIdentity(privateKey, password);
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			channel.put(inputStream, dst);
		} finally {
			IOUtils.closeQuietly(inputStream);
			closeQuietly(session, channel);
		}
	}

	public static Vector<LsEntry> sftpLs(String host, int port, String user, String privateKey, String password, String dir) {
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp channel = null;
		try {
			jsch.addIdentity(privateKey, password);
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			@SuppressWarnings("unchecked")
			Vector<LsEntry> lsResult = channel.ls(dir);
			return lsResult;
		} catch (Exception e) {
			String errorMsg = String.format("%s:%s, ls %s", host, port, dir);
			logger.error(errorMsg, e);
			return null;
		} finally {
			closeQuietly(session, channel);
		}
	}

	public static void sftpGet(String host, int port, String user, String privateKey, String password, String src, OutputStream dst) {
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp channel = null;
		try {
			jsch.addIdentity(privateKey, password);
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			channel.get(src, dst);
		} catch (Exception e) {
			String errorMsg = String.format("%s:%s, get %s", host, port, src);
			logger.error(errorMsg, e);
		} finally {
			closeQuietly(session, channel);
		}
	}

	public static void scpTo(String host, int port, String user, String privateKey, String password, String lFile, String rFile) throws Exception {
		File _lfile = new File(lFile);
		FileInputStream lFileStream = FileUtils.openInputStream(_lfile);
		String lFileName = null;
		if (lFile.lastIndexOf('/') > 0) {
			lFileName = lFile.substring(lFile.lastIndexOf('/') + 1);
		} else {
			lFileName = lFile;
		}
		long filesize = _lfile.length();
		long lFileModifiedSec = _lfile.lastModified() / 1000;
		scpTo(host, port, user, privateKey, password, lFileStream, lFileName, filesize, lFileModifiedSec, rFile);
	}

	public static void scpTo(String host, int port, String user, String privateKey, String password, InputStream lFileStream, String lFileName, long lFileSize, long lFileModifiedSec, String rFile) throws Exception {
		if (lFileStream == null || StringUtils.isBlank(lFileName)) {
			throw new Exception("lFile is null.");
		}
		Session session = null;
		Channel channel = null;
		OutputStream out = null;
		InputStream in = null;
		JSch jsch = new JSch();
		try {
			jsch.addIdentity(privateKey, password);
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			// exec 'scp -p -t rfile' remotely
			String command = "source /etc/profile && scp -p -t " + rFile;
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// get I/O streams for remote scp
			out = channel.getOutputStream();
			in = channel.getInputStream();

			channel.connect();

			if (checkAck(in) != 0) {
				throw new Exception("check inputStream error");
			}

			command = "T " + lFileModifiedSec + " 0";
			// The access time should be sent here,
			// but it is not accessible with JavaAPI ;-<
			command += (" " + lFileModifiedSec + " 0\n");
			out.write(command.getBytes());
			out.flush();
			if (checkAck(in) != 0) {
				throw new Exception("check inputStream error");
			}

			// send "C0644 filesize filename", where filename should not include
			// '/'
			command = "C0644 " + lFileSize + " " + lFileName + "\n";
			out.write(command.getBytes());
			out.flush();
			if (checkAck(in) != 0) {
				throw new Exception("check inputStream error");
			}

			// send a content of lfile
			byte[] buf = new byte[1024];
			while (true) {
				int len = lFileStream.read(buf, 0, buf.length);
				if (len <= 0)
					break;
				out.write(buf, 0, len);
			}
			out.flush();
			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
			if (checkAck(in) != 0) {
				throw new Exception("check inputStream error");
			}
		} finally {
			IOUtils.closeQuietly(lFileStream);
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
			closeQuietly(session, channel);
		}
	}

	private static int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				logger.error(sb.toString());
			}
			if (b == 2) { // fatal error
				logger.error(sb.toString());
			}
		}
		return b;
	}

	private static void closeQuietly(Session session, Channel channel) {
		try {
			if (channel != null) {
				channel.disconnect();
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		try {
			if (session != null) {
				session.disconnect();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public static class SSHMsg {
		private String stdMsg;
		private String errorMsg;
		private int exitStatus;

		public SSHMsg(String stdMsg, String errorMsg, int exitStatus) {
			super();
			this.stdMsg = stdMsg;
			this.errorMsg = errorMsg;
			this.exitStatus = exitStatus;
		}

		public SSHMsg() {
		}

		public boolean isSuccess() {
			return exitStatus == 0;
		}

		public String getStdMsg() {
			return stdMsg;
		}

		public void setStdMsg(String stdMsg) {
			this.stdMsg = stdMsg;
		}

		public String getErrorMsg() {
			return errorMsg;
		}

		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}

		public int getExitStatus() {
			return exitStatus;
		}

		public void setExitStatus(int exitStatus) {
			this.exitStatus = exitStatus;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
	}
}
