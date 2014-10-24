package com.missyouframeapp2;

import retrofit.http.Multipart;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedByteArray;


public interface PhotosManipulationClient {

	@retrofit.http.POST("/{user}/photos")
	@Multipart
	public String createPhoto(@Path("user") String user,
            @Part("file") TypedByteArray fileInputStream);
            
}
