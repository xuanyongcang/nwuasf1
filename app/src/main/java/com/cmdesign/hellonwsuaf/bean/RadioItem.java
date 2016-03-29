package com.cmdesign.hellonwsuaf.bean;

import android.graphics.Bitmap;

public class RadioItem {
	private final String EMPTY_URL = "radio/", BASE_URL = "http://jiangfan.ujs.edu.cn/xwzx/";
	//TODO:image base
	private String title, url, picUrl;
	private int id;
	Bitmap cover;
	
	public RadioItem(String title, String url, String picUrl) {
		this.title = title;
		this.url = fullUrl(url);
		this.picUrl = fullUrl(picUrl);
	}
	
	private final String fullUrl(String url) {
		if(url.equals(EMPTY_URL)) 
			return "";
		else if(url.startsWith(EMPTY_URL))
			return BASE_URL + url;
		else
			return url;
	}
	
	public RadioItem setId(int id) {
		this.id = id;
		return this;
	}
	
	public RadioItem setId(String id) {
		return setId(Integer.parseInt(id));
	}
	
	public RadioItem setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public RadioItem setURL(String url) {
		this.url = fullUrl(url);
		return this;
	}
	
	public RadioItem setPicUrl(String picUrl) {
		this.picUrl = fullUrl(picUrl);
		return this;
	}
	
	public RadioItem setCover(Bitmap cover) {
		this.cover = cover;
		return this;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getPicUrl() {
		return picUrl;
	}
	
	public Bitmap getCover() {
		return this.cover;
	}
}
