package com.nemory.bundledfun;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.nemory.bundledfun.helpers.Constants;
import com.nemory.bundledfun.helpers.Current;
import com.nemory.bundledfun.objects.Group;
import com.nemory.bundledfun.objects.Question;
import com.nemory.bundledfun.objects.User;

public class SplashActivity extends Activity {

	private ImageView ivSplashImage;
	private int tick = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		this.initialize();
	}

	private void setTimer(){
		
		int seconds = 4000;
		int interval = 1000;
		
		new CountDownTimer(seconds, interval) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				tick++;
				switch(tick){
				case 1:
					ivSplashImage.setImageResource(R.drawable.bundledfun);
					break;
				case 3:
					ivSplashImage.setImageResource(R.drawable.devs);
					break;
				}
			}

			@Override
			public void onFinish(){
				startActivity(new Intent(SplashActivity.this, LoginActivity.class));
				finish();
			}
		}.start();
	}
	
	private void initialize(){
		ivSplashImage = (ImageView) findViewById(R.id.ivSplashImage);
		Group.context = this;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Current.synced = prefs.getBoolean(Constants.KEY_PREFS_SYNCED, false);
		Constants.createFolders();
		Question.bind();
		User.bind();
		this.setTimer();
	}
}
