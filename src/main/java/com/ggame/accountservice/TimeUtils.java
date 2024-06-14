package com.ggame.accountservice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

public class TimeUtils {
	
	public static int getHour() {
		return Calendar.getInstance().get(Calendar.HOUR);
	}
	
	public static int getMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}
	
	public static long getTimeMilisecon() {
		return System.currentTimeMillis();
	}
	
	public static int getSecon() {
		return Calendar.getInstance().get(Calendar.SECOND);
	}
	
	public static int getDateOfMonth() {
		return Calendar.getInstance().get(Calendar.DATE);
	}
	
	public static int getDate() {
		return Calendar.getInstance().get(Calendar.DATE);
	}

	public static int getDateOfWeek() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
		if(day == 0) day = 7  ;
		return day;
	}
	
	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH)+1;
	}
	
	public static int getWeekOfMonth() {
		return Calendar.getInstance().get(Calendar.WEEK_OF_MONTH);
	}
	
	public static int getWeekOfYear() {
		return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
	}
	
	public static int getNumDayOfMonth() {
		LocalDateTime time = LocalDateTime.now(ZoneId.of("UTC+7"));
		LocalDate date = time.toLocalDate();
		return date.lengthOfMonth();
	}
	
}

