package com.gzucm.youyin.vo;

import java.util.HashMap;

import com.gzucm.youyin.parser.BaseParser;
import com.gzucm.youyin.util.Logger;

import android.content.Context;

/**
 * 请求的信息
 * @author 李先华
 *2015年5月2日上午9:43:32
 */
public class RequestVo {
	private static final String TAG = "RequestVo";
	public int requestUrl;     //访问服务器的地址，除去主机地址之后
	public Context context;
	public HashMap<String, String> requestDataMap;     //传递的参数
	public BaseParser<?> jsonParser;         //回调方法

	public RequestVo() {
		Logger.d(TAG, "requestUrl:"+requestUrl+"context:"+context+"requestDataMap:"+requestDataMap+"jsonParser:"+jsonParser);
	}

	public RequestVo(int requestUrl, Context context, HashMap<String, String> requestDataMap, BaseParser<?> jsonParser) {
		super();
		this.requestUrl = requestUrl;
		this.context = context;
		this.requestDataMap = requestDataMap;
		this.jsonParser = jsonParser;
	}
}
