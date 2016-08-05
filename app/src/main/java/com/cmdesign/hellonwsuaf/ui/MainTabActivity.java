package com.cmdesign.hellonwsuaf.ui;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.cmdesign.hellonwsuaf.R;
import com.cmdesign.hellonwsuaf.service.PlayerService;
public class MainTabActivity extends TabActivity implements OnCheckedChangeListener {	
	private RadioGroup mainTab;
	private TabHost mTabHost;
	
	//内容Intent
	private Intent mHomeIntent, mGuideIntent, mLatitudeIntent, mRadioIntent, mPediaIntent;	
	private final String INTENT_NAME = "URL";
	
	//private final static String TAB_TAG_WEB = "tab_tag_webview";
	
	private final static String TAB_TAG_HOME = "tab_tag_home",
			TAB_TAG_GUIDE = "tab_tag_guide",
			TAB_TAG_LATITUDE = "tab_tag_latitude",
			TAB_TAG_RADIO = "tab_tag_radio",
			TAB_TAG_PEDIA = "tab_tag_pedia";
		
	private Resources res;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        res = getResources();
        mainTab = (RadioGroup)findViewById(R.id.main_tab);
        mainTab.setOnCheckedChangeListener(this);
        prepareIntent();
        setupIntent();
    }
    
    /**
     * 准备tab的内容Intent
     */    
	private void prepareIntent() {
//		mHomeIntent = new Intent(this, WebPanelActivity.class)
//			.putExtra(INTENT_NAME, getString(R.string.url_panel_home));
		mHomeIntent = new Intent(this, javaclass.class)
				.putExtra(INTENT_NAME, getString(R.string.url_panel_home));
		mGuideIntent = new Intent(this, WebPanelActivity.class)		
		.putExtra(INTENT_NAME, getString(R.string.url_panel_guide));
		mLatitudeIntent = new Intent(this, KebiaoChoice.class);
		mPediaIntent = new Intent(this, RoutePlanDemo.class);
//		mPediaIntent=new Intent(this,WebView2Activity.class).putExtra("extra_inter2", "http://weibo.com/p/1005053488964770");
		mRadioIntent = new Intent(this, WebPanelActivity.class)
			.putExtra(INTENT_NAME, getString(R.string.url_panel_home));
//		mRadioIntent  = new Intent(this,FMActivity.class);
//		mPediaIntent = new Intent(this, WebPanelActivity.class)
//			.putExtra(INTENT_NAME, getString(R.string.url_panel_pedia));
	}
	
	/**
	 * 
	 */
	private void setupIntent() {
		TabHost localTabHost = this.mTabHost = getTabHost();
		localTabHost.addTab(buildTabSpec(TAB_TAG_HOME, R.string.main_home,
			R.drawable.ic_main_home, mHomeIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_GUIDE, R.string.main_guide,
				R.drawable.ic_main_guide, mGuideIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_LATITUDE, R.string.main_latitude,
				R.drawable.ic_main_latitude, mLatitudeIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_RADIO, R.string.main_fm,
			R.drawable.ic_main_gossip, mRadioIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_PEDIA, R.string.main_pedia,
				R.drawable.ic_main_pedia, mPediaIntent));
	}
	
	/**
	 * 构建TabHost的Tab页
	 * @param tag 标记
	 * @param resLabel 标签
	 * @param resIcon 图标
	 * @param content 该tab展示的内容
	 * @return 一个tab
	 */
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon, final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel),
			res.getDrawable(resIcon)).setContent(content);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		String tag = null;
		switch(checkedId){
		case R.id.radio_home:
			tag = TAB_TAG_HOME;
			break;
		case R.id.radio_pedia:
			tag = TAB_TAG_PEDIA;
			break;
		case R.id.radio_latitude:
			tag = TAB_TAG_LATITUDE;
			break;
		case R.id.radio_guide:
			tag = TAB_TAG_GUIDE;
			break;
		case R.id.radio_fm:
			tag = TAB_TAG_RADIO;
			break;
		default:			
		}		
		mTabHost.setCurrentTabByTag(tag);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {		
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK &&
        		event.getAction() == KeyEvent.ACTION_DOWN) {  
            confirmQuit();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	this.getMenuInflater().inflate(R.menu.menu_main, menu);
    	return true;
    }
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	int id = item.getItemId();
    	android.content.res.Resources res = getResources();
    	if (id == R.id.menu_item_exit) {
    		this.finish();
    		//android.os.Process.killProcess(android.os.Process.myPid());  
    	} else {
    		Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
    	    intent.putExtra("URL", res.getString(
    	    	id == R.id.menu_item_feedback ? R.string.url_remote_feedback : R.string.url_about));
    	    intent.putExtra("method", "local");
    	    startActivity(intent);
    	}
    	return false;
    }
    
    @Override
	protected void onDestroy() {
    	super.onDestroy();
    	try {
    		stopService(new Intent(this, PlayerService.class));
    		android.os.Process.killProcess(android.os.Process.myPid());
    	} catch(Exception ex) {
    		
    	}
    }
    
    private void confirmQuit() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(MainTabActivity.this);
        builder.setMessage(R.string.confirm_quit);
        builder.setTitle(R.string.tip);  
        builder.setPositiveButton(R.string.sure_to_quit,
	        new android.content.DialogInterface.OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	                dialog.dismiss();
	                MainTabActivity.this.finish();
	            }
	        });
        builder.setNegativeButton(R.string.cancel,  
	        new android.content.DialogInterface.OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	                dialog.dismiss();  
	            }
	        });  
        builder.create().show();   	
    }
}