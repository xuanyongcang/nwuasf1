package com.cmdesign.hellonwsuaf.ui;

import com.cmdesign.hellonwsuaf.R;
import com.cmdesign.hellonwsuaf.helper.DBManager;

import android.app.Activity;
import android.os.Bundle;

public class SearchActivity extends Activity {
	DBManager dbm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		dbm = new DBManager(this);

		
	}
	
	@Override
	protected void onDestroy() {
		dbm.closeDB();
		dbm = null;
		super.onDestroy();
	}
}
