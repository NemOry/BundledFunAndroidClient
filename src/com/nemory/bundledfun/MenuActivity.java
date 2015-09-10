package com.nemory.bundledfun;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nemory.bundledfun.R;

public class MenuActivity extends Activity implements OnClickListener{
	
	private Button btnPlay, btnOptions;
	private MediaPlayer mPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		this.initialize();
		
		mPlayer = MediaPlayer.create(MenuActivity.this, R.raw.bgmusic);
		mPlayer.setLooping(true);
		mPlayer.start();
		
		Button btnMaps = (Button) findViewById(R.id.btnMaps);
		btnMaps.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MenuActivity.this, OSMDroidActivity.class));
			}
		});
	}
	
	@Override
	protected void onPause() {
		mPlayer.pause();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		mPlayer.stop();
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		mPlayer.start();
		super.onResume();
	}
	
	private void initialize(){
		// BUTTON VIEWS
		btnPlay = (Button) findViewById(R.id.btnPlay);
		btnOptions = (Button) findViewById(R.id.btnOptions);
		
		// LISTENERS
		btnPlay.setOnClickListener(this);
		btnOptions.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.btnPlay:
			startActivity(new Intent(MenuActivity.this, QuizActivity.class));
			break;
			
		case R.id.btnOptions:
			startActivity(new Intent(MenuActivity.this, OptionsActivity.class));
			break;
		}
	}
}
