package com.gzucm.youyin.activity;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.gzucm.youyin.R;
import com.gzucm.youyin.application.ECApplication;
import com.gzucm.youyin.util.CommonUtil;
import com.gzucm.youyin.util.Constant;
import com.gzucm.youyin.util.Logger;
import com.gzucm.youyin.util.NetUtil;
import com.gzucm.youyin.widget.BaseDialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 找回密码填写验证码
 * @author 李先华
 *2015年5月29日上午9:06:34
 */

public class FindPasswordVerifyActivity extends BaseWapperActivity{

	private static final String TAG = "FindPasswordVerifyActivity";
	/*UI*/
	private TextView VerifyPhone; //填写验证码界面手机号码
	private EditText VerifyEdit; //验证码输入框
	private Button getVerifyBtn; //验证码获取按钮
	private TextView yuyin; //语音验证码
	private TextView unGetCodeTv; //收不到验证码？

	/*验证码SMSSDK*/
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = Constant.APPKEY;

	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = Constant.APPSECRET;

	private int verifyTime = 60; //验证码倒计时60秒

	private String mphone; //获取从找回密码界面传过来的电话号码
	private String mareaNum; //获取从找回密码界面传过来的国家区号

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getVerifyBtn:
			getVerifyCode();
			break;
		case R.id.yuyin:
			getVoiceVerifyCode();
			break;

