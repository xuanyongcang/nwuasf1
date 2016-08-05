package com.cmdesign.hellonwsuaf.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class NetManager2 {
	 private static HttpClient client = new DefaultHttpClient();
	 private static String COOKIE = "";
	 private static NetManager2 netManager = new NetManager2();

	 private NetManager2(){
		 
	 }
	 
	 public static NetManager2 getNetManager2()
	 {
		 if(netManager==null){
			 netManager = new NetManager2();
		 }
		 return netManager;
	 }
	 
	 /***************************
      * 通过Post的方法获得Web数据
	  ****************************/
	 public String getWebWithPost(String url,Map<String,String> map) {
		 HttpPost httpRequest = new HttpPost(url);
		 List<NameValuePair> params = new ArrayList<NameValuePair>();
		 for(String key : map.keySet()) {
			params.add(new BasicNameValuePair(key, map.get(key))); //添加参数
		 }

		httpRequest.setHeader("Cookie", "JSESSIONID=" + COOKIE);//加入Cookie
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "GBK"));
			HttpResponse httpResponse = client.execute(httpRequest); 			
			if (httpResponse.getStatusLine().getStatusCode() == 200) { 	//状态码
				return  readFromStream(httpResponse.getEntity().getContent());
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	 
	 	 
	 /***************************
      * 获得整个网页
	  ****************************/
	public static String readFromStream(InputStream inputStream)
				throws Exception {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		String data = "";
		while ((data = br.readLine()) != null) {
			sb.append(data);
		}		
		return sb.toString(); 
	}
	 
	/***************************
	 * 获得验证码
	 ****************************/	 
	 public Bitmap getcode() throws Exception {
		HttpPost httpPost = new HttpPost("http://yjspy.nwafu.edu.cn/");
		HttpResponse httpResponse = client.execute(httpPost);
		COOKIE = ((AbstractHttpClient) client).getCookieStore().getCookies().get(0).getValue();
		byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return bitmap;
	}
}
