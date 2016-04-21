package com.rxandroid.activities;

import com.jakewharton.rxbinding.widget.*;
import com.rxandroid.*;

import android.os.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import rx.*;
import rx.functions.*;

/**
 * Created by wilson on 4/19/16.
 */
public class CombineLatestActivity extends AppCompatActivity {

	private EditText name;
	private EditText email;
	private EditText password;
	private Button signupBtn;

	private Observable<CharSequence> nameObservable;
	private Observable<CharSequence> emailObservable;
	private Observable<CharSequence> passwordObservable;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_combine_latest);

		setupActionBar(getSupportActionBar());

		name = (EditText) findViewById(R.id.name_field);
		email = (EditText) findViewById(R.id.email_field);
		password = (EditText) findViewById(R.id.password_field);
		signupBtn = (Button) findViewById(R.id.signup);

		nameObservable = RxTextView.textChanges(name).skip(4);
		emailObservable = RxTextView.textChanges(email).skip(4);
		passwordObservable = RxTextView.textChanges(password).skip(4);

		signupBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Signup successful!", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		validateForm();
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

	private void validateForm() {
		Observable.combineLatest(nameObservable, emailObservable, passwordObservable, new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
			@Override
			public Boolean call(CharSequence nameInput, CharSequence emailInput, CharSequence passwordInput) {
				boolean isNameValid = isValid(nameInput.toString());
				boolean isEmailValid = isValid(emailInput.toString());
				boolean isPasswordValid = isValid(passwordInput.toString());

				if (!isPasswordValid) {
					password.setError("Invalid Password");
				}
				if (!isEmailValid) {
					email.setError("Invalid Email");
				}
				if (!isNameValid) {
					name.setError("Invalid name");
				}

				return isNameValid & isEmailValid & isPasswordValid;
			}
		}).subscribe(new Observer<Boolean>() {
			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
				Log.e(getClass().getSimpleName(), "Signup error");
			}

			@Override
			public void onNext(Boolean formValid) {
				if (formValid) {
					signupBtn.setEnabled(true);
				} else {
					signupBtn.setEnabled(false);
				}
			}
		});
	}

	private boolean isValid(String input) {
		return input != null && !input.equals("") && input.length() > 3;
	}
}
