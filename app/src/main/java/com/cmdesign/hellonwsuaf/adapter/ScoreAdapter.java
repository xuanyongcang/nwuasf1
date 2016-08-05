package com.cmdesign.hellonwsuaf.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cmdesign.hellonwsuaf.R;

import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2015/10/21.
 */
public class ScoreAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Score> scoreData=new ArrayList<Score>();
    private Score score;
    public ScoreAdapter(Context context, ArrayList<Score> scoreData1) {
        Log.e("tab","执行一次");
        this.context=context;
        this.scoreData=scoreData1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.score_item_layout, null);
            holder.score_tv = (TextView) convertView.findViewById(R.id.score_tv);
            holder.coursename_tv = (TextView) convertView.findViewById(R.id.coursename_tv);
            holder.credit_tv = (TextView) convertView.findViewById(R.id.credit_tv);
            holder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.point_tv = (TextView) convertView.findViewById(R.id.point_tv);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Score score=scoreData.get(position);
        String test=null;
        test=score.getExamScore();
        int circleShape = 0;
        if(!TextUtils.isEmpty(test)) {
            if (isDouble(test))
            {
                float grade =Float.parseFloat(test);
            int color = 0;
            if (grade >= 90) {
                circleShape = R.drawable.circle_deep_orange;
                color = context.getResources().getColor(R.color.deep_orange_500);
            } else if (grade >= 80 && grade < 90) {
                circleShape = R.drawable.circle_teal;
                color = context.getResources().getColor(R.color.indigo_500);
            } else if (grade >= 70 && grade < 80) {
                circleShape = R.drawable.circle_cyan;
                color = context.getResources().getColor(R.color.cyan_500);
            } else if (grade >= 60 && grade < 70) {
                circleShape = R.drawable.circle_blue;
                color = context.getResources().getColor(R.color.blue_500);
            } else {
                circleShape = R.drawable.circle_amber;
                color = context.getResources().getColor(R.color.amber_500);
            }
        }
            else{
                circleShape = R.drawable.circle_green;
                int color = 0;
                color = context.getResources().getColor(R.color.red_500);
        }

        }

        holder.score_tv.setBackgroundResource(circleShape);
        holder.coursename_tv.setText(score.getScoreName());
        holder.credit_tv.setText(score.getCredit());
        holder.type_tv.setText(score.getType());
        holder.point_tv.setText(score.getPoint());
        String s =score.getTestScore();
        if(TextUtils.isEmpty(s)){
            holder.score_tv.setText(score.getExamScore());
        }
        else
            holder.score_tv.setText(score.getExamScore());

        return convertView;
    }

    private static class ViewHolder {
        TextView score_tv;
        TextView coursename_tv;
        TextView credit_tv;
        TextView type_tv;
        TextView point_tv;
    }
    @Override
    public int getCount() {
        return scoreData.size();
    }

    @Override
    public Object getItem(int position) {
        return scoreData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }
}
