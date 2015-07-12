package com.gzucm.youyin.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import com.gzucm.youyin.R;

/**
 * 登陆注册页面
 * @author 李先华
 *2015年5月29日下午1:02:18
 */
public class LoginRegisterActivity extends BaseActivity {
	private static final String TAG = "LoginRegisterActivity"; 

	/*UI*/
	private ImageView loginImg; //登录
	private ImageView regImg; //注册
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginImg:
			startActivity(new Intent(this, LoginActivity.class));
			break;
		case R.id.regImg:
			startActivity(new Intent(LoginRegisterActivity.this, RegisterActivity.class));
			break;
		default:
			break;
		}

	}
	
	@Override
	protected void initParam() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.login_register_activity);
	}

	@Override
	protected void findViewById() {
		loginImg = (ImageView) findViewById(R.id.loginImg);
		regImg = (ImageView) findViewById(R.id.regImg);

	}

	@Override
	protected void setListener() {
		loginImg.setOnClickListener(this);
		regImg.setOnClickListener(this);
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

}
