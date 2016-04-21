package com.rxandroid.util;

import com.rxandroid.network.*;

import java.util.concurrent.*;

import retrofit.*;
import retrofit.android.*;

/**
 * Created by wilson on 4/19/16.
 */
public class RetrofitFactory {

	public static RestAdapter getRestAdapter(ExecutorService executorService) {
		return new RestAdapter.Builder()
				.setExecutors(executorService, new MainThreadExecutor())
				.setEndpoint("http://jsonplaceholder.typicode.com/")
				.build();
	}

	public static UserService getUserService(RestAdapter restAdapter) {
		return restAdapter.create(UserService.class);
	}
}
