package com.gzucm.youyin.activity;

import java.util.List;
import java.util.Vector;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import com.gzucm.youyin.R;
import com.gzucm.youyin.application.ECApplication;
import com.gzucm.youyin.service.IECManager;
import com.gzucm.youyin.util.Constant;
import com.gzucm.youyin.util.Logger;
import com.gzucm.youyin.util.NetUtil;
import com.gzucm.youyin.util.ThreadPoolManager;
import com.gzucm.youyin.vo.RequestVo;
import com.gzucm.youyin.widget.CustomProgressDialog;

/**
 * 无标题栏的基类
 * @author 李先华
 *2015年5月29日下午6:00:34
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "BaseActivity";

	public static final int NOT_LOGIN = 403;// //请求码
	public static final int LOGIN_SUCCESS = 10000000;// //登陆成功
	
	private String progessDialogMessage = null; //更新对话框信息
	private CustomProgressDialog progressDialog = null;
	protected Context context;
	private ThreadPoolManager threadPoolManager;
	private List<BaseTask> record = new Vector<BaseTask>(); // 线程集合
	
	public BaseActivity() {
		threadPoolManager = ThreadPoolManager.getInstance();
	}

	/**
	 * Android生命周期回调方法-创建
	 */
	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		ECApplication.getInstance().addActvity(this); //添加activity到application
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = getApplicationContext();
		initView();
	}

	/**
	 * activity依次执行方法
	 */
	private void initView() {
		initParam();
		loadViewLayout();
		findViewById();
		setListener();
		processLogic();
	}

	/**
	 * 1、在加载组件前初始化参数
	 */
	protected abstract void initParam();

	/**
	 * 2、加载视图方法
	 */
	protected abstract void loadViewLayout();

	/**
	 * 3、加载组件的方法
	 */
	protected abstract void findViewById();

	/**
	 * 4、绑定监听事件的方法
	 */
	protected abstract void setListener();

	/**
	 * 5、数据处理的方法
	 */
	protected abstract void processLogic();
	

	public IECManager getECManager() {
		return ECApplication.getInstance().getEcManager();
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == NOT_LOGIN) {
			if (resultCode == LOGIN_SUCCESS) {
				int size = record.size();
				if (size > 0) {
					for (int i = 0; i < size; i++) {
						threadPoolManager.addTask(record.get(0)); // get(i)？
						Logger.d(TAG, record.get(0)+"");
					}
				}
			} else {
				finish();
			}
		}
	}

	/**
	 * 显示进度框，无文字的
	 */
	protected void showProgressDialog() {

		if (progressDialog == null){
			progressDialog =new CustomProgressDialog(this,R.anim.progress_loading);
		}

		progressDialog.show();
	}
	
	/**
	 * 显示进度框，有文字的
	 */
	protected void showProgressDialog(String message) {
		
		if (progressDialog == null){
			progressDialog =new CustomProgressDialog(this, message,R.anim.progress_loading);
		}
		
		progressDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	protected void closeProgressDialog() {

		if (progressDialog != null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	/**
	 * 回调方法处理请求
	 */
	@SuppressWarnings("unchecked")
	class BaseHandler extends Handler {
		private Context context;
		private DataCallback callBack;
		private RequestVo reqVo;

		public BaseHandler(Context context, DataCallback callBack,
				RequestVo reqVo) {
			this.context = context;
			this.callBack = callBack;
			this.reqVo = reqVo;
		}

		/**
		 * @param msg
		 */
		public void handleMessage(Message msg) {
			closeProgressDialog();
			if (msg.what == Constant.SUCCESS) {
				if (msg.obj == null) {
					Toast.makeText(context, getString(R.string.net_server_no_respone), Toast.LENGTH_SHORT).show();// 服务器未响应对话框
				} else {
					callBack.processData(msg.obj, true);
				}
			} else if (msg.what == Constant.NET_FAILED) {
				Toast.makeText(context, getString(R.string.net_error), Toast.LENGTH_SHORT).show();  //网络未连接
			}

			Logger.d(TAG, "recordSize:" + record.size());
		}
	}

	/* 处理登陆的子线程 */
	class BaseTask implements Runnable {
		private Context context;
		private RequestVo reqVo;
		private Handler handler;

		public BaseTask(Context context, RequestVo reqVo, Handler handler) {
			this.context = context;
			this.reqVo = reqVo;
			this.handler = handler;
		}

		@Override
		public void run() {
			Object obj = null;
			Message msg = Message.obtain();
			try {
				if (NetUtil.hasNetwork(context)) {
					obj = NetUtil.post(reqVo);
					if (obj instanceof Status) {
						Intent intent = new Intent(BaseActivity.this,
								LoginActivity.class);
						intent.putExtra("notlogin", "notlogin");
						startActivityForResult(intent, NOT_LOGIN);
					} else {
						msg.what = Constant.SUCCESS;
						msg.obj = obj;
						handler.sendMessage(msg);
						record.remove(this);
					}
				} else {
					msg.what = Constant.NET_FAILED;
					msg.obj = obj;
					handler.sendMessage(msg);
					record.remove(this);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

	/**
	 * 回调函数，处理服务器返回的数据
	 * 
	 * @param paramObject
	 *            服务器返回的数据封装后的对象
	 * @param paramBoolean
	 *            服务器是否返回数据
	 */
	public abstract interface DataCallback<T> {

		public abstract void processData(T paramObject, boolean paramBoolean);
	}

	/**
	 * 向服务器发送请求
	 * 
	 * @param reqVo
	 * @param callBack
	 */
	protected void getDataFromServer(RequestVo reqVo, DataCallback callBack) {
		showProgressDialog(progessDialogMessage);
		BaseHandler handler = new BaseHandler(this, callBack, reqVo);
		BaseTask taskThread = new BaseTask(this, reqVo, handler);
		this.threadPoolManager.addTask(taskThread);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ECApplication.getInstance().removeActvity(this);
		record.clear();
		record = null;
		context = null;
		threadPoolManager = null;
		closeProgressDialog();
	}
}
