package com.cmdesign.hellonwsuaf.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cmdesign.hellonwsuaf.R;

/**
 * Created by Administrator on 2015/10/18 0018.
 */
public class choiceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice);
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button:
                        Intent intent = new Intent(choiceActivity.this,WebView2Activity.class);
                        intent.putExtra("extra_inter2", "http://m.5read.com/900");
                        choiceActivity.this.startActivity(intent);
                        break;
                    case R.id.button2:
                        Intent intent2 = new Intent(choiceActivity.this,WebView2Activity.class);
                        intent2.putExtra("extra_inter2", "http://3dmap.nwsuaf.edu.cn/mobile/");
                        choiceActivity.this.startActivity(intent2);
                        break;
                    case R.id.button3:
                        Intent intent3 = new Intent(choiceActivity.this,WebView2Activity.class);
                        intent3.putExtra("extra_inter2", "http://news.nwsuaf.edu.cn/");
                        choiceActivity.this.startActivity(intent3);
                        break;
                    case R.id.buttondao:
                        Intent intent4 = new Intent(choiceActivity.this,WebView2Activity.class);
                        intent4.putExtra("extra_inter2", "http://map.baidu.com/");
                        choiceActivity.this.startActivity(intent4);
                        break;
                    case R.id.buttondiantai:
                        Intent intent5 = new Intent(choiceActivity.this,FMActivity.class);
                        choiceActivity.this.startActivity(intent5);
                        break;
                    case R.id.buttonchong:
                        Intent intent6 = new Intent(choiceActivity.this,WebView2Activity.class);
//                        intent6.putExtra("extra_inter2", "http://ecardapp.nwsuaf.edu.cn:8070/Home/Index");
                        intent6.putExtra("extra_inter2", "http://121.42.160.43/");
                        choiceActivity.this.startActivity(intent6);
                        break;
                    default:
                        return;
                }
            }
        };
        findViewById(R.id.button).setOnClickListener(listener);
        findViewById(R.id.button2).setOnClickListener(listener);
        findViewById(R.id.button3).setOnClickListener(listener);
        findViewById(R.id.buttondao).setOnClickListener(listener);
        findViewById(R.id.buttondiantai).setOnClickListener(listener);
        findViewById(R.id.buttonchong).setOnClickListener(listener);
    }
}
