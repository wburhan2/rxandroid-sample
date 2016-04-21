package com.rxandroid.network;

import com.rxandroid.dto.*;

import java.util.*;

import retrofit.http.*;
import rx.Observable;

/**
 * Created by wilson on 4/19/16.
 */
public interface UserService {

	@GET("/users/")
	Observable<List<UserResponse>> getUser(@Query("id") String userId);

	@GET("/posts/")
	Observable<List<PostResponse>> getUserPost(@Query("userId") String userId);

}
