	package com.gzucm.youyin.activity;

import com.gzucm.youyin.R;

import android.view.View;
import android.webkit.WebView;

/**
 * 用户注册使用协议
 * @author 李先华
 *2015年5月29日下午4:21:24
 */
public class UserAgreementActivity extends BaseWapperActivity {
	private static final String TAG = "UserAgreementActivity"; 

	private WebView mWebView; //webview加载网页
	static final String url = "file:///android_asset/agreement.html"; //加载文件路径
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initParam() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.agreement_activity);
		setTitle("注册使用协议");
	}

	@Override
	protected void findViewById() {
		mWebView = (WebView) findViewById(R.id.agreementWebView);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processLogic() {
		mWebView.getSettings().setDefaultTextEncodingName("gb2312"); //设置编码
		mWebView.loadUrl(url); //加载文件
		mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY); //设置滚动条在外围显示

	}

}
