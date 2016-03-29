package com.cmdesign.hellonwsuaf.ui;

import com.cmdesign.hellonwsuaf.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PopMenuActivity extends Activity{

	private Button btnShareUrl, btnSharePicture, btnCancel;
	private LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup_menu);
		
		btnShareUrl = (Button)this.findViewById(R.id.button_menu_share_url);
		btnSharePicture = (Button)this.findViewById(R.id.button_menu_share_picture);
		btnCancel = (Button)this.findViewById(R.id.button_menu_cancel);
		
		layout = (LinearLayout)findViewById(R.id.pop_layout);
		
		//添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
		layout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！", 
						Toast.LENGTH_SHORT).show();	
			}
		});
		//添加按钮监听
		OnClickListener handler = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.button_menu_share_url:
				case R.id.button_menu_share_picture:
				case R.id.button_menu_cancel:
					//TODO
				default:
		               //数据是使用Intent返回
	                Intent intent = new Intent();
	                //把返回数据存入Intent
	                intent.putExtra("result", "My name is ");
	                //设置返回数据
	                setResult(RESULT_OK, intent);
	                //关闭Activity
					break;
				}
				finish();
			}
		};
		btnShareUrl.setOnClickListener(handler);
		btnSharePicture.setOnClickListener(handler);
		btnCancel.setOnClickListener(handler);
	}
	
	//实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
}
