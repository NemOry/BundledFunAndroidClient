package com.nemory.bundledfun.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;

import com.nemory.bundledfun.objects.Group;
import com.nemory.bundledfun.objects.MyFile;
import com.nemory.bundledfun.objects.Question;
import com.nemory.bundledfun.objects.User;

public class JSONParser {

	public static ArrayList<User> parseUsers() {
		
		ArrayList<User> users = new ArrayList<User>();
		
		String jsonString = "";

		try {
			
	        File dir = Environment.getExternalStorageDirectory();
	        File file = new File(dir, ".BundledFun/files/users.json");
	        FileInputStream stream = new FileInputStream(file);
	        
	        try{
	            FileChannel fc = stream.getChannel();
	            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
	            jsonString = Charset.defaultCharset().decode(bb).toString();
	        } catch (IOException e) {
				e.printStackTrace();
			} finally {
	            try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }

			JSONArray jsonArray = new JSONArray(jsonString);

			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				User user = new User();
				user.setId(obj.getInt("id"));
				user.setUsername(obj.getString("username"));
				user.setGroupId(obj.getInt("group_id"));
				user.setLevel(obj.getInt("level"));
				user.setAccessToken(obj.getString("access_token"));
				user.setName(obj.getString("name"));
				user.setPassword(obj.getString("password"));
				user.setPicture(obj.getString("picture"));
				user.setEmail(obj.getString("email"));
				user.setAccess(obj.getInt("access"));
				users.add(user);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	public static ArrayList<Question> parseQuestions() {
		
		ArrayList<Question> questions = new ArrayList<Question>();
		String jsonString = "";
		
		try {
			
		 	File dir = Environment.getExternalStorageDirectory();
	        File file = new File(dir, ".BundledFun/files/questions.json");
	        FileInputStream stream = new FileInputStream(file);
	        
	        try{
	            FileChannel fc = stream.getChannel();
	            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
	            jsonString = Charset.defaultCharset().decode(bb).toString();
	        } catch (IOException e) {
				e.printStackTrace();
			} finally {
	            try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }

			JSONArray jsonArray = new JSONArray(jsonString);
			
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				Question q = new Question();
				q.setText(obj.getString("text"));
				q.setDifficulty(obj.getString("difficulty"));
				q.setAnswer(obj.getString("answer"));
				q.setChoice_a(obj.getString("choice_a"));
				q.setChoice_b(obj.getString("choice_b"));
				q.setChoice_c(obj.getString("choice_c"));
				q.setFile(obj.getString("file"));
				q.setType(obj.getString("type"));
				q.setScore_points(obj.getInt("points"));
				q.setTimer(obj.getInt("timer"));
				questions.add(q);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return questions;
	}
	
	public static ArrayList<MyFile> parseFiles() {
		
		ArrayList<MyFile> files = new ArrayList<MyFile>();
		
		String jsonString = "";
		
		try {
			
	        File dir = Environment.getExternalStorageDirectory();
	        File file = new File(dir, ".BundledFun/files/files.json");
	        FileInputStream stream = new FileInputStream(file);
	        
	        try{
	            FileChannel fc = stream.getChannel();
	            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
	            jsonString = Charset.defaultCharset().decode(bb).toString();
	        } catch (IOException e) {
				e.printStackTrace();
			} finally {
	            try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }

			JSONArray jsonArray = new JSONArray(jsonString);

			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				MyFile myFile = new MyFile();
				myFile.setType(obj.getString("type"));
				myFile.setCount(Integer.parseInt(obj.getString("count")));
				files.add(myFile);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return files;
	}
	
	public static ArrayList<Group> parseGroups(String jsonString) {
		
		ArrayList<Group> groups = new ArrayList<Group>();
		
		try {

			JSONArray jsonArray = new JSONArray(jsonString);

			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				Group group = new Group();
				group.setID(obj.getInt("id"));
				group.setName(obj.getString("name"));
				groups.add(group);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return groups;
	}

	public static JSONArray getJSONArrayFromURL(String url) throws ClientProtocolException, IOException, JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		String jsonString = EntityUtils.toString(entity);
		JSONArray data = new JSONArray(jsonString);
		return data;
	}
}