package com.lapdict.lap089.lapdict.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import android.app.Activity;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.lapdict.lap089.lapdict.Dialog_Activity;
import com.lapdict.lap089.lapdict.R;

public class DictHead extends Service {

  private WindowManager windowManager;
  private ImageView chatHead;
  public static boolean signal_head=false; 

  @Override public IBinder onBind(Intent intent) {
    // Not used
    return null;
  }
  
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
      

	    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

	    chatHead = new ImageView(this);
	    chatHead.setImageResource(R.drawable.dictionary1);

	    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
	        WindowManager.LayoutParams.WRAP_CONTENT,
	        WindowManager.LayoutParams.WRAP_CONTENT,
	        WindowManager.LayoutParams.TYPE_PHONE,
	        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
	        PixelFormat.TRANSLUCENT);

	    params.gravity = Gravity.TOP | Gravity.LEFT;
	   
	    params.x = 0;
	    params.y = 100;

	    windowManager.addView(chatHead, params);
	    signal_head=true;
	   /* chatHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {			
				Intent inten =new Intent(getApplicationContext(), Dialog_Activity.class); 
			      inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			      startActivity(inten);
			     // windowManager.removeView(chatHead);				
			}

		});*/
	    
	    
	    chatHead.setOnTouchListener(new View.OnTouchListener() {
	    	  private int initialX;
	    	  private int initialY;
	    	  private float initialTouchX;
	    	  private float initialTouchY;

	    	  @SuppressWarnings("deprecation")
			@Override public boolean onTouch(View v, MotionEvent event) {
	    	    switch (event.getAction()) {
	    	      case MotionEvent.ACTION_DOWN:
	    	        initialX = params.x;
	    	        initialY = params.y;
	    	        initialTouchX = event.getRawX();
	    	        initialTouchY = event.getRawY();
	    	        return true;
	    	      case MotionEvent.ACTION_UP:
	    	    	  if(Math.abs(initialX - params.x) <= 10 && Math.abs(initialY-params.y) <= 10){
	    	    		  Intent inten =new Intent(getApplicationContext(), Dialog_Activity.class);
	    		      inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    		      startActivity(inten);
	    		      windowManager.removeView(chatHead);	
	    		     signal_head = false;
	    		//    new Screenshoot().execute("string");
	    		    /* LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	    			  View infl = inflater.inflate(R.layout.activity_dialog, null);
	    		     Toast.makeText(getApplicationContext(), "Shoot!!!", Toast.LENGTH_SHORT);
	    		     Bitmap bitmap = takeScreenshot();3
	    		     if(bitmap==null) Toast.makeText(getApplicationContext(), "NULL !!!!", Toast.LENGTH_SHORT);
	    		     else
	  		       saveBitmap(bitmap);
	    		       Log.d("Check: ","Take !!!!!!!!!!!!! ");*/
	    		 //     Toast.makeText(getApplicationContext(), "Shoot!!!", Toast.LENGTH_SHORT);
	    		      
	    	    	  }
	    	    	  else 
	    	    	  {
	    	    		  Display display = windowManager.getDefaultDisplay();
	    	    		 // final Point size = new Point();
	    	    		 // display.getSize(size);
	    	    		 float width = display.getWidth();
	    	    		 Log.d("Check: ",String.valueOf(params.x));
	    	    		 Log.d("Check: ",String.valueOf(params.y));
	    	    		 if(params.x<(width/2))
	    	    		 params.x=0;
	    	    		 else params.x=(int) width;
	    	    		  windowManager.updateViewLayout(chatHead, params);
	    	    		  
	    	    	  }
	    	        return true;
	    	      case MotionEvent.ACTION_MOVE:
	    	        params.x = initialX + (int) (event.getRawX() - initialTouchX);
	    	        params.y = initialY + (int) (event.getRawY() - initialTouchY);
	    	        
	    	        windowManager.updateViewLayout(chatHead, params);
	    	        
	    	        
	    	        return true;
	    	    }
	    	    return false;
	    	  }
	    	});
	    
	    	
	    
	  
      return START_STICKY;
  }
  
  
  /*public Bitmap takeScreenshot() {
	  LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	  View infl = inflater.inflate(R.layout.activity_dialog, null);
	 // View rootView = ((ListActivity) context).findViewById(android.R.id.content).getRootView();
	  View rootView = infl.findViewById(android.R.id.content);
	   rootView.setDrawingCacheEnabled(true);
	   return rootView.getDrawingCache();
	}*/
  
  
 /* public void saveBitmap(Bitmap bitmap) {
	  
	    File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
	    FileOutputStream fos;
	    try {
	        fos = new FileOutputStream(imagePath);
	        bitmap.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        Log.e("GREC", e.getMessage(), e);
	    } catch (IOException e) {
	        Log.e("GREC", e.getMessage(), e);
	    }
	}*/
  
  @Override public void onCreate() {
    super.onCreate();

    /*windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

    chatHead = new ImageView(this);
    chatHead.setImageResource(R.drawable.closebutton);

    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_PHONE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT);

    params.gravity = Gravity.TOP | Gravity.LEFT;
    params.x = 0;
    params.y = 100;

    windowManager.addView(chatHead, params);
    
    chatHead.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {			
			Intent inten =new Intent(getApplicationContext(), Dialog_Activity.class); 
		      inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		      startActivity(inten);
		     // windowManager.removeView(chatHead);				
		}

	});
    
    
    chatHead.setOnTouchListener(new View.OnTouchListener() {
    	  private int initialX;
    	  private int initialY;
    	  private float initialTouchX;
    	  private float initialTouchY;

    	  @Override public boolean onTouch(View v, MotionEvent event) {
    	    switch (event.getAction()) {
    	      case MotionEvent.ACTION_DOWN:
    	        initialX = params.x;
    	        initialY = params.y;
    	        initialTouchX = event.getRawX();
    	        initialTouchY = event.getRawY();
    	        return true;
    	      case MotionEvent.ACTION_UP:
    	    	  if(initialX==params.x && initialY==params.y){
    	    		  Intent inten =new Intent(getApplicationContext(), Dialog_Activity.class); 
    		      inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		      startActivity(inten);
    		      windowManager.removeView(chatHead);	}
    	        return true;
    	      case MotionEvent.ACTION_MOVE:
    	        params.x = initialX + (int) (event.getRawX() - initialTouchX);
    	        params.y = initialY + (int) (event.getRawY() - initialTouchY);
    	        windowManager.updateViewLayout(chatHead, params);
    	        return true;
    	    }
    	    return false;
    	  }
    	});
    
    	*/
    
    
  }
  
  
  
  
  
  /*public class Screenshoot extends AsyncTask<String, Void, String> {
  	 Context context;
  	
  	 @Override
  	    protected void onPreExecute() {
  		 	context = getApplicationContext();
  		 	Toast.makeText(context, "Start!!!", Toast.LENGTH_SHORT);
  	        super.onPreExecute();
  	    }
  	
		@Override
		protected String doInBackground(String... urls) {
			Bitmap bitmap = takeScreenshot();
		       saveBitmap(bitmap);
			return urls[0];
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(context, "Finish!!!", Toast.LENGTH_SHORT);
			
		}
	}*/
  
  
  

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (chatHead != null) {
		windowManager.removeView(chatHead);
		signal_head = false;
	}
  }
}