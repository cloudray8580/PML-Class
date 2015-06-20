package com.example.advertisementdemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;
import android.util.Log;

import com.sina.cloudstorage.auth.AWSCredentials;
import com.sina.cloudstorage.auth.BasicAWSCredentials;
import com.sina.cloudstorage.services.scs.SCS;
import com.sina.cloudstorage.services.scs.SCSClient;
import com.sina.cloudstorage.services.scs.model.S3Object;

public class SCSUtil {

	private String photoPath;
	String accessKey = "15m7fj67Nr82bjKRNL1K";
	String secretKey = "56385e718e7dc98f6d9bdc275f7391194db615f7";
	AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
	SCS conn = new SCSClient(credentials);

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
	 * 判断sd卡是否可写
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
}
