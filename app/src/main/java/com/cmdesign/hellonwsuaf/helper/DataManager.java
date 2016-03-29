package com.cmdesign.hellonwsuaf.helper;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
	/***************************
     * 解析数据获得课程表
	 ****************************/
	public static List<Map<String, String>> getScheduleList(String str) {
		List<Map<String, String>> scheduilLst = new ArrayList<Map<String, String>>();
		Document doc = Jsoup.parse(str);
		Elements datas = doc.getElementsByTag("table");
		Element data = datas.get(0);
		Elements trs = data.getElementsByTag("tr");
		Elements tds = trs.get(1).getElementsByTag("td");
		for (int j = 17; j < 65; j++) {
			if (!"".equals(tds.get(j).text())) {
				if ((j - 17) % 8 != 0) {
					scheduilLst.add(getScheduleMap(tds.get(j).text()));
				}	
			}
		}
		Log.i("Td","scheduilLst" + scheduilLst.size());
		return scheduilLst;
	}
	
	/***************************
     * 数据封装
	 ****************************/
	private static Map<String, String> getScheduleMap(String str) {
		String[] courseInfo = new String[] { "name", "teacher", "classroom",
				"time", "sordweek", "section", "name2", "teacher2",
				"classroom2", "time2", "sordweek2", "section2", "name3", "teacher3",
				"classroom3", "time3", "sordweek3", "section3", "name4", "teacher4",
				"classroom4", "time4", "sordweek4", "section4" };
		String[] sCourse = str.split(" ");
		int i = 0;
		Map<String, String> map = new HashMap<String, String>();
		for (; i < sCourse.length; i++) {
			   String s = sCourse[i];		
				map.put(courseInfo[i], s);
				Log.i("Td", i + courseInfo[i] + "==" + s);
		}
         
		return map;
	}
}
