package com.example.timemanager.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class DataUtil {

	// 仅添加key为String值的键值对
	public static void saveData(Context context, String sharedPreferencesName, String key, String value) {
		if (context == null)
			return;
		SharedPreferences.Editor editor = 
				context.getSharedPreferences(sharedPreferencesName, Activity.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String getData(Context context, String sharedPreferencesName, String key) {
		if (context == null)
			return "";
		String s = null;
		SharedPreferences pref = 
				context.getSharedPreferences(sharedPreferencesName, Activity.MODE_PRIVATE);
		s = pref.getString(key, "");
		return s;
	}
	
	public void clearData(Context context, String sharedPreferencesName) {
		if (context == null)
			return;
		SharedPreferences.Editor editor = 
				context.getSharedPreferences(sharedPreferencesName, Activity.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
	}
}
