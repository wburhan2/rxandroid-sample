package com.rxandroid.network;

import com.rxandroid.dto.*;

import java.util.*;

import retrofit.*;
import retrofit.http.*;
import rx.Observable;

/**
 * Created by wilson on 4/19/16.
 */
public interface UserService {

	// The response that we get from the fake REST API is being handled properly, thus I have to wrap the response in a List<>.

	@GET("/users/")
	Observable<List<UserResponse>> getUser(@Query("id") String userId);

	@GET("/posts/")
	Observable<List<PostResponse>> getUserPost(@Query("userId") String userId);

	@GET("/users/")
	void getUser(@Query("id") String userId, Callback<List<UserResponse>> callback);

	@GET("/posts/")
	void getUserPost(@Query("userId") String userId, Callback<List<PostResponse>> callback);

}
