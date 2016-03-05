package com.lapdict.lap089.lapdict;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lapdict.lap089.lapdict.service.ClipboardMonitor;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class MainActivity extends Activity {

    private NotificationManager mNM;
    private Sqlite dbase;
    private BroadcastReceiver mReceiver;
    private TextView dialog;
    private View view;

    @SuppressLint("NewApi") @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbase = new Sqlite(this);
        ArrayList arr =new ArrayList();
        try {
            dbase.Insert_Dictionary("");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        Toast.makeText(this, "Start service!", Toast.LENGTH_SHORT);
        ComponentName service = this.startService(
                new Intent(this, ClipboardMonitor.class));


    }

    //<action android:name="android.intent.action.MAIN" />	<category android:name="android.intent.category.LAUNCHER" />


   /*
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		IntentFilter intentFilter = new IntentFilter(
				"android.intent.action.MAIN");

		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				//extract our message from intent
				String msg_for_me = intent.getStringExtra("some_msg");
				//log our message value

				//LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			  //  View main = inflater.inflate(R.layout.activity_dialog, null);

				LinearLayout rowLink = (LinearLayout)getLayoutInflater().inflate(R.layout.activity_dialog, null);
					dialog = (TextView) rowLink.findViewById(R.id.dialog);
				dialog.setText("helloworld");
				//dialog.setTextColor(Color.parseColor("#FFFFFF"));
				Toast.makeText(context, "Broadcast: "+ msg_for_me, Toast.LENGTH_LONG).show();

				  Intent inten =new Intent(context, Dialog_Activity.class);
			        inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			        startActivity(inten);

			}
		};
		//registering our receiver
		this.registerReceiver(mReceiver, intentFilter);


    }   */

  /*  @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//unregister our receiver
		//this.unregisterReceiver(this.mReceiver);
	}*/

    //it is hard to do this way, maybe error sometime!!!

      /*  mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notif = new Notification(R.drawable.ic_launcher,"MyClips clipboard monitor is started",System.currentTimeMillis());
        notif.flags |= (Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        notif.setLatestEventInfo(this, getText(R.string.clip_monitor_service),
                "Tap here to enter MyClips UI", contentIntent);
        // Use layout id because it's unique
        mNM.notify(R.string.clip_monitor_service, notif);*/


       /* final ClipboardManager cliboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cliboardManager
        .addPrimaryClipChangedListener(new OnPrimaryClipChangedListener() {

            @SuppressLint("NewApi") @Override
            public void onPrimaryClipChanged() {
                ClipData clipData = cliboardManager.getPrimaryClip();
                System.out.println("********** clip changed, clipData: "
                                + clipData.getItemAt(0));
                Log.d("Check: ", "Changed! ");
                 ClipData.Item item = clipData.getItemAt(0);
                 String text = item.getText().toString();
                 Log.d("Check: ", "Changed to: "+ text);
                 Toast.makeText(getApplicationContext(), "Text: "+ item.getText().toString(), Toast.LENGTH_LONG).show();
                 }
            }
        );

	}*/


      /*  ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData abc = clipboard.getPrimaryClip();
        ClipData.Item item = abc.getItemAt(0);
        String text = item.getText().toString();*/

    // String[] a= {"a"};
    // String temp= dbase.Querry(a);
    //Log.d("Check: ",text);

    //  arr = dbase.getAllDictionary();
    // 	 Log.d("Check: ",(String) arr.get(0));

}
