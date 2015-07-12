package com.gzucm.youyin.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.gzucm.youyin.R;
import com.gzucm.youyin.vo.RequestVo;

/**
 * 网络工具
 * @author 李先华
 *2015年5月1日上午9:12:13
 */
public class NetUtil {
	private static final String TAG = "NetUtil";
	private static final String NOT_LOGIN = "notlogin";
	
	private static Header[] headers = new BasicHeader[11];   //传递时候的头信息
	static {
		headers[0] = new BasicHeader("Appkey", "");
		headers[1] = new BasicHeader("Udid", "");
		headers[2] = new BasicHeader("Os", "");
		headers[3] = new BasicHeader("Osversion", "");
		headers[4] = new BasicHeader("Appversion", "");
		headers[5] = new BasicHeader("Sourceid", "");
		headers[6] = new BasicHeader("Ver", "");
		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");
		headers[9] = new BasicHeader("Unique", "");
		headers[10] = new BasicHeader("Cookie", "");
		
	}

	/**
	 * post
	 * @param vo
	 * @return
	 */
	public static Object post(RequestVo vo) {
		DefaultHttpClient client = new DefaultHttpClient();
		String url = vo.context.getString(R.string.app_host).concat(vo.context.getString(vo.requestUrl));
		Logger.d(TAG, "Post " + url);
		HttpPost post = new HttpPost(url);
		post.setHeaders(headers);
		Logger.d(TAG, "headers[0].getValue() " + headers[0].getValue());
		Object obj = null;
		try {
			if (vo.requestDataMap != null) {
				HashMap<String, String> map = vo.requestDataMap;
				ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
					pairList.add(pair);
				}
				Logger.d(TAG, "pairList " + pairList);
				HttpEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
				post.setEntity(entity);
				Logger.d(TAG, "post.getEntity(): " + post.getEntity().getContent());
			}
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				setCookie(response);
				String result = EntityUtils.toString(response.getEntity(), "UTF-8");
				Logger.d(TAG, "result: " + result);
				try {
					if (invilidateLogin(result)) {  //验证是否登录成功
						return Status.Login;           //登录失败,需要重新登录
					}
					obj = vo.jsonParser.parseJSON(result);
				} catch (JSONException e) {
					Logger.e(TAG, e.getLocalizedMessage(), e);
				}
				return obj;
			}
		} catch (ClientProtocolException e) {
			Logger.e(TAG, e.getLocalizedMessage(), e);
		} catch (IOException e) {
			Logger.e(TAG, e.getLocalizedMessage(), e);
		}
 		return null;
	}

	/**
	 * 添加Cookie
	 * @param response
	 */
	private static void setCookie(HttpResponse response) {
		if (response.getHeaders("Set-Cookie").length > 0) {
			Logger.d(TAG, response.getHeaders("Set-Cookie")[0].getValue()) ;
			headers[10] = new BasicHeader("Cookie", response.getHeaders("Set-Cookie")[0].getValue());
			Logger.d(TAG, "headers[10].getValue():"+headers[10].getValue()) ;
		}
	}
	/**
	 * 验证是否需要登录
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	private static boolean invilidateLogin(String result) throws JSONException {
		JSONObject jsonObject = new JSONObject(result);
		String responseCode = jsonObject.getString("response");
		Logger.d(TAG, "responseCode:"+responseCode) ;
		if (NOT_LOGIN.equals(responseCode)) {
			return true;
		}
		return false;
	}
	
	/**
	 * get
	 * @param vo
	 * @return
	 */
	public static Object get(RequestVo vo) {
		DefaultHttpClient client = new DefaultHttpClient();
		String url = vo.context.getString(R.string.app_host).concat(vo.context.getString(vo.requestUrl));
		Logger.d(TAG, "Get " + url);
		HttpGet get = new HttpGet(url);
		get.setHeaders(headers);
		Object obj = null;
		try {

			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				setCookie(response);
				String result = EntityUtils.toString(response.getEntity(), "UTF-8");
				Logger.d(TAG, result);
				try {
					if (invilidateLogin(result)) {
						return Status.Login;
					}
					obj = vo.jsonParser.parseJSON(result);
				} catch (JSONException e) {
					Logger.e(TAG, e.getLocalizedMessage(), e);
				}
				return obj;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得网络连接是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			Toast.makeText(context, R.string.net_error, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	public static enum Status {
		Login
	}
}
