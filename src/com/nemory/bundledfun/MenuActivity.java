package com.nemory.bundledfun;

import java.io.File;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.nemory.bundledfun.helpers.Constants;
import com.nemory.bundledfun.helpers.Current;
import com.nemory.bundledfun.objects.Question;
import com.nemory.bundledfun.objects.User;

public class MenuActivity extends Activity implements OnClickListener{
	
	private Button btnPlay, btnLogout, btnSettings, btnUnSync, btnContactDeveloper, btnMaps;
	private MediaPlayer mPlayer;
	private SharedPreferences prefs;
	private SharedPreferences.Editor prefs_editor;
	public static CheckBox chkStreamOnline;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		this.initialize();
		mPlayer.start();
		
		if(Current.user != null){
			if(Current.user.getLevel() == 1){
				//btnUnSync.setVisibility(View.VISIBLE);
			}else{
				//btnUnSync.setVisibility(View.GONE);
			}
		}
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
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnSettings = (Button) findViewById(R.id.btnSettings);
		btnUnSync = (Button) findViewById(R.id.btnUnSync);
		btnContactDeveloper = (Button) findViewById(R.id.btnContactDeveloper);
		btnMaps = (Button) findViewById(R.id.btnMaps);
		
		chkStreamOnline = (CheckBox) findViewById(R.id.chkStreamOnline);
		
		chkStreamOnline.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(chkStreamOnline.isChecked()){
					Current.streamOnline = true;
				}else{
					Current.streamOnline = false;
				}
			}
		});
		
		// LISTENERS
		btnPlay.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		btnSettings.setOnClickListener(this);
		btnUnSync.setOnClickListener(this);
		btnContactDeveloper.setOnClickListener(this);
		btnMaps.setOnClickListener(this);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this);
		prefs_editor = prefs.edit();
		
		mPlayer = MediaPlayer.create(MenuActivity.this, R.raw.bgmusic);
		mPlayer.setLooping(true);
		
		Question.bind();
		User.bind();
		Current.synced = prefs.getBoolean(Constants.KEY_PREFS_SYNCED, false);
	}

	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.btnPlay:
			
			if(Question.questions.size() > 0 && User.users.size() > 0){
				
				int easyCount = 0, mediumCount = 0, hardCount = 0;

				for(Question q : Question.questions){
					if(q.getDifficulty().equalsIgnoreCase("easy")){
						easyCount++;
					}else if(q.getDifficulty().equalsIgnoreCase("medium")){
						mediumCount++;
					}else if(q.getDifficulty().equalsIgnoreCase("hard")){
						hardCount++;
					}
				}
				
				final int easyCountFinal = easyCount;
				final int mediumCountFinal = mediumCount;
				final int hardCountFinal = hardCount;
				
				AlertDialog d = new AlertDialog.Builder(this).create();
				d.setTitle("Select Difficulty?");
				d.setMessage("Get ready for the \"Bundled Fun Questions.\"");

				d.setButton(d.BUTTON_NEGATIVE, "Easy", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(easyCountFinal > 0){
							startActivity(new Intent(MenuActivity.this, QuizActivity.class).putExtra("DIFFICULTY", "easy"));
						}else{
							Toast.makeText(MenuActivity.this, "There are no available questions for this difficulty.", Toast.LENGTH_LONG).show();
						}
					}
				});
				
				d.setButton(d.BUTTON_NEUTRAL, "Medium", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(mediumCountFinal > 0){
							startActivity(new Intent(MenuActivity.this, QuizActivity.class).putExtra("DIFFICULTY", "medium"));
						}else{
							Toast.makeText(MenuActivity.this, "There are no available questions for this difficulty.", Toast.LENGTH_LONG).show();
						}
					}
				});
				
				d.setButton(d.BUTTON_POSITIVE, "Hard", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(hardCountFinal > 0){
							startActivity(new Intent(MenuActivity.this, QuizActivity.class).putExtra("DIFFICULTY", "hard"));
						}else{
							Toast.makeText(MenuActivity.this, "There are no available questions for this difficulty.", Toast.LENGTH_LONG).show();
						}
					}
				});
				
				d.show();

			}else{
				Toast.makeText(MenuActivity.this, "You need to sync 1st and must have atleast 1 user.", Toast.LENGTH_LONG).show();
			}
			break;
			
		case R.id.btnLogout:
			Current.user = null;
			Toast.makeText(this, "Successfully logged Out", Toast.LENGTH_SHORT).show();
			finish();
			break;
			
		case R.id.btnSettings:
			startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
			break;
			
		case R.id.btnUnSync:
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor prefs_editor = prefs.edit();
			
			prefs_editor.putBoolean(Constants.KEY_PREFS_SYNCED, false);
			Current.synced = false;
			prefs_editor.putInt(Constants.KEY_PREFS_ID, 0);
			prefs_editor.putString(Constants.KEY_PREFS_USERNAME, "");
			prefs_editor.putString(Constants.KEY_PREFS_PASSWORD, "");
			prefs_editor.commit();
			
			String appPath = Environment.getExternalStorageDirectory() + File.separator + ".BundledFun";
			Constants.deleteFiles(new File(appPath));
			
			startActivity(new Intent(MenuActivity.this, LoginActivity.class));
			Toast.makeText(this, "Successfully UnSynced", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.btnContactDeveloper:
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"nemoryoliver@gmail.com"});
			i.putExtra(Intent.EXTRA_SUBJECT, "User Report");
			try {
			    startActivity(Intent.createChooser(i, "Contact Developer"));
			} catch (android.content.ActivityNotFoundException ex) {
			    Toast.makeText(MenuActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.btnMaps:
			startActivity(new Intent(MenuActivity.this, OSMDroidActivity.class));
			break;
		}
	}
	
	private class InternetConnectionChecker extends AsyncTask<String, Integer, String>{
		
		@Override
		protected String doInBackground(String... params) {
			String connectionsAvailable = "";

		    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		    for (NetworkInfo network : netInfo) {
		        if (network.isConnected()){
		        	return network.getTypeName();
	            }
		    }
		    
		    return connectionsAvailable;
		}
	}
	
	private String isInternetAvailable(){
		InternetConnectionChecker i = new InternetConnectionChecker();
		
		String connectionsAvailable = "nothing";
		try {
			connectionsAvailable = i.execute().get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}

		return connectionsAvailable;
	}
}
