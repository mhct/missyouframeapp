package com.missyouframeapp2;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit.RestAdapter;
import retrofit.mime.TypedByteArray;
import android.os.AsyncTask;
import android.util.Log;

public class PhotoSenderHandler extends AsyncTask<String, Void, String> {

	private static final String TAG = "MissyouFrameApp";
	private final FileDescriptor fd;
	private final String user;
	private String fileExtension;

	public PhotoSenderHandler(FileDescriptor fd, String user, String fileExtension) {
		this.fd = fd;
		this.user = user;
		this.fileExtension = fileExtension;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String res = "";

		InputStream bis = new BufferedInputStream(new FileInputStream(fd));
		byte[] imageData = readAndClose(bis);
		
		Log.i(TAG, "received parameter");
		RestAdapter adapter = new RestAdapter.Builder().setEndpoint("http://app.missyouframe.com:9090").build();
		
		PhotosManipulationClient service = adapter.create(PhotosManipulationClient.class);
		TypedByteArray photo = new TypedByteArray("multipart/form-data", imageData);
		
		res = service.createPhoto(user, photo, fileExtension);
		Log.i(TAG, "res: " + res);
	
		return res;
	}
	

	byte[] readAndClose(InputStream aInput){
	    //carries the data from input to output :    
	    byte[] bucket = new byte[32*1024]; 
	    ByteArrayOutputStream result = null; 
	    try  {
	      try {
	        //Use buffering? No. Buffering avoids costly access to disk or network;
	        //buffering to an in-memory stream makes no sense.
	        result = new ByteArrayOutputStream(bucket.length);
	        int bytesRead = 0;
	        while((bytesRead = aInput.read(bucket)) != -1){
	          if(bytesRead > 0){
	            result.write(bucket, 0, bytesRead);
	          }
	        }
	      }
	      finally {
	        aInput.close();
	        //result.close(); this is a no-operation for ByteArrayOutputStream
	      }
	    }
	    catch (IOException ex){
	    	ex.printStackTrace();
	      Log.e(TAG, "IO Error");
	    }
	    return result.toByteArray();
	  }

	
}

