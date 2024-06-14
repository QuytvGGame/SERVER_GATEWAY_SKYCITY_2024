package com.sgc.game.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import com.galaxy.framework.util.FileUtils;
import com.galaxy.framework.util.StringUtil;
import com.ggame.accountservice.AgentGame;

public class LogsManager {
	public static int sleepLog = 1800000;
	public static int sleepUpdateGameManager = 45000;
	public static int sleepUpdateCongTrinh = 60000;
	public static int sleepUpdateEvent = 5000;
	public static int sleepAdminModitor = 10000; // 1 phút

	public static int sizeSaveMoneyLog = 300;
	public static int sizeSaveSmsLog = 1;
	public static int sizeSaveInvalidSmsLog = 1;
	public static int sizeSaveCCuLog = 12;
	public static int sizeGiveMoneyLog = 1; // transfer money

	public static void logToFile(String str, String PATH) {
		String fileName = StringUtil.format(new Date(), "yyyy-MM-dd");
		String time = StringUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		String path = PATH + File.separator + fileName;
		String st = time + "**" + str;
		AgentGame.logError(str);
		try {
			FileUtils.forceFolderExist(PATH);
			FileUtils.writeFile(path, st, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logToFile(Throwable e) {
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		logToFile(writer.toString());
	}

	public static void logToFile(Throwable e, int type, String des) {
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		logToFile(writer.toString(), type, des);
	}

	public static void logToFile(String log) {
		String fileName = StringUtil.format(new Date(), "yyyy-MM-dd");
		String time = StringUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		String path = NetworkServer.PATH_EXCEPTION + File.separator + fileName;
		String detailError = time + "**" + log;
		AgentGame.logError(detailError);
		try {
			FileUtils.forceFolderExist(NetworkServer.PATH_EXCEPTION);
			FileUtils.writeFile(path, detailError, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logToFile(String log, int type, String des) {
		String fileName = StringUtil.format(new Date(), "yyyy-MM-dd");
		String time = StringUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		String path = NetworkServer.PATH_EXCEPTION + File.separator + fileName;
		String detailError = time + "**" + log + "  " + des;
		AgentGame.logError(detailError);
		try {
			FileUtils.forceFolderExist(NetworkServer.PATH_EXCEPTION);
			FileUtils.writeFile(path, detailError, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logToFileName(String fileName, String log) {
		String fileName1 = StringUtil.format(new Date(), "yyyy-MM-dd") + "_" + fileName;
		String time = StringUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		String path = NetworkServer.PATH_EXCEPTION + File.separator + fileName1;
		String detailError = time + "** " + log;
		AgentGame.logError(detailError);
		try {
			FileUtils.forceFolderExist(NetworkServer.PATH_EXCEPTION);
			FileUtils.writeFile(path, detailError, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
