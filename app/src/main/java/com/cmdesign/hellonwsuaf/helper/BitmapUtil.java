package com.cmdesign.hellonwsuaf.helper;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtil {
	//TODO: build a helper
	public static Bitmap stretch(Bitmap bm, float newWidth, float newHeight){
		
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = newWidth / width;
		float scaleHeight = newHeight / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		bm.recycle();
		return newbm;
	}
}
