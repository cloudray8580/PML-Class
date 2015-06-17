package com.example.timemanager.tools;

import com.example.timemanager.TimerActivity;
import com.example.timemanager.data.DataUtil;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
//import java.sql.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.timemanager.data.DataBaseHelper;



public class TimeManager {
	
	DataBaseHelper dbHelper;
	//用户ID
	private String UserID;
	
	public TimeManager(Context context) {
		dbHelper = new DataBaseHelper(context, "TimeManager.db", null, 1);
		UserID = DataUtil.getData(context,"user", "username");
		Log.e("user_name_received", UserID);
	}
	
	public int getTotalUsageTime(){
		int totalUsageTime = 0;
		double total = 0;
		// 获得历史上总共使用时间
		// 返回分钟
		
		//查找指定用户的时间使用情况
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select name, action, time from record " +
				                    "where name == ?", new String[]{UserID});
		//每段时间的起始和终止
		DATE begin = null;
		DATE end = null;
		
		//遍历该用户的所有的记录
		String name;
		int action = -1;
		String time = null;
		DATE date = null;
		
		if (cursor.moveToFirst()){
			do {
				name = cursor.getString(cursor.getColumnIndex("name"));
				action = cursor.getInt(cursor.getColumnIndex("action"));
				//获得Calendar对象，即日期的容器
				time = cursor.getString(cursor.getColumnIndex("time"));
				date = new DATE();
				date.StringToDate(time);
			    if (action == 2) {
			    	begin = date;
			    }
			    if (action == 3) {
			    	if ( begin != null ) {
			    		end = date;
			    		DATE distance = end.sub(begin);
			    		total += distance.interval();
			    		
			    	}
			    	end = null;
			    	begin = null;
			    }
				Log.d(name, "" + date.getY() + " " + date.getM() + " " + date.getD() +  " "
					  +date.getMin() + " " + date.getS() + "Total is  " + total);
				
				
			} while (cursor.moveToNext());
		}
		// 单独处理最后一行的数据
        if (action != 3 && action != -1) {
        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		     
        	String current_time_str = sdf.format(new java.util.Date());  
        	DATE current_time = new DATE();
        	current_time.StringToDate(current_time_str);
        	DATE distance = current_time.sub(begin);
    		total += distance.interval();
    		
    		Log.d("" + 11, "test test!! " +begin.getD() +" " + begin.getH() + " " + begin.getMin() +" "+ begin.getS() + " " +" interval is : " + distance.interval());
    		Log.d("" + 10, "test test!! " + current_time_str +" interval is : " + distance.interval());
        }
		
		
		
		
		BigDecimal ss =  new BigDecimal(Double.toString(total));
		ss.setScale(0, BigDecimal.ROUND_HALF_UP);
		totalUsageTime = ss.intValue();
		Log.d("1",  "TotalUsageTime is  " + totalUsageTime);
		return totalUsageTime;
	}
	
	public int getTotalAvailableTime() {
		int totalAvailableTime = 0;
		double total = 0;
		// 获得历史上总共使用时间
		// 返回分钟
		
		//查找指定用户的时间使用情况
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select name, action, time from record " +
				                    "where name == ?", new String[]{UserID});
		//每段时间的起始和终止
		DATE begin = null;
		DATE end = null;
		
		String time = null;
		DATE date = null;
		int action = -1;
		String name = null;
		//遍历该用户的所有的记录
		if (cursor.moveToFirst()){
			do {
				name = cursor.getString(cursor.getColumnIndex("name"));
				action = cursor.getInt(cursor.getColumnIndex("action"));
				//获得Calendar对象，即日期的容器
				time = cursor.getString(cursor.getColumnIndex("time"));
				date = new DATE();
				date.StringToDate(time);
			    if ( action == 0 || action == 2) {
			    	begin = date;
			    }
			    if ( action == 1 || action == 3) {
			    	if ( begin != null ) {
			    		end = date;
			    		DATE distance = end.sub(begin);
			    		total += distance.interval();
			    		
			    	}
			    	end = null;
			    	begin = null;
			    }
				Log.d(name, "" + date.getY() + " " + date.getM() + " " + date.getD() +  " "
					  +date.getMin() + " " + date.getS() + "Total is  " + total);
				
				
			} while (cursor.moveToNext());
		}
		
		   if (action == 0 || action == 2) {
	        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		     
	        	String current_time_str = sdf.format(new java.util.Date());  
	        	DATE current_time = new DATE();
	        	current_time.StringToDate(current_time_str);
	        	DATE distance = current_time.sub(begin);
	    		total += distance.interval();
	    		
	    		Log.d("" + 11, "test test!! " +begin.getD() +" " + begin.getH() + " " + begin.getMin() +" "+ begin.getS() + " " +" interval is : " + distance.interval());
	    		Log.d("" + 10, "test test!! " + current_time_str +" interval is : " + distance.interval());
	        }
		BigDecimal ss =  new BigDecimal(Double.toString(total));
		ss.setScale(0, BigDecimal.ROUND_HALF_UP);
		totalAvailableTime = ss.intValue();
		Log.d("1",  "TotalUsageTime is  " + totalAvailableTime);
		return totalAvailableTime;
	}
	
	public int getOneDayTotalUsageTime(Date date1) {
		double total = 0;
		int oneDayTotalUsageTime = 0;
		// 获得某一天内总共使用时间
		// 返回分钟
		
		//日期转串，方便后面处理
		//String sdate=(new SimpleDateFormat("yyyy-MM-dd")).format(date1); 
		
		 //date1.
		 //DateFormat df1 = DateFormat.getDateInstance(DateFormat.LONG, Locale.CHINA );
		 //String d1 = df1.format(date1);
		 //Log.d("IngetOneDayTotal",d1);
		
		java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String s;
		s = format1.format(date1);
		
		String date2[] =  s.split("-");
		 
		Log.d("IngetOneDayTotal",date2[ 0 ] + " " + date2[ 1 ] +" " + date2[ 2 ]);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select name, action, time from record " +
				                    "where  strftime('%Y', time) == ? and " +
				                    "strftime('%m', time) == ?" +
				                    "and strftime('%d', time) == ?", new String[]{date2[ 0 ], date2[ 1 ], date2[ 2 ]});
				                    
		DATE begin = null;
		DATE end = null;
		
		//遍历该用户的所有的记录
		if (cursor.moveToFirst()){
			do {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				int action = cursor.getInt(cursor.getColumnIndex("action"));
				//获得Calendar对象，即日期的容器
				String time = cursor.getString(cursor.getColumnIndex("time"));
				DATE date = new DATE();
				date.StringToDate(time);
			    if (action == 2) {
			    	begin = date;
			    }
			    if (action == 3) {
			    	if ( begin != null ) {
			    		end = date;
			    		DATE distance = end.sub(begin);
			    		total += distance.interval();
			    		
			    	}
			    	end = null;
			    	begin = null;
			    }
				Log.d(name, "" + date.getY() + " " + date.getM() + " " + date.getD() +  " "
					  +date.getMin() + " " + date.getS() + "Total is  " + total);
				
				
			} while (cursor.moveToNext());
		}
		BigDecimal ss =  new BigDecimal(Double.toString(total));
		ss.setScale(0, BigDecimal.ROUND_HALF_UP);
		oneDayTotalUsageTime = ss.intValue();
	    return oneDayTotalUsageTime;
	}
	
	public int getOneDayTotalAvailableTime(Date date1) {
		int oneDayTotalAvailableTime = 0;
		double total = 0;
		// 获得某一天内总共使用时间
		// 返回分钟
		
		//日期转串，方便后面处理
		//String sdate=(new SimpleDateFormat("yyyy-MM-dd")).format(date1); 
		
		 //date1.
		 //DateFormat df1 = DateFormat.getDateInstance(DateFormat.LONG, Locale.CHINA );
		 //String d1 = df1.format(date1);
		 //Log.d("IngetOneDayTotal",d1);
		
		java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String s;
		s = format1.format(date1);
		
		String date2[] =  s.split("-");
		 
		Log.d("IngetOneDayTotal",date2[ 0 ] + " " + date2[ 1 ] +" " + date2[ 2 ]);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select name, action, time from record " +
				                    "where  strftime('%Y', time) == ? and " +
				                    "strftime('%m', time) == ?" +
				                    "and strftime('%d', time) == ?", new String[]{date2[ 0 ], date2[ 1 ], date2[ 2 ]});
				                    
		DATE begin = null;
		DATE end = null;
		
		//遍历该用户的所有的记录
		if (cursor.moveToFirst()){
			int A = cursor.getInt(cursor.getColumnIndex("action"));
			if (A == 1 || A == 3) {
				String time = cursor.getString(cursor.getColumnIndex("time"));
				DATE date = new DATE();
				date.StringToDate(time);
				total += (date.getH() - 0) * 60 + (date.getMin() - 0) + (double)(date.getS() - 0) / 60;
			}
			do {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				int action = cursor.getInt(cursor.getColumnIndex("action"));
				//获得Calendar对象，即日期的容器
				String time = cursor.getString(cursor.getColumnIndex("time"));
				DATE date = new DATE();
				date.StringToDate(time);
			    if ( action == 0 || action == 2) {
			    	begin = date;
			    }
			    if ( action == 1 || action == 3) {
			    	if ( begin != null ) {
			    		end = date;
			    		DATE distance = end.sub(begin);
			    		total += distance.interval();
			    		
			    	}
			    	end = null;
			    	begin = null;
			    }
				if (cursor.isLast()) {
					int A1 = cursor.getInt(cursor.getColumnIndex("action"));
					if (A1 == 0 || A1 == 2) {
						String time1 = cursor.getString(cursor.getColumnIndex("time"));
						DATE date11 = new DATE();
						date11.StringToDate(time);
						total += (23 - date11.getH()) * 60 + (60 - date11.getMin()) + (double)(0 - date11.getS()) / 60;
					}
					
				}
			    
			    Log.d(name, "" + date.getY() + " " + date.getM() + " " + date.getD() +  " "
					  +date.getMin() + " " + date.getS() + "Total is  " + total);
				
				
			} while (cursor.moveToNext());
		}
		BigDecimal ss =  new BigDecimal(Double.toString(total));
		ss.setScale(0, BigDecimal.ROUND_HALF_UP);
		oneDayTotalAvailableTime = ss.intValue();
		return oneDayTotalAvailableTime;
	}
	
	// !!! 下面这两个函数最为重要，优先完成下面这两个！！！
	// 关于下面的时间段，我大致想分为六组：
	// 0~8 8~12 12~14 14~18 18~20 20~24
	//具体范围为[0,8) [8,12) ...
	// 或者合并为4组 ：凌晨、上午 、下午 、晚上
	public int[] getUsageTimeByPeriod(Date date1) {
		int [] usageTimeByPeriod = {8, 4, 2, 4, 2, 4};
		// 获得一天内各时间段内总共使用时间
		// 返回一个整形数组
		
		return usageTimeByPeriod;
	}
	
	public int[] getAvailableTimeByPeriod(Date date1, int taskType) {
		int [] availableTimeByPeriod = new int [ 6 ];
		@SuppressWarnings("unchecked")
		LinkedList<Tuple> TimeInterval[] = new LinkedList[ 6 ];
		double [] total = new double [ 6 ];
		for (int i  = 0; i <= 5; ++i) {
			TimeInterval[ i ] = new LinkedList<Tuple>();
			total[ i ] = 0;
		}
		// 获得一天内各时间段内总共有效时间
		// 返回一个整形数组
		Log.e("hello i'm in 338","!!!!!!!!!!!!!!!1");
		
		java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String s;
		s = format1.format(date1);
		
		String date2[] =  s.split("-");
		Log.e(date2[0] + ' ' + date2[ 1 ] + ' ' + date2[ 2 ], s);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select name, action, time from record " +
                "where  strftime('%Y', time) == ? and " +
                "strftime('%m', time) == ?" +
                "and strftime('%d', time) == ? and pid == ?", new String[]{date2[ 0 ], date2[ 1 ], date2[ 2 ], Integer.toString(taskType)});
		Log.e("hello i'm in 345","!!!!!!!!!!!!!!!!!!" + cursor.getCount());
		//遍历所有查到的记录
		if (cursor.moveToFirst()){
			do {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				int action = cursor.getInt(cursor.getColumnIndex("action"));
				//获得Calendar对象，即日期的容器
				String time = cursor.getString(cursor.getColumnIndex("time"));
				Log.e(time,	"i'm in 359");
				DATE date = new DATE();
				date.StringToDate(time);
				//获得时间区间
				int index = getIndex(date.getH());
				Log.e(Integer.toString(index),"i'm 364");
				Tuple record = new Tuple(date.getH(), date.getMin(),date.getS(),action);
				TimeInterval[ index ].add(record);
				
			} while (cursor.moveToNext());
		}
		Log.e("hello i'm in 362","!!!!!!!!!!!!!!!!!!");
		/*测试时间点分发是否正确*/
		for (int i = 0; i <= 5; ++i) {
			for (Tuple element : TimeInterval[ i ]) {
				Log.e("" + i, element.toString());
			}
		}
		for (int i = 0; i <= 5; ++i ) {
			//处理一段学习时间被两个时间段分开的情况
			if (!TimeInterval[ i ].isEmpty()) {
			  total[ i ] += deal_with_head((Tuple)(TimeInterval[ i ].getFirst()), i);
			  total[ i ] += deal_with_end((Tuple)(TimeInterval[ i ].getLast()), i);
			  Log.e(i + "","interval: " + i + "s total available time is: " + total[ i ]);
			  Tuple begin = null;
			  Tuple end = null;
			  for (Tuple ele : TimeInterval[ i ]) {
				  if (ele.getA() == 0 || ele.getA() == 2) {
					  begin = ele;
				  }
				  if (ele.getA() == 1 || ele.getA() == 3) {
					  if ( begin != null ) {
			    		  end = ele;
			    		  Tuple distance = end.sub(begin);
			    		  total[ i ] += distance.interval();
			    	  }
			    	  end = null;
			    	  begin = null;
				  }
				 
			  }
		    }
			Log.e(i + "","interval: " + i + "s total available time is: " + total[ i ]);
		}
		Log.e("hello i'm in 395","");
		for (int i = 0; i <= 5; ++i) {
			BigDecimal ss =  new BigDecimal(Double.toString(total[ i ]));
			ss.setScale(0, BigDecimal.ROUND_HALF_UP);
			availableTimeByPeriod[ i ] = ss.intValue();
		}
		return availableTimeByPeriod;
	}
	public static int getIndex(int hour) {
		if (hour >= 0 && hour < 8) return 0;
		if (hour >= 8 && hour <12 ) return 1;
		if (hour >= 12 && hour < 14) return 2;
		if (hour >= 14 && hour < 18) return 3;
		if (hour >= 18 && hour < 20) return 4;
		if (hour >= 20 && hour <= 23) return 5;
		return -1;
	}
	
	//处理时间段内第一个时间点的标记是 1 或 3 的情况
	public static double deal_with_head(Tuple ele, int i) {
		double result = 0;
	    if (ele.getA() == 1 || ele.getA() == 3) {
	    	if ( i == 0 ) {
	    	  result = (ele.getH() - 0) * 60  + (ele.getMin() - 0) + (double)(ele.getS() * 1 / 60); 
	    	}
	    	else if ( i == 1) {
	    		 result = (ele.getH() - 8) * 60  + (ele.getMin() - 0) + (double)(ele.getS() * 1 / 60); 
	    	}
	    	else if ( i == 2) {
	    		 result = (ele.getH() - 12) * 60  + (ele.getMin() - 0) + (double)(ele.getS() * 1 / 60); 
	    	}
	    	else if ( i == 3) {
	    		 result = (ele.getH() - 14) * 60  + (ele.getMin() - 0) + (double)(ele.getS() * 1 / 60); 
	    	}
	    	else if ( i == 4) {
	    		 result = (ele.getH() - 18) * 60  + (ele.getMin() - 0) + (double)(ele.getS() * 1 / 60);
	    	}
	    	else if ( i == 5) {
		         result = (ele.getH() - 20) * 60  + (ele.getMin() - 0) + (double)(ele.getS() * 1 / 60); 
		    }	 
	    }
	    else {
	    	return 0;
	    }
		return result;
	}
	
	public static double deal_with_end(Tuple ele, int i) {
		double result = 0;
	    if (ele.getA() == 0 || ele.getA() == 2) {
	    	if ( i == 0 ) {
	    	  result = ( 8 - ele.getH()) * 60  + (0 - ele.getMin()) + (double)(0 - ele.getS()) * 1 / 60; 
	    	}
	    	else if ( i == 1) {
	    		 result = ( 12 - ele.getH()) * 60  + (0 - ele.getMin()) + (double)(0 - ele.getS()) * 1 / 60; 
	    	}
	    	else if ( i == 2) {
	    		 result = (14 - ele.getH()) * 60  + (0 - ele.getMin()) + (double)(0 - ele.getS()) * 1 / 60; 
	    	}
	    	else if ( i == 3) {
	    		 result = (18 - ele.getH()) * 60  + (0 - ele.getMin()) + (double)(0 - ele.getS()) * 1 / 60; 
	    	}
	    	else if ( i == 4) {
	    		 result = (20 - ele.getH()) * 60  + (0 - ele.getMin()) + (double)(0 - ele.getS()) * 1 / 60;
	    	}
	    	else if ( i == 5) {
		         result = (23 - ele.getH()) * 60  + ( 60 - ele.getMin()) + (double)(0 - ele.getS()) * 1 / 60; 
		    }	 
	    }
	    else {
	    	return 0;
	    }
		return result;
	}
}
