package com.nemory.bundledfun.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.nemory.bundledfun.LoginActivity;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Bundle bundle = intent.getExtras();
		SmsMessage[] messages = null;
		
		if (bundle != null) {	
			Object[] pdus = (Object[]) bundle.get("pdus");
			messages = new SmsMessage[pdus.length];

			String from = "";
			String message = "";
			
			for (int i = 0; i < messages.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				from = messages[i].getOriginatingAddress();
				message = messages[i].getMessageBody().toString();
			}
			
			String messageSplit[];
			
			if(message.contains("MESSAGE")){
				messageSplit = message.split("MESSAGE");
				
				String locker = messageSplit[0];
				locker = locker.trim().toUpperCase();
				String messageToSay = messageSplit[1];
				
				Log.d("MESSAGE", "locker: " + locker + ", msg: " + messageToSay);
				
				//if(from.equals("ADMIN NUMBER HERE")){
					if(locker.equals("LOCK")){
						Intent loginIntent = new Intent(context, LoginActivity.class);
						loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(loginIntent);
						Toast.makeText(context, "From: Your Group Admin\n\n " + messageToSay, Toast.LENGTH_LONG).show();
					}else if(locker.equals("UNLOCK")){
						
					}
				//}
			}else{
				//if(from.equals("ADMIN NUMBER HERE")){
					if(message.equals("LOCK")){
						Intent loginIntent = new Intent(context, LoginActivity.class);
						loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(loginIntent);
						Toast.makeText(context, "Application locked by your group admin.", Toast.LENGTH_LONG).show();
					}else if(message.equals("UNLOCK")){
						// set preference to allow students to login
					}
				//}
			}
			
			//if(from.equals("ADMIN NUMBER HERE")){
				if(message.equals("LOCK")){
					// set preference to do not allow students to login
					// quit the current quiz
					// exit the application
					Intent loginIntent = new Intent(context, LoginActivity.class);
					loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(loginIntent);
					Toast.makeText(context, "from: " + from + ", message: " + message, Toast.LENGTH_LONG).show();
				}else if(message.equals("UNLOCK")){
					// set preference to allow students to login
				}
			//}

		}
	}
}