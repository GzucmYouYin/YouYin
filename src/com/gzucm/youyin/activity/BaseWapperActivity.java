package com.gzucm.youyin.activity;

import java.util.List;
import java.util.Vector;

import net.tsz.afinal.FinalActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiConfiguration.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
 * 有标题栏的基类
 * @author 李先华
 *2015年5月1日上午9:11:35
 */
public abstract class BaseWapperActivity extends FinalActivity implements
OnClickListener {
	private static final String TAG = "BaseWapperActivity";

	public static final int NOT_LOGIN = 403;// //请求码
	public static final int LOGIN_SUCCESS = 10000000;// //登陆成功
	
	private CustomProgressDialog progressDialog = null;
	protected Context context;
	private ThreadPoolManager threadPoolManager;
	private List<BaseTask> record = new Vector<BaseTask>(); // 线程集合
	
	private View inflate;
	private RelativeLayout head_layout; // TitleLayout
	private LinearLayout layout_content; //父容器布局
	private ImageButton headLeftBtn; //返回
	private Button headRightBtn; //标题栏右边
	private TextView head_title; //title
	private ButtomClick buttomClick;
	

	public BaseWapperActivity() {
		threadPoolManager = ThreadPoolManager.getInstance();
	}

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		ECApplication.getInstance().addActvity(this); //添加activity到application
		
		// 自定义标题
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.setContentView(R.layout.frame);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_land);
		

		buttomClick = new ButtomClick();
		layout_content = (LinearLayout) super.findViewById(R.id.frame_content); 
		head_layout = (RelativeLayout) super.findViewById(R.id.head_layout); // 标题栏布局
		head_title = (TextView) super.findViewById(R.id.head_title); // 标题栏的标题
		headLeftBtn = (ImageButton) super.findViewById(R.id.head_left);// 标题栏左边
		headRightBtn = (Button) super.findViewById(R.id.head_right); // 标题栏右边
		headLeftBtn.setOnClickListener(buttomClick);
		headRightBtn.setOnClickListener(buttomClick);

		context = getApplicationContext(); // getApplicationContext()
		// 返回应用的上下文(context)，生命周期是整个应用，应用摧毁它才摧毁
		initView();
	}

	@Override
	public void setContentView(int layoutResID) {
		inflate = getLayoutInflater().inflate(layoutResID, null);
		setContentView(inflate);
	}

	public void setContentView(View view) {
		layout_content.removeAllViews();
		layout_content.addView(inflate);
	};

	@Override
	public View findViewById(int id) {
		return inflate.findViewById(id);
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

	/* 回调方法处理请求 */
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
			Looper.prepare(); //创建Looper对象,先调用Looper.prepare()启用Looper
			Object obj = null; 
			Message msg = Message.obtain();
			try {
				if (NetUtil.hasNetwork(context)) {
					obj = NetUtil.post(reqVo);
					if (obj instanceof Status) {
						Intent intent = new Intent(BaseWapperActivity.this,
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
			Looper.loop(); //让Looper开始工作，从消息队列里取消息，处理消息
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
		showProgressDialog();
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
		inflate = null;
		head_layout = null;
		headLeftBtn = null;
		headRightBtn = null;
		head_title = null;
		buttomClick = null;
		closeProgressDialog();
	}

	@Override
	public void setTitle(CharSequence title) { // CharSequence的值是可读可写序列，String的值是只读序列
		head_title.setText(title);
	}

	@Override
	public void setTitle(int titleId) {
		head_title.setText(titleId);
	}


	private class ButtomClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.head_left:
				onHeadLeftButton(v);
				break;
			case R.id.head_right:
				onHeadRightButton(v);
				break; 
			}
		}
	}

	protected void onHeadLeftButton(View v) {
		finish();
	}

	protected void onHeadRightButton(View v) {

	} 

	protected void setHeadLeftBackgroundResource(int resid) {
		headLeftBtn.setBackgroundResource(resid);
	}

	protected void setHeadLeftVisibility(int visibility) {
		headLeftBtn.setVisibility(visibility);
	}

	protected void setHeadRightText(CharSequence text) {
		headRightBtn.setText(text);
	}

	protected void setHeadRightText(int resid) {
		headRightBtn.setText(resid);
	}

	protected void setHeadRightBackgroundResource(int resid) {
		headRightBtn.setBackgroundResource(resid);
	}
	
	protected void setHeadRightEnabled(boolean tag) {
			headRightBtn.setEnabled(tag);
	}
	
	protected void setHeadRightVisibility(int visibility) {
		headRightBtn.setVisibility(visibility);
	}

	protected void setHeadBackgroundResource(int resid) {
		head_layout.setBackgroundResource(resid);
	}


	protected void BackgroundDrawable(Drawable d) {
		head_layout.setBackgroundDrawable(d);
	}

	//隐藏layout
	protected void setHeadLayoutVisibility(int visibility) {
		head_layout.setVisibility(visibility);
	}

}
