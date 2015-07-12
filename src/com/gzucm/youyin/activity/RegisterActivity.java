package com.gzucm.youyin.activity;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.gzucm.youyin.R;
import com.gzucm.youyin.country.CharacterParserUtil;
import com.gzucm.youyin.country.CountrySortModel;
import com.gzucm.youyin.country.GetCountryNameSort;
import com.gzucm.youyin.util.CommonUtil;
import com.gzucm.youyin.util.Logger;
import com.gzucm.youyin.util.NetUtil;
import com.gzucm.youyin.widget.BaseDialog;

/**
 * 注册界面
 * @author 李先华
 *2015年5月6日下午5:50:18
 */
public class RegisterActivity extends BaseWapperActivity{

	private static final String TAG = "RegisterActivity";
	
	private EditText regist_phonenumber_edit; //号码输入
	
	private TextView register_agree; //注册协议

	private TextView editText_countryNum; //国家区号+86
	
	private RelativeLayout relative_choseCountry; //国家区号选择区域-选择

	private Button tv_countryName; //中国-选择
	
	private TextView eare_hint ; //国家和地区-选择

	//国家区号选择
	private GetCountryNameSort countryChangeUtil;
	private CharacterParserUtil characterParserUtil;
	private List<CountrySortModel> mAllCountryList;
	String beforText = null;
	
	private String areaNum; //区号
	
	private String phone; //手机号
	
	private String mphone; //获取从找回密码界面传过来的电话号码

	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.register_agree:
			startActivity(new Intent(this, UserAgreementActivity.class));
			break;
		case R.id.rala_chose_country:
			intentToCountry();
			break;
		case R.id.tv_chosed_country:
			intentToCountry();
			break;
		case R.id.area_hint:
			intentToCountry();
			break;

