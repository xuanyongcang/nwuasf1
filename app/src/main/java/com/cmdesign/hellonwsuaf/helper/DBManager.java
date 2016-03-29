package com.cmdesign.hellonwsuaf.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cmdesign.hellonwsuaf.bean.Article;
import com.cmdesign.hellonwsuaf.bean.MyGeoPoint;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context) {
		helper = new DBHelper(context);
		//因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
		//所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getReadableDatabase();
		int v = db.getVersion();
	}
	
	public String findRelativeArticle(String title) {
		String param[] = {title}, result;
		Cursor cur = db.rawQuery("select url from article where title = ?", param);
		if(cur != null && cur.moveToFirst()) {
			result = cur.getString(cur.getColumnIndex("url"));
		} else {
			result = null;
		}
		cur.close();
		return result;
	}
	
	public Article read(String url) {
		Article article = null;
		if(url == null) return article;
		String param[] = {url};
		Cursor cur = db.rawQuery("SELECT * FROM article where url = ?", param);
		if (cur != null && cur.moveToFirst()) {
			article = new Article()
	    		.setTitle(cur.getString(cur.getColumnIndex("title")))
	    		.setContent(cur.getString(cur.getColumnIndex("content")))
	    		.setKeyword(cur.getString(cur.getColumnIndex("keyword")))
	    		.setPicture(cur.getString(cur.getColumnIndex("picture")))
	    		.setUrl(url)
	    		;
		}
		cur.close();
		return article;
	}
		
	/**
	 * query all article
	 * @return List<Article>
	 */
	public List<Article> list() {
		ArrayList<Article> list = new ArrayList<Article>();
		Cursor cur = db.rawQuery("SELECT * FROM article", null);
        do {
        	Article article = new Article()
        		.setTitle(cur.getString(cur.getColumnIndex("title")))
        		.setUrl(cur.getString(cur.getColumnIndex("url")))
        		.setContent(cur.getString(cur.getColumnIndex("content")))
        		.setKeyword(cur.getString(cur.getColumnIndex("keyword")))
        		.setPicture(cur.getString(cur.getColumnIndex("picture")))
        	;
        	
        	//title, url, content keyword, picture
        	
        	list.add(article);
        } while (cur.moveToNext());
        cur.close();
        return list;
	}
	

	public List<MyGeoPoint> getPoints(int... types) {
		ArrayList<MyGeoPoint> list = new ArrayList<MyGeoPoint>();
		StringBuilder sb = new StringBuilder(types.length * 2);
		for (int type : types) 
			sb.append(type + ",");
		sb.setLength(types.length * 2 - 1);
		
		String selection = String.format("type in (%s)", sb);
	
		Cursor cur = db.query("geopoints",
				new String[]{"title", "detail", "lon", "lat", "type"},
				selection, null, null, null, null);
		
		if(cur == null || !cur.moveToFirst()) return null;
		
		int lat = (int)(cur.getFloat(cur.getColumnIndex("lat")) * 1E6);
		int lon = (int)(cur.getFloat(cur.getColumnIndex("lon")) * 1E6);
		
        while (cur.moveToNext()) {
        	MyGeoPoint point = new MyGeoPoint()
        		.setTitle(cur.getString(cur.getColumnIndex("title")))
        		.setDetail(cur.getString(cur.getColumnIndex("detail")))
        		.setLon(lon).setLat(lat)
        		.setType(cur.getInt(cur.getColumnIndex("type")))
        	;
        	//title, url, content keyword, picture        	
        	list.add(point);
        }
        cur.close();
		return list;
	}
	/**
	 * close database
	 */
	public void closeDB() {
		helper.close();
		db.close();
	}
}
