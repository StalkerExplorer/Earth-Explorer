package com.earth.explorer.Util;

import android.os.*;
import android.widget.*;
import android.content.*;
import android.view.*;
import android.graphics.*;


public class Toaster
{

	public static void toast(Context context, String msg) {

		Handler handler = null;
		final Toast[] toasts = new Toast[1];
		int millisec=500;

		Handler  mHandler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				toasts[0].show();
			}
		};


		//этот код нужен для установки длительности показа 
		for(int i = 0; i < millisec; i+=2000) {
			toasts[0] = Toast.makeText(context, msg, Toast.LENGTH_SHORT);

			mHandler.sendEmptyMessage(0);
			if(handler == null) {
				handler = new Handler();
				handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							toasts[0].cancel();
						}
					}, millisec);
			}
		}
		//этот код нужен для установки длительности
	}





	public static void toast(Context context, String msg, int millisec) {

		Handler handler = null;
		final Toast[] toasts = new Toast[1];

		Handler  mHandler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				toasts[0].show();
			}
		};


		//этот код нужен для установки длительности показа 
		for(int i = 0; i < millisec; i+=2000) {
			toasts[0] = Toast.makeText(context, msg, Toast.LENGTH_SHORT);

			mHandler.sendEmptyMessage(0);
			if(handler == null) {
				handler = new Handler();
				handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							toasts[0].cancel();
						}
					}, millisec);
			}
		}
		//этот код нужен для установки длительности
	}



}
