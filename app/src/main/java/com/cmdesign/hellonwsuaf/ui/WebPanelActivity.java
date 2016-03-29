/**
 * 
 */
package com.cmdesign.hellonwsuaf.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cmdesign.hellonwsuaf.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Home
 * @author ChiChou
 * @since 2013-07-20
 */
@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface", "DefaultLocale" })
public class WebPanelActivity extends Activity {	
	private Resources res;
	private String PREFIX;
	private final String CACHE_NAME = "web_cache",
			CACHE_WEATHER = "cache_weather",
			CACHE_WEATHER_TIME = "cache_weather_time",
			CACHE_NEWS = "cache_news",
			CACHE_NEWS_TIME = "cache_news_time";
	private final long CACHE_EXPIRE = 7200 * 1000;
	private final String ERROR = "error";
	
	private final String SCHEME_ACTIVITY = "activity://",
			SCHEME_HTTP = "http://", SCHEME_ARTICLE = "article://";
	private final String ACTIVITY_TV = "tv", ACTIVITY_CAMERA = "camera";

	private WebView webView;
	
	private String jsAddSlashes(String source) {
		String result = source.replace("\'", "\\\'").replace("\"", "\\\"").replace("\n", "\\n");
		return result;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		res = getResources();
		PREFIX = res.getString(R.string.url_assset_prefix);
		
		webView = new WebView(this);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportMultipleWindows(true);	
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setWebChromeClient(new WebChromeClient(){
        	@Override
			public boolean onCreateWindow (WebView view, boolean isDialog, boolean isUserGesture,
        		Message resultMsg) {
    			return false;
    		}
        });
        webView.setWebViewClient(new WebViewClient() {
        	@Override
        	public void onPageFinished(WebView view, String url) {
        		String prefix = getString(R.string.url_assset_prefix);
        		if(!url.startsWith(prefix)) return;
        		
        		AsyncHttpClient client = new AsyncHttpClient();        		
        		SharedPreferences cacheData = getSharedPreferences(CACHE_NAME, 0);
        		
        		String page = url.substring(prefix.length()); //"file:///android_asset/".length
        		if(page.equalsIgnoreCase(getString(R.string.url_panel_home))) {
        			//首页
            		long timeWeather = cacheData.getLong(CACHE_WEATHER_TIME, 0),
            				timeNews = cacheData.getLong(CACHE_NEWS_TIME, 0);
					final long time = System.currentTimeMillis();
		    		String strWeather = cacheData.getString(CACHE_WEATHER, null),
		    				strNews = cacheData.getString(CACHE_NEWS, null);
		    		
		    		Boolean refreshWeather = timeWeather + CACHE_EXPIRE < time,
		    				refreshNews = timeNews + CACHE_EXPIRE < time;
		    		
		    		if(!refreshWeather)
		    			webView.loadUrl(String.format(res.getString(
		    					R.string.url_js_weather_callback), strWeather));
		    		
		    		if(!refreshNews)
		    			webView.loadUrl(String.format(res.getString(
		    					R.string.url_js_news_callback), strNews));
            		//天气
        			if(refreshWeather)
            		client.get(res.getString(R.string.url_weather), new AsyncHttpResponseHandler() {
            		    @Override
            		    public void onSuccess(String raw) {
            		    	String encoded = jsAddSlashes(raw);
            		    	SharedPreferences.Editor editor = getSharedPreferences(CACHE_NAME, 0).edit(); 
            		    	editor.putLong(CACHE_WEATHER_TIME, System.currentTimeMillis());
            		    	editor.putString(CACHE_WEATHER, encoded);
            		    	editor.commit();
            		        webView.loadUrl(String.format(res.getString(
            		        		R.string.url_js_weather_callback), encoded));
            		    }
            		    
            		    @Override
						public void onFailure(Throwable e, String response) {
            		        webView.loadUrl(String.format(res.getString(
            		        		R.string.url_js_weather_callback), ERROR));	    	
            		    }
            		});
        			
        			if(refreshNews)
            		//新闻
            		client.get(res.getString(R.string.url_news), new AsyncHttpResponseHandler() {
            		    @Override
            		    public void onSuccess(String raw) {
            		    	String encoded = jsAddSlashes(raw);
            		    	SharedPreferences.Editor editor = getSharedPreferences(CACHE_NAME, 0).edit(); 
            		    	editor.putLong(CACHE_NEWS_TIME, System.currentTimeMillis());
            		    	editor.putString(CACHE_NEWS, encoded);
            		    	editor.commit();            		    	
            		        webView.loadUrl(String.format(res.getString(
            		        		R.string.url_js_news_callback), encoded));
            		    }
            		    
            		    @Override
						public void onFailure(Throwable e, String response) {
            		        webView.loadUrl(String.format(res.getString(
            		        		R.string.url_js_news_callback), ERROR));
            		    }
            		});
        		} else {
        			
        		}
        	}
        	@Override
        	public void onPageStarted(WebView view, String url, Bitmap favicon) {
        		
        	}
        	@SuppressLint("DefaultLocale")
			@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		boolean handled = false;
        		android.content.Context context = getApplicationContext();
                if(url.startsWith(SCHEME_HTTP) || url.startsWith(SCHEME_ARTICLE)) {
            	    startActivity(new Intent(context, WebViewActivity.class)
            	    	.putExtra("URL", url));
            	    //webView.stopLoading();
            	    handled = true;
                } else if(url.startsWith(SCHEME_ACTIVITY)) {
                	boolean launch = false;
                	Intent intent = new Intent();
                	String path = url.substring(SCHEME_ACTIVITY.length()).toLowerCase();
//                	if(path.equalsIgnoreCase(ACTIVITY_TV)) {
//                		intent.setClass(WebPanelActivity.this, JDTVActivity.class);
//                		launch = true;
//                	} else if(path.startsWith(ACTIVITY_CAMERA)) {
//                		//TODO
//                		Toast.makeText(getApplicationContext(), "水印相机功能即将登场，敬请期待！",
//                				Toast.LENGTH_LONG).show();
//                	}
                	if(launch) startActivity(intent);
                	handled = true;
                }
                return handled;
		    }
        });
		setContentView(webView);	
		processExtraData();
	}
	
	/*
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent); //must store the new intent unless getIntent() will return the old one
		processExtraData();
	}
	*/
	private void processExtraData(){
		Intent intent = getIntent();	
		String url = intent.getStringExtra("URL");
		webView.loadUrl(PREFIX + url);
	}
	
	@Override
	public void onDestroy(){
	    super.onDestroy();
	    webView.destroy();
	    webView = null;
	    finish();
	    System.gc();
	}
}
