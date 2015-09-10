package com.nemory.bundledfun;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;

import com.nemory.bundledfun.objects.Question;
import com.nemory.bundledfun.objects.User;
import com.nemory.bundledfun.tools.JSONParser;

public class SplashActivity extends Activity {

	private ImageView ivSplashImage;
	private int tick = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		ivSplashImage = (ImageView) findViewById(R.id.ivSplashImage);
		this.setTimer();
		this.bind();
//		startActivity(new Intent(SplashActivity.this, MenuActivity.class));
//		finish();
	}
	
	public static class UserReader extends AsyncTask<JSONArray, Integer, JSONArray>{
		
		@Override
		protected JSONArray doInBackground(JSONArray... params) {
			JSONArray users = new JSONArray();
			try {
				users = JSONParser.getJSONFromURL("http://192.168.1.112/bundledfun/public/json_users.php");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return users;
		}
		
		//		try {
		//		UserReader userReader = new UserReader();
		//		JSONArray users = new JSONArray();
		//		users = userReader.execute().get();
		//		Toast.makeText(this, users.get(0).toString() + "", 100000).show();
		//	} catch (InterruptedException e) {
		//		e.printStackTrace();
		//	} catch (ExecutionException e) {
		//		e.printStackTrace();
		//	} catch (JSONException e) {
		//		// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	}
	}
	
	private void bind(){
		try {
			Question.questions 	= JSONParser.parseQuestion(getApplicationContext().getAssets().open("questions.json"));
			User.users 			= JSONParser.parseUsers();
			//User.users = JSONParser.parseStudent(getApplicationContext().getAssets().open("users.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void setTimer(){
		
		int seconds = 4000;
		int interval = 1000;
		
		new CountDownTimer(seconds, interval) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				tick++;
				switch(tick){
				case 1:
					ivSplashImage.setImageResource(R.drawable.android_logo);
					break;
				case 2:
					ivSplashImage.setImageResource(R.drawable.android_logo);
					break;
				case 3:
					ivSplashImage.setImageResource(R.drawable.android_logo);
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
}
