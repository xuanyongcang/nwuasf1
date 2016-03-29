package com.cmdesign.hellonwsuaf.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

/*
Notification notification = new Notification(R.drawable.icon, getText(R.string.ticker_text),
        System.currentTimeMillis());
Intent notificationIntent = new Intent(this, ExampleActivity.class);
PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
notification.setLatestEventInfo(this, getText(R.string.notification_title),
        getText(R.string.notification_message), pendingIntent);
startForeground(ONGOING_NOTIFICATION, notification);
 */
public class PlayerService extends Service {
	private MediaPlayer mPlayer;
	private String url;
	private boolean playing = false, ready = false;
	
	public static final String RECEIVER_NAME = "android.intent.action.CC_RECEIVER";

	public static final String ACTION_TOGGLE = "toggle",
			ACTION_PAUSE = "pause",
			ACTION_LOAD = "load",
			ACTION_STOP = "stop",
			ACTION_EXIT = "exit";

	public static final String EVENT_ERROR = "error",
			EVENT_NEXT = "next",
			EVENT_READY = "ready",
			EVENT_COMPLETE = "complete",
			EVENT_BUFFER = "buffer",
			EVENT_PAUSE = "false";

	public static final String EXTRA_PLAYING = "playing",
			EXTRA_ACTION = "action",
			EXTRA_EVENT = "event",
			EXTRA_URL = "url",
			EXTRA_READY = "ready",
			EXTRA_PROGRESS = "progress";

	class CyclePhoneListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if(!ready || mPlayer == null) return;
			
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				//来电时player在播放就暂停
				playing = false;
				toggle();
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				playing = true;
				toggle();
			default:

			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		//监听来电
		TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		manager.listen(new CyclePhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stop();
		stopSelf();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent.getStringExtra(EXTRA_ACTION);
		if(action.equalsIgnoreCase(ACTION_LOAD)) {
			url = intent.getStringExtra(EXTRA_URL);
			//if(url != null) {
			loadMusic(url);
		} else if(action.equalsIgnoreCase(ACTION_TOGGLE)) {
			playing = intent.getBooleanExtra(EXTRA_PLAYING, !playing);
			toggle();
		} else if(action.equalsIgnoreCase(ACTION_STOP)) {
			stop();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void toggle() {
		if(mPlayer == null) return;
		if(playing) {
			mPlayer.start();
		} else {
			mPlayer.pause();
		}
		playing = !playing;
	}

	private void stop() {
		if(mPlayer != null && ready){
			mPlayer.stop();
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}		
	}
	/** 
	 *
	 * @return 
	 */    
	private void loadMusic(String url) {
		try {
			ready = false;
			if(mPlayer != null){
				mPlayer.reset();
				mPlayer.release();
				mPlayer = null;
			}

			mPlayer = new MediaPlayer();
			mPlayer.setOnErrorListener(new OnErrorListener(){
				@Override
				public boolean onError(MediaPlayer mp, int arg1, int arg2) {				  
					mp.reset();
					Intent intent = new Intent();  
					intent.setAction(RECEIVER_NAME);
					intent.putExtra(EXTRA_EVENT, EVENT_ERROR);  
					sendBroadcast(intent);
					return false;
				}
			});
			mPlayer.setOnPreparedListener(new OnPreparedListener() {
				public void onPrepared(MediaPlayer player) {
					mPlayer.start();				   
					ready = true;
					Intent intent = new Intent();  
					intent.setAction(RECEIVER_NAME);
					intent.putExtra(EXTRA_EVENT, EVENT_READY);
					intent.putExtra(EXTRA_READY, true);
					sendBroadcast(intent);        
				}
			});
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					Intent intent = new Intent();  
					intent.setAction(RECEIVER_NAME);
					intent.putExtra(EXTRA_EVENT, EVENT_COMPLETE);
					sendBroadcast(intent);
				}
			});
			mPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
				@Override
				public void onBufferingUpdate(MediaPlayer mp, int percent) {
					Intent intent = new Intent();  
					intent.setAction(RECEIVER_NAME);
					intent.putExtra(EXTRA_EVENT, EVENT_BUFFER);
					intent.putExtra(EXTRA_PROGRESS, percent);
					sendBroadcast(intent);
				}
			});
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.setDataSource(url);  
			mPlayer.prepareAsync();
		} catch (Exception e) {  

		}
	}
}
