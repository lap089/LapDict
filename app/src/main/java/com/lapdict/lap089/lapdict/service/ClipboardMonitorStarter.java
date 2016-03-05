package com.lapdict.lap089.lapdict.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lapdict.lap089.lapdict.LogTag;


public class ClipboardMonitorStarter extends BroadcastReceiver
implements LogTag {
/* This class should be public; otherwise, the system don't have privilege
* to instantiate it and cause exception occurs.
*/

@Override
public void onReceive(Context context, Intent intent) {
if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
    ComponentName service = context.startService(
            new Intent(context, ClipboardMonitor.class));
    if (service == null) {
        Log.e(TAG, "Can't start service "
                + ClipboardMonitor.class.getName());
    }
} else {
    Log.e(TAG, "Recieved unexpected intent " + intent.toString());
}
}
}
	
	