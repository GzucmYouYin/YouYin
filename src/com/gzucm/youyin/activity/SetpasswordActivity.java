package com.gzucm.youyin.activity;

import cn.bmob.v3.listener.SaveListener;

import com.gzucm.youyin.R;
import com.gzucm.youyin.util.CommonUtil;
import com.gzucm.youyin.util.Logger;
import com.gzucm.youyin.util.MD5Util;
import com.gzucm.youyin.vo.SpecialUser;
import com.gzucm.youyin.widget.BaseDialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 设置密码
 * @author 李先华
 *2015年6月2日下午11:51:33
 */
public class SetpasswordActivity extends BaseWapperActivity {

	private static final String TAG = "SetpasswordActivity";

	private EditText enter_password; //密码

	private EditText retype_password; //确认密码

	private String mPhone; //注册的手机号
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initParam() {
		//获取从上个activity传递过来的值
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			mPhone = bundle.getString("phone");
		}
		
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.setpassword_activity);
		setTitle("设置密码");
		setHeadRightText("下一步");
		setHeadRightVisibility(View.VISIBLE);
	}

	@Override
	protected void findViewById() {

		enter_password = (EditText) findViewById(R.id.enter_password);
		
		retype_password = (EditText) findViewById(R.id.retype_password);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

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
		final BaseDialog baseDialog = new BaseDialog(SetpasswordActivity.this, "提示","确定返回？", "返回", "取消");
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

	@Override
	protected void onHeadRightButton(View v) {

		String pwd = enter_password.getText().toString().trim();
		String pwd2 = retype_password.getText().toString().trim();

		if (null == pwd || "".equals(pwd)) {
			Toast.makeText(SetpasswordActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (null == pwd2 || "".equals(pwd2)) {
			Toast.makeText(SetpasswordActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (pwd.length() < 6 && pwd2.length() < 6) {  
			Toast.makeText(this, "密码不能小于6位数！",  
					Toast.LENGTH_SHORT).show();  
			return;  
		}
		if (pwd.length() > 16 && pwd2.length() > 16) {  
			Toast.makeText(this, "密码不能大于16位数！",  
					Toast.LENGTH_SHORT).show();  
			return;  
		} 
		if(!pwd.equals(pwd2)){
			final BaseDialog baseDialog = new BaseDialog(SetpasswordActivity.this, "提示","两次密码输入不一致，请重新输入", "确定");
			baseDialog.show();
			baseDialog.setClicklistener(new BaseDialog.ClickListenerInterface() {
				
				@Override
				public void doConfirm() {
					//清空密码
					enter_password.setText("");
					retype_password.setText("");

					//设置第一个输入框获取焦点
					enter_password.setFocusable(true);
					
					baseDialog.dismiss();
				}
				
				@Override
				public void doCancel() {
					// TODO Auto-generated method stub
					baseDialog.dismiss();
					
				}
			});
			
			return;
		}

		showProgressDialog("正在注册...");
		//把密码、手机号存到后台。跳转到首页
		register(mPhone, pwd);

	}

	public void register(String phone, String password) {
		final SpecialUser su = new SpecialUser();
		su.setUsername(phone);
		su.setPassword(MD5Util.digest(password));
		su.setMobilePhoneNumber(phone);
		//注意：不能用save方法进行注册
		su.signUp(this, new SaveListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				closeProgressDialog();
				Toast.makeText(SetpasswordActivity.this, "注册成功", Toast.LENGTH_LONG).show();
				startActivity(new Intent(SetpasswordActivity.this, MainActivity.class));
			}
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				closeProgressDialog();
				Toast.makeText(SetpasswordActivity.this, "注册失败:"+msg, Toast.LENGTH_LONG).show();
				Logger.e(TAG, "注册失败:"+msg);
			}
		});
	}

}
