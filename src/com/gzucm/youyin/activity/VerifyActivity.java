package com.gzucm.youyin.activity;

import com.gzucm.youyin.R;
import com.gzucm.youyin.util.Constant;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 注册填写验证码
 * @author 李先华
 *2015年5月29日上午9:06:34
 */
public class VerifyActivity extends BaseWapperActivity {

	private static final String TAG = "VerifyoneActivity";
	/*UI*/
	private TextView VerifyPhone; //填写验证码界面手机号码
	private EditText VerifyEdit; //验证码输入框
	private Button getVerifyBtn; //验证码获取按钮

	/*验证码SMSSDK*/
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = Constant.APPKEY;
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = Constant.APPSECRET;
	private int i = 60; //验证码倒计时60秒

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void initParam() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.verify_activity);
		setTitle("请填写验证码");
		setHeadRightText("下一步");
		setHeadRightVisibility(View.VISIBLE);
	}

	@Override
	protected void findViewById() {

		VerifyPhone = (TextView) findViewById(R.id.VerifyPhone);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String phone = bundle.getString("phone");
		VerifyPhone.setText(phone);

		VerifyEdit = (EditText) findViewById(R.id.VerifyEdit);
		getVerifyBtn = (Button) findViewById(R.id.getVerifyBtn);
		
//		initSDK(); //启动短信验证sdk
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

	//标题栏左边返回
	@Override
	protected void onHeadLeftButton(View v) {
		backOrWait();

	}

	//返回或等待
	public void backOrWait() {
		final Builder builder = new AlertDialog.Builder(this); //定义一个AlertDialog.Builder对象
		builder.setMessage("确定取消并返回？"); //设置对话框显示的内容
		builder.setPositiveButton("返回", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.setNegativeButton("等待", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show(); //创建并显示对话框
	}

	//监听返回键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			backOrWait();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
