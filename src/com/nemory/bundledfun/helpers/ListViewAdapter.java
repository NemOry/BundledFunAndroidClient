package com.nemory.bundledfun.helpers;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nemory.bundledfun.R;

public class ListViewAdapter extends ArrayAdapter<String> {
	
	private final Context context;
	private final int[] id;
	private final String[] group_name;
	
	public ListViewAdapter(Context context, String[] group_name, int[] id) {
		super(context, R.layout.row_layout, group_name);
		this.context = context;
		this.id = id;
		this.group_name = group_name;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_layout, parent, false);

		TextView tvID = (TextView) rowView.findViewById(R.id.tvID);
		TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
		
		tvName.setText(group_name[position]);
		tvID.setText(id[position]);
		
		return rowView;
	}
}