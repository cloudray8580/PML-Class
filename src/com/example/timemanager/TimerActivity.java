package com.example.timemanager;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

import com.example.advertisementdemo.Advertisement;
import com.example.timemanager.data.DataBaseHelper;
import com.example.timemanager.data.DataUtil;
import com.example.timemanager.tools.TimeManager;
import com.yvelabs.chronometer2.Chronometer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.Chronometer;
import android.widget.Toast;

/**
 * @author sony-pc
 * ���Ĺ���Activity��������ʵ���������������������򿪻�ͼactivity��Button
 * ���ֵĻ���������һ��Chronometer, �м�һ����ʼ����Button������һ������ͼ��Button
 */
public class TimerActivity extends Activity implements OnClickListener{
	
	Chronometer chronometer; // import com.yvelabs.chronometer2.Chronometer;
//	Chronometer chronometer; // import android.widget.Chronometer;
	Button mainButton;
	Button showAdvertisement; // ������ת�����ҳ
	Button gotoChart;
	Button share;
	static boolean isOn = false;
	int taskType; // BroadcastReceiver ��������Ҫ�� �����֡�ѧϰ��Щ)
	ScreenOnReceiver screenOnReceiver;
	ScreenOffReceiver screenOffReceiver;
//	String[] pauseTime; // ��¼��ʱ����ʱ�䣬Ϊ����һ��������ʱ������Ϊ��ʹstop�������ں�̨��ʱ
//	String[] recoverTime;
//	long pauseTime;
//	long recoverTime;
	DataBaseHelper dbHelper;
	
	private void findView() {
		chronometer = (Chronometer)findViewById(R.id.chronometer);
		chronometer.setTextSize(70.0f);
		mainButton = (Button)findViewById(R.id.mainButton);
		showAdvertisement = (Button)findViewById(R.id.showAdvertisement);
		gotoChart = (Button)findViewById(R.id.gotoChart);
		share = (Button)findViewById(R.id.share);
	}
	
