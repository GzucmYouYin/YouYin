package com.gzucm.youyin.vo;

import java.io.File;
import java.sql.Array;
import cn.bmob.v3.BmobUser;

/**
 * 用户表
 * @author 李先华
 *2015年6月4日下午10:46:20
 */
public class SpecialUser extends BmobUser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private File pic; //图片文件
	private String phone; //手机号
	private String weiChatId; //微信号
	private String qq; //QQ
	private Array address; //地址
	private String zhifubao; //支付宝
	private String identity; //身份证
	
	public File getPic() {
		return pic;
	}
	public void setPic(File pic) {
		this.pic = pic;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWeiChatId() {
		return weiChatId;
	}
	public void setWeiChatId(String weiChatId) {
		this.weiChatId = weiChatId;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public Array getAddress() {
		return address;
	}
	public void setAddress(Array address) {
		this.address = address;
	}
	public String getZhifubao() {
		return zhifubao;
	}
	public void setZhifubao(String zhifubao) {
		this.zhifubao = zhifubao;
	}
	
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
}
