package com.cmdesign.hellonwsuaf.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.cmdesign.hellonwsuaf.R;
import com.cmdesign.hellonwsuaf.adapter.ClassInfo;
import com.cmdesign.hellonwsuaf.adapter.ScheduleView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/13 0013.
 */
public class javaclass2 extends Activity {
    private ArrayList<ClassInfo> classList;
    private String result;
    private ScheduleView scheduleView;
    private SharedPreferences pref2;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course2);
        pref2 = PreferenceManager.getDefaultSharedPreferences(this);
        scheduleView = (ScheduleView) this.findViewById(R.id.scheduleView2);
        result= pref2.getString("schedulebenke", "");
        toShow();
        System.out.println(result);
        scheduleView.setClassList(classList);// 将课程信息导入到课表中
        scheduleView.setOnItemClassClickListener(new ScheduleView.OnItemClassClickListener() {
            @Override
            public void onClick(ClassInfo classInfo) {
                Toast.makeText(javaclass2.this,
                        classInfo.getClassname(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void toShow() {
        classList = new ArrayList<ClassInfo>();
        String text2 = null;
        boolean isRemember = pref2.getBoolean("remember_password2",false);
        if (!isRemember) {
            Intent intent = new Intent(javaclass2.this, LoginnActivity.class);
            Toast.makeText(javaclass2.this, "载入失败请先登录", Toast.LENGTH_SHORT).show();
            javaclass2.this.startActivity(intent);
        } else {
            Toast.makeText(javaclass2.this, "载入成功", Toast.LENGTH_SHORT).show();
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

    }
}