package com.cmdesign.hellonwsuaf.bean;

public class MyGeoPoint {
	private String title, detail;
	private int lat, lon, type;
	
	public MyGeoPoint() {
		
	}
	
	public MyGeoPoint(int lat, int lon, String title, String detail) {
		this.lat = lat;
		this.lon = lon;
		this.title = title;
		this.detail = detail;
	}
	public MyGeoPoint(int lat, int lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public MyGeoPoint setLat(int lat) {
		this.lat = lat;
		return this;
	}
	
	public MyGeoPoint setLon(int lon) {
		this.lon = lon;
		return this;
	}
	
	public MyGeoPoint setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public MyGeoPoint setDetail(String detail) {
		this.detail = detail;
		return this;
	}
	
	public MyGeoPoint setType(int type) {
		this.type = type;
		return this;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDetail() {
		return detail;
	}
	
	public int getLat() {
		return lat;
	}
	
	public int getLon() {
		return lon;
	}
	
	public int getType() {
		return type;
	}
}
