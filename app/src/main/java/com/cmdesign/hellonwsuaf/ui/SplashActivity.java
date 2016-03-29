package com.cmdesign.hellonwsuaf.ui;

import java.util.HashMap;

import com.cmdesign.hellonwsuaf.R;
import com.cmdesign.hellonwsuaf.helper.ExtractUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity; 
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent; 
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle; 
import android.os.Handler; 
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

public class SplashActivity extends Activity {    

	private final int SPLASH_DISPLAY_LENGHT = 1000; //延迟1秒

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return true;
        }
		return super.onKeyDown(keyCode, event);
    }
	
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
    	super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_splash); 
        new Handler().postDelayed(new Runnable() {
	        @Override 
			public void run() {
				Intent mainIntent = new Intent(SplashActivity.this, MainTabActivity.class); 
			    SplashActivity.this.startActivity(mainIntent); 
			    SplashActivity.this.finish(); 
			}
        }, SPLASH_DISPLAY_LENGHT); 
        
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String REGISTER_KEY = "registed";
        boolean registed = pref.getBoolean(REGISTER_KEY, false);
        if(!registed) {
        	DisplayMetrics metrics = new DisplayMetrics();
        	getWindowManager().getDefaultDisplay().getMetrics(metrics);
        	HashMap<String, String> param = new HashMap<String, String>();
        	param.put("imei", ((TelephonyManager)this.getSystemService(
        			Context.TELEPHONY_SERVICE)).getDeviceId());
        	param.put("model", android.os.Build.MODEL);
        	param.put("band", android.os.Build.BRAND);        	
        	param.put("sdk", android.os.Build.VERSION.SDK);
        	param.put("os", android.os.Build.VERSION.RELEASE);
        	param.put("screen", metrics.widthPixels + "x" + metrics.heightPixels);
        	RequestParams params = new RequestParams(param);
        	new AsyncHttpClient().get(getString(R.string.url_register), params,
        			new AsyncHttpResponseHandler() {
        	    @Override
        	    public void onSuccess(String response) {
        	    	pref.edit().putBoolean(REGISTER_KEY, true).commit();
        	    }
        	});
        }
        
        //解压文件
        new Thread() {
        	@Override
        	public void run() {
        		try {
        	        ExtractUtil extract = new ExtractUtil(getApplicationContext());
        	        //extract.extractDb(true);
        	        extract.extractMap();
                } catch (Exception ex) {
                	
                } 
        	}
        }.start();
        
        //检查更新
        new Thread() {
        	@Override
        	public void run() {
        		//detect wifi
        		WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        		if(wm.getWifiState() != WifiManager.WIFI_STATE_ENABLED) return;
        		
        		int v = 0;
        	    try {
        	        v = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        	    } catch (Exception e) {
        	        return;
        	    }
        	    
            	RequestParams param = new RequestParams();
            	param.put("v", String.valueOf(v));

            	new AsyncHttpClient().get(getString(R.string.url_update), param,
            			new AsyncHttpResponseHandler() {
            	    @Override
            	    public void onSuccess(String response) {
            	    	if(response.substring(0,7) != "http://") return;
            	    	NotificationManager nm = (NotificationManager)getSystemService(
            	    			NOTIFICATION_SERVICE);
            	    	Notification notification = new Notification(R.drawable.ic_launcher,
            	    			getString(R.string.new_version_found), System.currentTimeMillis());
            	    	            	    	
            	    	Intent intent = new Intent(Intent.ACTION_VIEW);
            	    	intent.setData(Uri.parse(response));
            	    	
            	    	PendingIntent pendingIntent = PendingIntent.getActivity(
            	    			SplashActivity.this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
            	    	notification.setLatestEventInfo(SplashActivity.this, 
            	    			getString(R.string.app_name),
            	    			getString(R.string.new_version_found), pendingIntent);
            	    	notification.flags = Notification.FLAG_AUTO_CANCEL;
            	    	nm.notify(R.string.new_version_found, notification);
            	    }
            	});
        	}
        }.start();
    }
}
