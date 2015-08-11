package kr.mamo.travelpoint.util;


import android.content.Context;
import android.content.res.Resources;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.IOException;

import kr.mamo.travelpoint.constant.Constants;

public class DipUtil {
	public static int convertDpToPixel(float dp, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}
}
 