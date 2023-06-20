package com.wang.scaffold.utils.friendlyId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 月份相关工具类
 *
 * @author xiaoping
 *
 */
public class DateUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
	private static Calendar calendar = Calendar.getInstance();

	/*
	 * 输入日期字符串比如2021-10，返回指定月份第一天的Date
	 */
	public static Date getCurMonthFirstDay(String month) {
		try {
			Date nowDate = sdf.parse(month);
			calendar = Calendar.getInstance();
			calendar.setTime(nowDate);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			return calendar.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 输入日期字符串，返回指定月下个月第一天的Date
	 */
	public static Date getLastMonthFirstDay(String month) {
		try {
			Date nowDate = sdf.parse(month);
			calendar = Calendar.getInstance();
			calendar.setTime(nowDate);
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			return calendar.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws ParseException {
		String month = "2021-10";
		System.out.println(getCurMonthFirstDay(month));
		System.out.println(getLastMonthFirstDay(month));
	}

}
