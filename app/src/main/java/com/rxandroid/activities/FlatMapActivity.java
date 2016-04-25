package com.rxandroid.activities;

import com.rxandroid.*;
import com.rxandroid.dto.*;
import com.rxandroid.network.*;
import com.rxandroid.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import java.util.*;
import java.util.concurrent.*;

import retrofit.*;
import retrofit.client.*;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.*;
import rx.functions.*;
import rx.schedulers.*;

/**
 * Created by wilson on 4/19/16.
 */
public class FlatMapActivity extends AppCompatActivity {

	private EditText userIdInput;
	private Button getUserBtn;
	private TextView name;
	private TextView username;
	private TextView phone;
	private TextView email;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flat_map);

		setupActionBar(getSupportActionBar());

		userIdInput = (EditText) findViewById(R.id.user_id_edit_text);
		getUserBtn = (Button) findViewById(R.id.get_user_btn);
		name = (TextView) findViewById(R.id.name_value);
		username = (TextView) findViewById(R.id.username_value);
		phone = (TextView) findViewById(R.id.phone_value);
		email = (TextView) findViewById(R.id.email_value);


		getUserBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String inputText = userIdInput.getText().toString();
				if (inputText.equals("")) {
					userIdInput.setError("Please enter a number 1-10");
				}
				else {
					int numInput = Integer.decode(inputText);
					if (numInput < 1 && numInput > 11) {
						userIdInput.setError("Please enter a number 1-10");
					}
					else {
						getUserDataUsingRxJava();
					}
				}
			}
		});
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

	private void dismissProgressBar() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	/**
	 * This network call utilize flat map operation to retrieve the user's data.
	 */
	private void getUserDataUsingRxJava() {
		progressDialog = ProgressDialog.show(FlatMapActivity.this, "", "Getting user information..");
		Observable.just(userIdInput.getText().toString())
				.flatMap(new Func1<String, Observable<List<UserResponse>>>() {
					@Override
					public Observable<List<UserResponse>> call(String s) {
						RestAdapter restAdapter = RetrofitFactory.getRestAdapter(Executors.newSingleThreadExecutor());
						UserService userService = RetrofitFactory.getUserService(restAdapter);
						return userService.getUser(s);
					}
				}).subscribeOn(Schedulers.io())
				  .observeOn(AndroidSchedulers.mainThread())
				  .subscribe(new Observer<List<UserResponse>>() { // The response that we get from the fake REST API is funky, thus I have to wrap the response in a List<> to handle it.
			@Override
			public void onCompleted() {
				dismissProgressBar();
			}

			@Override
			public void onError(Throwable e) {
				dismissProgressBar();

				Log.e(getClass().getSimpleName(), e.getMessage());
				new AlertDialog.Builder(FlatMapActivity.this).setTitle("Error").setMessage("Please enter a valid user ID").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
			}

			@Override
			public void onNext(List<UserResponse> userResponseList) {
				UserResponse userResponse = userResponseList.get(0);
				name.setText(userResponse.getName());
				username.setText(userResponse.getUsername());
				phone.setText(userResponse.getPhone());
				email.setText(userResponse.getEmail());
			}
		});
	}

	/**
	 * This is the network call implementation with just using plain retrofit call.
	 */
	private void getUserDataWithoutRxJava() {
		progressDialog = ProgressDialog.show(FlatMapActivity.this, "", "Getting user information..");

		RestAdapter restAdapter = RetrofitFactory.getRestAdapter(Executors.newSingleThreadExecutor());
		UserService userService = RetrofitFactory.getUserService(restAdapter);
		userService.getUser(userIdInput.getText().toString(), new Callback<List<UserResponse>>() {
			@Override
			public void success(List<UserResponse> userResponseList, Response response) {
				dismissProgressBar();

				UserResponse userResponse = userResponseList.get(0);
				name.setText(userResponse.getName());
				username.setText(userResponse.getUsername());
				phone.setText(userResponse.getPhone());
				email.setText(userResponse.getEmail());
			}

			@Override
			public void failure(RetrofitError error) {
				dismissProgressBar();

				Log.e(getClass().getSimpleName(), error.getMessage());
				new AlertDialog.Builder(FlatMapActivity.this).setTitle("Error").setMessage("Please enter a valid user ID").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
			}
		});
	}
}
