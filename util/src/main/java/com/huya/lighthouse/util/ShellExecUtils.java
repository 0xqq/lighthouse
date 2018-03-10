package com.huya.lighthouse.util;

import java.io.OutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * apache commons-exec，可执行shell
 * 
 */
public class ShellExecUtils {

	private static Logger logger = LoggerFactory.getLogger(ShellExecUtils.class);

	public static void main(String[] args) throws Exception {
		String program = args[0];
		long timeoutMillis = Long.parseLong(args[1]);
		exec(program, System.out, System.err, timeoutMillis);
	}
	
	public static boolean execQuietly(String program) {
		return execQuietly(program, 0);
	}
	
	public static boolean execQuietly(String program, long timeoutMillis) {
		try {
			exec(program, System.out, System.err, timeoutMillis);
			return true;
		} catch (Exception e) {
			logger.error("exec fail", e);
		}
		return false;
	}
	
	public static void exec(String program) throws Exception {
		exec(program, 0);
	}
	
	public static void exec(String program, long timeoutMillis) throws Exception {
		exec(program, System.out, System.err, timeoutMillis);
	}
	
	public static void exec(String program, OutputStream stdOut, OutputStream errorOut, long timeoutMillis) throws Exception {
		logger.info("program = " + program);
		DefaultExecutor executor = new DefaultExecutor();
		
		if (timeoutMillis > 0) {
			ExecuteWatchdog watchdog = new ExecuteWatchdog(timeoutMillis);
			executor.setWatchdog(watchdog);
		}
		
		CommandLine commandline = CommandLine.parse(program);
		PumpStreamHandler streamHandler = new PumpStreamHandler(stdOut, errorOut);
		executor.setStreamHandler(streamHandler);
		int exitCode = executor.execute(commandline);
		if (exitCode != 0) {
			throw new Exception("exitCode = " + exitCode);
		}
	}
}
