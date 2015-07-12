package com.gzucm.youyin.util;

import com.gzucm.youyin.R;
import com.gzucm.youyin.widget.BaseDialog;
import com.gzucm.youyin.widget.BaseDialog.ClickListenerInterface;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @author 李先华
 *2015年5月1日上午10:40:43
 */
public class CommonUtil {

/*	public static void showInfoDialog(Context context, String message) {  //提示对话框
		showInfoDialog(context, message, "提示", "确定", null);
	}*/

	public static boolean isValidEmail(String paramString) {

		String regex = "[a-zA-Z0-9_\\.]{1,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
		if (paramString.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidMobiNumber(String paramString) {
		String regex = "^1\\d{10}$";
		if (paramString.matches(regex)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobileNums) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobileNums))
			return false;
		else
			return mobileNums.matches(telRegex);
	}

/*	public static void showInfoDialog(Context context, String message, String titleStr, String positiveStr,
			DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
		localBuilder.setTitle(titleStr);
		localBuilder.setMessage(message);
		if (onClickListener == null)
			onClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			};
		localBuilder.setPositiveButton(positiveStr, onClickListener);
		localBuilder.show();
	}*/
	
	/*public static void showInfoDialog2(Context context, String message, String titleStr, String positiveStr,
			DialogInterface.OnClickListener onClickListener, String negativeStr
			) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
		localBuilder.setTitle(titleStr);
		localBuilder.setMessage(message);
		if (onClickListener == null)
		{
			onClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			};
		}
		
		localBuilder.setPositiveButton(positiveStr, onClickListener);
		localBuilder.setNegativeButton(negativeStr, null);
		
		localBuilder.show();
	}*/
	
	//调用自定义的对话框
/*	public static void showOneBtnDialog(Context context, String title, String message, String positiveStr, BaseDialog.ClickListenerInterface onClickListenerInterface) {
		final BaseDialog baseDialog = new BaseDialog(context, title, message, positiveStr);
		baseDialog.show();

		if (onClickListenerInterface == null) {
			onClickListenerInterface = new BaseDialog.ClickListenerInterface() {
				
				@Override
				public void doConfirm() {

				}
				
				@Override
				public void doCancel() {
					// TODO Auto-generated method stub
					
				}
			};
		}
		baseDialog.setClicklistener(onClickListenerInterface);
	}*/
	
	/**
	 * 获得网络连接是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager con=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		if(wifi|internet){
			//执行相关操作
			return true;
		}else{
			Toast.makeText(context.getApplicationContext(),
					"亲，网络连了么？", Toast.LENGTH_LONG)
					.show();
			return false;
		}
	}
}
