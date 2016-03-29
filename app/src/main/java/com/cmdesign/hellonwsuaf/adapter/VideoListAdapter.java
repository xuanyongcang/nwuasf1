package com.cmdesign.hellonwsuaf.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmdesign.hellonwsuaf.R;
import com.cmdesign.hellonwsuaf.bean.VideoItem;

import java.util.List;

public class VideoListAdapter extends ArrayAdapter<VideoItem> {
    private LayoutInflater mInflater;
    
    private int mViewResourceId;
    private List<VideoItem> mVideoItems;
    
    public VideoListAdapter(Context context, int resource, List<VideoItem> videoItems) {
    	super(context, resource, videoItems);
    	
    	mInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    	mVideoItems = videoItems;
        mViewResourceId = resource;
    }

    @Override
    public int getCount() {
    	return mVideoItems.size();
    }

    @Override
    public VideoItem getItem(int position) {
    	return mVideoItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	if(convertView == null)
    		convertView = mInflater.inflate(mViewResourceId, null);
    	
    	VideoItem item = mVideoItems.get(position);
    	if(item == null) return convertView;
    	
    	ImageView image = (ImageView)convertView.findViewById(R.id.image_thumb);
    	Bitmap bitmap = item.getBitmap();
    	if(bitmap != null)
    		image.setImageBitmap(bitmap);
    	else
    		image.setImageResource(R.drawable.default_video_cover);
        TextView text = (TextView)convertView.findViewById(R.id.text_video_title);
        text.setText(item.getTitle());
        text = (TextView)convertView.findViewById(R.id.text_video_date);
        text.setText(item.getDate());
        return convertView;
    }
}
