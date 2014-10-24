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

	public PhotoSenderHandler(FileDescriptor fd, String user) {
		this.fd = fd;
		this.user = user;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String res = "";

		InputStream bis = new BufferedInputStream(new FileInputStream(fd));
		byte[] imageData = readAndClose(bis);
		
		Log.i(TAG, "received parameter");
		RestAdapter adapter = new RestAdapter.Builder().setEndpoint("http://192.168.1.148:8080").build();
		
		PhotosManipulationClient service = adapter.create(PhotosManipulationClient.class);
//		TypedFile photo = new TypedFile("multipart/form-data", new File(params[0]));
		TypedByteArray photo = new TypedByteArray("multipart/form-data", imageData);
		
		res = service.createPhoto(user, photo);
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

