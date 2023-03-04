package com.earth.explorer.Util;

import android.content.*;
import android.os.*;

public class Vibration
{
	public static boolean vibroBoolean=true;

	public static void vibrate(Context context){
		if(vibroBoolean){
			long mills = 50L;
			Vibrator vibrator = (Vibrator) context. getSystemService(Context.VIBRATOR_SERVICE);
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(mills);
			}}}

}
