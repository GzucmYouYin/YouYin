package com.gzucm.youyin.activity;

import cn.bmob.v3.Bmob;

import com.gzucm.youyin.R;
import com.gzucm.youyin.application.ECApplication;
import com.gzucm.youyin.util.Logger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

/**
 * 欢迎界面
 * @author 李先华
 *2015年5月31日下午3:00:17
 */
public class WelcomeActivity extends BaseActivity implements AnimationListener {
	private static final String TAG = "WelcomeActivity"; 

	//是否第一次
	public static final int VERSION = 2;

	//软存储
	public static SharedPreferences sp;

	private ImageView  imageView = null;
	private Animation alphaAnimation = null;

	/*public void run() {
		try {
			//延迟两秒
			Thread.sleep(2000);
			// 读取SharedPreferences中需要的数据
			sp = getSharedPreferences("YouYin_Setting", Context.MODE_PRIVATE);
			*//**
			 * 如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
			 *//*
			if (sp.getInt("VERSION", 0) != VERSION && ECApplication.getInstance().getCurrentActivitySize() > 0) {
				Logger.d(TAG, "VERSION "+sp.getInt("VERSION", 0));
				startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
			} else if(sp.getInt("VERSION", 0) == VERSION && ECApplication.getInstance().getCurrentActivitySize() > 0) {
				startActivity(new Intent(WelcomeActivity.this, LoginRegisterActivity.class));
			}
			ECApplication.getInstance().exit();
		} catch (InterruptedException e) {
		}
	}*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initParam() {
		// 初始化 Bmob SDK
		// 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
		Bmob.initialize(this, "545545245415d59c0a423172d7011f68");

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_welcome);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		imageView =(ImageView)findViewById(R.id.welcome_image_view);

		alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);
		alphaAnimation.setFillEnabled(true); //启动Fill保持
		alphaAnimation.setFillAfter(true);  //设置动画的最后一帧是保持在View上面
		imageView.setAnimation(alphaAnimation);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		alphaAnimation.setAnimationListener(this);  //为动画设置监听
	}

	@Override
	protected void processLogic() {
		//启动一个延迟线程
		//		new Thread(this).start();
	}

	//监听返回键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			ECApplication.getInstance().exit();
			ECApplication.getInstance().setApplicationNull();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// 读取SharedPreferences中需要的数据
		sp = getSharedPreferences("YouYin_Setting", Context.MODE_PRIVATE);
		/**
		 * 如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
		 */
		if (sp.getInt("VERSION", 0) != VERSION && ECApplication.getInstance().getCurrentActivitySize() > 0) {
			Logger.d(TAG, "VERSION "+sp.getInt("VERSION", 0));
			startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
		} else if(sp.getInt("VERSION", 0) == VERSION && ECApplication.getInstance().getCurrentActivitySize() > 0) {
			startActivity(new Intent(WelcomeActivity.this, LoginRegisterActivity.class));
		}
		ECApplication.getInstance().exit();

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}
}
