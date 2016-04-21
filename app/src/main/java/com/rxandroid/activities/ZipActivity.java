package com.rxandroid.activities;

import com.rxandroid.*;
import com.rxandroid.adapter.*;
import com.rxandroid.dto.*;
import com.rxandroid.network.*;
import com.rxandroid.util.*;

import android.app.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.support.v7.app.ActionBar;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;
import java.util.concurrent.*;

import retrofit.*;
import rx.*;
import rx.android.schedulers.*;
import rx.functions.*;
import rx.schedulers.*;

public class ZipActivity extends AppCompatActivity {

	private TextView name;
	private TextView email;
	private TextView phone;
	private ListView postingList;
	private ZipAdapter zipAdapter;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zip);

		name = (TextView) findViewById(R.id.name);
		email = (TextView) findViewById(R.id.email);
		phone = (TextView) findViewById(R.id.phone);
		postingList = (ListView) findViewById(R.id.post_list);
		zipAdapter = new ZipAdapter(new ArrayList<PostResponse>());
		postingList.setAdapter(zipAdapter);

		setupActionBar(getSupportActionBar());
		retrieveUserDataAndPost();
	}

	protected void setupActionBar(ActionBar actionBar) {
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return false;
		}
	}

	private void retrieveUserDataAndPost() {
		progressDialog = ProgressDialog.show(this, "", "Loading...");
		RestAdapter restAdapter = RetrofitFactory.getRestAdapter(Executors.newSingleThreadExecutor());
		UserService userService = RetrofitFactory.getUserService(restAdapter);
		// I combined both ItemsResponse and UserDetailResponse by using the zip operator into a new response called UserDetailResponse through the "call" callback.
		// Then, I updated both header and user items in onNext.
		rx.Observable
				.zip(userService.getUser("1"), userService.getUserPost("1"), new Func2<List<UserResponse>, List<PostResponse>, UserAndPostResponse>() {
					@Override
					public UserAndPostResponse call(List<UserResponse> userResponse, List<PostResponse> postResponses) {
						return new UserAndPostResponse(userResponse, postResponses);
					}
				})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<UserAndPostResponse>() {
			@Override
			public void onCompleted() {
				dismissProgressBar();
			}

			@Override
			public void onError(Throwable e) {
				Log.e(getClass().getSimpleName(), e.getMessage());
				dismissProgressBar();
			}

			@Override
			public void onNext(UserAndPostResponse userAndPostResponse) {
				List<UserResponse> userResponse = userAndPostResponse.getUserResponse();
				List<PostResponse> postResponse = userAndPostResponse.getPostResponse();

				name.setText(String.format("Name: %s", userResponse.get(0).getName()));
				email.setText(String.format("Email: %s", userResponse.get(0).getEmail()));
				phone.setText(String.format("Phone: %s", userResponse.get(0).getPhone()));

				zipAdapter.addAll(postResponse);
				zipAdapter.notifyDataSetChanged();
			}
		});
	}

	private void dismissProgressBar() {
		if(progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	private static class UserAndPostResponse {
		private List<UserResponse> userResponse;
		private List<PostResponse> postResponse;

		public UserAndPostResponse(List<UserResponse> userResponse, List<PostResponse> postResponse) {
			this.postResponse = postResponse;
			this.userResponse = userResponse;
		}

		public List<PostResponse> getPostResponse() {
			return postResponse;
		}

		public List<UserResponse> getUserResponse() {
			return userResponse;
		}
	}
}
