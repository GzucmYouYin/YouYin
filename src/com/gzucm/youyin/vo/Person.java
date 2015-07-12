package com.gzucm.youyin.vo;

import cn.bmob.v3.BmobObject;

/**
 * @author 李先华
 *2015年5月31日下午9:43:25
 */
public class Person extends BmobObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
    private String address;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