	private void setListener() {
		mainButton.setOnClickListener(this);
		showAdvertisement.setOnClickListener(this);
		gotoChart.setOnClickListener(this);
		share.setOnClickListener(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
		
		Bmob.initialize(this, "1f4db504aaa7ba5294274c998668d463");
		
		Intent intent = getIntent();
		String temp = intent.getStringExtra("TaskType");
		taskType = Integer.parseInt(temp);
		Log.e("taskType", taskType+"");
		findView();
		setListener();
	}

	public void onClick(View v) {
		
		dbHelper = new DataBaseHelper(this, "TimeManager.db", null, 1);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
	//	SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd/HH:mm:ss");
	//	Date curDate = new Date(System.currentTimeMillis());
	//	String str = format.format(curDate);

		switch(v.getId()){
		case R.id.mainButton:
			if (isOn) {
				// �������Ϊִ��״̬��ֹͣ���񣬸�λ��ʱ�������İ�ť��ʽ����������
			//	chronometer.setBase(SystemClock.elapsedRealtime());
				chronometer.reset();
				chronometer.stop();
				
				Toast.makeText(TimerActivity.this, "��λ��ִ��", Toast.LENGTH_SHORT).show();
				
				mainButton.setBackgroundColor(Color.GREEN);
				mainButton.setText("START");
				
				unregisterReceiver(screenOnReceiver);
				unregisterReceiver(screenOffReceiver);
				
				isOn = false;
				
				values.put("pid", taskType);
				values.put("name", DataUtil.getData(TimerActivity.this, "user", "username"));
				values.put("action", 3); // �������			
			//	values.put("time", curDate);
				long a = db.insert("record", null, values);
				if (a == -1)
					Toast.makeText(TimerActivity.this, "���������¼����ʧ��", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(TimerActivity.this, "���������¼����ɹ�", Toast.LENGTH_SHORT).show();
				values.clear();
			} else {
				// �������Ϊ����״̬����ʼ����������ʱ�������İ�ť��ʽ����������,д���¼
			//	chronometer.setBase(SystemClock.elapsedRealtime());
				chronometer.reset();
				chronometer.start();
				
				mainButton.setBackgroundColor(Color.RED);
				mainButton.setText("FINISH");
				
				IntentFilter intentFilterOn = new IntentFilter();
				IntentFilter intentFilterOff = new IntentFilter();
				//ò��Ҫ�ŵ����������
				intentFilterOn.addAction(Intent.ACTION_SCREEN_ON);
				intentFilterOff.addAction(Intent.ACTION_SCREEN_OFF);
				screenOnReceiver = new ScreenOnReceiver();
				screenOffReceiver = new ScreenOffReceiver();
				registerReceiver(screenOnReceiver, intentFilterOn);
				registerReceiver(screenOffReceiver, intentFilterOff);
				
				isOn = true;
				
				values.put("pid", taskType);
				values.put("name", DataUtil.getData(TimerActivity.this, "user", "username"));
				values.put("action", 2); // ����ʼ			
		//		values.put("time", str);
				long a = db.insert("record", null, values);
				if (a != -1)
					Toast.makeText(TimerActivity.this, "��ʼ�����¼����ɹ�", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(TimerActivity.this, "��ʼ�����¼����ʧ��", Toast.LENGTH_SHORT).show();
				values.clear();
			}
			break;
		case R.id.showAdvertisement:
			Intent intentAdv = new Intent(TimerActivity.this, AdvertisementActivity.class);
			startActivity(intentAdv);
			break;
		case R.id.gotoChart:
			Intent intent = new Intent(TimerActivity.this, ChartActivity.class);
			intent.putExtra("TaskType", taskType);
			startActivity(intent);
			break;
		case R.id.share:
			ShareRecord shareRecord = new ShareRecord();
			shareRecord.setUsername(DataUtil.getData(this, "user", "username"));
			TimeManager temp = new TimeManager(this);
			shareRecord.setAvailableTimeToday(temp.getOneDayTotalAvailableTime(new Date()));
			
			shareRecord.save(this, new SaveListener() {

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(TimerActivity.this, "�������ʧ��"+arg1, Toast.LENGTH_SHORT).show();
					Log.e("Insert Data Failed!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!111");
				}

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Toast.makeText(TimerActivity.this, "������ݳɹ�", Toast.LENGTH_SHORT).show();
					Log.e("Insert Data Success!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!111");
	//				Intent intentShare = new Intent(TimerActivity.this, ShareListActivity.class);
	//				startActivity(intentShare);
				}
				
			});
			
			Intent intentShare = new Intent(TimerActivity.this, ShareListActivity.class);
			startActivity(intentShare);
			break;
		default:
			break;
		}
		//!!!
		db.close();
	}
//==============================inner class=====================================
	public class ScreenOnReceiver extends BroadcastReceiver{

		Context mContext;
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			dbHelper = new DataBaseHelper(context, "TimeManager.db", null, 1);
			mContext = context;
			insertPauseTimeToDB();
			stopTimer();
		}
		
		/**
		 * ����һ��ʱ���¼�����ݿ�
		 */
		public void insertPauseTimeToDB() {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("pid", taskType);
			values.put("name", DataUtil.getData(TimerActivity.this, "user", "username"));
			values.put("action", 0);
			
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd/HH:mm:ss");
//			Date curDate = new Date(System.currentTimeMillis());
//			String str = format.format(curDate);
//			
//			values.put("time", str);
			db.insert("record", null, values);
			values.clear();
			// !!!
			db.close();
		}
		
		/**
		 * ��ͣ��Ļ��ʱ��
		 */
		public void stopTimer() {
			chronometer.pause();
		//	chronometer.stop();
			// ��¼��ǰʱ��
		//	pauseTime = chronometer.getText().toString().split(":");
		//	pauseTime = SystemClock.elapsedRealtime();
		//	pauseTime = System.currentTimeMillis();
		}
	}
//==============================inner class=====================================
	public class ScreenOffReceiver extends BroadcastReceiver{

		Context mContext;
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			dbHelper = new DataBaseHelper(context, "TimeManager.db", null, 1);
			mContext = context;
			insertReceverTimeToDB();
			startTimer();
		}
		
		/**
		 * ����һ��ʱ���¼�����ݿ�
		 */
		public void insertReceverTimeToDB() {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("pid", taskType);
			values.put("name", DataUtil.getData(TimerActivity.this, "user", "username"));
			values.put("action", 1);
			
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd/HH:mm:ss");
//			Date curDate = new Date(System.currentTimeMillis());
//			String str = format.format(curDate);
//			
//			values.put("time", str);
			db.insert("record", null, values);
			values.clear();
		//	db.insert("record", null, values);
			// !!!
			db.close();
		}
		
		/**
		 * �ָ���Ļ��ʱ��
		 */
		public void startTimer() {
			//long lastTimeDouble = (long)Double.parseDouble(pauseTime[1])*1000;
			// ֻ����long����
		//	recoverTime = SystemClock.elapsedRealtime();
		//	recoverTime = System.currentTimeMillis();
		//	long sub = recoverTime - pauseTime;
		//	chronometer.setBase(SystemClock.elapsedRealtime() - sub);
			chronometer.start();
		}
	}
}
