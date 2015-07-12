package com.gzucm.youyin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * MD5加密
 * @author 李先华
 *2015年5月28日上午10:36:10
 */
public class MD5Util {
	/**
	 * 使用MD5对原文进行加密
	 * @param value
	 * 				原文
	 * @return MD5加密后
	 */
	public static String digest(String value) {
		StringBuilder sb = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] result = digest.digest(value.getBytes());
			sb = new StringBuilder();
			for (byte b : result) {
				String hexString = Integer.toHexString(b & 0xFF);
				if (hexString.length() == 1) {
					sb.append("0" + hexString); //0~F
				} else {
					sb.append(hexString);
				}
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

}
