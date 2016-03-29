package com.cmdesign.hellonwsuaf.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;

import com.cmdesign.hellonwsuaf.R;

public class CameraActivity extends Activity {
	private final static int RESULT_CAMERA = 1;
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
    	super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_camera); 
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
        startActivityForResult(intent, RESULT_CAMERA);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode == RESULT_CAMERA) {
    		Bundle bundle = data.getExtras();
    		if(bundle != null) {
    			Bitmap bitmap = (Bitmap)bundle.get("data");
    			
    			//processImage(bitmap, );
    		}
    	} else {
    		super.onActivityResult(requestCode, resultCode, data);
    	}
    }
    
    /**
     * Helper Functions
     * @throws IOException 
     */
    private Bitmap getBitmapFromAsset(String strName)
    {
    	Bitmap bitmap;
    	try{
	        AssetManager assetManager = getAssets();
	
	        java.io.InputStream istr = assetManager.open(strName);
	        bitmap = BitmapFactory.decodeStream(istr);
	        istr.close();
    	}catch(Exception e) {
    		bitmap = null;
    	}
        return bitmap;
    }
    
    public Bitmap createBitmap(Bitmap src, String title, String icon) {
    	int WIDTH = 640, HEIGHT = 852;
    	Bitmap waterMark = getBitmapFromAsset(icon);   	
    	
        if (src == null) {  
            return src;  
        } 
        // 获取原始图片与水印图片的宽与高  
        int w = src.getWidth();  
        int h = src.getHeight();  
        int ww = waterMark.getWidth();  
        int wh = waterMark.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);  
        Canvas mCanvas = new Canvas(newBitmap);  
        // 往位图中开始画入src原始图片  
        mCanvas.drawBitmap(src, 0, 0, null);  
        // 在src的右下角添加水印  
        Paint paint = new Paint();  
        //paint.setAlpha(100);  
        mCanvas.drawBitmap(waterMark, w - ww - 5, h - wh - 5, paint);  
        // 开始加入文字  
        if (null != title) {  
            Paint textPaint = new Paint();  
            textPaint.setColor(Color.RED);  
            textPaint.setTextSize(16);  
            String familyName = "宋体";  
            Typeface typeface = Typeface.create(familyName,  
                    Typeface.BOLD_ITALIC);  
            textPaint.setTypeface(typeface);  
            textPaint.setTextAlign(Align.CENTER);  
            mCanvas.drawText(title, w / 2, 25, textPaint);  
              
        }  
        mCanvas.save(Canvas.ALL_SAVE_FLAG);  
        mCanvas.restore();  
        return newBitmap;  
    } 
}