package com.rxandroid.activities;

import com.jakewharton.rxbinding.widget.*;
import com.rxandroid.*;

import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import java.util.*;
import java.util.concurrent.*;

import rx.Observer;
import rx.android.schedulers.*;
import rx.functions.*;

/**
 * Created by wilson on 4/19/16.
 */
public class DebounceActivity extends AppCompatActivity {

	private EditText inputText;
	private ListView logListView;
	private Button clearLogButton;
	private List<String> logList;
	private LogAdapter logAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debounce);

		inputText = (EditText) findViewById(R.id.input_text);
		logListView = (ListView) findViewById(R.id.log_list_view);
		clearLogButton = (Button) findViewById(R.id.clear_log_btn);

		clearLogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearLog();
			}
		});

		logList = new ArrayList<>();
		logAdapter = new LogAdapter(this, new ArrayList<String>());
		logListView.setAdapter(logAdapter);

		RxTextView.textChangeEvents(inputText).debounce(400, TimeUnit.MILLISECONDS)
				.filter(new Func1<TextViewTextChangeEvent, Boolean>() {
					@Override
					public Boolean call(TextViewTextChangeEvent textViewTextChangeEvent) {
						return !inputText.getText().toString().equals("");
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<TextViewTextChangeEvent>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						log("Error");
					}

					@Override
					public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
						log(String.format("Inputted text is: %s", textViewTextChangeEvent.text().toString()));
					}
				});
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

	private void log(String logMsg) {
		logList.add(0, logMsg);
		logAdapter.clear();
		logAdapter.addAll(logList);
		logAdapter.notifyDataSetChanged();
	}

	private void clearLog() {
		logList.clear();
		logAdapter.clear();
	}

	private class LogAdapter extends ArrayAdapter<String> {
		public LogAdapter(Context context, List<String> logs) {
			super(context, R.layout.item_log_row, R.id.item_log, logs);
		}
	}
}
