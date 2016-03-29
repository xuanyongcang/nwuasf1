package com.cmdesign.hellonwsuaf.helper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private Context context;
	public static final int DB_VERSION = 9;
	
	public DBHelper(Context context) {
		super(context, PathUtil.FILE_DB, null, DB_VERSION);		
		this.context = context;
	}
		
	public synchronized SQLiteDatabase getReadableDatabase() {
		SQLiteDatabase db;
		String path = PathUtil.getDatabase();
		ExtractUtil extractUtil = new ExtractUtil(context);
		try {
			db = SQLiteDatabase.openDatabase(path, null, 0);
			//update
			if(db.getVersion() < DB_VERSION) {
				db.close();
				//reopen
				extractUtil.extractDb(true);
				db = SQLiteDatabase.openDatabase(path, null, 0);
			}
		} catch(SQLException ex) {
			extractUtil.extractDb();
			db = SQLiteDatabase.openDatabase(path, null, 0);
		}
		return db;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
