package com.example.timemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {

	private Context mContext;
	
	private static final String CREATE_USER_TABLE = "create table if not exists user"
			+ "(uid integer primary key autoincrement,"
			+ "name text, "
			+ "password text)";
	
	// pid	  : 1 part1 ; 2 part2; ...
	// action : 0 屏幕点亮 ； 1 屏幕熄灭 ；2 任务开始 ；3 任务结束
	private static final String CREATE_RECORD_TABLE = "create table if not exists record"
			+ "(rid integer primary key autoincrement,"
			+ "pid integer,"
			+ "name text,"
			+ "action integer,"
			+ "time timestamp default (datetime('now', 'localtime')))";
	//		+ "time timestamp default CURRENT_TIMESTAMP)";
	// 建表语句每个列后面要有逗号哦！
	
	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_RECORD_TABLE);
		Toast.makeText(mContext, "Create Table Succeed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS user");
		db.execSQL("DROP TABLE IF EXISTS record");
		onCreate(db);
		Toast.makeText(mContext, "Upgrade Table Succeed", Toast.LENGTH_SHORT).show();
	}

}
