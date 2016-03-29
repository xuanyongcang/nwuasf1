package com.cmdesign.hellonwsuaf.bean;

//TODO
public class Article {
	private final int TRUNCATE_LENGTH = 18;
	private final String DEFAULT_PICTURE = "default-background.jpg";
	private String title, url, content, keyword, picture;
	
	public Article() {
		
	}
	
	public Article(String title, String url, String content, String keyword, String picture) {
		this.title = title;
		this.url = url;
		this.content = content;
		this.keyword = keyword;
		this.picture = picture;
	}
		
	public Article setTitle(String title) {
		this.title = title;
		return this;
	}

	public Article setUrl(String url) {
		this.url = url;
		return this;
	}
	
	public Article setContent(String content) {
		this.content = content;
		return this;
	}
	
	public Article setKeyword(String keyword) {
		this.keyword = keyword;
		return this;
	}
	
	public Article setPicture(String picture) {
		this.picture = picture == null ? DEFAULT_PICTURE : picture;
		return this;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public String getPicture() {
		return picture;
	}
	
	public String toString() {
		return title.length() > TRUNCATE_LENGTH ? 
			title.substring(0, TRUNCATE_LENGTH) + "...":
			title;
	}
}
