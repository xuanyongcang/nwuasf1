package com.cmdesign.hellonwsuaf.bean;

import android.graphics.Bitmap;

public class VideoItem {
	private String title, content, url, thumbUrl, date;
	private Bitmap bmp = null;
	
	public VideoItem(String title, String content, String url, String thumbUrl, String date) {
		setTitle(title).setContent(content).setUrl(url).setThumbUrl(thumbUrl).setDate(date);		
	}
	
	public VideoItem setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public VideoItem setContent(String content) {
		this.content = content;
		return this;
	}
	
	public VideoItem setUrl(String url) {
		this.url = url;
		return this;
	}
	
	public VideoItem setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
		return this;
	}
	
	public VideoItem setBitmap(Bitmap bmp) {
		this.bmp = bmp;
		return this;
	}

	public VideoItem setDate(String date) {
		this.date = date;
		return this;
	}
	
	public Bitmap getBitmap() {
		return bmp;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getThumbUrl() {
		return thumbUrl;
	}
	
	public String getDate() {
		return date;
	}
	
	protected void finalize() { 
		if(bmp != null)
			bmp.recycle();
	}
}
