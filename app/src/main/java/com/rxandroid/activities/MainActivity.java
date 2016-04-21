package com.rxandroid.activities;

import com.rxandroid.*;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

	private View combineLatest;
	private View debounce;
	private View flatMap;
	private View zip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		combineLatest = findViewById(R.id.combine_latest);
		debounce = findViewById(R.id.debounce);
		flatMap = findViewById(R.id.flat_map);
		zip = findViewById(R.id.zip);

		combineLatest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, CombineLatestActivity.class);
				startActivity(intent);
			}
		});

		debounce.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DebounceActivity.class);
				startActivity(intent);
			}
		});

		flatMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, FlatMapActivity.class);
				startActivity(intent);
			}
		});

		zip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ZipActivity.class);
				startActivity(intent);
			}
		});
	}
}
