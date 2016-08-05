package com.cmdesign.hellonwsuaf.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cmdesign.hellonwsuaf.R;
import com.cmdesign.hellonwsuaf.helper.NetManager2;

import java.util.HashMap;
import java.util.Map;

public class Loginn2Activity extends Activity {

	private String TAG = Loginn2Activity.class.getName();
	private ImageView imageView;
	private SharedPreferences pref2;
	private SharedPreferences pref23;
	private SharedPreferences.Editor editor;
	private CheckBox rememberPass;
	private Bitmap bitmap;
	private NetManager2 NetManager2;
	private ProgressBar roundProBar;
	private Button loginBtn;
	private EditText idEdit;
	private EditText pwdEdit;
//	private EditText codeEdit;
	private final int GET_CODE_SUCCESS = 1;
	private final int GET_CODE_ERROR = 2;
	private final int LOGIN_SUCCESS = 3;
	private final int LOGIN_ERROR = 4;
	private Button button;
	private final String LOGIN_URL = "http://yjspy.nwafu.edu.cn/j_acegi_security_check";
	private String result = null;
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int tag =  msg.what;

			switch(tag){
				case GET_CODE_SUCCESS:
					//imageView.setImageBitmap(bitmap);
					break;
				case GET_CODE_ERROR:
//					Toast.makeText(Loginn2Activity.this, "get code error", Toast.LENGTH_SHORT).show();
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
		setContentView(R.layout.activity_login2);
//		pref23 = PreferenceManager.getDefaultSharedPreferences(this);
//		result= pref23.getString("schedule", "");
//		if(result!="") {
//			Intent intentja = new Intent();
//			intentja.setClass(Loginn2Activity.this, SplashActivity.class);
//			startActivity(intentja);
//		}
		init();
		new GetCodeThread().start();
	}

	/***************************
	 * 初始化UI
	 ****************************/
	private void init(){
		NetManager2 = NetManager2.getNetManager2();
//		imageView = (ImageView) findViewById(R.id.login_image1);
		loginBtn = (Button) findViewById(R.id.user_login_btn1);
		idEdit = (EditText) findViewById(R.id.user_id1);
		pwdEdit = (EditText) findViewById(R.id.user_pwd1);
//		codeEdit = (EditText) findViewById(R.id.user_code1);
		roundProBar = (ProgressBar) findViewById(R.id.login_progressbar1);
		roundProBar.setVisibility(View.GONE);
		pref2 = PreferenceManager.getDefaultSharedPreferences(this);
		rememberPass = (CheckBox) findViewById(R.id.checkBox1231);
		rememberPass.setChecked(true);

		boolean isRemember = pref2.getBoolean("remember_password", false);
		if (isRemember) {
			String account = pref2.getString("account2", "");
			String password = pref2.getString("password2", "");
			idEdit.setText(account);
			pwdEdit.setText(password);
			rememberPass.setChecked(true);
		}
		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String id = idEdit.getText().toString().trim();
				String pwd = pwdEdit.getText().toString().trim();
//				String code = codeEdit.getText().toString().trim();
				editor = pref2.edit();
				if (rememberPass.isChecked()) {
					editor.putBoolean("remember_password", true);
					editor.putString("account2", id);
					editor.putString("password2", pwd);
				} else {
					editor.clear();
				}
				editor.commit();
				Map<String, String> map = new HashMap<String, String>();
//				map.put("j_captcha", code);
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
					result = NetManager2.getWebWithPost(url,map);
					Log.d(TAG, result);
					Log.d(TAG, result);
					msg.what = LOGIN_SUCCESS;
				}catch(Exception e){
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
				bitmap = NetManager2.getcode();
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
		it.setClass(this, MainTabActivity.class);
		startActivity(it);
	}
}