		default:
			break;
		}
	}

	@Override
	protected void initParam() {
		Intent intent = getIntent();
		if (intent != null) {
			mphone = intent.getStringExtra("phone");
			Logger.d(TAG, "mphone "+mphone);
		}
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.register_activity);
		setTitle("注册");
		setHeadRightText("下一步");
		setHeadRightVisibility(View.VISIBLE);

	}

	@Override
	protected void findViewById() {
		regist_phonenumber_edit = (EditText) findViewById(R.id.regist_phonenumber_edit);
		regist_phonenumber_edit.setText(mphone);
		
		register_agree = (TextView) findViewById(R.id.register_agree);
		
		//国家区号选择
		relative_choseCountry = (RelativeLayout) findViewById(R.id.rala_chose_country);
		editText_countryNum = (TextView) findViewById(R.id.edt_chosed_country_num);
		tv_countryName = (Button) findViewById(R.id.tv_chosed_country);
		eare_hint=(TextView)findViewById(R.id.area_hint);

		mAllCountryList = new ArrayList<CountrySortModel>();
		countryChangeUtil = new GetCountryNameSort();
		characterParserUtil = new CharacterParserUtil();

		if (!TextUtils.isEmpty(regist_phonenumber_edit.getText().toString())) {
			setHeadRightEnabled(true); //设置下一步为不可点击状态
		} else {
			setHeadRightEnabled(false); //设置下一步为可点击状态
		}
		
	}
	
	@Override
	protected void setListener() {
		regist_phonenumber_edit.addTextChangedListener(new TextWatcher() {
			
			/******************************EditText有无输入监听-start**************************************/
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(regist_phonenumber_edit.getText().toString())) {
					setHeadRightEnabled(true); //设置下一步为不可点击状态
				} else {
					setHeadRightEnabled(false); //设置下一步为可点击状态
				}
				/*******************************end*************************************/
			}
		});
		register_agree.setOnClickListener(this);
		
		relative_choseCountry.setOnClickListener(this);
		
		tv_countryName.setOnClickListener(this);
		
		eare_hint.setOnClickListener(this);

		editText_countryNum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				beforText = s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				String contentString = editText_countryNum.getText().toString();

				CharSequence contentSeq = editText_countryNum.getText();

				Log.i(TAG, "contentString :" + contentString.length());

				if (contentString.length() > 1) {
					// 按照输入内容进行匹配
					List<CountrySortModel> fileterList = (ArrayList<CountrySortModel>) countryChangeUtil
							.search(contentString, mAllCountryList);

					if (fileterList.size() == 1) {
						tv_countryName.setText(fileterList.get(0).countryName);
					} else {
						tv_countryName.setText("国家代码无效");
					}

				} else {
					if (contentString.length() == 0) {
						editText_countryNum.setText(beforText);
						tv_countryName.setText("从列表选择");
					} else if (contentString.length() == 1
							&& contentString.equals("+")) {
						tv_countryName.setText("从列表选择");
					}

				}

				if (contentSeq instanceof Spannable) {
					Spannable spannable = (Spannable) contentSeq;
					Selection.setSelection(spannable, contentSeq.length());
				}
			}
		});
	}

	@Override
	protected void processLogic() {
		initCountryList();
		
	}
	
	//初始化国家列表
	private void initCountryList() {
		String[] countryList = getResources().getStringArray(
				R.array.country_code_list_ch);

		for (int i = 0, length = countryList.length; i < length; i++) {
			String[] country = countryList[i].split("\\*");

			String countryName = country[0];
			String countryNumber = country[1];
			String countrySortKey = characterParserUtil.getSelling(countryName);
			CountrySortModel countrySortModel = new CountrySortModel(
					countryName, countryNumber, countrySortKey);
			String sortLetter = countryChangeUtil
					.getSortLetterBySortKey(countrySortKey);
			if (sortLetter == null) {
				sortLetter = countryChangeUtil
						.getSortLetterBySortKey(countryName);
			}

			countrySortModel.sortLetters = sortLetter;
			mAllCountryList.add(countrySortModel);
		}

	}

	public void intentToCountry() {
		Intent intent = new Intent();
		intent.setClass(RegisterActivity.this, CountryActivity.class);
		startActivityForResult(intent, 12);
	}
	
	//activity finish()后调用
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Logger.i(TAG+" onActivityResult","requestCode："+ requestCode
				+"resultCode："+resultCode
				+"data："+data);

		// TODO Auto-generated method stub
		switch (requestCode) {
		case 12:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				String countryName = bundle.getString("countryName");
				String countryNumber = bundle.getString("countryNumber");

				editText_countryNum.setText(countryNumber);
				tv_countryName.setText(countryName);
				Logger.e(TAG+" onActivityResult", "countryNumber："+countryNumber+" "+"countryName："+countryName);

			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void toVerify() {
		//对话框跳转到验证码输入界面
		final BaseDialog baseDialog = new BaseDialog(RegisterActivity.this, "确认手机号", "我们将发送验证码短信到这个手机号码："+areaNum+" "+phone, "好");
		baseDialog.show();
		baseDialog.setClicklistener(new BaseDialog.ClickListenerInterface() {
			
			@Override
			public void doConfirm() {
				Intent intent = new Intent(RegisterActivity.this, RegisterVerifyActivity.class);
				intent.putExtra("areaNum", areaNum);
				intent.putExtra("phone",phone);
				startActivity(intent);
				baseDialog.dismiss();
			}
			
			@Override
			public void doCancel() {
				// TODO Auto-generated method stub
				baseDialog.dismiss();
				
			}
		});
	}

	public void toLogin() {
		//对话框跳转到登录界面
		final BaseDialog baseDialog = new BaseDialog(RegisterActivity.this, "提示","此号码已注册，请登录", "好", "取消");
		baseDialog.show();
		baseDialog.setClicklistener(new BaseDialog.ClickListenerInterface() {
			
			@Override
			public void doConfirm() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
				//				intent.putExtra("phone", phone); //没注册时用这种方法不可行
				//可以把要传递的参数放到一个bundle里传递过去，bundle可以看做一个特殊的map.  
				Bundle bundle=new Bundle();  
				bundle.putString("phone", phone);
				intent.putExtras(bundle);  
				startActivity(intent);
				baseDialog.dismiss();
			}
			
			@Override
			public void doCancel() {
				// TODO Auto-generated method stub
				baseDialog.dismiss();
				
			}
		});
	}

	//点击下一步弹出确认手机号对话框
	@Override
	protected void onHeadRightButton(View v) {
		areaNum = editText_countryNum.getText().toString();
		phone = regist_phonenumber_edit.getText().toString().trim();
		Logger.e(TAG, "areaNum："+areaNum+" "+"phone："+phone);
		if (!CommonUtil.isMobileNO(phone)) {
			Toast.makeText(RegisterActivity.this, "手机号码输入错误", Toast.LENGTH_LONG).show();
			return;
		}
		if (!areaNum.equals("+86")) {
			Toast.makeText(RegisterActivity.this, "仅支持中国地区", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (NetUtil.hasNetwork(RegisterActivity.this)) {
			showProgressDialog("正在验证手机号...");
			veriryIsReg();
//			toVerify();
		} else {
			Toast.makeText(RegisterActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();  //网络未连接
		}

	}

	/**
	 * 验证手机号是否已经注册过
	 */
	public void veriryIsReg() {
		//查询用户
		BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
		query.addWhereEqualTo("mobilePhoneNumber", phone);
		query.findObjects(this, new FindListener<BmobUser>() {
		    @Override
		    public void onSuccess(List<BmobUser> object) {
		    	closeProgressDialog();
		    	//如果查到用户，即object.size() > 0则提示已经注册
		    	if (object.size() > 0) {
			    	toLogin();
				} else {
					toVerify();
				}
	    		Logger.e(TAG, "查询用户成功："+object.size());
		    }
		    @Override
		    public void onError(int code, String msg) {
		    	closeProgressDialog();
		    	Toast.makeText(RegisterActivity.this, "查询失败："+msg, Toast.LENGTH_LONG).show();
		    	Logger.e(TAG, "查询失败："+msg);
		    }
		});
		
	}

}
