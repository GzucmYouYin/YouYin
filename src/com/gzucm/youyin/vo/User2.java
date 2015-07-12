package com.gzucm.youyin.vo;

/**
 * 用户表
 * @author 李先华
 *2015年5月26日上午11:43:05
 */
public class User2 {
	
	private int uid; //用户主键
	private String phone; //手机号
	private String name; //姓名
	private String password; //密码
	private String identity; //身份证
	private String wechatid; //微信号
	private String qqid; //QQ号
	private String bankcard; //银行卡号
	private String zhifubao; //支付宝账号
	private String date; //时间
	private String type; //客户端标准字段

	public User2()
	{
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getWechatid() {
		return wechatid;
	}

	public void setWechatid(String wechatid) {
		this.wechatid = wechatid;
	}

	public String getQqid() {
		return qqid;
	}

	public void setQqid(String qqid) {
		this.qqid = qqid;
	}

	public String getBankcard() {
		return bankcard;
	}

	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	public String getZhifubao() {
		return zhifubao;
	}

	public void setZhifubao(String zhifubao) {
		this.zhifubao = zhifubao;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
