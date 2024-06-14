package com.ggame.logs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ggame.util.Constant;

import config.*;

public class FileLogger {

	public static boolean VERBOSE = false;

	public static boolean exist(String path) {
		File f = new File(path);
		boolean tmp = f.exists();
		f = null;
		return tmp;
	}

	public static void createDirectory(String path) {
		if (!exist(path))
			(new File(path)).mkdirs();
	}

	public synchronized static void log(String partnerID, String str, int type) {
		String folderName = null;
		String fileType;
		switch (type) {
		case Constant.log_action:
			folderName = "./action";
			fileType = Constant.log_action_file;
			break;
		case Constant.log_exception:
			folderName = "./action_exception";
			fileType = Constant.log_exception_file;
			break;
		case Constant.log_system_exception:
			folderName = "./sys_exception";
			fileType = Constant.system_exception;
			break;
		default:
			folderName = "./action";
			fileType = Constant.log_action_file;
			break;
		}
		try {
			Date date = new Date();

			SimpleDateFormat formatter = new SimpleDateFormat("yy");

			String year = formatter.format(date);

			formatter.applyPattern("MM");
			String month = formatter.format(date);

			formatter.applyPattern("dd");
			String day = formatter.format(date);

			formatter.applyPattern("HH");
			String hour = formatter.format(date);

			formatter.applyPattern("mm");
			String minute = formatter.format(date);

			formatter.applyPattern("ss");
			String second = formatter.format(date);

			formatter.applyPattern("yyMMdd_HH");

			String path =  folderName +"/" + partnerID + "/" + year + "/" + month + "/" + day;

			createDirectory(path);

			String fileName = fileType + ".dat";

			BufferedWriter out = new BufferedWriter(new FileWriter(path
					+ File.separatorChar + fileName, true));

			formatter.applyPattern("hh:mm:ss");

			out.write(formatter.format(date) + "\r" + str);
			out.newLine();
			out.newLine();

			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized static void wslog(String log_path, String partnerID,
			String str, int type) {
		String folderName = null;
		String fileType = null;
		switch (type) {
		case Constant.log_action:
			// folderName = BatchConfig.log_dir_error;
			fileType = Constant.log_action_file;
			break;
		case Constant.log_exception:
			// folderName = BatchConfig.log_dir_info;
			fileType = Constant.log_exception_file;
			break;
		default:
			// folderName = BatchConfig.log_dir_info;
			fileType = Constant.log_action_file;
			break;
		}
		folderName = log_path;
		try {
			Date date = new Date();

			SimpleDateFormat formatter = new SimpleDateFormat("yy");

			String year = formatter.format(date);

			formatter.applyPattern("MM");
			String month = formatter.format(date);

			formatter.applyPattern("dd");
			String day = formatter.format(date);

			formatter.applyPattern("HH");
			String hour = formatter.format(date);

			formatter.applyPattern("mm");
			String minute = formatter.format(date);

			formatter.applyPattern("ss");
			String second = formatter.format(date);

			formatter.applyPattern("yyMMdd_HH");

			String path = folderName + "/" + partnerID + "/" + year + "/"
					+ month + "/" + day;

			createDirectory(path);

			String fileName = fileType + ".dat";

			BufferedWriter out = new BufferedWriter(new FileWriter(path
					+ File.separatorChar + fileName, true));

			formatter.applyPattern("hh:mm:ss");

			out.write(formatter.format(date) + " " + str);
			out.newLine();
			out.newLine();

			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
