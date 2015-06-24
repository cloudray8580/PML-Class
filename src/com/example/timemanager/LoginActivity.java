package com.example.timemanager;

import com.example.timemanager.data.DataUtil;
import com.example.timemanager.data.DataBaseHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 
 * @author sony-pc
 * 登录Activity，实现用户登录功能，主要有两个EditText，一个OK Button
 * 登陆时判断用户名是否为空、是否为NULL，密码是否为空等，
 * 一切无误后调用数据库进行验证，如无误则启动主界面（MenuActivity）
 */
public class LoginActivity extends Activity implements OnClickListener{

	SharedPreferences pref;
	SharedPreferences.Editor editor;
	EditText name;
	EditText pw;
	CheckBox rememberPass;
	Button login;
	DataBaseHelper dbHelper;
	String nameStr;
	String pwStr;

	private void findView() {
		name = (EditText)findViewById(R.id.loginUsername);
		pw = (EditText)findViewById(R.id.loginPassword);
		rememberPass = (CheckBox)findViewById(R.id.remember_pass);
		login = (Button)findViewById(R.id.loginOK);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	private void setListener() {
		login.setOnClickListener(this);
	}

	private void checkRemember() {
		boolean b = pref.getBoolean("remember_password", false);
		if (b) {
			String account = pref.getString("account", "");
			String password = pref.getString("password", "");
			name.setText(account);
			pw.setText(password);
			rememberPass.setChecked(true);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findView();
		setListener();
		checkRemember();
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.loginOK:
			if (checkFormat() && checkCorrectness()) {
				Toast.makeText(this, "check is ok", Toast.LENGTH_SHORT).show();
				if (rememberPass.isChecked()) {
					editor = pref.edit(); // remember this!
					editor.putBoolean("remember_password", true);
					editor.putString("account", name.getText().toString().trim());
					editor.putString("password", pw.getText().toString().trim());
				} else {
					editor = pref.edit(); // remember this! 这里也要 不然会有NullPointerException
					editor.clear();
				}
				editor.commit();
				Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	public boolean checkFormat() {
		nameStr = name.getText().toString().trim();
		pwStr = pw.getText().toString().trim();
		if (nameStr == null || nameStr.equals("")) {
			Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
			return false;
		} else if (pwStr == null || pw.equals("")) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			return false;
		} else
			return true;
	}
	
	public boolean checkCorrectness() {
		dbHelper = new DataBaseHelper(this, "TimeManager.db", null, 1);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select password from user where name = '"+nameStr+"'",null);
		if(cursor!=null && cursor.moveToNext())
		{
			if(!pwStr.equals(cursor.getString(cursor.getColumnIndex("password")))) {
				Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
				return false;
			}
			else {
				DataUtil.saveData(this, "user", "username", nameStr);
				cursor.close();
				db.close();
				dbHelper.close();
				return true;
			}
		} else
			return false;
	}
}
