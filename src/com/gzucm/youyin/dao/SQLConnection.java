package com.gzucm.youyin.dao;

import com.gzucm.youyin.util.Logger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 李先华
 *2015年5月1日上午10:39:22
 */
public class SQLConnection extends SQLiteOpenHelper {

	private static final String TAG = "SQLConnection";
	public static final String DATABASE_NAME = "youyin.db"; // 数据库名称
	public static final int DATABASE_VERSION = 2;// 数据库版本

	public SQLConnection(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/**
		 * 演示数据 程序锁数据表
		 */
		String applock = "create table applock (packename varchar(200))";
		db.execSQL(applock);
		Logger.d(TAG, applock);

		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	@SuppressWarnings("unused")
	private void intData(SQLiteDatabase db) {
		db.execSQL("insert into pub_cant (cant_code,cant_name,super_code) values ('110000','北京市','CN')");
	}

}
