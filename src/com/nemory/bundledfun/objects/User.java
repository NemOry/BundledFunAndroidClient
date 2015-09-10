package com.nemory.bundledfun.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.nemory.bundledfun.helpers.Constants;
import com.nemory.bundledfun.tools.JSONParser;

public class User {
	
	private int 	id;
	private int 	group_id;
	private String 	username;
	private String 	password;
	private int 	level;
	private String 	accessToken;
	private String 	name;
	private String 	picture;
	private String 	email;
	private int 	access;

	public static ArrayList<User> users = new ArrayList<User>();
	
	public static User getSingle(String id) {
		for(User user : users){
			if(user.getUsername().equals(id)){
				return user;
			}
		}
		return null;
	}
	
	public static boolean exists(String id) {
		for(User user : users){
			if(user.getUsername().equals(id)){
				return true;
			}
		}
		return false;
	}
	
	public User authenticate() {
		for(User user : users){
			if(user.getUsername().equals(this.username) && user.getPassword().equals(this.password)){
				return user;
			}
		}
		return null;
	}
	
	public User authenticateOnline() throws JSONException {
		User user = null;
			try {
				Authenticator auth = new Authenticator();
				user = auth.execute(this.username, this.password).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		return user;
	}
	
	public static class Authenticator extends AsyncTask<String, Integer, User>{
		
		@Override
		protected User doInBackground(String... params) {
			User user = null;
			JSONObject jsonUser = null;
			try {
				jsonUser = JSONParser.getJSONArrayFromURL(Constants.HOST_NAME + "/BundledFun/includes/jsons/auth_user.php?username=" + params[0] + "&password=" + params[1]).getJSONObject(0);
				if(!jsonUser.has("exceptions")){
					user = new User();
					user.setId(jsonUser.getInt("id"));
					user.setUsername(jsonUser.getString("username"));
					user.setGroupId(jsonUser.getInt("group_id"));
					user.setLevel(jsonUser.getInt("level"));
					user.setAccessToken(jsonUser.getString("access_token"));
					user.setName(jsonUser.getString("name"));
					user.setPassword(jsonUser.getString("password"));
					user.setPicture(jsonUser.getString("picture"));
					user.setEmail(jsonUser.getString("email"));
					user.setAccess(jsonUser.getInt("access"));
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return user;
		}
	}
	
	public User getByUsernameOnline() throws JSONException {
		User user = null;
			try {
				UserGetter auth = new UserGetter();
				user = auth.execute(this.username).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		return user;
	}
	
	public static class UserGetter extends AsyncTask<String, Integer, User>{
		
		@Override
		protected User doInBackground(String... params) {
			User user = null;
			JSONObject jsonUser = null;
			try {
				jsonUser = JSONParser.getJSONArrayFromURL(Constants.HOST_NAME + "/BundledFun/includes/jsons/get_by_username.php?username=" + params[0]).getJSONObject(0);
				if(!jsonUser.has("exceptions")){
					user = new User();
					user.setId(jsonUser.getInt("id"));
					user.setUsername(jsonUser.getString("username"));
					user.setGroupId(jsonUser.getInt("group_id"));
					user.setLevel(jsonUser.getInt("level"));
					user.setAccessToken(jsonUser.getString("access_token"));
					user.setName(jsonUser.getString("name"));
					user.setPassword(jsonUser.getString("password"));
					user.setPicture(jsonUser.getString("picture"));
					user.setEmail(jsonUser.getString("email"));
					user.setAccess(jsonUser.getInt("access"));
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return user;
		}
	}
	
	public static boolean saveScoreOnline(int id, int score, int time_elapsed, int correct_answers, String access_token) throws JSONException {
		boolean success = false;
		try {
			ScoreSaver scoreSaver = new ScoreSaver();
			User user = scoreSaver.execute(id + "", score + "", time_elapsed + "", correct_answers + "", access_token).get();
			if(user != null){
				success = true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return success;
	}
	
	public static class ScoreSaver extends AsyncTask<String, Integer, User>{
		
		@Override
		protected User doInBackground(String... params) {
			User user = null;
			JSONObject jsonUser = null;
			try {

				jsonUser = JSONParser.getJSONArrayFromURL(Constants.HOST_NAME + "/BundledFun/includes/jsons/save_score.php?user_id=" + params[0] + "&score=" + params[1] + "&time_elapsed=" + params[2] + "&correct_answers=" + params[3] + "&user_access_token=" + params[4]).getJSONObject(0);
				if(jsonUser != null){
					user = new User();
				}
				
				Log.d("USER", jsonUser.toString());
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return user;
		}
	}

	/** ------------ SETTERS AND GETTERS ------------ **/
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getGroupId() {
		return group_id;
	}

	public void setGroupId(int group_id) {
		this.group_id = group_id;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAccess() {
		return access;
	}

	public void setAccess(int access) {
		this.access = access;
	}
	
	public static void bind(){
		String path = Environment.getExternalStorageDirectory() + File.separator + Constants.BUNDLEDFUN_FOLDER + File.separator;
		File users = new File(path + "files" + File.separator + "users.json");

		if(users.exists()){
			User.users 	= JSONParser.parseUsers();
		}else{
			Log.d("MISSING FILE", users.getAbsolutePath());
		}
	}
}