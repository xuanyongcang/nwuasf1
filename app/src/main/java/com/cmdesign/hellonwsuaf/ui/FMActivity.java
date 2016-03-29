package com.cmdesign.hellonwsuaf.ui;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import com.cmdesign.hellonwsuaf.R;
import com.cmdesign.hellonwsuaf.bean.RadioItem;
import com.cmdesign.hellonwsuaf.helper.MD5Helper;
import com.cmdesign.hellonwsuaf.helper.PathUtil;
import com.cmdesign.hellonwsuaf.service.PlayerService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
//import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FMActivity extends Activity {
	private final String URL = "http://jiangfan.ujs.edu.cn/xwzx/radio.php?s=a&id=%s&var=";
	private int id = 0;	
	private RadioItem current = null, previous = null;
	private boolean playing = false;
	
	private NotificationManager notifyMan;
	private Notification notify;
	
	AsyncHttpClient jsonHttpClient, imageHttpClient;
	
	private MyReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fm);
		
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				switch(v.getId()) {
				case R.id.button_next:
					next();
					break;
				case R.id.button_prev:
					prev();
					break;
				default:
					return;
				}
			}
		};
		
		findViewById(R.id.button_next).setOnClickListener(listener);
		findViewById(R.id.button_prev).setOnClickListener(listener);
		((CompoundButton)findViewById(R.id.button_toggle_play))
			.setOnCheckedChangeListener(
				new OnCheckedChangeListener() {  
				@Override  
				public void onCheckedChanged(CompoundButton buttonView,  
						boolean isChecked) {  
					if(isChecked){  
						play();				
					} else {  
						pause();
					}
				}
			});
	
		jsonHttpClient = new AsyncHttpClient();
		imageHttpClient = new AsyncHttpClient();
		
        receiver = new MyReceiver();  
        IntentFilter filter = new IntentFilter();  
        filter.addAction(PlayerService.RECEIVER_NAME);
        registerReceiver(receiver, filter);  
        
        //NOTIFY
        notifyMan = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notify = new Notification();
		notify.icon = R.drawable.ic_launcher;
		notify.tickerText = getString(R.string.app_name);
		notify.flags = Notification.FLAG_ONGOING_EVENT;
        
		//detect wifi
		if(((WifiManager)getSystemService(Context.WIFI_SERVICE)).getWifiState()
				== WifiManager.WIFI_STATE_ENABLED) {
			next();
			return;
		}
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(FMActivity.this);	    
        builder.setMessage(R.string.wifi_not_opened);
        builder.setTitle(R.string.tip);
        builder.setPositiveButton(R.string.ok,
	        new android.content.DialogInterface.OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	                dialog.dismiss();
	                next();
	            }
	        });
        builder.setNegativeButton(R.string.cancel,  
	        new android.content.DialogInterface.OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	                dialog.dismiss();
	                failure();
	            }
	        });  
        builder.create().show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		notifyMan.cancel(R.string.app_name);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		/*
		if(current != null && playing) {
			//添加通知到顶部任务栏			
			//实例化Notification
			
			notify.when = System.currentTimeMillis();
			Context context = getApplicationContext();
			//实例化Intent
			Intent intent = new Intent(context, MainTabActivity.class);
			//获得PendingIntent
			PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
			//设置事件信息，显示在拉开的里面
			notify.setLatestEventInfo(FMActivity.this, getString(R.string.app_name),
				String.format(getString(R.string.fm_running), current.getTitle()), pi);
			//发出通知
			notifyMan.notify(R.string.app_name, notify);
		}
		*/
	}
	
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //不要忘了这一步  
        unregisterReceiver(receiver);  
    }
	
	/*
	 * message handler
	 */
	private class MyReceiver extends BroadcastReceiver {  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String event = intent.getStringExtra(PlayerService.EXTRA_EVENT);
            if(event == null) return;
            if(event.equalsIgnoreCase(PlayerService.EVENT_READY)) {
            	//ready = intent.getBooleanExtra(PlayerService.EVENT_READY, false);
            	ready();
            } else if(event.equalsIgnoreCase(PlayerService.EVENT_COMPLETE)) {
            	next();
            } else if(event.equalsIgnoreCase(PlayerService.EVENT_ERROR)) {
            	Toast.makeText(getApplicationContext(), 
            			getString(R.string.fm_load_error), Toast.LENGTH_SHORT).show();
            	next();
            } else if(event.equalsIgnoreCase(PlayerService.EVENT_PAUSE)) {
            	pause();
            }/* else if(event.equalsIgnoreCase(PlayerService.EVENT_BUFFER)) {
            	final int progress = intent.getIntExtra(PlayerService.EXTRA_PROGRESS, 100);
            	FMActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ProgressBar circleProgressBar = 
							(ProgressBar)findViewById(R.id.prog_loading);												
						circleProgressBar.setProgress(progress);  
						circleProgressBar.setVisibility(progress == 100 ? View.GONE : View.VISIBLE);
					}	
            	});
            } */
        }  
    }
	
    private void loadImage(String picUrl) {
    	if(picUrl.equals("")){
    		setCover(null);
    		return;
    	}
    	
    	final String fileName = PathUtil.getCachePath() + MD5Helper.encodeString(picUrl);
    	File file = new File(fileName);
    	if(file.exists()) {
    		//缓存已存在
    		Bitmap bmp = BitmapFactory.decodeFile(fileName);
    		setCover(bmp);
    	}
    	
    	AsyncHttpClient client = new AsyncHttpClient();
    	String[] allowedContentTypes = new String[] { "image/png", "image/jpeg" };
    	client.get(picUrl, new BinaryHttpResponseHandler(allowedContentTypes) {
    	    @Override
    	    public void onSuccess(byte[] data) {
    	    	Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);  
    	    	setCover(bmp);
    	    	try{
    	    		DataOutputStream d = new DataOutputStream(new FileOutputStream(fileName));
    	    		d.write(data);
    	    		d.flush();
    	    		d.close();
    	    	} catch(Exception e) {
    	    		
    	    	}
    	    }
    	});
    }
    
    private void setCover(Bitmap bmp) {
		ImageView image = (ImageView)findViewById(R.id.img_radio_cover);
    	if(bmp != null) {
    		image.setImageBitmap(bmp);
    	} else {
    		//randomly pick a cover picture
    		int ids[] = {R.drawable.radio_cover_1, R.drawable.radio_cover_2,
    				R.drawable.radio_cover_3, R.drawable.radio_cover_4,
    				R.drawable.radio_cover_5};
    		java.util.Random random = new java.util.Random();
    		random.setSeed(System.currentTimeMillis() * 10 + 0x01B21DD213814000L);
    		id = ids[Math.abs(random.nextInt()) % ids.length];
    		image.setImageResource(id);
    	}
    }
	
    private void ready() {
    	//ready = true;
    	enableControls(true);
    	play();
    }
    
	private void play() {
		playing = true;
		toggle();
	}
	
	private void stop() {
		Intent intent = new Intent(this, PlayerService.class)
			.putExtra(PlayerService.EXTRA_ACTION, PlayerService.ACTION_STOP);
        startService(intent);
	}
	
	private void pause() {
		playing = false;
		toggle();
	}
	
	private void toggle() {
		Intent intent = new Intent(this, PlayerService.class);
		intent.putExtra(PlayerService.EXTRA_ACTION, PlayerService.ACTION_TOGGLE);
		intent.putExtra(PlayerService.EXTRA_PLAYING, playing);		
        startService(intent);
        ((CompoundButton)findViewById(R.id.button_toggle_play)).setChecked(playing);
	}
	
	private void next() {		
		if(current != null) previous = current;
		enableControls(false);
		stop();
		//ready = false;
				
		//
		String url = String.format(URL, id);
    	jsonHttpClient.get(url, new AsyncHttpResponseHandler() {
    	    @Override
    	    public void onSuccess(String response) {
    	    	if(response == null) return;
    	    	
    	    	int start, end;
    	    	CharSequence keys[] = {"id", "title", "file", "image"};
    	    	HashMap<CharSequence, String> info = new HashMap<CharSequence, String>();
    	    	
    	    	for(CharSequence key : keys){
	    	    	String str = String.format("\"%s\":\"", key);   	    			
	    	    	start = response.indexOf(str);
	    	    	end = response.indexOf('"', start + str.length());
	    	    	info.put(key, response.substring(start + str.length(), end));	    	    	
    	    	}
    	    	
    	    	RadioItem radio = new RadioItem(info.get(keys[1]),
    	    		info.get(keys[2]),
    	    		info.get(keys[3])
    	    		);
    	    	radio.setId(info.get(keys[0]));
    	    	
    	    	id = radio.getId();
    	    	current = radio;
    	    	loadProgram(radio);
    	    }
    	    
    	    @Override
    	    public void onFailure(Throwable e, String response) {
    	    	failure();
    	    }
    	});
	}
	
	private void prev() {
		if(previous == null) return;
		stop();
		current = previous;
		previous = null;
		loadProgram(current);
	}
	
	private void failure() {
	    findViewById(R.id.fm_control_toolbar).setVisibility(View.GONE);
	    TextView text = (TextView)findViewById(R.id.text_radio_title);
	    text.setText(R.string.network_down);
	}
	
	private void loadProgram(RadioItem radio) {		
		Intent intent = new Intent(this, PlayerService.class);
		intent.putExtra(PlayerService.EXTRA_ACTION, PlayerService.ACTION_LOAD);
		intent.putExtra(PlayerService.EXTRA_URL, radio.getUrl());
		startService(intent);
		
    	loadImage(radio.getPicUrl());
    	TextView title = (TextView)findViewById(R.id.text_radio_title);
    	title.setText(radio.getTitle());
	}
	
	private void enableControls(boolean enable){
		for(int id : new int[]{
			R.id.button_next,
			R.id.button_prev,
			R.id.button_toggle_play
			})
		findViewById(id).setEnabled(enable);
	}
}
