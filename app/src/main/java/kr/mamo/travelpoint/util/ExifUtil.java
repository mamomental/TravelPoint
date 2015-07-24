package kr.mamo.travelpoint.util;


import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

import kr.mamo.travelpoint.constant.Constants;

public class ExifUtil {

	public static void test(String filePath) {
		try {
			ExifInterface exif = new ExifInterface(filePath);
			test2( exif);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void  test2(ExifInterface exif) {
		String datetime = exif.getAttribute(ExifInterface.TAG_DATETIME);
		Log.i(Constants.LOGCAT_TAGNAME, "datetime : " + datetime);
		String latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
		Log.i(Constants.LOGCAT_TAGNAME, "latitude : " + latitude);
		String longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
		Log.i(Constants.LOGCAT_TAGNAME, "longitude : " + longitude);
	}

}
 