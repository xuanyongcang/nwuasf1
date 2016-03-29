package com.cmdesign.hellonwsuaf.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import com.cmdesign.hellonwsuaf.R;

import android.content.Context;

/*
 * to extract files to SDCard
 */
public class ExtractUtil {
	private Context context;
	private final String SEPARATOR = File.separator;
	
	public ExtractUtil(Context context){
		this.context = context;
		//try build folder
		PathUtil.getAppPath();
	}
	/*
	 * 解压离线地图
	 */
	public boolean extractMap() {
		String fileName = PathUtil.getMapAchieve();
				
		InputStream[] files = {
			context.getResources().openRawResource(R.raw.map_001),
			context.getResources().openRawResource(R.raw.map_002),
			context.getResources().openRawResource(R.raw.map_003)
		};
		
		try {
			File file = new File(fileName);
			if(file.exists()) file.delete();
			
			FileOutputStream os = new FileOutputStream(fileName);
			muxFiles(files, os);
			os = null;
 			
			UnZipFolder(fileName, PathUtil.sdcard());					 		
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/*
	 * 
	 */
	public static void muxFiles(InputStream[] streamList, FileOutputStream output) {
 		try {
 			for(InputStream stream : streamList){
				int TotalLength = 0;
				try {
					TotalLength = stream.available();
				} catch (IOException e) {
				}
				// Reading and writing the file Method 1 :
				byte[] buffer = new byte[TotalLength];
				int len = 0;
				try {
					len = stream.read(buffer);
				} catch (IOException e) {
				}
				output.write(buffer, 0, len);
				stream.close();
			}
			output.close();
		} catch (IOException e) {
		}
	}
	
	public void UnZipFolder(String zipFileString, String outPathString) throws Exception {  
		ZipInputStream inZip = new ZipInputStream(
			new FileInputStream(zipFileString));  
	    java.util.zip.ZipEntry zipEntry;
	    String szName = "";
	      
	    while ((zipEntry = inZip.getNextEntry()) != null)
	    {
	        szName = zipEntry.getName();  
	      
	        if (zipEntry.isDirectory()) {  
	            // get the folder name of the widget  
	            szName = szName.substring(0, szName.length() - 1);  
	            File folder = new File(outPathString + SEPARATOR + szName);  
	            if(!folder.exists()) folder.mkdirs();  
	      
	        } else {	      
	            File file = new File(outPathString + SEPARATOR + szName);  
	            if(file.exists()) file.delete();
	            file.createNewFile();  
	            // get the output stream of the file  
	            FileOutputStream out = new FileOutputStream(file);  
	            int len;
	            byte[] buffer = new byte[1024*1024];  
	            // read (len) bytes into buffer  
	            while ((len = inZip.read(buffer)) != -1) {  
	                // write (len) byte from buffer at the position 0  
	                out.write(buffer, 0, len);
	                out.flush();
	            }
	            buffer = null;
	            out.close();  
	        }  
	    } //end of while  

	    inZip.close();  
	}
	
	public boolean extractDb() {
		return extractDb(false);
	}
	
	public boolean extractDb(boolean override) {
	    try
	    {
	    	String fileName = PathUtil.getDatabase();
	    	File file = new File(fileName);

	    	if(file.exists()) {
	    		if(override)
	    			file.delete();
	    		else
	    			return true;
	    	}
	    		    		
	    	InputStream is;
	    	FileOutputStream os;
	    	byte[] buffer;
	    	
    		is = context.getResources().openRawResource(R.raw.inner);
    		
	        os = new FileOutputStream(fileName);
	        buffer = new byte[1024];
	    	
	    	int count;
			while ((count = is.read(buffer)) > 0) {
				os.write(buffer, 0, count);
				os.flush();
			}
			
			is.close();
			os.close();
    	
	    	return true;
	    } catch (Exception localException) {
	    	localException.printStackTrace();
	    	return false;
	    }
	}
}
