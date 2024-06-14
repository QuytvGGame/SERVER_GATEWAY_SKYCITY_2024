/*
 * Copyright (c) 2008 - 2011 VNPT EPAY. All rights reserved
 * 
 * @author: Tieu Cong Thang 
 * @date: May 19, 2011
 * @file: Debug.java
 */
package com.ggame.logs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;



public class Debug {
	
	/** The debug on. */
	private static boolean debugOn = true;
	
	/** The log file. */
	private static PrintStream logFile = null;
	
	/** The log file name. */
	private static String logFileName = "Admin.log";
	
	/** The log file dir. */
	private static String logFileDir = "log";
		
	/**
	 * Log to file.
	 */
	public static void logToFile() {
		try {
			File localFile1 = new File(logFileDir);
			localFile1.mkdirs();

			File localFile2 = new File(localFile1, logFileName);
			showMessage("File size: " + localFile2.length());
			if ((localFile2.exists()) && (localFile2.isFile()) && (localFile2.length() != 0L)) {
				archiveFile(logFileDir, logFileName);
			}

			FileOutputStream localFileOutputStream = new FileOutputStream(localFile2);
			logFile = new PrintStream(localFileOutputStream);
		} catch (Exception localException) {
			showError(localException.getMessage());
			showException(localException);
		}
	}

	public static void setDebugOn(boolean paramBoolean) {
		debugOn = paramBoolean;
	}

	public static boolean isDebugOn() {
		return debugOn;
	}

	/**
	 * Show message.
	 * 
	 * @param param
	 *            the param
	 */
	public static void showMessage(Object param) {
		if (debugOn) {
			System.out.println(getMessagePrefix() + param.toString());
			if (logFile != null) {
				logFile.println(Calendar.getInstance().getTime() + getMessagePrefix() + param);
			}
		}
	}

	/**
	 * Show error.
	 * 
	 * @param paramString
	 *            the param string
	 */
	public static void showError(String paramString) {
		if (debugOn) {
			System.err.println(getMessagePrefix() + paramString);
		}
		if (logFile != null) {
			logFile.println(Calendar.getInstance().getTime() + getMessagePrefix() + paramString);
		}
	}

	/**
	 * Show exception.
	 * 
	 * @param paramException
	 *            the param exception
	 */
	public static void showException(Exception paramException) {
		paramException.printStackTrace();
		if (logFile != null) {
			logFile.println(paramException.getClass().getName() + ":" + paramException.getMessage());
			paramException.printStackTrace(logFile);
		}
	}

	private static String getMessagePrefix() {
		String stackTrace = Thread.currentThread().getStackTrace()[0].getClassName().toString();
		return "(" + Thread.currentThread().getStackTrace()[3].getClassName() + ") :";
		
	}

	/**
	 * Archive file.
	 * 
	 * @param paramString1
	 *            the param string1
	 * @param paramString2
	 *            the param string2
	 */
	private static void archiveFile(String paramString1, String paramString2) {
		try {
			long l = Calendar.getInstance().getTime().getTime();
			File localFile1 = new File(paramString1, paramString2 + "_" + String.valueOf(l));
			File localFile2 = new File(paramString1, paramString2);
			localFile2.renameTo(localFile1);
		} catch (Exception localException) {
		}
	}
}
