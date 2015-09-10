package com.nemory.bundledfun.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

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
			
			if(from.equals("ADMIN NUMBER HERE")){
				if(message.equals("LOCK")){
					// set preference to do not allow students to login
					// quit the current quiz
					// exit the application
				}else if(message.equals("UNLOCK")){
					// set preference to allow students to login
				}
			}
			
			Toast.makeText(context, "from: " + from + ", message: " + message, Toast.LENGTH_LONG).show();
		}
	}
}