package com.gzucm.youyin.application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.gzucm.youyin.service.ECServiceManager;
import com.gzucm.youyin.service.IECManager;
import com.gzucm.youyin.util.Logger;

/**
 * application，入口，用来保存全局变量
 * @author 李先华
 *2015年4月30日下午11:09:47
 */
public class ECApplication extends Application {

	private static final String TAG = "ECApplication";

	private static String cacheDir; //缓存路径 

	private static String userId = null; //用户id 
	
	private IECManager ecManager; //？

	private List<Activity> records = new ArrayList<Activity>(); //activity集合

	private static ECApplication application;  //静态全局变量

	/**真正的Android入口点，保存全局变量的*/
	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {  
		super.onCreate();
		bindService(new Intent(this, ECServiceManager.class), new ECServiceConnection(), Context.BIND_AUTO_CREATE);
		initCacheDirPath();
	}

	public static String getCacheDirPath() {
		return cacheDir;
	}


	/**
	 *  初始化目录 
	 */
	private void initCacheDirPath() {
		File f;
		// 判断SD卡是否存在，并且是否具有读写权限
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // 优先保存到SD卡中
			f = getApplicationContext().getExternalCacheDir();//SDCard/Android/data/你的应用包名/cache/目录
			Log.d(TAG, f.getAbsolutePath());
			if (!f.exists()) {
				f.mkdir();
			}
		} else {// 如果SD卡不存在，就保存到本应用的目录下
			f = getApplicationContext().getCacheDir();
		}
		cacheDir = f.getAbsolutePath();
	}

	public IECManager getEcManager() {
		return ecManager;
	}

	private class ECServiceConnection  implements ServiceConnection {

		// 当与service的连接建立后被调用  
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Logger.d(TAG, "onServiceConnected");
			ecManager = (IECManager) service;
		}

		// 当与service的连接意外断开时被调用 
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Logger.e(TAG,  "onServiceDisconnected" );
		}
	}


	public synchronized static ECApplication getInstance() { 
		if (null == application) { 
			application = new ECApplication(); 
		} 
		return application; 
	} 
	// add Activity 
	public void addActvity(Activity activity) {
		records.add(activity);
		Logger.d(TAG, "addActvity -> Current Acitvity Size :" + getCurrentActivitySize());
	}

	/**
	 * remove Actvity
	 * @param activity
	 */
	public void removeActvity(Activity activity) {
		records.remove(activity);
		Logger.d(TAG, "removeActvity -> Current Acitvity Size :" + getCurrentActivitySize());
	}

	public void exit() {
		for (Activity activity : records) {
			activity.finish();
		}
	}

	public int getCurrentActivitySize() {
		return records.size();
	}

	//设置application为null
	public void setApplicationNull() {
		application = null;
	}
	
	/**
	 * 设置登陆用户的id
	 * @param userId
	 */
	public static void setUserId(String userId) {
		ECApplication.userId = userId;
	}
	
}
