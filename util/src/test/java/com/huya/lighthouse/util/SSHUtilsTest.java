package com.huya.lighthouse.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huya.lighthouse.util.SSHUtils.SSHMsg;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHUtilsTest {

	protected static Logger logger = LoggerFactory.getLogger(SSHUtilsTest.class);
	
	@Test
	public void test() throws Exception {
		String host = "14.215.104.210";
		String user = "huya";
		int port = 9022;
		String privateKey = "f:/data/apps/lighthouse/conf/id_rsa";
		JSch jsch = new JSch();
		Session session = null;
		ChannelExec channel = null;
		String command = "source /etc/profile && sudo sudo -u cw1 /bin/bash /home/huya/data_chenwu/${DWENV}.sh";
		command = "sudo sudo -u root cat /home/huya/data_chenwu/test.sh";
		// command =
		// "scp F:/data/key/id_rsa huya@14.215.104.210:/home/huya/data_chenwu/";
		// command = "sudo mkdir -p -m 777 /home/huya/data_chenwu/test2";
		// command = "ls /home/huya/data_chenwu";
		// command = "fre -m | sed -n '3p' | awk '{print $3/$4}'";
		// command =
		// "source /etc/profile && df -h | sed -n '/\\/$/'p | awk '{print $5}' | sed 's/\\%//' && df -h | sed -n '/\\'$light_house_disk'$/'p | awk '{print $5}' | sed 's/\\%//'";
		command = "ls /home/huya/data_chenwu";
//		command = "mkdir -p -m 777 /home/huya/data_chenwu/mk";
		command = "find /home/huya/data_chenwu/sftp -depth -type f -mtime +7 | xargs rm -fr";
		try {
			jsch.addIdentity(privateKey,"9UwkF9nfXQFa0dP9RY8k");
//			jsch.addIdentity(privateKey);
			session = jsch.getSession(user, host, port);
//			session.setPassword("9UwkF9nfXQFa0dP9RY8k");
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(command);
			channel.connect();

			// System.out.println(channel.getExitStatus());
//			InputStream errStream = channel.getErrStream();
//			String errorMsg = IOUtils.toString(errStream);
//			if (StringUtils.isNotBlank(errorMsg)) {
//				throw new Exception(errorMsg);
//			}
			InputStream inputStream = channel.getInputStream();
			String result = IOUtils.toString(inputStream);

			// InputStream input = channel.getInputStream();
			// System.out.println(channel.getExitStatus());
			// System.out.println(IOUtils.toString(channel.getErrStream()));
			// String result = IOUtils.toString(input);

			System.out.println("result=" + result);
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			if (channel != null)
				channel.disconnect();
			if (session != null)
				session.disconnect();
		}
	}

	@Test
	public void testX() throws Exception {
		String host = "14.215.104.210";
		String user = "huya";
		int port = 9022;
		String privateKey = "F:/data/key/id_rsa";
		String password = "9UwkF9nfXQFa0dP9RY8k";
		String src = "F:/data/apps/lighthouse/lib/lighthouse-plugin-hql-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
		String dst = "/home/huya/data_chenwu/mk/lighthouse-plugin-hql-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
		SSHUtils.scpTo(host, port, user, privateKey, password, src, dst);
		
		src = "F:/data/apps/lighthouse/lib/lighthouse-plugin-http-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
		dst = "/home/huya/data_chenwu/mk/lighthouse-plugin-http-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
		SSHUtils.scpTo(host, port, user, privateKey, password, src, dst);
		
		src = "F:/data/apps/lighthouse/lib/lighthouse-plugin-shell-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
		dst = "/home/huya/data_chenwu/mk/lighthouse-plugin-hql-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
		SSHUtils.scpTo(host, port, user, privateKey, password, src, dst);
		
		src = "F:/data/apps/lighthouse/lib/lighthouse-plugin-toJdbc-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
		dst = "/home/huya/data_chenwu/mk/lighthouse-plugin-toJdbc-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
		SSHUtils.scpTo(host, port, user, privateKey, password, src, dst);
		
		src = "F:/data/apps/lighthouse/lib/lighthouse-plugin-toRedis-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
		dst = "/home/huya/data_chenwu/mk/lighthouse-plugin-toRedis-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
		SSHUtils.scpTo(host, port, user, privateKey, password, src, dst);
		System.out.println("Done");
	}
	

	@Test
	public void test1() throws Exception {
		String host = "14.215.104.210";
		String user = "huya";
		int port = 9022;
		String privateKey = "f:/data/apps/lighthouse/conf/id_rsa";
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp channel = null;
		try {
			jsch.addIdentity(privateKey,"9UwkF9nfXQFa0dP9RY8k1");
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			@SuppressWarnings("unchecked")
			Vector<LsEntry> lsResult = channel.ls("/home/huya/data_chenwu/sftp");
			for (LsEntry lsLsEntry : lsResult) {
				if (StringUtils.equals(lsLsEntry.getFilename(), ".") || StringUtils.equals(lsLsEntry.getFilename(), "..")) {
					continue;
				}
				System.out.println(lsLsEntry);
				channel.get("/home/huya/data_chenwu/sftp/" + lsLsEntry.getFilename(), System.out);
				// System.out.println("@@@@@@@@@@@@@@@@@@@@@@");
			}
			channel.put("F:/data/key/*", "/home/huya/data_chenwu/sftp/");
			InputStream lFileStream = new ByteArrayInputStream("============\n".getBytes());
			channel.put(lFileStream, "/home/huya/data_chenwu/sftp/object");
			System.out.println(channel.getExitStatus());
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			if (channel != null)
				channel.disconnect();
			if (session != null)
				session.disconnect();
		}
	}

	@Test
	public void testScpTo() throws Exception {
		String lfile = "F:/data/key/abc.txt";
		lfile = "F:/Downloads/json2.data";
		String rfile = "/home/huya/data_chenwu/${DWENV}/abc.txt";
		String host = "14.215.104.210";
		String user = "huya";
		int port = 9022;
		String privateKey = "F:/data/key/id_rsa";
		String password = "9UwkF9nfXQFa0dP9RY8k";
		SSHUtils.scpTo(host, port, user, privateKey, password, lfile, rfile);
	}

	@Test
	public void testScpTo2() throws Exception {
		String rfile = "/home/huya/data_chenwu/123.txt";
		String host = "14.215.104.210";
		String user = "huya";
		int port = 9022;
		String privateKey = "F:/data/key/id_rsa";
		String password = "9UwkF9nfXQFa0dP9RY8k";
		String str = "11111111111111\n2222222222\n";
		str = IOUtils.toString(FileUtils.openInputStream(new File("F:/Downloads/json2.data")));
		InputStream lFileStream = IOUtils.toInputStream(str);
		String lFileName = "123.txt";
		long lFileSize = str.getBytes().length;
		long lFileModifiedSec = System.currentTimeMillis() / 1000;
		SSHUtils.scpTo(host, port, user, privateKey, password, lFileStream, lFileName, lFileSize, lFileModifiedSec, rfile);
	}

	@Test
	public void test2() throws Exception {
		FileInputStream fis1 = FileUtils.openInputStream(new File("F:/data/key/abc.txt"));
		FileInputStream fis2 = FileUtils.openInputStream(new File("F:/data/key/abc.txt"));
		FileOutputStream fos1 = FileUtils.openOutputStream(new File("F:/data/key/abcO.txt"));
		IOUtils.copy(fis1, fos1);
		IOUtils.copy(fis2, fos1);
		IOUtils.closeQuietly(fis1);
		IOUtils.closeQuietly(fis2);
		IOUtils.closeQuietly(fos1);
	}

	@Test
	public void test3() throws Exception {
		String rfile = "/home/huya/data_chenwu/serial.txt";
		String host = "14.215.104.210";
		String user = "huya";
		int port = 9022;
		String privateKey = "F:/data/key/id_rsa";
		String password = "9UwkF9nfXQFa0dP9RY8k";

		User userInfo = new User(1, "chenwu", 11, true);
		userInfo.getContextMap().put("k1", "v1");
		byte[] userByte = SerializationUtils.serialize(userInfo);
		InputStream lFileStream = new ByteArrayInputStream(userByte);

		String lFileName = "serial.txt";
		long lFileSize = userByte.length;
		long lFileModifiedSec = System.currentTimeMillis() / 1000;
		SSHUtils.scpTo(host, port, user, privateKey, password, lFileStream, lFileName, lFileSize, lFileModifiedSec, rfile);
	}

	@Test
	public void test4() throws Exception {
		ObjectInputStream is = new ObjectInputStream(new FileInputStream("F:/Downloads/serial.txt"));
		User userInfo = (User) is.readObject();
		System.out.println(userInfo);
	}

	@Test
	public void test5() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tdate", new Date());
		paramMap.put("linuxUser", "root");

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("F:/Downloads/serial1.txt"));
		oos.writeObject(paramMap);
		oos.flush();

		ObjectInputStream is = new ObjectInputStream(new FileInputStream("F:/Downloads/serial1.txt"));
		@SuppressWarnings("unchecked")
		Map<String, Object> paramMapResult = (Map<String, Object>) is.readObject();
		System.out.println(paramMapResult);
	}
	
	@Test
	public void test6() throws Exception {
		String cmd = "source /etc/profile && /bin/bash /data/apps/lighthouse//20170506/10000/10003/20170506084000/cron/asyn.sh";
		cmd = "df -h | awk '{print $5}' | sed '1d' | sed 's/%//g'";
		String password = "9UwkF9nfXQFa0dP9RY8k";
		SSHMsg sshMsg = SSHUtils.exec("172.17.5.19", 9022, "huya", "/data/apps/lighthouse/conf/id_rsa", password, cmd);
		System.out.println(sshMsg);
	}
	
	@Test
	public void testx() throws Exception {
		String cmd = "cat /proc/cpuinfo | grep 'processor' | wc -l    &&    uptime | sed 's/^.\\+average:\\ \\+\\([^,]*\\).*/\\1/g'";
		System.out.println(cmd);
	}
}
