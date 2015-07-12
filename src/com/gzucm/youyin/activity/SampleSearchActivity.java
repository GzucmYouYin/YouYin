package com.gzucm.youyin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gzucm.youyin.R;

public class SampleSearchActivity extends BaseActivity {

	private TextView quxiaoTextView;
	private EditText sousuoEditText;
	private ListView list;
    private String[] array ={"五花茶","梨花茶","菊花茶"};

	
	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);

	}
	 

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.search_quxiao:
			startActivity(new Intent(this,MainActivity.class));
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
		setContentView(R.layout.searchtitle_activity);
		

	}

	@Override
	protected void findViewById() {
		quxiaoTextView=(TextView)findViewById(R.id.search_quxiao);
		list = (ListView) findViewById(R.id.list);
		sousuoEditText=(EditText)findViewById(R.id.sousuo_edit);

	}

	@Override
	protected void setListener() {

		quxiaoTextView.setOnClickListener(this);
		sousuoEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String str=quxiaoTextView.getText().toString().trim();
				if(!str.equals(""))
				{
					ArrayAdapter<String> adapter=new ArrayAdapter<String>(SampleSearchActivity.this, android.R.layout.simple_list_item_1,array);
					list.setAdapter(adapter);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		}) ;
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(array[position].equals("五花茶"))
				{
					startActivity(new Intent(SampleSearchActivity.this,MainActivity.class));
				}
				if(array[position].equals("梨花茶"))
				{
					startActivity(new Intent(SampleSearchActivity.this,MainActivity.class));
				}
				if(array[position].equals("菊花茶"))
				{
					startActivity(new Intent(SampleSearchActivity.this,MainActivity.class));
				}
			}
			
		});
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

}
