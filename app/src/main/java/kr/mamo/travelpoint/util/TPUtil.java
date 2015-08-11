package kr.mamo.travelpoint.util;


import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import kr.mamo.travelpoint.R;

public class TPUtil {
	static SimpleDateFormat gmtFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
	static SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
	static {
		gmtFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	public static Date convertStringToUTC(String dateString) {
		try {
			return  gmtFormat.parse(dateString);
		} catch (ParseException e) {

		}
		return null;
	}

	public static String convertDateToString(Date d) {
		try {
			return  gmtFormat.parse(d.toString()).toString();
		} catch (ParseException e) {

		}
		return null;
	}

	public static String convertString(String dateString) {
		try {
			Date d = gmtFormat.parse(dateString);
			return d.toString();
		} catch (ParseException e) {

		}
		return "";
	}

	public static String convertString(Context context, String dString) {
		Date d = convertStringToUTC(dString);
		String result = convertString(context, d);

		if (null == result) {
			result = localFormat.format(d);
		}

		return result;
	}

	public static String convertString(Context context, Date d) {
		final long SECOND = 1000;
		final long MINUTE = SECOND * 60;
		final long HOUR = MINUTE * 60;
		final long DAY = HOUR * 24;
		final long WEEK = DAY * 7;

		long time = System.currentTimeMillis() - d.getTime();

		if (0 < time / WEEK) {
			return convertString(d.toString());
		}

		if (0 < time / DAY) {
//			return String.format(context.getString(R.string.difference_time_day), (time / DAY));
			return null;
		}

		if (0 < time / HOUR) {
			return String.format(context.getString(R.string.difference_time_hour), (time / HOUR));
		}

		if (0 < time / MINUTE) {
			return String.format(context.getString(R.string.difference_time_minute), (time / MINUTE));
		}

		if (0 < time / SECOND) {
			return String.format(context.getString(R.string.difference_time_second), (time / SECOND));
		}

		return null;
	}
}
 