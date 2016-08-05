package com.cmdesign.hellonwsuaf.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cmdesign.hellonwsuaf.R;

/**
 * Created by Administrator on 2015/11/10 0010.
 */
public class KebiaoChoice extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice);
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.yanjiusheng:
                        Intent intent1 = new Intent(KebiaoChoice.this,CourseFenshu.class);
                        KebiaoChoice.this.startActivity(intent1);
                        break;
                    case R.id.benke:
                        Intent intent5 = new Intent(KebiaoChoice.this,LoginnActivity.class);
                        KebiaoChoice.this.startActivity(intent5);
                        break;
                    case R.id.button:
                        Intent intent = new Intent(KebiaoChoice.this,WebView2Activity.class);
                        intent.putExtra("extra_inter2", "http://m.5read.com/900");
                        KebiaoChoice.this.startActivity(intent);
                        break;
                    case R.id.button2:
                        Intent intent2 = new Intent(KebiaoChoice.this,WebView2Activity.class);
                        intent2.putExtra("extra_inter2", "http://3dmap.nwsuaf.edu.cn/mobile/");
                        KebiaoChoice.this.startActivity(intent2);
                        break;
                    case R.id.button3:
                        Intent intent3 = new Intent(KebiaoChoice.this,WebView2Activity.class);
                        intent3.putExtra("extra_inter2", "http://news.nwsuaf.edu.cn/");
                        KebiaoChoice.this.startActivity(intent3);
                        break;
                    case R.id.buttondao:
                        Intent intent4 = new Intent(KebiaoChoice.this,WebView2Activity.class);
                        intent4.putExtra("extra_inter2", "http://map.baidu.com/mobile/webapp/index/index/");
                        KebiaoChoice.this.startActivity(intent4);
                        break;
                    case R.id.buttondiantai:
                        Intent intent8 = new Intent(KebiaoChoice.this,FMActivity.class);
                        KebiaoChoice.this.startActivity(intent8);
                        break;
                    case R.id.buttonchong:
                        Intent intent6 = new Intent(KebiaoChoice.this,WebView2Activity.class);
                        intent6.putExtra("extra_inter2", "http://210.27.80.195:8001/");
                        KebiaoChoice.this.startActivity(intent6);
                        break;
                    default:
                        return;
                }
            }
        };
        findViewById(R.id.yanjiusheng).setOnClickListener(listener);
        findViewById(R.id.benke).setOnClickListener(listener);
        findViewById(R.id.button).setOnClickListener(listener);
        findViewById(R.id.button2).setOnClickListener(listener);
        findViewById(R.id.button3).setOnClickListener(listener);
        findViewById(R.id.buttondao).setOnClickListener(listener);
        findViewById(R.id.buttondiantai).setOnClickListener(listener);
        findViewById(R.id.buttonchong).setOnClickListener(listener);
    }
}
