package com.nemory.bundledfun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nemory.bundledfun.helpers.Constants;
import com.nemory.bundledfun.helpers.Current;
import com.nemory.bundledfun.objects.Group;
import com.nemory.bundledfun.objects.User;
import com.nemory.bundledfun.tools.JSONParser;

public class RegistrationActivity extends Activity implements OnItemClickListener{
	
	private ListView listGroups;
	private ArrayList<Group> groups;
	private ArrayList<String> list = new ArrayList<String>();;
	private ArrayAdapter<String> adapter;
	private TextView tvGroup;
	private Button btnRegister;
	private EditText etUsername, etPassword, etEmail, etName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.initialize();
        this.loadGroups();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_registration, menu);
        return true;
    }
    
    private void initialize(){
    	listGroups = (ListView) findViewById(R.id.listGroups);
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
    	listGroups.setAdapter(adapter);
    	listGroups.setOnItemClickListener(this);
    	tvGroup = (TextView) findViewById(R.id.tvGroup);
    	etUsername = (EditText) findViewById(R.id.etUsername);
    	etPassword = (EditText) findViewById(R.id.etPassword);
    	etEmail = (EditText) findViewById(R.id.etEmail);
    	etName = (EditText) findViewById(R.id.etName);
    	btnRegister = (Button) findViewById(R.id.btnRegister);
    	
    	btnRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int group_id = Integer.parseInt(tvGroup.getText().toString().split(", ")[1]);
				try {
					register(group_id, etName.getText().toString(), etUsername.getText().toString(), etPassword.getText().toString(), etEmail.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
    	
    }
    
    private void loadGroups(){

    	final ProgressDialog fetcherDialog = new ProgressDialog(this);
		fetcherDialog.setMessage("Fetching list of groups...");
		fetcherDialog.setIndeterminate(true);
		fetcherDialog.setCancelable(false);
		fetcherDialog.show();
		
		Thread t = new Thread(new Runnable() {
			
			boolean running = true;
			
			public void run() {
				while(running){
					
					try {
						
						JSONArray j = Group.fetchMultipleGroups();
						 groups = JSONParser.parseGroups(j.toString());
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					running = false;
					fetcherDialog.dismiss();
				}
			}
		});

		t.start();
		
		while(t.isAlive()){}
		
		bindList();
    }
    
    private void bindList(){
    	for(Group g : groups){
    		list.add(g.getName() + ", " + g.getID());
    	}
    }

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		TextView t = (TextView) view;
		tvGroup.setText(t.getText().toString());
	}
	
	private void register(int groupID, String name, String username, String password, String email) throws JSONException{
		if(!name.equals("") || !username.equals("") || !password.equals("") || !email.equals("")){
			try {
				
				Registrator registrator = new Registrator();
				JSONArray response = registrator.execute(Integer.toString(groupID), name, username, password, email).get();
				JSONObject jsonUser = response.getJSONObject(0);
		
				if(response.length() == 1){ // if no errors
					User user = new User();
					user.setUsername(jsonUser.getString("username"));
					user.setPassword(jsonUser.getString("password"));
					Current.user = user.authenticateOnline();
					Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
					finish();
				}else{
					Toast.makeText(RegistrationActivity.this, "Sorry, " + response.getJSONObject(1).getString("exceptions"), Toast.LENGTH_LONG).show();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}else{
			Toast.makeText(RegistrationActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
		}
	}
	
	public static class Registrator extends AsyncTask<String, Integer, JSONArray>{
		
		@Override
		protected JSONArray doInBackground(String... params) {
			JSONArray response = new JSONArray();
			try {
				response = JSONParser.getJSONArrayFromURL(Constants.HOST_NAME + "/BundledFun/includes/functions/registrator.php?group_id=" + params[0] + "&name=" + params[1] + "&username=" + params[2] + "&password=" + params[3] + "&email=" + params[4]);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return response;
		}
	}
}
