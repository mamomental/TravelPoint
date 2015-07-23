package kr.mamo.travelpoint.util;


import android.media.ExifInterface;

import java.io.IOException;

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

	}

}
 