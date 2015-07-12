package com.gzucm.youyin.widget;

import com.gzucm.youyin.R;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义的dialog
 * 
 * @author guosheng
 *
 */
public class BaseDialog extends Dialog {

	private Context context;
	private String title;
	private String confirmButtonText=null;
	private String cacelButtonText=null;
	private String message;

	private ClickListenerInterface clickListenerInterface;

	/*
	 * 接口形式写出按钮 方便实行具体的业务
	 */
	public interface ClickListenerInterface {

		public void doConfirm();

		public void doCancel();
		
	}
	
	/*
	 * 
	 * 无图标的构造函数
	 */
	public BaseDialog(Context context, String title, String message,
			String confirmButtonText, String cacelButtonText) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.title = title;
		this.message = message;
		this.confirmButtonText = confirmButtonText;
		this.cacelButtonText = cacelButtonText;
	}
	public BaseDialog(Context context, String title, String message,
			String confirmButtonText) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.title = title;
		this.message = message;
		this.confirmButtonText = confirmButtonText;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		init();
	}

	public void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog, null);
		setContentView(view);

		TextView tvTitle = (TextView) view.findViewById(R.id.title);
		TextView tvmessage = (TextView) view.findViewById(R.id.message);
		Button tvConfirm = (Button) view.findViewById(R.id.confirm);
		Button tvCancel = (Button) view.findViewById(R.id.cancel);

		tvTitle.setText(title);
		tvmessage.setText(message);
		
		if(confirmButtonText==null||cacelButtonText==null)
		{
			tvCancel.setVisibility(View.GONE);
			//tvConfirm.setVisibility(View.GONE);
		    LinearLayout linearLayout=(LinearLayout)findViewById(R.id.button_linearLayout);
		   linearLayout.setPadding(55, 0, 55, 0);
		    
			tvConfirm.setOnClickListener(new clickListener());
		}
		
		
		else{
		tvConfirm.setText(confirmButtonText);
		tvCancel.setText(cacelButtonText);

		tvConfirm.setOnClickListener(new clickListener());
		tvCancel.setOnClickListener(new clickListener());
		}

		
		
		
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
		dialogWindow.setAttributes(lp);
	}

	public void setClicklistener(ClickListenerInterface clickListenerInterface) {
		this.clickListenerInterface = clickListenerInterface;
	}

	private class clickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch (id) {
			case R.id.confirm:
				clickListenerInterface.doConfirm();
				break;
			case R.id.cancel:
				clickListenerInterface.doCancel();
				break;
			}
		}

	};

}