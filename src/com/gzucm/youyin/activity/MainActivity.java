package com.gzucm.youyin.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.gzucm.youyin.R;
import com.gzucm.youyin.application.ECApplication;
import com.gzucm.youyin.fastscroll.CircleFlowIndicator;
import com.gzucm.youyin.fastscroll.ImageAdapter;
import com.gzucm.youyin.fastscroll.ViewFlow;
import com.gzucm.youyin.util.Logger;
import com.gzucm.youyin.vo.HomePage;

/**
 * 首页
 * @author 李先华
 *2015年6月7日下午11:56:39
 */
public class MainActivity extends BaseActivity {

	private static final String TAG = "MainActivity"; 

	private static Boolean isExit = false; //是否退出

	private ImageView touxiangImg; //头像

	private ImageView shareAppImg; //分享APP

	private RelativeLayout lvItemLayout; //listItem选择区域

	private EditText first_hint; //搜索

	//图片轮循
	private ViewFlow viewFlow;
//	private static final int[] ids = { R.drawable.test1,R.drawable.test2,R.drawable.test3 };
	/*private String[] urls = {
			"http://bbs.1205.cn/forum.php?mod=attachment&aid=MTQzNzIxfGFiOGU0NzRmfDE0MzQzMzE3NzZ8MHwxMTM0NzQ%3D&noupdate=yes",
			"http://a.hiphotos.baidu.com/image/pic/item/728da9773912b31b789ec7288418367adbb4e1c5.jpg",
			"http://file.bmob.cn/M01/67/FA/oYYBAFV-RgWAA44SAACiP-AU9KQ288.jpg",};*/
	private String[] urls = new String[3]; //图片路径集合
	
	private String bmobFileUrl = "http://file.bmob.cn/"; //Bmob官网文件的路径

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.touxiangImg:
			Toast.makeText(MainActivity.this, "这是张背景图片", Toast.LENGTH_LONG).show();
			startActivity(new Intent(MainActivity.this, PersonInformationActivity.class));
			break;
		case R.id.shareAppImg:
//			Toast.makeText(MainActivity.this, "分享APP的介绍、图标和下载链接，还没做", Toast.LENGTH_LONG).show();
			showShare();
			break;
		case R.id.lvItemLayout:
			startActivity(new Intent(MainActivity.this, ProductDetailsActivity.class));
			break;
		case R.id.first_hint:
			startActivity(new Intent(MainActivity.this, SampleSearchActivity.class));
			break;

		default:
			break;
		}

	}
	
	/**
	 * 一键分享功能
	 */
	private void showShare() {
		 ShareSDK.initSDK(MainActivity.this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(getString(R.string.app_name));
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("我是分享文本");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(this);
		 }

	@Override
	protected void initParam() {

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.first_activity);
		setTitle("首页");

	}

	@Override
	protected void findViewById() {
		touxiangImg = (ImageView) findViewById(R.id.touxiangImg);

		shareAppImg = (ImageView) findViewById(R.id.shareAppImg);

		lvItemLayout = (RelativeLayout) findViewById(R.id.lvItemLayout);

		first_hint = (EditText) findViewById(R.id.first_hint);

		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
	}

	@Override
	protected void setListener() {
		touxiangImg.setOnClickListener(this);
		shareAppImg.setOnClickListener(this);
		lvItemLayout.setOnClickListener(this);
		first_hint.setOnClickListener(this);
	}

	@Override
	protected void processLogic() {
		//获取图片路径URL
		getUrl();
		//首页图片轮循
		viewFlow.setAdapter(new ImageAdapter(this, urls));
//		viewFlow.setmSideBuffer(ids.length); // 实际图片张数， 我的ImageAdapter实际图片张数为3
		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
		viewFlow.setTimeSpan(5000); //切换时间5s
		viewFlow.setSelection(3 * 1000); // 设置初始位置
		viewFlow.startAutoFlowTimer(); // 启动自动播放

	}

	/**
	 * 获取图片路径
	 */
	private void getUrl() {
		// TODO Auto-generated method stub
		BmobQuery<HomePage> query = new BmobQuery<HomePage>();
		//执行查询方法
		query.addQueryKeys("fastScrollPic");
		query.findObjects(this, new FindListener<HomePage>() {
			@Override
			public void onSuccess(List<HomePage> object) {
				// TODO Auto-generated method stub
//				Toast.makeText(MainActivity.this, "查询成功：共"+object.size()+"条数据。", Toast.LENGTH_LONG).show();
				for (int i = 0; i < object.size(); i++) {
					urls[i] = bmobFileUrl+object.get(i).getFastScrollPic().getUrl();
					Logger.e(TAG, "urls："+urls[i]);
				}
				
			}
			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "查询失败："+msg, Toast.LENGTH_LONG).show();
			}
		});
		
	}

	/**
	 * 菜单、返回键响应
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{  
			exitBy2Click(MainActivity.this); //调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	public static void exitBy2Click(Context context) {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			ECApplication.getInstance().exit();
			ECApplication.getInstance().setApplicationNull();
		}
	}
}
