package com.nemory.bundledfun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nemory.bundledfun.helpers.Constants;
import com.nemory.bundledfun.helpers.Current;
import com.nemory.bundledfun.objects.Group;
import com.nemory.bundledfun.objects.Question;
import com.nemory.bundledfun.objects.User;
import com.nemory.bundledfun.tools.JSONParser;

public class LoginActivity extends Activity {

	private ImageView ivPicture;
	private TextView tvName;
	private EditText etUsername, etPassword, etHostName;
	private Button btnLogin, btnRegister;
	private SharedPreferences prefs;
	private SharedPreferences.Editor prefs_editor;
	private DownloadManager downloadManager;
	private DownloadManager.Request request;
	private BroadcastReceiver downloadReceiver;
	private ProgressDialog progressDialog;
	
	JSONArray filesArray = new JSONArray();
	JSONArray usersArray = new JSONArray();
	JSONArray questionsArray = new JSONArray();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		this.initialize();
	}

	private void sync(String group_id){
		try {
			
			File f = new File(Environment.getExternalStorageDirectory() + "/" + Constants.BUNDLEDFUN_FOLDER);
			
			Constants.deleteFiles(f);
			
			progressDialog.setMax(filesArray.length());
			progressDialog.show();
			
			for(int files = 0; files < filesArray.length(); files++){
				String file = filesArray.getString(files);
				
				if(!file.equals("")){
					String file_path = Environment.getExternalStorageDirectory() + "/" + Constants.BUNDLEDFUN_FOLDER + "/files/questions/" + file;
					if(!new File(file_path).exists()){
						progressDialog.setMessage("Syncing: " + file);
						download(Constants.HOST_NAME + "/BundledFun/public/groups/" + Current.group.getName() + "/files/questions/" + file);
					}
				}
			}		
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void download(String url){
		String directory = Constants.BUNDLEDFUN_FOLDER;
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		
		request = new DownloadManager.Request(Uri.parse(url));
		request.setVisibleInDownloadsUi(false);
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
		
		String file_path = directory + "/files/questions/";

		request.setDestinationInExternalPublicDir(file_path, fileName);
		downloadManager.enqueue(request);
	}
	
	private JSONArray fetchFiles(String group_id) throws JSONException {
		JSONArray files = new JSONArray();
		FileFetcher fetcher = new FileFetcher();
		try {
			files = fetcher.execute(group_id).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return files;
	}
	
	private static class FileFetcher extends AsyncTask<String, Integer, JSONArray>{
		
		@Override
		protected JSONArray doInBackground(String... params) {
			JSONArray files = new JSONArray();
			try {
				String url = Constants.HOST_NAME + "/BundledFun/includes/jsons/get_files.php?group_id=" + params[0];
				files = JSONParser.getJSONArrayFromURL(url);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return files;
		}
	}
	
	private JSONArray fetchUsers(String group_id) throws JSONException {
		JSONArray users = new JSONArray();
		UserFetcher fetcher = new UserFetcher();
			try {
				users = fetcher.execute(group_id).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		return users;
	}
	
	private static class UserFetcher extends AsyncTask<String, Integer, JSONArray>{
		
		@Override
		protected JSONArray doInBackground(String... params) {
			JSONArray users = new JSONArray();
			try {
				users = JSONParser.getJSONArrayFromURL(Constants.HOST_NAME + "/BundledFun/includes/jsons/get_users.php?group_id=" + params[0]);
				Log.d("USERS", users.length() + "");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return users;
		}
	}
	
	private JSONArray fetchQuestions(String group_id) throws JSONException {
		JSONArray questions = new JSONArray();
		QuestionFetcher fetcher = new QuestionFetcher();
			try {
				questions = fetcher.execute(group_id).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		return questions;
	}
	
	private static class QuestionFetcher extends AsyncTask<String, Integer, JSONArray>{
		
		@Override
		protected JSONArray doInBackground(String... params) {
			JSONArray questions = new JSONArray();
			try {
				questions = JSONParser.getJSONArrayFromURL(Constants.HOST_NAME + "/BundledFun/includes/jsons/get_questions.php?group_id=" + params[0]);
				Log.d("QUESTIONS", questions.length() + "");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return questions;
		}
	}
	
	private boolean checkToSync(){
		
		boolean needsSync = false;
		
//		String appPath = Environment.getExternalStorageDirectory() + File.separator + ".BundledFun";
//		File bundledFunFolder = new File(appPath);
//		
//		if(Constants.countFiles(bundledFunFolder) < 100){
//			needsSync = true;
//		}
		
		needsSync = !Current.synced;
		
		return needsSync;
	}
	
	private void askToSync(){
		String message = "";
		message = "BundledFun needs to sync data before use.\nDo you want to sync now?\n\nNOTE: ";
		if(isInternetAvailable().equalsIgnoreCase("wifi")){
			message += "This will use your WiFi connection.";
		}else if(isInternetAvailable().equalsIgnoreCase("mobile")){
			message += "This will use your Mobile Data connection.\nBeware of your data network charges.";
		}else{
			Toast.makeText(this, "You have no internet connection available", Toast.LENGTH_LONG).show();
		}
		
		final AlertDialog d = new AlertDialog.Builder(this).create();
		d.setCancelable(false);
		d.setTitle("Sync Files");
		d.setMessage(message);
		d.setButton(d.BUTTON_POSITIVE, "Sync", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				d.dismiss();
				sync(Current.user.getGroupId() + "");
			}
		});
		
		d.setButton(d.BUTTON_NEUTRAL, "Stream Only", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				d.dismiss();
				startActivity(new Intent(LoginActivity.this, MenuActivity.class));
			}
		});
		
		d.setButton(d.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				d.dismiss();
			}
		});
		
		d.show();
	}
	
	private void fetchJSONS(){
		
		final ProgressDialog fetcherDialog = new ProgressDialog(this);
		fetcherDialog.setMessage("Fetching data...");
		fetcherDialog.setIndeterminate(true);
		fetcherDialog.setCancelable(false);
		
		fetcherDialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				if(checkToSync() == true){
					askToSync();
				}else{
					startActivity(new Intent(LoginActivity.this, MenuActivity.class));
					Toast.makeText(LoginActivity.this, "Successfully logged in.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		fetcherDialog.show();
		
		Thread t = new Thread(new Runnable() {
			
			boolean running = true;
			
			public void run() {
				while(running){
					try {
						
						filesArray = fetchFiles(Current.user.getGroupId() + "");
						usersArray = fetchUsers(Current.user.getGroupId() + "");
						questionsArray = fetchQuestions(Current.user.getGroupId() + "");
						
						Current.group = Group.fetchSingleGroup(Current.user.getGroupId());
												
						String path = Environment.getExternalStorageDirectory() + File.separator + Constants.BUNDLEDFUN_FOLDER + File.separator;
						
						File files = new File(path + "files" + File.separator + "files.json");
						
						if(files.exists()){
							files.delete();
						}
						
						files.createNewFile();
						FileOutputStream fileOutPutStream = new FileOutputStream(files);
						OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutPutStream);
						outputStreamWriter.append(filesArray.toString());
						outputStreamWriter.close();
						fileOutPutStream.close();
						
						File users = new File(path  + "files" + File.separator + "users.json");
						
						if(users.exists()){
							users.delete();
						}
						
						users.createNewFile();
						fileOutPutStream = new FileOutputStream(users);
						outputStreamWriter = new OutputStreamWriter(fileOutPutStream);
						outputStreamWriter.append(usersArray.toString());
						outputStreamWriter.close();
						fileOutPutStream.close();
						
						File questions = new File(path  + "files" + File.separator + "questions.json");
						
						if(questions.exists()){
							questions.delete();
						}
						
						questions.createNewFile();
						fileOutPutStream = new FileOutputStream(questions);
						outputStreamWriter = new OutputStreamWriter(fileOutPutStream);
						outputStreamWriter.append(questionsArray.toString());
						outputStreamWriter.close();
						fileOutPutStream.close();
						
					} catch (Exception e) {
						
					}

					running = false;
					fetcherDialog.dismiss();
				}
			}
		});

		t.start();
	}
	
	private void authenticate(final User user){

		final ProgressDialog authenticatingDialog = new ProgressDialog(this);
		authenticatingDialog.setMessage("Authenticating...");
		authenticatingDialog.setIndeterminate(true);
		authenticatingDialog.setCancelable(false);
		
		authenticatingDialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				if(Current.user != null)
				{
					if(Current.user.getAccess() == 1)
					{
						fetchJSONS();
					}
					else
					{
						Toast.makeText(LoginActivity.this, "Sorry, you have been disabled by your group admin.", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(LoginActivity.this, "User ID or Password is incorrect.", Toast.LENGTH_LONG).show();
				}
			}
		});

		authenticatingDialog.show();
		
		Thread t = new Thread(new Runnable() {
			
			boolean running = true;
			
			public void run() {
				while(running){
					
					try {
						
						Current.user = user.authenticateOnline();
						
					} catch (JSONException e) {
						e.printStackTrace();
					}

					running = false;
					authenticatingDialog.dismiss();
				}
			}
		});

		t.start();
	}
	
	private void login(){
		if(etUsername.getText().toString().length() > 0 && etPassword.getText().toString().length() > 0){
			if(!isInternetAvailable().equalsIgnoreCase("nothing")){
				User user = new User();
				user.setUsername(etUsername.getText().toString());
				user.setPassword(etPassword.getText().toString());
				authenticate(user);
			}else{
				Toast.makeText(LoginActivity.this, "You have no internet connection available.", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(this, "Please enter a username and password.", Toast.LENGTH_LONG).show();
		}
	}
	
	private void scan(){
		try {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	        startActivityForResult(intent, 0);
		} catch (Exception e) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse("market://details?id=com.google.zxing.client.android"));
			startActivity(i);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	            String username = intent.getStringExtra("SCAN_RESULT");
	            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	            if(format.equals("QR_CODE")){
	            	User user = new User();
	            	user.setUsername(username);
	            	identifyUserOnline(user);
	            }
	        } else if (resultCode == RESULT_CANCELED) {
	        	Toast.makeText(this, "Scanning canceled.", Toast.LENGTH_LONG).show();
	        }
	    }
	}
	
	private static class ImageLoader extends AsyncTask<String, Integer, Drawable>{
		
		@Override
		protected Drawable doInBackground(String... params) {
			Drawable imageLoaded;
			imageLoaded = LoadImageFromWebOperations(params[0]);
			return imageLoaded;
		}
	}
	
	private static Drawable LoadImageFromWebOperations(String url)
    {
      try{
        InputStream is = (InputStream) new URL(url).getContent();
        Drawable d = Drawable.createFromStream(is, "src name");
        return d;
      }catch (Exception e) {
        System.out.println("Exc="+e);
        return null;
      }
   }
	
	private void identifyUserOnline(final User user){

		final ProgressDialog gettingUserDialog = new ProgressDialog(this);
		gettingUserDialog.setMessage("Identifying...");
		gettingUserDialog.setIndeterminate(true);
		gettingUserDialog.setCancelable(false);
		
		gettingUserDialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				if(Current.user != null){
					
					try {
						Current.group = Group.fetchSingleGroup(Current.user.getGroupId());
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					
					etUsername.setText(Current.user.getUsername());
					
					String file_url = Constants.HOST_NAME + "/BundledFun/public/uploader/thumbnails/" + Current.user.getPicture();
					
					Drawable drawable = null;
					
					try {
						drawable = new ImageLoader().execute(file_url).get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					
		            ivPicture.setImageDrawable(drawable);
					
				}else{
					Toast.makeText(LoginActivity.this, "Username does not exists.", Toast.LENGTH_LONG).show();
				}
			}
		});

		gettingUserDialog.show();
		
		Thread t = new Thread(new Runnable() {
			
			boolean running = true;
			
			public void run() {
				while(running){
					try {
						
						Current.user = user.getByUsernameOnline();
						
					} catch (JSONException e) {
						e.printStackTrace();
					}

					running = false;
					gettingUserDialog.dismiss();
				}
			}
		});

		t.start();
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
	
	private void initialize(){
		ivPicture = (ImageView) findViewById(R.id.ivPicture); 
		tvName = (TextView) findViewById(R.id.tvName);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etHostName = (EditText) findViewById(R.id.etHostName);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs_editor = prefs.edit();
		
		etHostName.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				Constants.HOST_NAME = etHostName.getText().toString();
				
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					InputMethodManager imm = (InputMethodManager)getSystemService(
						      Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(etUsername.getWindowToken(), 0);
					login();
				}
				
				return false;
			}
		});
		
		etUsername.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				User user = User.getSingle(etUsername.getText().toString());
				if(user != null){
	            	tvName.setText(user.getName());
	            	etUsername.setText(user.getUsername());
	            	Uri imageUri = Uri.fromFile(new File(Constants.DATAPATH + "files/users/" + user.getPicture()));
	            	ivPicture.setImageURI(imageUri);
				}
				
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					InputMethodManager imm = (InputMethodManager)getSystemService(
						      Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(etUsername.getWindowToken(), 0);
					login();
				}
				
				return false;
			}
		});
		
		etPassword.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					InputMethodManager imm = (InputMethodManager)getSystemService(
						      Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
					login();
				}
				return false;
			}
		});
		
		btnLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				login();
			}
		});
		
		btnRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
			}
		});
		
		ivPicture.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				scan();
			}
		});
		
		progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Syncing Questions");
        progressDialog.setCancelable(false);
        progressDialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				Question.bind();
				User.bind();
				Current.synced = true;
				prefs_editor.putBoolean(Constants.KEY_PREFS_SYNCED, true);
				prefs_editor.putInt(Constants.KEY_PREFS_ID, Current.user.getGroupId());
				prefs_editor.commit();
				startActivity(new Intent(LoginActivity.this, MenuActivity.class));
				Toast.makeText(LoginActivity.this, "Syncing Successfully Completed", Toast.LENGTH_LONG).show();
			}
		});
		
		downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
		
		downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	String action = intent.getAction();
 
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                	progressDialog.setProgress(progressDialog.getProgress() + 1);
                	if(progressDialog.getProgress() == progressDialog.getMax()){
                		progressDialog.dismiss();
                	}
                }
            }
        };
        
        registerDownloadReceiver();

        Current.synced = prefs.getBoolean(Constants.KEY_PREFS_SYNCED, false);
        Current.group_id = prefs.getInt(Constants.KEY_PREFS_ID, 0);
        
        // prevent keyboard from automatically opening
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void onResume() {
		this.registerDownloadReceiver();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		this.unRegisterDownloadReceiver();
		super.onPause();
	}
	
	private void registerDownloadReceiver(){
		this.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}
	
	private void unRegisterDownloadReceiver(){
		this.unregisterReceiver(downloadReceiver);
	}
}