		default:
			break;
		}

	}

	@Override
	protected void initParam() {
		//获取从上个activity传递过来的值
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mphone = bundle.getString("phone");
		mareaNum = bundle.getString("areaNum");

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.verifyone_activity);
		setTitle("请填写验证码");
		setHeadRightText("下一步");
		setHeadRightVisibility(View.VISIBLE);
	}

	@Override
	protected void findViewById() {

		VerifyPhone = (TextView) findViewById(R.id.VerifyPhone);
		VerifyPhone.setText(mareaNum+" "+mphone);

		VerifyEdit = (EditText) findViewById(R.id.VerifyEdit);
		getVerifyBtn = (Button) findViewById(R.id.getVerifyBtn);

		yuyin = (TextView) findViewById(R.id.yuyin);
		unGetCodeTv = (TextView) findViewById(R.id.unGetCodeTv);

		if (!TextUtils.isEmpty(VerifyEdit.getText().toString())) {
			setHeadRightEnabled(true); //设置下一步为不可点击状态
		} else {
			setHeadRightEnabled(false); //设置下一步为可点击状态
		}

		initSDK(); //启动短信验证sdk
		getVerifyCode(); //获取验证码
	}

	@Override
	protected void setListener() {
		VerifyEdit.addTextChangedListener(new TextWatcher() {

			/******************************EditText有无输入监听-start**************************************/
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(VerifyEdit.getText().toString())) {
					setHeadRightEnabled(true); //设置下一步为不可点击状态
				} else {
					setHeadRightEnabled(false); //设置下一步为可点击状态
				}
				/*******************************end*************************************/
			}
		});
		getVerifyBtn.setOnClickListener(this);
		yuyin.setOnClickListener(this);
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

	/**
	 * 获取语音验证码
	 */
	public void getVoiceVerifyCode() {
		final BaseDialog baseDialog = new BaseDialog(FindPasswordVerifyActivity.this, "语音验证码", "我们将以电话形式通知到你，请注意接听", "好");
		baseDialog.show();
		baseDialog.setClicklistener(new BaseDialog.ClickListenerInterface() {

			@Override
			public void doConfirm() {
				//收不到验证码文字变为电话提示，语音验证码文字隐藏
				unGetCodeTv.setText("电话拨打中...请留意电话");
				yuyin.setVisibility(View.GONE);

				//语音获取验证码
				SMSSDK.getVoiceVerifyCode(mphone, mareaNum.substring(1, mareaNum.length()));
				Logger.e(TAG, "getVoiceVerifyCode->ok");

				baseDialog.dismiss();
			}

			@Override
			public void doCancel() {
				// TODO Auto-generated method stub
				baseDialog.dismiss();

			}
		});
	}

	@Override
	protected void onHeadRightButton(View v) {
		//验证码验证是否正确，正确跳转到设置密码，否则重新输入验证码
		if (NetUtil.hasNetwork(FindPasswordVerifyActivity.this)) {

			VerifySMSCode();

		} else {
			Toast.makeText(FindPasswordVerifyActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();  //网络未连接
		}

	}

	private void VerifySMSCode(){

		String code = VerifyEdit.getText().toString().trim(); //获取输入框中的验证码

		if (code.length() != 4) {
			Toast.makeText(FindPasswordVerifyActivity.this, "验证码输入有误", Toast.LENGTH_LONG).show();
			return;
		}
		showProgressDialog("正在验证手机验证码...");
		SMSSDK.submitVerificationCode("86", mphone, code); //提交短信验证码
		closeProgressDialog();

	}


	//标题栏左边返回
	@Override
	protected void onHeadLeftButton(View v) {
		backOrWait();

	}

	//返回或等待
	public void backOrWait() {
		final BaseDialog baseDialog = new BaseDialog(FindPasswordVerifyActivity.this, "提示","确定取消并返回？", "返回", "取消");
		baseDialog.show();
		baseDialog.setClicklistener(new BaseDialog.ClickListenerInterface() {
			
			@Override
			public void doConfirm() {
				// TODO Auto-generated method stub
				finish();
				baseDialog.dismiss();
			}
			
			@Override
			public void doCancel() {
				// TODO Auto-generated method stub
				baseDialog.dismiss();
				
			}
		});
	}

	//监听返回键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			backOrWait();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 初始化短信SDK
	 */
	private void initSDK() {
		// 启动短信验证sdk
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		EventHandler eventHandler = new EventHandler(){
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		//注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
	}

	public void getVerifyCode() {
		// 1. 通过sdk发送短信验证
		SMSSDK.getVerificationCode(mareaNum.substring(1, mareaNum.length()), mphone);
		Logger.e(TAG, "mphone:"+mphone+"区号："+mareaNum.substring(1, mareaNum.length()));
		// 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
		getVerifyBtn.setClickable(false);
		getVerifyBtn.setText(verifyTime + "秒");
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (; verifyTime > 0; verifyTime--) {
					handler.sendEmptyMessage(-9);
					if (verifyTime <= 0) {
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(-8);
			}
		}).start();
	}

	/*******************************start 功能：获取验证码的相关操作*******************************************/
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == -9) {
				getVerifyBtn.setText(verifyTime + "秒");
				getVerifyBtn.setBackgroundResource(R.color.verifying_gray); //设置获取验证码时的背景
				getVerifyBtn.setTextColor(getResources().getColor(R.color.daojishi));
			} else if (msg.what == -8) {
				getVerifyBtn.setText("重新获取");
				getVerifyBtn.setClickable(true);
				getVerifyBtn.setBackgroundResource(R.drawable.verify_code_bg); //设置没获取验证码时的背景
				getVerifyBtn.setTextColor(getResources().getColor(R.color.white));
				verifyTime = 60;
			} else {
				int event = msg.arg1;
				int result = msg.arg2;
				Object data = msg.obj;
				Log.e("event", "event=" + event);

				//				if (result == SMSSDK.RESULT_COMPLETE) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					/** 提交验证码 */
					afterSubmit(result, data);
				}else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
					/** 获取验证码成功后的执行动作 */
					afterGet(result, data);
					//getSMSCode(); //自动填充验证码
				}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
					//返回支持发送验证码的国家列表
				}
				else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
					/** 获取语音版验证码成功后的执行动作 */
					afterGetVoice(result, data);
				}
				/*}else{                                                                 
					((Throwable)data).printStackTrace(); 
				}*/
			}
		}
	};
	/*******************************end*******************************************/

	/**
	 * 提交验证码成功后的执行事件
	 *
	 * @param result
	 * @param data
	 */
	private void afterSubmit(final int result, final Object data) {
		if (result == SMSSDK.RESULT_COMPLETE) {
			Intent intent = new Intent(FindPasswordVerifyActivity.this, ResetpasswordActivity.class);
			intent.putExtra("phone",mphone);
			startActivity(intent);
		} else {
			((Throwable) data).printStackTrace();
			// 验证码不正确
			Toast.makeText(FindPasswordVerifyActivity.this, "验证码输入有误，请重新输入", Toast.LENGTH_SHORT).show();

		}
	}

	/**
	 * 获取短信验证码成功后的执行动作
	 *
	 * @param result
	 * @param data
	 */
	private void afterGet(final int result, final Object data) {
		if (result == SMSSDK.RESULT_COMPLETE) {
			Toast.makeText(getApplicationContext(), "验证码已经发送",
					Toast.LENGTH_SHORT).show();
		} else {
			((Throwable) data).printStackTrace();
			// 验证码发送错误
			Toast.makeText(FindPasswordVerifyActivity.this, "验证码发送出错，请重试", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 获取语音验证码成功后的执行动作
	 *
	 * @param result
	 * @param data
	 */
	private void afterGetVoice(final int result, final Object data) {
		//收到语音验证码后还原
		unGetCodeTv.setText("收不到验证码？");
		yuyin.setVisibility(View.VISIBLE);
		if (result == SMSSDK.RESULT_COMPLETE) {
			Toast.makeText(FindPasswordVerifyActivity.this, "语音验证码已经发送", Toast.LENGTH_SHORT).show();
		} else {
			((Throwable) data).printStackTrace();
			// 语音验证码发送错误
			Toast.makeText(FindPasswordVerifyActivity.this, "语音验证码发送出错，请重试", Toast.LENGTH_SHORT).show();
		}
	}

	/**重写父类的onDestroy()方法*/
	@Override
	protected void onDestroy() {
		SMSSDK.unregisterAllEventHandler(); //调用反注册代码将其注销
		super.onDestroy();
		ECApplication.getInstance().removeActvity(this);
	}

}
