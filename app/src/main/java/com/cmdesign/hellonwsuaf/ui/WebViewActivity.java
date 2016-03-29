package com.cmdesign.hellonwsuaf.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.util.EncodingUtils;

import com.cmdesign.hellonwsuaf.bean.Article;
import com.cmdesign.hellonwsuaf.helper.DBManager;
import com.cmdesign.hellonwsuaf.helper.PathUtil;

import com.cmdesign.hellonwsuaf.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebViewActivity extends Activity {
	private final String PREFIX = "file:///android_asset/";
	public static final String //SCHEME_ACTIVITY = "activity://",
			SCHEME_ARTICLE = "article://",
			SCHEME_HTTP = "http://";
	//private final String ACTIVITY_MAP = "map";
	private final String TEMPLATE_ARTICLE = "article.htm", 
			TEMPLATE_ERROR = "error.htm";
	private final String ENCODING = "UTF-8", MIME = "text/html";
	
	private String url;
	private WebView webView = null;
	
	private void displayErrorPage(WebView view, int errorCode, String description, String failingUrl) {
		view.stopLoading();
		view.clearView();
		
    	String data = readAsset(TEMPLATE_ERROR);
    	if(data != null) {
    		data = data.replace("{$code}", String.valueOf(errorCode))
    			.replace("{$description}", description)
    			.replace("{$url}", failingUrl);
    	}
    	view.loadDataWithBaseURL(PREFIX, data, MIME, ENCODING, null);
    	hideNavigation();
    	findViewById(R.id.button_share).setVisibility(View.INVISIBLE);
	}
	//TODO: add a class to encode text
	/*
	 * 	        	data = data.replace("#", "%23").replace("%", "%25")
	        			.replace("\\", "%27").replace("?", "%3f");	
	 */
	
	private String readAsset(String template) {
		String data = null;
    	try {  
            InputStream in = getResources().getAssets().open(template);  
            int length = in.available(); //获取文件的字节数    
            byte[] buff = new byte[length];  //创建byte数组  
            //将文件中的数据读到byte数组中  
            in.read(buff);		                
            data = EncodingUtils.getString(buff, ENCODING);
        } catch(Exception ex) {
        	
        }
		return data;
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface", "SimpleDateFormat" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		
		Intent intent = getIntent();
		Bundle bunde = intent.getExtras();
		if(bunde == null) this.finish();
		this.url = bunde.getString("URL");
		
		//if(!url.startsWith(SCHEME_ARTICLE))
		//	url = PREFIX + url;
		
		webView = (WebView)findViewById(R.id.webViewMain);
		//TODO: reduce memory usage
		
		webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) { // 获取到Title
                super.onReceivedTitle(view, title);
                TextView text = (TextView)findViewById(R.id.webview_title);
                text.setText(title);
            }
            @Override
            public void onCloseWindow(WebView view) {
            	WebViewActivity.this.finish();
            }
        });
        webView.setWebViewClient(new WebViewClient() {        	
        	@SuppressLint("DefaultLocale")        	
			@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		if(url == "about:blank") return true;
        		//boolean handled = false;
        		WebViewActivity.this.url = url;
        		return loadArticle(url);
        		/*
        		android.content.Context context = getApplicationContext();
                if(url.startsWith(SCHEME_ACTIVITY)) {
                	boolean launch = false;
                	Intent intent = new Intent();
                	String path = url.substring(SCHEME_ACTIVITY.length()).toLowerCase();
                	if(path.startsWith(ACTIVITY_MAP)) {
                		String[] parts = path.split("\\/|\\,");
                		intent.setClass(context, CampusMapActivity.class);
                		if(parts.length != 1) {
                			int lat = Integer.parseInt(parts[1]), lon = Integer.parseInt(parts[2]), zoom = 20;
                			if(parts.length == 4) zoom = Integer.parseInt(parts[3]);
                			intent.putExtra("lat", lat).putExtra("lon", lon).putExtra("zoom", zoom);
                		}
                		launch = true;
                	}
                	if(launch) startActivity(intent);
                	handled = true;
                }
                return handled;
                */
        	}
        	@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        	{
        		displayErrorPage(view, errorCode, description, failingUrl);
        	}
    		
        	@Override
        	public void onPageStarted(WebView view, String url, Bitmap favicon) {

        	}
        });
        
        if(!loadArticle(url)) webView.loadUrl(url);
		
        OnClickListener clickHandler = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(v.getId()) {
					case R.id.button_close_page:
						WebViewActivity.this.finish();
						break;
					case R.id.icon_go_back:
						webView.goBack();
						break;
					case R.id.icon_go_forward:
						webView.goForward();
						break;
					case R.id.icon_refresh:
						webView.reload();
						break;
					case R.id.button_share:
						share();
					default:
				}
			}
		};
		
		for(int i : new int[]{
				R.id.button_close_page, R.id.icon_go_back,
				R.id.icon_go_forward, R.id.icon_refresh, R.id.button_share
				}) {
			findViewById(i).setOnClickListener(clickHandler);
		}
	}
	
	private boolean loadArticle(String url) {
		if(url.startsWith(SCHEME_ARTICLE)) {
        	String articleUrl = url.substring(SCHEME_ARTICLE.length());
        	String anchor = null;
        	int index = url.indexOf("#");        	
        	if(index != -1) {
        		anchor = url.substring(index + 1); 
        	}
        	index = articleUrl.indexOf("/");        	
        	if(index != -1) {
        		articleUrl = articleUrl.substring(0, index);
        	}                		
        	DBManager dbm = new DBManager(WebViewActivity.this);
        	Article article = dbm.read(articleUrl);
        	dbm.closeDB();
        	if(article != null) {
	        	String data = null;
	        	data = readAsset(TEMPLATE_ARTICLE)
        			.replace("{$title}", article.getTitle())
        			.replace("{$content}", article.getContent())
        			.replace("{$picture}", article.getPicture());
	        	/*
	        	 * '#', '%', '\', '?' 
	        	 * %23, %25, %27, %3f
	        	 */
	        	data = data.replace("#", "%23").replace("%", "%25")
	        		.replace("\\", "%27").replace("?", "%3f");	        	
				webView.loadDataWithBaseURL(getString(R.string.url_assset_prefix),
	        		data, "text/html", ENCODING, null);
				hideNavigation();
				if(anchor != null)
					webView.loadUrl(anchor);
	        	//if(data != null)
	        } else {
	        	displayErrorPage(webView, 0, getString(R.string.article_not_found), url);
	        }
        	return true;
        }
		return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.removeAllViews();
		webView.clearCache(true);
		webView.destroy();		
	}
	
	private void hideNavigation() {
    	findViewById(R.id.navigation_bar).setVisibility(View.GONE);
	}
	
	@SuppressLint("SimpleDateFormat")
	private void share() {
		//SHARE
		StringBuilder reason = new StringBuilder(getString(R.string.share));
		if(url.startsWith(SCHEME_HTTP)) reason.append(url);
			
		Resources res = getResources();
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_SUBJECT, reason.toString());
		
		/*
		if(urlType == Type.REMOTE) {
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, res.getString(R.string.share_body) + 
				webTitle + " " + webURL);
			return;
		}*/
		
		String path = PathUtil.getCaptureScreen();
		WebView webView = (WebView)findViewById(R.id.webViewMain);			
		Picture picture = webView.capturePicture();
		
		Bitmap bmp = Bitmap.createBitmap(picture.getWidth(),picture.getHeight(),
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmp); picture.draw(c);  
							        			        
		String fileName = path + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".png";
		FileOutputStream fos = null; 
		try { 
			fos = new FileOutputStream(fileName); 
			if (fos != null) 
			{
				bmp.compress(Bitmap.CompressFormat.PNG, 100, fos); 
				fos.flush(); 
				fos.close();
				
				intent.setType("image/png");							
				intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(fileName)));
				intent.putExtra(Intent.EXTRA_TEXT, res.getString(R.string.share_body));							
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	
		startActivity(Intent.createChooser(intent, res.getString(R.string.share)));	
	}
}
