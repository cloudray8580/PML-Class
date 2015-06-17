package com.example.timemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 
 * @author sony-pc
 * 这个是主menu，即实现几种不同的模块，实际上就是几个Button，
 * 都是启动Timer Activity，Intent里面放进去的Extra不同而已
 */
public class MenuActivity extends Activity implements OnClickListener{

	Button part1;
	Button part2;
	Button part3;
	Button part4;
	Button part5;
	Button part6;
	
	private void findView() {
		part1 = (Button)findViewById(R.id.part1);
		part2 = (Button)findViewById(R.id.part2);
		part3 = (Button)findViewById(R.id.part3);
		part4 = (Button)findViewById(R.id.part4);
		part5 = (Button)findViewById(R.id.part5);
		part6 = (Button)findViewById(R.id.part6);
	}
	
	private void setListener() {
		part1.setOnClickListener(this);
		part2.setOnClickListener(this);
		part3.setOnClickListener(this);
		part4.setOnClickListener(this);
		part5.setOnClickListener(this);
		part6.setOnClickListener(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
		setListener();
	}
	
	public void onClick(View v) {
		Intent intent = new Intent(MenuActivity.this, TimerActivity.class);
		switch(v.getId()){
		case R.id.part1:
			intent.putExtra("TaskType", "1");
		//	Log.e("part1","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			break;
		case R.id.part2:
			intent.putExtra("TaskType", "2");
		//	Log.e("part2","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			break;
		case R.id.part3:
			intent.putExtra("TaskType", "3");
		//	Log.e("part3","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			break;
		case R.id.part4:
			intent.putExtra("TaskType", "4");
		//	Log.e("part4","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			break;
		case R.id.part5:
			intent.putExtra("TaskType", "5");
		//	Log.e("part5","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			break;
		case R.id.part6:
			intent.putExtra("TaskType", "6");
		//	Log.e("part6","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			break;
		default:
			break;
		}
		startActivity(intent);
	}
}
