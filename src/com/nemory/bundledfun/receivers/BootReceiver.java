package com.nemory.bundledfun.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// startService(new Intent(context, SMSReceiverService.class));
		Log.d("BOOT", "BOOTED");
	}
}