package com.cmdesign.hellonwsuaf.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cmdesign.hellonwsuaf.R;
import com.cmdesign.hellonwsuaf.helper.NetManager;

import java.util.HashMap;
import java.util.Map;

public class LoginnActivity extends Activity {

	private String TAG = LoginnActivity.class.getName();
	private ImageView imageView;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private CheckBox rememberPass;
	private Bitmap bitmap;
	private NetManager netManager;
	private ProgressBar roundProBar;
	private Button loginBtn;
	private EditText idEdit;
	private EditText pwdEdit;
	private EditText codeEdit;
	private final int GET_CODE_SUCCESS = 1;
	private final int GET_CODE_ERROR = 2;
	private final int LOGIN_SUCCESS = 3;
	private final int LOGIN_ERROR = 4;
	private final String LOGIN_URL = "http://jwgl.nwsuaf.edu.cn/academic/j_acegi_security_check";
	private String result = null;
	private Button button12;
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int tag =  msg.what;

			switch(tag){
				case GET_CODE_SUCCESS:
					imageView.setImageBitmap(bitmap);
					break;
				case GET_CODE_ERROR:
					Toast.makeText(LoginnActivity.this, "验证码获取失败", Toast.LENGTH_SHORT).show();
					break;
				case LOGIN_SUCCESS:
//					Toast.makeText(LoginnActivity.this, "login success", Toast.LENGTH_SHORT).show();
					toShowSchedule();
					break;
				case LOGIN_ERROR:
//					Toast.makeText(LoginnActivity.this, "login error", Toast.LENGTH_SHORT).show();
					break;
			}
			roundProBar.setVisibility(View.GONE);
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		new GetCodeThread().start();
	}

	/***************************
	 * 初始化UI
	 ****************************/
	private void init(){
		netManager = NetManager.getNetManager();
		button12 = (Button) findViewById(R.id.button4);
		imageView = (ImageView) findViewById(R.id.login_image);
		loginBtn = (Button) findViewById(R.id.user_login_btn);
		idEdit = (EditText) findViewById(R.id.user_id);
		pwdEdit = (EditText) findViewById(R.id.user_pwd);
		codeEdit = (EditText) findViewById(R.id.user_code);
		roundProBar = (ProgressBar) findViewById(R.id.login_progressbar);
		roundProBar.setVisibility(View.GONE);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		rememberPass = (CheckBox) findViewById(R.id.checkBox123);
		boolean isRemember = pref.getBoolean("remember_password", false);
		if (isRemember) {
			String account = pref.getString("account", "");
			String password = pref.getString("password", "");
			idEdit.setText(account);
			pwdEdit.setText(password);
			rememberPass.setChecked(true);
		}
		button12.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginnActivity.this, javaclass2.class);
				startActivity(intent);
			}
		});
		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String id = idEdit.getText().toString().trim();
				String pwd = pwdEdit.getText().toString().trim();
				String code = codeEdit.getText().toString().trim();
				editor = pref.edit();
				if (rememberPass.isChecked()) {
					editor.putBoolean("remember_password", true);
					editor.putString("account", id);
					editor.putString("password", pwd);
				} else {
					editor.clear();
				}
				editor.commit();
				Map<String, String> map = new HashMap<String, String>();
				map.put("j_captcha", code);
				map.put("j_password", pwd);
				map.put("j_username", id);
				roundProBar.setVisibility(View.VISIBLE);
				login(LOGIN_URL, map);
			}
		});
	}
	/***************************
	 * 登陆
	 ****************************/
	private void login(final String url,final Map<String,String> map){

		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				try{
					result = netManager.getWebWithPost(url,map);
					msg.what = LOGIN_SUCCESS;
				}catch(Exception e){
					msg.what = LOGIN_ERROR;
				}
				handler.sendMessage(msg);
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					result = netManager.getWebWithPost(url, map);
					msg.what = LOGIN_SUCCESS;
				} catch (Exception e) {
					msg.what = LOGIN_ERROR;
				}
				handler.sendMessage(msg);
			}
		}).start();

	}
	/***************************
	 * 获得验证码
	 ****************************/
	private class GetCodeThread extends Thread {
		@Override
		public void run() {
			Message msg = new Message();
			try {
				msg.what = GET_CODE_SUCCESS;
				bitmap = netManager.getcode();
			} catch (Exception e) {
				msg.what = GET_CODE_ERROR;
				e.printStackTrace();
			}

			handler.sendMessage(msg);
		}
	}

	/***************************
	 * 跳转至课程表页面
	 ****************************/
	private void toShowSchedule(){
		Intent it = new Intent();
		it.setClass(this, Schedulebenke.class);
		startActivity(it);
	}
}
