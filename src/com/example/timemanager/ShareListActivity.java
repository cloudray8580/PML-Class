package com.example.timemanager;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ShareListActivity extends Activity implements OnClickListener {

	ListView list;
	Button refresh;
	private String[] data_MainThread = new String[50];
	ArrayAdapter<String> adapter;
	int location;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case 1:
				data_MainThread = msg.getData().getStringArray("data");
				location = msg.getData().getInt("location");
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_list);
		
//		adapter = new ArrayAdapter<String>(ShareListActivity.this, android.R.layout.simple_list_item_1, data_MainThread);	
		list = (ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
		refresh = (Button)findViewById(R.id.refresh);
		refresh.setOnClickListener(this);
		
		BmobQuery<ShareRecord> bmobQuery = new BmobQuery<ShareRecord>();
		bmobQuery.setLimit(50);
		bmobQuery.findObjects(this, new FindListener<ShareRecord>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.e("getDataErro","!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				Toast.makeText(ShareListActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(List<ShareRecord> object) {
				// TODO Auto-generated method stub
				String[] data = new String[50]; // 这个变量要写在onSuccess里，应为这是个线程！访问全局变量有风险
				// 另外 确定数组的值要大于等于返回值条目数
				
			//	Log.e("getDataSuccess","!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				Toast.makeText(ShareListActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
				// Toast貌似是线程安全的，不影响
				int i = 0;
			//	Log.e("getDataSuccess", object.size()+"");
				for (ShareRecord sr : object) {
					if (sr.getUsername() != null) {
					//	Log.e("ShareRecord SIZE",object.size()+"");
						data[i] = sr.getCreatedAt()+"\n"+sr.getUsername()+": my available time is: "+sr.getAvailableTimeToday();
					} else {
					//	Log.e("no record", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11");
						data[i] = "no username";
					}
					i++;
				}
				
				int location = i;
				
				for (; i < 50; i++) {
					data[i] = "no record";
				}
				
				Message msg = new Message();
				msg.what = 1;
				
				Bundle bundle = new Bundle();
				bundle.putStringArray("data", data);
				bundle.putInt("location", location);

				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		});
		
	//	ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShareListActivity.this, android.R.layout.simple_list_item_1, data_MainThread);	
	//	list.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.e("data_MainThread_SIZE", data_MainThread.length+"");
		switch (v.getId()) {
		case R.id.refresh:
			if (data_MainThread[0] == null) {
				Toast.makeText(ShareListActivity.this, "正在加载...", Toast.LENGTH_SHORT).show();
				break;
			}
			adapter = new ArrayAdapter<String>(ShareListActivity.this, android.R.layout.simple_list_item_1, data_MainThread);
			adapter.notifyDataSetChanged();
			list.setAdapter(adapter);
			list.setSelection(location);
			break;
		default:
			break;
		}
	}
}
