package com.cmdesign.hellonwsuaf.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmdesign.hellonwsuaf.R;
import com.cmdesign.hellonwsuaf.adapter.ClassInfo;
import com.cmdesign.hellonwsuaf.adapter.ScheduleView;
import com.cmdesign.hellonwsuaf.helper.DataManager;
import com.cmdesign.hellonwsuaf.helper.NetManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Schedulebenke extends Activity {
    private ScheduleView scheduleView;
    private ArrayList<ClassInfo> classList;
    private TextView view;
    private SharedPreferences pref2;
    private SharedPreferences.Editor editor;
    private NetManager netManager;
    private ProgressBar roundProBar;
    private String result;
    private String getResult;
    private final int GET_SCHEDULE_SUCCESS = 1;
    private final int GET_SCHEDULE_ERROR = 2;
    private final String GET_SCHEDULE_URL = "http://jwgl.nwsuaf.edu.cn/academic/manager/coursearrange/showTimetable.do?id=262331&yearid=35&termid=3&timetableType=STUDENT&sectionType=COMBINE";
    private List<Map<String, String>> scheduleList;
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int tag =  msg.what;
            switch(tag){
                case GET_SCHEDULE_SUCCESS:
//				Toast.makeText(ScheduleActivity.this, "get schedule success", Toast.LENGTH_SHORT).show();
                    toShow();
                    break;
                case GET_SCHEDULE_ERROR:
//				Toast.makeText(ScheduleActivity.this, "get schedule success", Toast.LENGTH_SHORT).show();
                    toShow();
                    break;
            }
            roundProBar.setVisibility(View.GONE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course1);
        scheduleView = (ScheduleView) this.findViewById(R.id.scheduleView);
        init();

    }

    /***************************
     * 初始化UI
     ****************************/
    private void init(){
        pref2 = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref2.getBoolean("remember_schedule", false);
        editor = pref2.edit();
        boolean isRemember2 = pref2.getBoolean("remember_password2",false);
        netManager = NetManager.getNetManager();
//		view = (TextView)findViewById(R.id.schedule_view);
        roundProBar = (ProgressBar) findViewById(R.id.schedule_progressbar1);
        Map<String,String> map = new HashMap<String,String>();
//        map.put("YearTermNO", "14");
        getSchedule(GET_SCHEDULE_URL, map);
    }

    /***************************
     * 获得课程表
     ****************************/
    private void getSchedule(final String url,final Map<String,String> map){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try{
                    result = netManager.getWebWithPost(url,map);
                    getResult=result;
                    scheduleList = DataManager.getScheduleList(result);
                    msg.what = GET_SCHEDULE_SUCCESS;
                }catch(Exception e){
                    msg.what = GET_SCHEDULE_ERROR;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    /***************************
     * 显示课程表
     ****************************/
    private void toShow() {
		classList = new ArrayList<ClassInfo>();
		String text2 = null;
		if (result == null) {
			Intent intent = new Intent(Schedulebenke.this, LoginnActivity.class);
			Toast.makeText(Schedulebenke.this, "登陆失败请检查学号密码和验证码是否正确", Toast.LENGTH_SHORT).show();
			Schedulebenke.this.startActivity(intent);
		} else {

            editor.putBoolean("remember_schedule2", true);
            editor.putString("schedulebenke", result);
            editor.commit();
			Toast.makeText(Schedulebenke.this, "登陆成功", Toast.LENGTH_SHORT).show();
			Document doc = Jsoup.parse(result, "UTF-8");
			Elements tables = doc.select("table[id=timetable]");
			Elements trs = tables.select("tr");
			Elements tds = trs.select("td[class=infolist_hr_common]");//td[rowspan=2]
            int num=1;
            int suanshu=1;
			for (org.jsoup.nodes.Element table : tables) {  //课程表
				String text = table.text();
				System.out.println("tabletext:" + text);
			}
			String nbsp = Jsoup.parse(" ").text().toString();
			int line_shu = 0;
			for (org.jsoup.nodes.Element tr : trs) {  //行，表格中的一行
				line_shu++;
				int week=1;
				if (line_shu >= 0) {
					Elements tdss = tr.select("td");
					System.out.println("tdssSize:" + tdss.size());
					for (org.jsoup.nodes.Element td : tdss) {  //表格中的一个格子
						String text1 = td.text();
                        int ji=text1.indexOf(" ");
//						text2 = text1.replaceAll(nbsp, " ");
                        if(ji!=-1)
                        {
						ClassInfo classInfo4 = new ClassInfo();
						classInfo4.setClassname(text1);
						classInfo4.setFromClassNum(num);
						classInfo4.setClassNumLen(2);
						classInfo4.setClassRoom("");
						classInfo4.setWeekday(week);
						classList.add(classInfo4);
                        }
                        week++;
					}
                    if (suanshu>1) {
                        num = num + 2;
                    }
                    suanshu++;
				}

			}
		}
        scheduleView.setClassList(classList);// 将课程信息导入到课表中
        scheduleView.setOnItemClassClickListener(new ScheduleView.OnItemClassClickListener() {
            @Override
            public void onClick(ClassInfo classInfo) {
                Toast.makeText(Schedulebenke.this,
                        classInfo.getClassname(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
