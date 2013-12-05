package com.apple.asset;

/**
 * 
 * @author hushaoping 2012
 * <a>used date </a>
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.content.Context;
import android.text.format.Time;
import android.util.Log;

public class DateHelper {
	/**
	 * get Calendar
	 * 
	 * @return
	 */
	public static Calendar getDateCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * get current Calendar
	 * 
	 * @return
	 */
	public static Calendar getCalendarMill(long second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(second);
		return calendar;
	}

	/**
	 * get current Calendar
	 * 
	 * @return
	 */
	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		return calendar;
	}

	/**
	 * get current year
	 * 
	 * @return
	 */
	public static int getYear() {
		return getDateCalendar(getDate()).get(Calendar.YEAR);
	}

	/**
	 * get current Month
	 * 
	 * @return
	 */
	public static int getMonth() {
		return getDateCalendar(getDate()).get(Calendar.MONTH) + 1;
	}

	/**
	 * get current day
	 * 
	 * @return
	 */
	public static int getHour() {
		return getDateCalendar(getDate()).get(Calendar.HOUR_OF_DAY);
	}

	public static int getDay() {
		return getDateCalendar(getDate()).get(Calendar.DATE);
	}

	/**
	 * get current minute
	 */
	public static int getMinute() {
		return getDateCalendar(getDate()).get(Calendar.MINUTE);
	}

	/**
	 * get current secound
	 * 
	 * @return
	 */
	public static int getSecound() {
		return getDateCalendar(getDate()).get(Calendar.SECOND);
	}

	/**
	 * string to date
	 * 
	 * @param str
	 * @param type
	 * @return
	 */
	public static Date StrToDate(String str, String type) {
		SimpleDateFormat format = new SimpleDateFormat(type);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 
	 * @param date
	 * @param type
	 * @return
	 */
	public static String dateToStr(Date date, String type) {
		SimpleDateFormat format = new SimpleDateFormat(type);
		return format.format(date);
	}

	/**
	 * get month max day
	 * 
	 * @return
	 */
	public static int getDayofMonth() {
		int day = getDateCalendar(getDate()).getActualMaximum(Calendar.DATE);
		return day;
	}

	public static int findDayInMonth(Date date) {
		Calendar calendar = getDateCalendar(date);
		int day = getDateCalendar(date).getActualMaximum(Calendar.DAY_OF_MONTH);
		return day;
	}

	/**
	 * 
	 * @param date
	 * @param msecond
	 * @return
	 */

	public static Calendar addDate(Date date, int msecond) {
		long myTime = date.getTime() + msecond;
		date.setTime(myTime);
		Calendar c = getDateCalendar(date);
		return c;
	}
	
	public static Calendar cutDate(Date date, int msecond) {
		long myTime = date.getTime() - msecond;
		date.setTime(myTime);
		Calendar c = getDateCalendar(date);
		return c;
	}
	
	/**
	 * getDateCalendar(getDate())
	 * 
	 * @return
	 */
	public static int getCurWeek(Calendar c) {
		return c.get(Calendar.DAY_OF_WEEK) - 2;
	}

	/**
	 * get current system time date
	 */
	public static Date getDate() {
		Date date = new Date(System.currentTimeMillis());
		return date;
	}

	public static Calendar setCalendarYear(Calendar calendar, int year) {
		if (year > 0) {
			calendar.set(Calendar.YEAR, year);
		}
		return calendar;
	}

	public static Calendar setCalendarmonth(Calendar calendar, int month) {
		if (month > 0) {
			calendar.set(Calendar.MONTH, month - 1);
		}
		return calendar;
	}

	public static Calendar setCalendarDay(Calendar calendar, int day) {
		if (day > 0) {
			calendar.set(Calendar.DATE, day);
		}
		return calendar;
	}

	/**
	 * ʱ�����time nextTime����
	 * 
	 * @param nextTime
	 * @param time
	 * @return
	 */
	
	public static int getLastTime(int nextTime, String[] time) {
		int lastTime = -1;
		int newMinTime = Integer.parseInt(time[nextTime]) * 60
				+ Integer.parseInt(time[nextTime]);
		int minTime = 0;
		for (int i = 0; i < time.length; i++) {
			int startTime = Integer.parseInt(time[i].substring(0, 2)) * 60
					+ Integer.parseInt(time[i].substring(3, 5));
			if (startTime < newMinTime) {
				if (startTime > minTime) {
					minTime = startTime;
					lastTime = i;
				} else if (minTime == 0) {
					minTime = startTime;
					lastTime = i;
				}
			}
		}
		return lastTime;
	}

	/**
	 * Get first day of week as android.text.format.Time constant.
	 * 
	 * @return the first day of week in android.text.format.Time
	 */
	public static int getFirstDayOfWeek() {
		int startDay = Calendar.getInstance().getFirstDayOfWeek();
		if (startDay == Calendar.SATURDAY) {
			return Time.SATURDAY;
		} else if (startDay == Calendar.MONDAY) {
			return Time.MONDAY;
		} else {
			return Time.SUNDAY;
		}
	}

	/**
	 * @param row
	 *            The row, 0-5, starting from the top.
	 * @param column
	 *            The column, 0-6, starting from the left.
	 * @return The day at a particular row, column
	 */
	public static int getDayAt(int row, int column, int mOffset,
			int mNumDaysInPrevMonth, int mNumDaysInMonth) {
		if (row == 0 && column < mOffset) {
			return mNumDaysInPrevMonth + column - mOffset + 1;
		}
		int day = 7 * row + column - mOffset + 1;
		return (day > mNumDaysInMonth) ? day - mNumDaysInMonth : day;
	}
}
