package com.example.timemanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.sina.cloudstorage.auth.AWSCredentials;
import com.sina.cloudstorage.auth.BasicAWSCredentials;
import com.sina.cloudstorage.services.scs.SCS;
import com.sina.cloudstorage.services.scs.SCSClient;
import com.sina.cloudstorage.services.scs.model.S3Object;

import com.example.advertisementdemo.*;

public class AdvertisementActivity extends Activity implements OnClickListener {

	private AutoViewPager mViewPager;

	private List<View> mViewList;

	private AutoPagerAdapter mAdapter;

	public static String TAG = "MainActivity";

	private TextView getButton;

	private Context mContext;

	private String advName;

	private String advBucket;

	private int advType;

	private int advNum;

	private String[] advAddress;

	// sina相关
	private String photoPath;
	String accessKey = "15m7fj67Nr82bjKRNL1K";
	String secretKey = "56385e718e7dc98f6d9bdc275f7391194db615f7";
	AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
	SCS conn = new SCSClient(credentials);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advertisement);
		Bmob.initialize(this, "48cd84eff010d82333fcfd906fffb606");
		getButton = (TextView) findViewById(R.id.btn_get);
		mViewPager = (AutoViewPager) findViewById(R.id.viewpager_adv);
		mViewList = new ArrayList<View>();
		mAdapter = new AutoPagerAdapter(mViewList);
		mViewPager.setAdapter(mAdapter);
		mViewPager.startAutoScrolled();
		getButton.setOnClickListener(this);
		mContext = this;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_get:
			// 获取广告名和广告目录
			getAdvInfo();

			break;
		default:
			break;
		}
	}

	/**
	 * 获取广告名和广告目录
	 */
	private void getAdvInfo() {
		BmobQuery<Person> personQuery = new BmobQuery<Person>();
		personQuery.addWhereEqualTo("userIp", "0.0.0.0");
		personQuery.findObjects(mContext, new FindListener<Person>() {
			@Override
			public void onSuccess(List<Person> personList) {
				String avdId = personList.get(0).getAdvID();
				Log.e(TAG, "外层马丹成功�?");
				BmobQuery<Advertisement> advertiseQuery = new BmobQuery<Advertisement>();
				advertiseQuery.addWhereEqualTo("objectId", avdId);
				advertiseQuery.findObjects(mContext, new FindListener<Advertisement>() {
					@Override
					public void onSuccess(List<Advertisement> advList) {
						Log.e(TAG, "内层马丹成功�?");
						// 获取广告基本信息
						advName = advList.get(0).getAdvName();
						advBucket = advList.get(0).getAdvAddress();
						advType = advList.get(0).getAdvType();
						advNum = advList.get(0).getAdvNumber();
						// 获得广告�?有名�?
						advAddress = new String[advNum];
						String[] nameSplit = advName.split("\\.");
						Log.e(TAG, advBucket + " **** " + advName);
						for (int i = 0; i < advNum; i++) {
							advAddress[i] = nameSplit[0] + (i + 1) + "." + nameSplit[1];
							Log.e(TAG, advAddress[i]);
						}
						// �?始下载啦
						for (int i = 0; i < advNum; i++) {
							final String fileName = advAddress[i];
							new Thread(new Runnable() { // 匿名子线�?
										@Override
										public void run() {
											try {
												downloadObject(advBucket, fileName);

											} catch (Exception e) {
											}
										}

									}).start();

						}

					}

					@Override
					public void onError(int arg0, String arg1) {
						Log.e(TAG, "内层马丹错误�?");
					}

				});
			}

			@Override
			public void onError(int arg0, String arg1) {
				Log.e(TAG, "外层马丹错误�?");
			}
		});
	}

	// 以下为新浪下载的代码
	public void downloadObject(String bucket, String objectPath) {
		String basePath = getExternalStorageBasePath();
		File destFile = null;
		if (basePath != null) {
			photoPath = basePath + "/" + objectPath;
			Log.e("TAG", photoPath);
			destFile = new File(photoPath);
			S3Object s3Obj = conn.getObject(bucket, objectPath);
			InputStream in = s3Obj.getObjectContent();
			byte[] buf = new byte[1024];
			OutputStream out = null;
			try {
				out = new FileOutputStream(destFile);
				int count;
				while ((count = in.read(buf)) != -1) {
					if (Thread.interrupted()) {
						throw new InterruptedException();
					}
					out.write(buf, 0, count);
				}
				Log.e(TAG, "下载完成");
				Message msg = new Message();
				msg.what = 0;
				msg.obj = objectPath;
				mHandler.sendMessage(msg);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 判断sd卡是否可�?
	 * 
	 * @return
	 */
	private boolean isExternalStorageWriteable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}

		return false;
	}

	/**
	 * 获取存储文件的根路径
	 * 
	 * @return
	 */
	private String getExternalStorageBasePath() {
		if (isExternalStorageWriteable()) {
			File file = new File(Environment.getExternalStorageDirectory() + "/advertise/");
			file.mkdirs();
			return file.getAbsolutePath();
		}

		return null;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String photoPath = getExternalStorageBasePath() + "/" + msg.obj;
				Bitmap bm = BitmapFactory.decodeFile(photoPath);
				ImageView view = new ImageView(mContext);
				view.setImageBitmap(bm);
				mViewList.add(view);
				mAdapter.notifyDataSetChanged();
				// 全部下载完成
				if (mAdapter.getCount() == advAddress.length) {
					sendEmptyMessage(1);
				}
				break;
			case 1:
				mViewPager.startAutoScrolled();
				break;
			}
		}
	};

}
