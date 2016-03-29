package com.cmdesign.hellonwsuaf.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cmdesign.hellonwsuaf.R;

/**
 * Created by Administrator on 2015/10/18 0018.
 */
public class WebView2Activity extends Activity{
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);//����ʹ�ù�ִ��JS�ű�
        webView.getSettings().setBuiltInZoomControls(true);//����ʹ֧������
//      webView.getSettings().setDefaultFontSize(5);
        String url;
        Intent intent = getIntent();
        url = intent.getStringExtra("extra_inter2");
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);// ʹ�õ�ǰWebView������ת
                return true;//true��ʾ���¼��ڴ˴�����������Ҫ�ٹ㲥
            }
            @Override   //ת�����ʱ�Ĵ���
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                Toast.makeText(WebView2Activity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override   //Ĭ�ϵ���˼������˳�Activity�����������������ʹ������WebView�ڷ���
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
