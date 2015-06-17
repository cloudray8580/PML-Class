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
	//�û�ID
	private String UserID;
	
	public TimeManager(Context context) {
		dbHelper = new DataBaseHelper(context, "TimeManager.db", null, 1);
		UserID = DataUtil.getData(context,"user", "username");
		Log.e("user_name_received", UserID);
	}
	
	public int getTotalUsageTime(){
		int totalUsageTime = 0;
		double total = 0;
		// �����ʷ���ܹ�ʹ��ʱ��
		// ���ط���
		
		//����ָ���û���ʱ��ʹ�����
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select name, action, time from record " +
				                    "where name == ?", new String[]{UserID});
		//ÿ��ʱ�����ʼ����ֹ
		DATE begin = null;
		DATE end = null;
		
		//�������û������еļ�¼
		String name;
		int action = -1;
		String time = null;
		DATE date = null;
		
		if (cursor.moveToFirst()){
			do {
				name = cursor.getString(cursor.getColumnIndex("name"));
				action = cursor.getInt(cursor.getColumnIndex("action"));
				//���Calendar���󣬼����ڵ�����
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
		// �����������һ�е�����
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
		// �����ʷ���ܹ�ʹ��ʱ��
		// ���ط���
		
		//����ָ���û���ʱ��ʹ�����
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select name, action, time from record " +
				                    "where name == ?", new String[]{UserID});
		//ÿ��ʱ�����ʼ����ֹ
		DATE begin = null;
		DATE end = null;
		
		String time = null;
		DATE date = null;
		int action = -1;
		String name = null;
		//�������û������еļ�¼
		if (cursor.moveToFirst()){
			do {
				name = cursor.getString(cursor.getColumnIndex("name"));
				action = cursor.getInt(cursor.getColumnIndex("action"));
				//���Calendar���󣬼����ڵ�����
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
		// ���ĳһ�����ܹ�ʹ��ʱ��
		// ���ط���
		
		//����ת����������洦��
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
		
		//�������û������еļ�¼
		if (cursor.moveToFirst()){
			do {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				int action = cursor.getInt(cursor.getColumnIndex("action"));
				//���Calendar���󣬼����ڵ�����
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
		// ���ĳһ�����ܹ�ʹ��ʱ��
		// ���ط���
		
		//����ת����������洦��
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
		
		//�������û������еļ�¼
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
				//���Calendar���󣬼����ڵ�����
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
	
	// !!! ����������������Ϊ��Ҫ�������������������������
	// ���������ʱ��Σ��Ҵ������Ϊ���飺
	// 0~8 8~12 12~14 14~18 18~20 20~24
	//���巶ΧΪ[0,8) [8,12) ...
	// ���ߺϲ�Ϊ4�� ���賿������ ������ ������
	public int[] getUsageTimeByPeriod(Date date1) {
		int [] usageTimeByPeriod = {8, 4, 2, 4, 2, 4};
		// ���һ���ڸ�ʱ������ܹ�ʹ��ʱ��
		// ����һ����������
		
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
		// ���һ���ڸ�ʱ������ܹ���Чʱ��
		// ����һ����������
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
		//�������в鵽�ļ�¼
		if (cursor.moveToFirst()){
			do {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				int action = cursor.getInt(cursor.getColumnIndex("action"));
				//���Calendar���󣬼����ڵ�����
				String time = cursor.getString(cursor.getColumnIndex("time"));
				Log.e(time,	"i'm in 359");
				DATE date = new DATE();
				date.StringToDate(time);
				//���ʱ������
				int index = getIndex(date.getH());
				Log.e(Integer.toString(index),"i'm 364");
				Tuple record = new Tuple(date.getH(), date.getMin(),date.getS(),action);
				TimeInterval[ index ].add(record);
				
			} while (cursor.moveToNext());
		}
		Log.e("hello i'm in 362","!!!!!!!!!!!!!!!!!!");
		/*����ʱ���ַ��Ƿ���ȷ*/
		for (int i = 0; i <= 5; ++i) {
			for (Tuple element : TimeInterval[ i ]) {
				Log.e("" + i, element.toString());
			}
		}
		for (int i = 0; i <= 5; ++i ) {
			//����һ��ѧϰʱ�䱻����ʱ��ηֿ������
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
	
	//����ʱ����ڵ�һ��ʱ���ı���� 1 �� 3 �����
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
