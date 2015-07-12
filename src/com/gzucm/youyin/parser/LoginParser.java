package com.gzucm.youyin.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.gzucm.youyin.util.Logger;
import com.gzucm.youyin.vo.User;

/**
 * 登录的相关操作
 * @author 李先华
 *2015年5月26日下午4:21:49
 */
public class LoginParser extends BaseParser<User> {
	private static final String TAG = "LoginParser";
	/**
	 * 处理登陆的返回的json数据
	 * @param paramString 返回的json字符串
	 * @author 万允山
	 */
	@Override
	public User parseJSON(String paramString) throws JSONException {
		if (super.checkResponse(paramString) != null) {
			
			JSONObject jsonObject = new JSONObject(paramString);
			String type = jsonObject.getString("type");
			User localUser = new User();
			if ("3".equals(type)||type=="3") {
				int userId = jsonObject.getInt("uid");
				localUser.setUid(userId);
			}
			localUser.setType(type);
			Logger.d(TAG, "uid:"+localUser.getUid());
			return localUser;
			
		}
		return new User();
	}

}
