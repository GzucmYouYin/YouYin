package com.gzucm.youyin.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.gzucm.youyin.util.Logger;

/**
 * 应用服务管理
 * @author 李先华
 *2015年4月30日下午11:16:54
 */
public class ECServiceManager extends Service {

	private static final String TAG = "ECServiceManager";
	
	private MyIECManager myIECManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		myIECManager = new MyIECManager();
		Logger.d(TAG, "ECServiceManager is start");
	}
	
	/**新建service时系统自动覆盖onBind()方法，用于通信*/
	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		Logger.d(TAG, "onBind ");
		return myIECManager;
	}

	private class MyIECManager extends Binder implements IECManager {
		
	}
}
