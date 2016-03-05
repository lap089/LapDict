package com.lapdict.lap089.lapdict;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by user on 5/31/2015.
 */
public class NotificationPanel {
    private static Context parent;
    private NotificationManager nManager;
    private NotificationCompat.Builder nBuilder;
    public static RemoteViews remoteView;

    public NotificationPanel(Context parent) {
        // TODO Auto-generated constructor stub
        this.parent = parent;

        nBuilder = new NotificationCompat.Builder(parent)
                .setContentTitle("Parking Meter")
                .setSmallIcon(R.drawable.ic_launcher)
                .setOngoing(true).setPriority(NotificationCompat.PRIORITY_HIGH);

        remoteView = new RemoteViews(parent.getPackageName(), R.layout.notificationview);
       if(Dialog_Activity.Isservicerunning)
        remoteView.setTextViewText(R.id.notification_activate,"Deactivate");
        else remoteView.setTextViewText(R.id.notification_activate,"Activate");

        //set the button listeners
        setListeners(remoteView);
        nBuilder.setContent(remoteView);

        nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(2, nBuilder.build());

    }

    public static void setListeners(RemoteViews view){

        //listener 1
        Intent active = new Intent(parent,Dialog_Activity.class);
        active.putExtra("DO", "activate");
        PendingIntent btn1 = PendingIntent.getActivity(parent, 0, active, 0);
        view.setOnClickPendingIntent(R.id.notification_activate, btn1);

        //listener 2
        Intent search = new Intent(parent, Dialog_Activity.class);
        search.putExtra("DO", "search");
        PendingIntent btn2 = PendingIntent.getActivity(parent, 1, search, 0);
        view.setOnClickPendingIntent(R.id.notification_search, btn2);
    }

    public void notificationCancel() {
        nManager.cancel(2);
    }
}




