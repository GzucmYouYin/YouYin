package com.gzucm.youyin.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.gzucm.youyin.R;
import com.gzucm.youyin.country.CharacterParserUtil;
import com.gzucm.youyin.country.CountryComparator;
import com.gzucm.youyin.country.CountrySortAdapter;
import com.gzucm.youyin.country.CountrySortModel;
import com.gzucm.youyin.country.GetCountryNameSort;
import com.gzucm.youyin.country.SideBar;


/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author duanbokan
 * 
 */

public class CountryActivity extends BaseWapperActivity {
	String TAG = "CountryActivity";

	private List<CountrySortModel> mAllCountryList;

	private ListView country_lv_countryList;

	private CountrySortAdapter adapter;

	private SideBar sideBar;

	private TextView dialog;

	private CountryComparator pinyinComparator;

	private GetCountryNameSort countryChangeUtil;

	private CharacterParserUtil characterParserUtil;


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initParam() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.country);

		setTitle("选择国家和地区");
		
	}

	@Override
	protected void findViewById() {
		initView();
		
	}

	@Override
	protected void processLogic() {
	    getCountryList();
		
	}
	
	/****
	 * 添加监听
	 */
	protected void setListener() {

		country_lv_countryList
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long arg3) {
						String countryName = null;
						String countryNumber = null;

						countryName = mAllCountryList.get(position).countryName;
						countryNumber = mAllCountryList.get(position).countryNumber;

						Intent intent = new Intent();
						intent.setClass(CountryActivity.this,
								RegisterActivity.class);
						intent.putExtra("countryName", countryName);
						intent.putExtra("countryNumber", countryNumber);
						setResult(RESULT_OK, intent);
						Log.e(TAG, "countryName: + " + countryName
								+ "countryNumber: " + countryNumber
								+ "RESULT_OK：" + RESULT_OK);
						finish();

					}
				});

	}

	/**
	 * 初始化界面
	 */
	private void initView() {

		country_lv_countryList = (ListView) findViewById(R.id.country_lv_list);
		mAllCountryList = new ArrayList<CountrySortModel>();
		pinyinComparator = new CountryComparator();
		countryChangeUtil = new GetCountryNameSort();
		characterParserUtil = new CharacterParserUtil();

		// 将联系人进行排序，按照A~Z的顺序
		Collections.sort(mAllCountryList, pinyinComparator);
		adapter = new CountrySortAdapter(this, mAllCountryList);
		country_lv_countryList.setAdapter(adapter);

	}
	
	/**
	 * 获取国家列表
	 */
	private void getCountryList() {
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

		Collections.sort(mAllCountryList, pinyinComparator);
		adapter.updateListView(mAllCountryList);
		Log.e(TAG, "changdu" + mAllCountryList.size());
	}
}
