package com.cmdesign.hellonwsuaf.helper;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class PathUtil {
	public static final String FOLDER_NAME = "Hellonwusf",
			FOLDER_SCREEN = "screenCapture",
			FOLDER_CACHE = "cache",
			FOLDER_BAIDUSDK = "BaiduMapSDK",
			FILE_DB = "inner9.db",
			FILE_MAP_ACHIEVE = "map.zip",
			SEPARATOR = File.separator;
		
	public static boolean isMounted() {
		return android.os.Environment.getExternalStorageState().equals(
			android.os.Environment.MEDIA_MOUNTED);
	}

	public static String base() {
		return new StringBuilder(isMounted() ? sdcard() : data()).toString();
	}
	
	public static String data() {
		return new StringBuilder(Environment.getDataDirectory()
			.getAbsolutePath()).append(SEPARATOR).toString();
	}
	
	public static String sdcard() {
		return new StringBuilder(Environment.getExternalStorageDirectory()
			.getAbsolutePath()).append(SEPARATOR).toString();
	}
	
	public static String getCachePath() {
		return createIfNonExist(new StringBuilder(getAppPath()).append(FOLDER_CACHE)
				.append(SEPARATOR).toString());
	}
	
	public static String getAppPath() {
		return createIfNonExist(new StringBuilder(base()).append(FOLDER_NAME)
				.append(File.separator).toString());
	}
	
	public static String getBaiduSDK() {
		return createIfNonExist(new StringBuilder(sdcard()).append(FOLDER_BAIDUSDK).toString());
	}
	
	public static String getDatabase() {
		return new StringBuilder(getAppPath()).append(FILE_DB).toString();
	}
	
	public static String getMapAchieve() {
		return new StringBuilder(getAppPath()).append(FILE_MAP_ACHIEVE).toString();
	}
	
	public static String getCaptureScreen() {
		String path = new StringBuilder(getAppPath()).append(FOLDER_SCREEN)
			.append(SEPARATOR).toString();
		createIfNonExist(path);
		try {
			File nomedia = new File(path + SEPARATOR + ".nomedia");
			if(!nomedia.exists())
			nomedia.createNewFile();
		} catch (IOException e) {
		}
		return path;
	}
	
	private static String createIfNonExist(String path) {
		File folder = new File(path);
		if(!folder.exists()) folder.mkdir();
		return path;
	}
}
