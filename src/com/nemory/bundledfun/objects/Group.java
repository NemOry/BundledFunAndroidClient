package com.nemory.bundledfun.objects;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.nemory.bundledfun.helpers.Constants;
import com.nemory.bundledfun.tools.JSONParser;

public class Group {
	
	private int id;
	private String 	name;
	public static Context context;
	
	/** ------------ SETTERS AND GETTERS ------------ **/
	
	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static JSONArray fetchMultipleGroups() throws JSONException {
		JSONArray groups = new JSONArray();
		MultipleGroupFetcher fetcher = new MultipleGroupFetcher();
		try {
			groups = fetcher.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return groups;
	}
	
	private static class MultipleGroupFetcher extends AsyncTask<String, Integer, JSONArray>{
		
		@Override
		protected JSONArray doInBackground(String... params) {
			JSONArray groups = new JSONArray();
			try {
				groups = JSONParser.getJSONArrayFromURL(Constants.HOST_NAME + "/BundledFun/includes/jsons/get_groups.php");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return groups;
		}
	}
	
	public static Group fetchSingleGroup(int group_id) throws JSONException {
		Group group = null;
		SingleGroupFetcher fetcher = new SingleGroupFetcher();
		try {
			JSONObject jsonGroup = fetcher.execute(group_id).get();
			if(jsonGroup != null){
				group = new Group();
				group.setID(jsonGroup.getInt("id"));
				group.setName(jsonGroup.getString("name"));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return group;
	}
	
	private static class SingleGroupFetcher extends AsyncTask<Integer, Integer, JSONObject>{
		
		@Override
		protected JSONObject doInBackground(Integer... params) {
			JSONObject group = new JSONObject();
			try {
				group = JSONParser.getJSONArrayFromURL(Constants.HOST_NAME + "/BundledFun/includes/jsons/get_group.php?group_id=" + params[0]).getJSONObject(0);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return group;
		}
	}
	
}