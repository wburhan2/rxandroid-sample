package com.rxandroid.adapter;

import com.rxandroid.*;
import com.rxandroid.dto.*;

import android.view.*;
import android.widget.*;

import java.util.*;

/**
 * Created by wilson on 4/19/16.
 */
public class ZipAdapter extends BaseAdapter {

	private List<PostResponse> postList;

	public ZipAdapter(List<PostResponse> postList) {
		this.postList = postList;
	}

	@Override
	public int getCount() {
		return postList.size();
	}

	@Override
	public Object getItem(int position) {
		if (postList != null) {
			return postList.get(position);
		}
		return null;
	}

	public void addAll(List<PostResponse> postResponses) {
		postList.clear();
		postList.addAll(postResponses);
	}

	@Override
	public long getItemId(int position) {
		return postList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if(convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.post_title);
			holder.body = (TextView) convertView.findViewById(R.id.post_message);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		PostResponse postResponse = postList.get(position);
		holder.title.setText(postResponse.getTitle());
		holder.body.setText(postResponse.getBody());
		return convertView;
	}

	private static class ViewHolder {
		TextView title;
		TextView body;
	}
}
