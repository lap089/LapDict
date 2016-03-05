package com.lapdict.lap089.lapdict.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Broadcast_dialog extends BroadcastReceiver {

	   @Override
	   public void onReceive(Context context, Intent intent) {
	      Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
	   }

	

	}
