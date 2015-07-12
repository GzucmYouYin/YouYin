package com.gzucm.youyin.activity;

import cn.bmob.v3.BmobQuery;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.OrderQueryListener;
import com.bmob.pay.tool.PayListener;
import com.gzucm.youyin.R;
import com.gzucm.youyin.util.Logger;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 产品详情
 * @author 李先华
 *2015年6月8日下午1:33:22
 */
/**
 * @author 李先华
 *2015年6月8日下午9:58:05
 */
public class ProductDetailsActivity extends BaseWapperActivity {
	private static final String TAG = "ProductDetailsActivity"; 

	private RelativeLayout buy;//立即购买
	private RelativeLayout enter_cart;//加入购物车

	private String mOrderId; //订单号

	private Button payByWX; //微信支付测试

	private Button payByAlipay; //支付宝支付测试


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buy:
//			buyByAlipay();
//			buyByWX();
			break;
		case R.id.enter_cart:
			orderQuery();
			break;
		case R.id.payByAlipay:
			buyByAlipay();
			break;
		case R.id.payByWX:
			buyByWX();
			break;

		default:
			break;
		}

	}

	@Override
	protected void initParam() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.details_activity);
		setTitle("宝贝详情");
		setHeadRightVisibility(View.VISIBLE);
		setHeadRightText("刷新");
	}

	@Override
	protected void findViewById() {
		buy = (RelativeLayout) findViewById(R.id.buy);
		enter_cart = (RelativeLayout) findViewById(R.id.enter_cart);

		payByAlipay = (Button) findViewById(R.id.payByAlipay);
		payByWX = (Button) findViewById(R.id.payByWX);

	}

	@Override
	protected void setListener() {
		buy.setOnClickListener(this);
		enter_cart.setOnClickListener(this);

		payByAlipay.setOnClickListener(this);
		payByWX.setOnClickListener(this);
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

	/**
	 * 订单查询
	 */
	private void orderQuery() {
		new BmobPay(ProductDetailsActivity.this).query(mOrderId, new OrderQueryListener() {

			@Override
			public void succeed(String status) {
				Toast.makeText(ProductDetailsActivity.this, "status："+status, Toast.LENGTH_LONG).show();
				if (status.equals("SUCCESS")) {
					Toast.makeText(ProductDetailsActivity.this, "订单查询成功，已经支付", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ProductDetailsActivity.this, "订单查询失败，支付失败", Toast.LENGTH_LONG).show();

				}

			}

			@Override
			public void fail(int code, String reason) {
				Toast.makeText(ProductDetailsActivity.this, "支付失败 "+"code:"+code+" reason:"+reason, Toast.LENGTH_LONG).show();
			}
		});

	}

	/**
	 * 支付宝支付
	 */
	private void buyByAlipay() {
		new BmobPay(ProductDetailsActivity.this).pay(0.02, "优饮五花茶", "优饮，有得饮", new PayListener() {

			@Override
			public void unknow() {
				Toast.makeText(ProductDetailsActivity.this, "因为网络等问题,不能确认是否支付成功,请稍后手动查询", Toast.LENGTH_LONG).show();
				Logger.e(TAG, "pay unknow");
			}

			@Override
			public void succeed() {
				Toast.makeText(ProductDetailsActivity.this, "支付成功", Toast.LENGTH_LONG).show();
				Logger.e(TAG, "支付成功");
			}

			@Override
			public void orderId(String orderId) {
				mOrderId = orderId;
				Logger.e(TAG, "订单号：" + orderId);

			}

			@Override
			public void fail(int code, String reason) {
				Toast.makeText(ProductDetailsActivity.this, "支付失败 "+"code:"+code+" reason:"+reason, Toast.LENGTH_LONG).show();

			}
		});

	}

	/**
	 * 微信支付
	 */
	private void buyByWX() {
		new BmobPay(ProductDetailsActivity.this).payByWX(0.02, "优饮五花茶", "优饮，有得饮", new PayListener() {

			@Override
			public void unknow() {
				Toast.makeText(ProductDetailsActivity.this, "因为网络等问题,不能确认是否支付成功,请稍后手动查询", Toast.LENGTH_LONG).show();
				Logger.e(TAG, "pay unknow");
			}

			@Override
			public void succeed() {
				Toast.makeText(ProductDetailsActivity.this, "支付成功", Toast.LENGTH_LONG).show();
				Logger.e(TAG, "支付成功");
			}

			@Override
			public void orderId(String orderId) {
				mOrderId = orderId;
				Logger.e(TAG, "订单号：" + orderId);

			}

			@Override
			public void fail(int code, String reason) {
				switch (code) {
				case -3:
					Toast.makeText(ProductDetailsActivity.this, "未安装微信支付插件", Toast.LENGTH_LONG).show();
					break;
				case -2:
					Toast.makeText(ProductDetailsActivity.this, "微信支付用户中断操作", Toast.LENGTH_LONG).show();
					break;
				case -1:
					Toast.makeText(ProductDetailsActivity.this, reason, Toast.LENGTH_LONG).show();
					break;

				default:
					Toast.makeText(ProductDetailsActivity.this, "支付失败 "+"code:"+code+" reason:"+reason, Toast.LENGTH_LONG).show();
					break;
				}
			}
		});

	}
}