//public class LoginnActivity extends Activity {
//	private CheckBox rem_pw;
//	private String TAG = LoginnActivity.class.getName();
//	private ImageView imageView;
//	private Bitmap bitmap;
//	private NetManager2 NetManager2;
//	private ProgressBar roundProBar;
//	private Button loginBtn;
//	private EditText idEdit;
//	private EditText pwdEdit;
//	private EditText codeEdit;
//	private final int GET_CODE_SUCCESS = 1;
//	private final int GET_CODE_ERROR = 2;
//	private final int LOGIN_SUCCESS = 3;
//	private final int LOGIN_ERROR = 4;
//	private final String LOGIN_URL = "http://jwgl.nwsuaf.edu.cn/academic/j_acegi_security_check";
////	private SharedPreferences sp;
//
//	private String result = null;
//
//	final Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			int tag =  msg.what;
//
//			switch(tag){
//			case GET_CODE_SUCCESS:
//				imageView.setImageBitmap(bitmap);
//				break;
//			case GET_CODE_ERROR:
//				Toast.makeText(LoginnActivity.this, "get code error", Toast.LENGTH_SHORT).show();
//				break;
//			case LOGIN_SUCCESS:
//				Toast.makeText(LoginnActivity.this, "login success", Toast.LENGTH_SHORT).show();
//				toShowSchedule();
//				break;
//			case LOGIN_ERROR:
//				Toast.makeText(LoginnActivity.this, "login error", Toast.LENGTH_SHORT).show();
//				break;
//			}
//			roundProBar.setVisibility(View.GONE);
//		}
//	};
//
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_login);
////		rem_pw.findViewById(R.id.checkBox123);
//		idEdit = (EditText) findViewById(R.id.user_id);
//		pwdEdit = (EditText) findViewById(R.id.user_pwd);
////		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
////		if(sp.getBoolean("ISCHECK", false))
////		{
////			//设置默认是记录密码状态
////			rem_pw.setChecked(true);
////			idEdit.setText(sp.getString("USER_NAME", ""));
////			pwdEdit.setText(sp.getString("PASSWORD", ""));
////			init2();
////		} else
//init();
////		rem_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////				if (rem_pw.isChecked()) {
////
////					System.out.println("记住密码已选中");
////					sp.edit().putBoolean("ISCHECK", true).commit();
////
////				} else {
////
////					System.out.println("记住密码没有选中");
////					sp.edit().putBoolean("ISCHECK", false).commit();
////
////				}
////
////			}
////		});
//		new GetCodeThread().start();
//	}
//
//	/***************************
//	 * 初始化UI
//	 ****************************/
//	private void init(){
//		NetManager2 = NetManager2.getNetManager2();
//		imageView = (ImageView) findViewById(R.id.login_image);
//		loginBtn = (Button) findViewById(R.id.user_login_btn);
//		codeEdit = (EditText) findViewById(R.id.user_code);
//		roundProBar = (ProgressBar) findViewById(R.id.login_progressbar);
//		roundProBar.setVisibility(View.GONE);
//		loginBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				if (rem_pw.isChecked()) {
////					SharedPreferences.Editor editor = sp.edit();
////					editor.putString("USER_NAME", idEdit.getText().toString());
////					editor.putString("PASSWORD", pwdEdit.getText().toString());
////					editor.commit();
////				}
//				String id = idEdit.getText().toString().trim();
//				String pwd = pwdEdit.getText().toString().trim();
//				String code = codeEdit.getText().toString().trim();
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("j_captcha", code);
////				map.put("j_password", "306524");
////				map.put("j_username", "2012013465");
//				map.put("j_password", pwd);
//				map.put("j_username", id);
//				roundProBar.setVisibility(View.VISIBLE);
//				login(LOGIN_URL, map);
//			}
//		});
//	}

//	/***************************
//	 * 登陆
//	 ****************************/
//	private void login(final String url,final Map<String,String> map){
//
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				Message msg = new Message();
//				try{
//					result = NetManager2.getWebWithPost(url,map);
//					Log.d(TAG, result);
//					msg.what = LOGIN_SUCCESS;
//				}catch(Exception e){
//					msg.what = LOGIN_ERROR;
//				}
//				handler.sendMessage(msg);
//			}
//		}).start();
//	}
//
//	/***************************
//	 * 获得验证码
//	 ****************************/
//	private class GetCodeThread extends Thread {
//		@Override
//		public void run() {
//			Message msg = new Message();
//			try {
//				msg.what = GET_CODE_SUCCESS;
//				bitmap = NetManager2.getcode();
//			} catch (Exception e) {
//				msg.what = GET_CODE_ERROR;
//				e.printStackTrace();
//			}
//			handler.sendMessage(msg);
//		}
//	}
//
//	/***************************
//	 * 跳转至课程表页面
//	 ****************************/
//   private void toShowSchedule(){
//	   Intent it = new Intent();
//	   it.setClass(this, ScheduleActivity2.class);
//	   startActivity(it);
//   }
//}
