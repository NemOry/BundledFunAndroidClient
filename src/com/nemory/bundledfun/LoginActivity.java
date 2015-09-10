package com.nemory.bundledfun;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nemory.bundledfun.helpers.Constants;
import com.nemory.bundledfun.objects.User;

public class LoginActivity extends Activity {

	private ImageView ivIDPicure;
	private TextView tvName, tvID;
	private EditText etPassword;
	private Button btnLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		ivIDPicure = (ImageView) findViewById(R.id.ivIDPicture);
		tvName = (TextView) findViewById(R.id.tvName);
		tvID = (TextView) findViewById(R.id.tvID);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		
		btnLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean exists = User.authenticate(tvID.getText().toString(), etPassword.getText().toString());
				if(exists){
					startActivity(new Intent(LoginActivity.this, MenuActivity.class));
					finish();
				}else{
					Toast.makeText(LoginActivity.this, "password incorrect", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		ivIDPicure.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				scan();
			}
		});
	}
	
	private void scan(){
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	            String id = intent.getStringExtra("SCAN_RESULT");
	            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	            if(format.equals("QR_CODE")){
	            	if(User.exists(id)){
	            		User user = User.getSingle(id);
		            	tvName.setText(user.getName());
		            	tvID.setText(user.getId());
		            	Uri imageUri = Uri.fromFile(new File(Constants.DATAPATH + "users/" + user.getPicture()));
						ivIDPicure.setImageURI(imageUri);
	            	}else{
	            		Toast.makeText(LoginActivity.this, "User with id: " + id + " does not exists.", Toast.LENGTH_LONG).show();
	            	}
	            }
	        } else if (resultCode == RESULT_CANCELED) {
	        	Toast.makeText(this, "Scanning canceled.", Toast.LENGTH_LONG).show();
	        }
	    }
	}
	
}
