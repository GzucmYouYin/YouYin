package com.gzucm.youyin.activity;
import java.util.ArrayList;
import java.util.List;

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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;

import com.gzucm.youyin.R;
import com.gzucm.youyin.R.id;
import com.gzucm.youyin.country.CharacterParserUtil;
import com.gzucm.youyin.country.CountrySortModel;
import com.gzucm.youyin.country.GetCountryNameSort;
import com.gzucm.youyin.util.CommonUtil;
import com.gzucm.youyin.util.Logger;
import com.gzucm.youyin.util.MD5Util;
import com.gzucm.youyin.vo.SpecialUser;
import com.gzucm.youyin.widget.BaseDialog;

/**
 * 登录界面
 * @author 李先华
 *2015年5月2日上午10:05:03
 */
public class LoginActivity extends BaseWapperActivity {

	private static final String TAG = "LoginActivity"; 

	private TextView findPasswordBtn; //找回密码

	private EditText prephonenumber_edit; //手机输入

	private EditText password; //密码输入

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

	private String phone; //本界面手机号

	private String mphone; //获取从注册界面传过来的电话号码
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.area_selectLog:
			intentToCountry();
			break;
		case R.id.country_select:
			intentToCountry();
			break;
		case R.id.area_hintLog:
			intentToCountry();
			break;
		case R.id.findPassword:
			startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
			break;
		default:
			break;
		}

	}

	@Override
	protected void initParam() {
		//取得从上注册传递过来的手机号
		Intent intent = getIntent();
		if (intent != null) {
			mphone = intent.getStringExtra("phone");
			Logger.d(TAG, "mphone "+mphone);
		}
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.login_activity);
		setTitle("登录");
		setHeadRightText("下一步");
		setHeadRightVisibility(View.VISIBLE);

	}

	@Override
	protected void findViewById() {
		prephonenumber_edit = (EditText) findViewById(R.id.prephonenumber_edit);
		password = (EditText) findViewById(R.id.password);
		findPasswordBtn = (TextView) findViewById(id.findPassword);

		prephonenumber_edit.setText(mphone);

		//国家区号选择
		relative_choseCountry = (RelativeLayout) findViewById(R.id.area_selectLog);
		editText_countryNum = (TextView) findViewById(R.id.prephonenumber);
		tv_countryName = (Button) findViewById(R.id.country_select);
		eare_hint=(TextView)findViewById(R.id.area_hintLog);

		mAllCountryList = new ArrayList<CountrySortModel>();
		countryChangeUtil = new GetCountryNameSort();
		characterParserUtil = new CharacterParserUtil();

		if (!TextUtils.isEmpty(prephonenumber_edit.getText().toString())) {
			setHeadRightEnabled(true); //设置下一步为不可点击状态
		} else {
			setHeadRightEnabled(false); //设置下一步为可点击状态
		}

	}

	@Override
	protected void setListener() {
		findPasswordBtn.setOnClickListener(this);

		prephonenumber_edit.addTextChangedListener(new TextWatcher() {

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
				if (!TextUtils.isEmpty(prephonenumber_edit.getText().toString())) {
					setHeadRightEnabled(true); //设置下一步为不可点击状态
				} else {
					setHeadRightEnabled(false); //设置下一步为可点击状态
				}
				/*******************************end*************************************/
			}
		});
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


	/**
	 * 跳转到国家区号选择界面
	 */
	public void intentToCountry() {
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, CountryActivity.class);
		startActivityForResult(intent, 12);
	}


	/* *
	 * activity finish()后调用
	 */
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

	/* 
	 *下一步跳转
	 */
	@Override
	protected void onHeadRightButton(View v) {
		areaNum = editText_countryNum.getText().toString();
		String phoneString = prephonenumber_edit.getText().toString().trim();
		String passwordString = password.getText().toString().trim();
		if (!CommonUtil.isMobileNO(phoneString)) {
			Toast.makeText(LoginActivity.this, "手机号码输入有误", Toast.LENGTH_LONG).show();
			return;
		}
		if (null == passwordString || "".equals(passwordString)) {
			Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
			return;
		}

		Logger.d(TAG, "passwordMD5："+MD5Util.digest(passwordString)); //密码加密

		if (!areaNum.equals("+86")) {
			Toast.makeText(LoginActivity.this, "仅支持中国地区", Toast.LENGTH_LONG).show();
			return;
		}

		phone = phoneString;

		
		//判断是否联网,连接服务器判断登录的代码
		if (CommonUtil.hasNetwork(LoginActivity.this)) {
			//			veriryLogin(phoneString,MD5Util.digest(passwordString));
			
			//查询手机号是否存在
			//如果存在，则登录；否则跳转到注册页面
			verifyHasPhone(phoneString, passwordString);

		} else {
			Toast.makeText(LoginActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();  //网络未连接
		}

	}

	/**
	 * 查询手机号是否存在
	 * @param phoneString
	 * @return 
	 * @return
	 */
	private void verifyHasPhone(String phoneString, final String passwordString) {
		showProgressDialog("正在登陆...");
		BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
		query.addWhereEqualTo("mobilePhoneNumber", phone);
		query.findObjects(this, new FindListener<BmobUser>() {
		    @Override
		    public void onSuccess(List<BmobUser> object) {
		    	//如果查到用户，即object.size() > 0则提示已经注册
		    	if (object.size() > 0) {
		    		loginByPhonePwd(MD5Util.digest(passwordString));
				} else {
					closeProgressDialog();
					toReg();
				}
	    		Logger.e(TAG, "查询用户成功："+object.size());
		    }
		    @Override
		    public void onError(int code, String msg) {
		    	closeProgressDialog();
		    	Toast.makeText(LoginActivity.this, "查询失败："+msg, Toast.LENGTH_LONG).show();
		    	Logger.e(TAG, "查询失败："+msg);
		    }
		});
	}
	
	/**
	 * 跳转到注册
	 */
	protected void toReg() {
		//对话框跳转到注册界面
		final BaseDialog baseDialog = new BaseDialog(LoginActivity.this, "提示","此号码还没注册，请先注册", "好", "取消");
		baseDialog.show();
		baseDialog.setClicklistener(new BaseDialog.ClickListenerInterface() {
			
			@Override
			public void doConfirm() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
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

	/**
	 * 通过手机号码和密码登陆
	 * @param password
	 */
	private void loginByPhonePwd(String password){
//		showProgressDialog("正在登陆...");
		BmobUser.loginByAccount(this, phone, password, new LogInListener<SpecialUser>() {
			@Override
			public void done(SpecialUser user, BmobException e) {
				// TODO Auto-generated method stub
				if(user!=null){
					closeProgressDialog();
					Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show(); 
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					Log.i(TAG, ""+user.getUsername()+"-"+user.getObjectId()+"-"+user.getEmail());
				}else{
					closeProgressDialog();
					Toast.makeText(LoginActivity.this, "错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); 
				}
			}
		});
	}

}
