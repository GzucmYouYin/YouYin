package com.gzucm.youyin.vo;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 首页
 * @author 李先华
 *2015年6月15日上午11:15:25
 */
public class HomePage extends BmobObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BmobFile fastScrollPic; //图片轮循图片
	
	public HomePage() {
		// TODO Auto-generated constructor stub
	}
	
	public BmobFile getFastScrollPic() {
		return fastScrollPic;
	}
	public void setFastScrollPic(BmobFile fastScrollPic) {
		this.fastScrollPic = fastScrollPic;
	}
	
}
