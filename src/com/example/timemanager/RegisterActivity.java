package com.example.timemanager;

import com.example.timemanager.data.DataBaseHelper;
import com.example.timemanager.data.DataUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * @author sony-pc
 * ע��Activity����Ҫ������EditText��һ��OK Button
 * ���ж����������Ƿ�Ϸ���û�����������ݿ�������
 * Ȼ����ȡ�ող����ֵ���ŵ�SharedPrefenence��
 */
public class RegisterActivity extends Activity implements OnClickListener {

	EditText name;
	EditText pw1;
	EditText pw2;
	Button registe;
	DataBaseHelper dbHelper;
	String nameStr;
	String pw1Str;
	String pw2Str;

	private void findView() {
		name = (EditText)findViewById(R.id.registerUsername);
		pw1 = (EditText)findViewById(R.id.registerPassword1);
		pw2 = (EditText)findViewById(R.id.registerPassword2);
		registe = (Button)findViewById(R.id.registerOK);
	}

	private void setListener() {
		registe.setOnClickListener(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		findView();
		setListener();
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.registerOK:
			if (checkFormat() && checkExist()) {
				dbHelper = new DataBaseHelper(this, "TimeManager.db", null, 1);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("name", nameStr);
				values.put("password", pw1Str);
				db.insert("user", null, values);
				values.clear();
				Toast.makeText(this, "ע��ɹ�", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}
	
	public boolean checkFormat() {
		nameStr = name.getText().toString().trim();
		pw1Str = pw1.getText().toString().trim();
		pw2Str = pw2.getText().toString().trim();
		if (nameStr == null || nameStr.equals("")) {
			Toast.makeText(this, "�������û���", Toast.LENGTH_SHORT).show();
			return false;
		} else if (pw1Str == null || pw1.equals("")) {
			Toast.makeText(this, "����������", Toast.LENGTH_SHORT).show();
			return false;
		} else if (pw2Str == null || pw2.equals("")) {
			Toast.makeText(this, "���ٴ���������", Toast.LENGTH_SHORT).show();
			return false;
		} else if (!pw1Str.equals(pw2Str)) {
			Toast.makeText(this, "�����������벻һ��", Toast.LENGTH_SHORT).show();
			return false;
		} else
			return true;
	}
	
	public boolean checkExist() {
		dbHelper = new DataBaseHelper(this, "TimeManager.db", null, 1);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select password from user where name = '"+nameStr+"'",null);
		if(cursor!=null && cursor.moveToNext()) {
			Toast.makeText(this, "�û����Ѵ���", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			DataUtil.saveData(this, "user", "username", nameStr);
			return true;
		}
	}
}
