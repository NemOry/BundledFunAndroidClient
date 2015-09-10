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
	private final String[] pics;
	private final String[] names;
	private final String[] ids;
	private final String[] pass;
	private final int[] highScores;
	
	public ListViewAdapter(Context context, String[] pics, String[] names, String[] ids, String[] pass, int[] highScores) {
		super(context, R.layout.row_layout, names);
		this.context = context;
		this.pics = pics;
		this.names = names;
		this.ids = ids;
		this.pass = pass;
		this.highScores = highScores;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_layout, parent, false);
		
		ImageView ivPic = (ImageView) rowView.findViewById(R.id.ivPic); 
		TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
		TextView tvID = (TextView) rowView.findViewById(R.id.tvID);
		TextView tvPass = (TextView) rowView.findViewById(R.id.tvPass);
		TextView tvHighScore = (TextView) rowView.findViewById(R.id.tvHighScore);
		
		Uri imageUri = Uri.fromFile(new File(Constants.DATAPATH + "images/users/" + pics[position]));
		ivPic.setImageURI(imageUri);
		
		tvName.setText(names[position]);
		tvID.setText(ids[position]);
		tvPass.setText(pass[position]);
		tvHighScore.setText(highScores[position] + "");
		
		return rowView;
	}
}