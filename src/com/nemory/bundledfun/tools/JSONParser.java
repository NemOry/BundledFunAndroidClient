package com.nemory.bundledfun.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import com.nemory.bundledfun.objects.Question;
import com.nemory.bundledfun.objects.User;

public class JSONParser {
	
//	try {
//
//        File dir = Environment.getExternalStorageDirectory();
//        File yourFile = new File(dir, ".BundledFun/data/questions.json");
//        FileInputStream stream = new FileInputStream(yourFile);
//        String jString = null;
//        
//        try {
//            FileChannel fc = stream.getChannel();
//            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
//            jString = Charset.defaultCharset().decode(bb).toString();
//          }
//          finally {
//            stream.close();
//          }
//        
//        Log.d("JSON", jString);
//
//        JSONArray jObject = new JSONArray(jString); 
//
//    } catch (Exception e) {e.printStackTrace();}
	
	public static ArrayList<User> parseUsers() {
		
		ArrayList<User> users = new ArrayList<User>();
		
		String jsonString = "";
		
		try {

	        File dir = Environment.getExternalStorageDirectory();
	        File yourFile = new File(dir, ".BundledFun/users/users.json");
	        FileInputStream stream = new FileInputStream(yourFile);
	        
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
				user.setId(obj.getString("ID"));
				user.setName(obj.getString("NAME"));
				user.setPass(obj.getString("PASS"));
				user.setPicture(obj.getString("PICTURE"));
				user.setTimeElapsed(obj.getString("TIME_ELAPSED"));
				user.setCorrectAnswers(obj.getInt("CORRECT_ANSWERS"));
				user.setHighScore(obj.getInt("HIGH_SCORE"));
				users.add(user);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	public static ArrayList<User> parseStudent(InputStream inputStream) {
		
		ArrayList<User> users = new ArrayList<User>();
		
		try {
			int size = inputStream.available();
			byte[] buffer = new byte[size];
			inputStream.read(buffer);
			inputStream.close();
			String bufferString = new String(buffer);
			JSONArray jsonArray = new JSONArray(bufferString);
			
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				User user = new User();
				user.setId(obj.getString("ID"));
				user.setName(obj.getString("NAME"));
				user.setPass(obj.getString("PASS"));
				user.setPicture(obj.getString("PICTURE"));
				user.setTimeElapsed(obj.getString("TIME_ELAPSED"));
				user.setCorrectAnswers(obj.getInt("CORRECT_ANSWERS"));
				user.setHighScore(obj.getInt("HIGH_SCORE"));
				users.add(user);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	public static ArrayList<Question> parseQuestion(InputStream inputStream) {
		
		ArrayList<Question> questions = new ArrayList<Question>();

		try {
			int size = inputStream.available();
			byte[] buffer = new byte[size];
			inputStream.read(buffer);
			inputStream.close();
			String bufferString = new String(buffer);
			JSONArray jsonArray = new JSONArray(bufferString);

			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				Question q = new Question();
				q.setText(obj.getString("text"));
				q.setDifficulty(obj.getString("difficulty"));
				q.setAnswer(obj.getString("answer"));
				q.setChoice_a(obj.getString("choice_a"));
				q.setChoice_b(obj.getString("choice_b"));
				q.setChoice_c(obj.getString("choice_c"));
				q.setFile(obj.getString("file_name"));
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
	
	public static JSONArray getJSONFromURL(String url) throws ClientProtocolException, IOException, JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		String data = EntityUtils.toString(entity);
		JSONArray users = new JSONArray(data);
		return users;
	}
}