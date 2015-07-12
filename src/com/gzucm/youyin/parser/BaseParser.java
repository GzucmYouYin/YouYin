package com.gzucm.youyin.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.gzucm.youyin.util.Logger;
/**
 * 回调方法
 * @author 李先华
 *2015年5月1日上午10:39:37
 * @param <T>
 */
public abstract class BaseParser<T> {
	private static final String TAG = "BaseParser";
	/**
	 * 将返回的json数据转化为相应的对象
	 * @param paramString
	 * @throws JSONException
	 */
	public abstract T parseJSON(String paramString) throws JSONException;

	/**
	 * 检测服务器响应信息，并返回response中的内容-----处理的servlet名
	 * @param paramString
	 * @throws JSONException
	 */
	public String checkResponse(String paramString) throws JSONException {
		if (paramString == null) {
			return null;
		} else {
			Logger.d(TAG, "paramString:"+paramString);
			JSONObject jsonObject = new JSONObject(paramString);
			String result = jsonObject.getString("response");
			if (result != null && !result.equals("error")) {
				Logger.d(TAG, "result:"+result);
				return result;
			} else {
				return null;
			}

		}
	}
}
