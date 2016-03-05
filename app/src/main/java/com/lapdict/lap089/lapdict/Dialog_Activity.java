package com.lapdict.lap089.lapdict;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.w3c.dom.DocumentFragment;
import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lapdict.lap089.lapdict.Listview3d.Dynamics;
import com.lapdict.lap089.lapdict.Listview3d.MyListView;
import com.lapdict.lap089.lapdict.service.ClipboardMonitor;
import com.lapdict.lap089.lapdict.service.DictHead;

public class Dialog_Activity extends Activity implements LogTag {


	//private BroadcastReceiver mReceiver;

	private ScrollView scrollview;
	public static TextView value;
	public static EditText key;
	private ImageButton button;
	private ImageButton button_active;
	private ImageButton button_clear;
	private Sqlite dbase;
	private SharedPreferences mPrefs;
	private ClipboardManager mCM;
	//private ListView3d lv;
	private String[] listview_names;
	private static ArrayList<String> array_sort;
	private ImageButton button_sound;
	int textlength = 0;
	private MediaPlayer mp;
	public NotificationManager mNotifyManager;
	public NotificationCompat.Builder mBuilder;
	public static ProgressDialog mProgressDialog1, mProgressDialog, mProgressDialog2;
	public String Filename = "lapdict";
	public String Path = Environment.getExternalStorageDirectory().toString() + "/";
	public boolean Isalive = true;                                // check for dialog process, true means it is still ok, otherwise it was closed
	int id = 1;
	public static boolean Isdisplayed = false;
	public static boolean Istableexistent = false;
	public static boolean Isservicerunning = false;
	public String Value = "";
	private String[] Setting = {"Check"};
	public Setting[] _settings;
	public static NotificationPanel mPanel;
	private MyListView lv;
	public MyAdapter adapter;
	public SimpleDynamics simdynamic;
   /* @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.x = 50;
        lp.y = 50;
        //lp.width = 300;
        //lp.height = 300;
        getWindowManager().updateViewLayout(view, lp);
    }*/

	@Override
	public void onBackPressed() {
		Log.d("CDA", "onBackPressed Called");
		//if(isMyServiceRunning(DictHead.class)==false)
		if (DictHead.signal_head == false)
			startService(new Intent(Dialog_Activity.this, DictHead.class));
		finish();
	}


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPrefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		int temp = mPrefs.getInt("Theme", 0);
		//Bundle bundle1 = getIntent().getExtras();
		//if(bundle1 != null && bundle1.getInt("Theme",-1)==0) {
		//		setContentView(R.layout.activity_dialog);
		//	Current_layout = 0;
		//}
		//else {
		//	setContentView(R.layout.activity_dialog_2);
		//	Current_layout = 1;
		//}
		if (temp == 0)
			setContentView(R.layout.activity_dialog);
		else
			setContentView(R.layout.activity_dialog_2);
		this.setFinishOnTouchOutside(false);


		simdynamic = new SimpleDynamics(0.9f, 0.6f);
		mPanel = new NotificationPanel(getApplication());
		Isdisplayed = true;
		dbase = new Sqlite(getApplicationContext());
		key = (EditText) findViewById(R.id.key);
		value = (TextView) findViewById(R.id.value);
		scrollview = (ScrollView) findViewById(R.id.scroll);
		button = (ImageButton) findViewById(R.id.search);
		button_active = (ImageButton) findViewById(R.id.active);
		button_clear = (ImageButton) findViewById(R.id.clear);
		mCM = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	//	lv = (ListView3d) findViewById(android.R.id.list);
		listview_names = new String[10];
		button_sound = (ImageButton) findViewById(R.id.sound);
		mp = new MediaPlayer();
		//	Spinner  SpinnerExample = (Spinner)findViewById(R.id.spinner);

		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.dimAmount = 0.0f;
		this.getWindow().setAttributes(lp);
		Istableexistent = dbase.isTableExists();
		Isservicerunning = isMyServiceRunning(ClipboardMonitor.class);


		lv = (MyListView) findViewById(R.id.listview);




		//LayoutInflater layoutInflater =
		//      (LayoutInflater)getBaseContext()
		//     .getSystemService(LAYOUT_INFLATER_SERVICE);
		//View popupView = layoutInflater.inflate(R.layout.activity_dialog, null);

		// final WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		// ImageButton popupView = (ImageButton) findViewById(R.id.clear);
		RelativeLayout relative = (RelativeLayout) findViewById(R.id.relative);
		relative.setOnTouchListener(new OnTouchListener() {
			int orgX, orgY;
			int offsetX, offsetY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						orgX = (int) event.getX();
						orgY = (int) event.getY();
						//   button_clear.callOnClick();
						break;
					case MotionEvent.ACTION_MOVE:
						offsetX = (int) event.getRawX() - orgX;
						offsetY = (int) event.getRawY() - orgY;

						WindowManager.LayoutParams layoutParams = Dialog_Activity.this.getWindow().getAttributes();
						layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
						layoutParams.x = offsetX;
						layoutParams.y = offsetY;
						Dialog_Activity.this.getWindow().setAttributes(layoutParams);
		          
		          /*lp.gravity = Gravity.TOP | Gravity.LEFT;
		          lp.dimAmount = 0;
		          lp.x = offsetX;
		          lp.y = offsetY;
		          LayoutInflater inflater = getLayoutInflater();
		          RelativeLayout ll = (RelativeLayout) inflater.inflate(
		                  R.layout.activity_dialog, null);
		          setContentView(ll, lp);
		          
		          
		          
		          View view = getWindow().getDecorView();
		          WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
		          lp.gravity = Gravity.LEFT | Gravity.TOP;
		          lp.x = offsetX;
		          lp.y = offsetY;
		          //lp.width = 300;
		          //lp.height = 300;
		          getWindowManager().updateViewLayout(view, lp);*/
						// popupWindow.update(offsetX, offsetY, -1, -1, true);
						break;
				}
				return true;
			}
		});
		    
		   /* LayoutInflater inflater = getLayoutInflater();
		    LinearLayout ll = (LinearLayout) inflater.inflate(
		            R.layout.input_address, null);
		    setContentView(ll, lp);
		    */


		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {

			String notification_action = bundle.getString("DO");

			if (notification_action != null) {
				if (notification_action.compareTo("activate") == 0) {
					if (Isservicerunning) {
						getApplicationContext().stopService(new Intent(getApplicationContext(), ClipboardMonitor.class));
						Toast.makeText(getApplicationContext(), "service is stopped", Toast.LENGTH_SHORT).show();
						Isservicerunning = false;
						//	NotificationPanel.remoteView.setTextViewText(R.id.notification_activate,"Activate");
					} else {
						getApplicationContext().startService(new Intent(getApplicationContext(), ClipboardMonitor.class));
						Toast.makeText(getApplicationContext(), "service is starting...", Toast.LENGTH_SHORT).show();
						Isservicerunning = true;
						//	NotificationPanel.remoteView.setTextViewText(R.id.notification_activate,"Activate");
					}
					mPanel.notificationCancel();
					mPanel = new NotificationPanel(getApplication());
					Dialog_Activity.this.finish();
				}


			}



		this.setFinishOnTouchOutside(true);
		lv.setVisibility(View.GONE);
		//TODO here get the string stored in the string variable and do
		// setText() on userName
		key.setText(bundle.getString("Key"), TextView.BufferType.EDITABLE);
		if (bundle.getString("Value") != null)
			value.setText(Html.fromHtml(bundle.getString("Value"), null, new TextViewHtmlTagHandler()));

	}

	else if(mPrefs.getString("Key",null)!=null)

	{
		lv.setVisibility(View.GONE);
		String temp_key = mPrefs.getString("Key", "Has been null");
		key.setText(temp_key, TextView.BufferType.EDITABLE);
		//dbase = new Sqlite(Dialog_Activity.this);
		// String[] a = {temp_key};
		String temp_value = mPrefs.getString("Value", "Has been null");

		value.setText(Html.fromHtml(temp_value, null, new TextViewHtmlTagHandler()));
	}


	button.setOnClickListener(new

	OnClickListener() {
		@Override
		public void onClick (View arg0){
			lv.setVisibility(View.GONE);
			final String str = key.getText().toString().trim();
			if (str.length() == 0) {
				Toast.makeText(Dialog_Activity.this, "Please input your word!", Toast.LENGTH_SHORT).show();
				return;
			}

			ClipboardMonitor.Signal = false;
			mCM.setText(str);

			new Setviewtask(Dialog_Activity.this, str).execute();
			// value.loadDataWithBaseURL(null, Value, "text/html", "utf-8", null)


		}
	}

	);


	button_active.setOnClickListener(new

	OnClickListener() {
		@Override
		public void onClick (View arg0){

			setListData();
			// define the list adapter with the choices
			ListAdapter adapter = new CustomArrayAdapter(Dialog_Activity.this, _settings);

			final AlertDialog.Builder ad = new AlertDialog.Builder(Dialog_Activity.this);
			// define the alert dialog with the choices and the action to take
			// when one of the choices is selected
			ad.setTitle("Settings");
			ad.setIcon(R.drawable.imagesetting);
			ad.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// a choice has been made!
					if (_settings[which].getName() == "Activate") {
						Isalive = true;
						//Toast.makeText(getApplicationContext(), "Starting to update!", Toast.LENGTH_SHORT).show();
						mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						mBuilder = new NotificationCompat.Builder(Dialog_Activity.this);
						mBuilder.setContentTitle("Update")
								.setContentText("Update in progress...")
								.setSmallIcon(R.drawable.update);


						if (isMyServiceRunning(ClipboardMonitor.class) == false) {
							ComponentName service = getApplicationContext().startService(new Intent(getApplicationContext(), ClipboardMonitor.class));
							Toast.makeText(getApplicationContext(), "service is starting...", Toast.LENGTH_SHORT).show();
							Isservicerunning = true;
						} else {
							Toast.makeText(getApplicationContext(), "service has been already started!", Toast.LENGTH_SHORT).show();
							Isservicerunning = true;
						}


						if (Istableexistent == true) {
							mBuilder.setContentText("completed")
									// Removes the progress bar
									.setProgress(0, 0, false);
							mNotifyManager.notify(id, mBuilder.build());
							key.setFocusableInTouchMode(true);
							Toast.makeText(Dialog_Activity.this, "Update completed!", Toast.LENGTH_SHORT).show();
							//	Toast.makeText(Dialog_Activity.this, "Service has already started!", Toast.LENGTH_SHORT).show();
							Log.d("Check: ","True");

							dialog.dismiss();
							return;
						}
						Log.d("Check: ","False");

						final DownloadTask downloadTask = new DownloadTask(getApplicationContext());
						downloadTask.execute("https://drive.google.com/uc?export=download&id=0B345W5Qx9YfXZHRYaEVhdUl4QWM");
						mProgressDialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								downloadTask.cancel(true);
								Isalive = false;
							}
						});

						//Log.d("Check: ", "Download done!!!");
						final UnzipTask unzipTask = new UnzipTask(Dialog_Activity.this);
						unzipTask.execute();
						mProgressDialog2.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								unzipTask.cancel(true);
								Isalive = false;
							}
						});

                        //Log.d("Check: ", "Unzip done!!!");

						final Insert_Dictionary insert_Dictionary = new Insert_Dictionary(Dialog_Activity.this);
						insert_Dictionary.execute();
						mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								insert_Dictionary.cancel(true);
								Isalive = false;
							}
						});

					} else if (_settings[which].getName() == "Deactivate") {
						// stop service
						getApplicationContext().stopService(new Intent(getApplicationContext(), ClipboardMonitor.class));
						Toast.makeText(getApplicationContext(), "service is stopped", Toast.LENGTH_SHORT).show();
						Isservicerunning = false;

					} else {
						//Change theme


						//	layout.putExtra("Layout")
						//		if(Current_layout == 0)
						//		layout.putExtra("Theme",1);
						//		else layout.putExtra("Theme",0);
						Editor editor = mPrefs.edit();

						if (mPrefs.getInt("Theme", 0) == 0)
							editor.putInt("Theme", 1);
						else
							editor.putInt("Theme", 0);
						editor.commit();
						Dialog_Activity.this.finish();
						Intent layout = new Intent(Dialog_Activity.this, Dialog_Activity.class);
						startActivity(layout);

						//setContentView(R.layout.activity_dialog_2);

					}


					dialog.dismiss();
				}
			});
			ad.show().getWindow().setLayout(200, 400);

		}


	}

	);


	button_clear.setOnClickListener(new

	OnClickListener() {
		@Override
		public void onClick (View arg0){
			lv.setVisibility(View.GONE);
			key.setText("");
			//	finish();
			//ComponentName service = getApplicationContext().startService(new Intent(getApplicationContext(), ClipboardMonitor.class));
			//Toast.makeText(getApplicationContext(), "service has been started!", Toast.LENGTH_SHORT).show();
		}

	}

	);


	button_sound.setOnClickListener(new

	OnClickListener() {

		@Override
		public void onClick (View v){

			new Thread(new Runnable() {
				public void run() {


					try {

						if (mp.isPlaying()) {
							mp.stop();
							mp.release();
						}
						mp = new MediaPlayer();

						//   AssetFileDescriptor afd = getAssets().openFd("AudioFile.mp3");
						//  mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
						String file_name = key.getText().toString().trim().toLowerCase() + ".mp3";
						Uri myUri = Uri.parse("https://ssl.gstatic.com/dictionary/static/sounds/de/0/" + file_name);
						//	Toast.makeText(getApplicationContext(), "starting : "+ myUri.toString(), Toast.LENGTH_SHORT).show();

						// 	String OUTPUT_FILE = "http://followthelamb.net/19/SID19913.mp3";
						//String OUTPUT_FILE = "/Removable/MicroSD/LapDict/Speech/"+(char)(file_name.charAt(0)-32)+"/"+file_name;
						//File file =new File(OUTPUT_FILE);
						//if(file.exists()){
						//Toast.makeText(getApplicationContext(), "starting : "+ OUTPUT_FILE, Toast.LENGTH_SHORT).show();
						//}
						//else {

						//	OUTPUT_FILE = "/Removable/MicroSD/LapDict/Speech/"+(char)(file_name.charAt(0)-32)+"/"+file_name.replace(file_name.charAt(0), (char)(file_name.charAt(0)-32));
						//	Toast.makeText(getApplicationContext(), "starting : "+ OUTPUT_FILE, Toast.LENGTH_SHORT).show();
						//	File temp =new File(OUTPUT_FILE);
						//	file=temp;
						//}

						mp.setDataSource(Dialog_Activity.this, myUri);
						mp.prepare();
						mp.start();

					} catch (Exception e) {
						e.printStackTrace();
					}


				}
			}).start();
		}
	}

	);


	array_sort=new ArrayList<String>(Arrays.asList(listview_names));

	//setListAdapter(new bsAdapter(this));
		//setListAdapter(new MyAdapter(this));
		 adapter = new MyAdapter(this,array_sort);

		lv.setAdapter(adapter);
		lv.setDynamics(simdynamic);

	key.setOnEditorActionListener(new TextView.OnEditorActionListener()

	{
		@Override
		public boolean onEditorAction (TextView v,int actionId, KeyEvent event){
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			lv.setVisibility(View.GONE);

			String str = key.getText().toString().trim();
			if (str.length() == 0) {
				Toast.makeText(Dialog_Activity.this, "Please input your word!", Toast.LENGTH_SHORT).show();
				return true;
			}

			ClipboardMonitor.Signal = false;
			mCM.setText(str);
			Editor editor = mPrefs.edit();
			editor.putString("Key", str);


			dbase = new Sqlite(Dialog_Activity.this);
			//String[] a = {str};
			ArrayList<String> temp = dbase.Query_Array(str);
			String Value = " ";
			int size = temp.size();
			for (int i = 0; i < size; ++i) {
				if (Value != " ")
					Value = Value + "<br/>" + temp.get(i);
				else Value = temp.get(i);
			}
			value.setText(Html.fromHtml(Value, null, new TextViewHtmlTagHandler()));
			scrollview.scrollTo(0, 0);
			editor.putString("Value", Value);
			editor.commit();
			ClipboardMonitor.Signal = true;
			Toast.makeText(Dialog_Activity.this, str, Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}


	}

	);


	key.addTextChangedListener(new

	TextWatcher() {
		public void afterTextChanged (Editable s){
			// Abstract Method of TextWatcher Interface.
		}

	public void beforeTextChanged(CharSequence s,
								  int start, int count, int after) {
		// Abstract Method of TextWatcher Interface.
	}

	public void onTextChanged(CharSequence s,
							  int start, int before, int count) {
		lv.setVisibility(View.VISIBLE);
		textlength = key.getText().toString().trim().length();
		if (textlength == 0) {
			lv.setVisibility(View.GONE);
			return;
		}

		array_sort.clear();
		dbase = new Sqlite(Dialog_Activity.this);
		array_sort =  dbase.getArraywords(key.getText().toString());
		if (array_sort != null)
			AppendList(array_sort);
	}
});

		lv.setOnItemClickListener(
				new OnItemClickListener(){
					public void onItemClick(AdapterView<?>arg0,
		View arg1,int position,long arg3)
		{

		String temp=array_sort.get(position);

		key.setText(temp);


		button.callOnClick();

		}
		});


		}


	public void setListData()
	{_settings = new Setting[2];

		// define the display string, the image, and the value to use
		// when the choice is selected
		if(Isservicerunning == true)
		_settings[0]  = new Setting(getImg(R.drawable.imagedeactivate),
				"Deactivate");
		else
		_settings[0]  = new Setting(getImg( R.drawable.imageactivate),
				"Activate");
		_settings[1]  = new Setting(getImg( R.drawable.imagesetting ),
				"Theme" );
	}

	private Drawable getImg( int res )
	{
		Drawable img = getResources().getDrawable( res );
		img.setBounds(0, 0, 48, 48);
		return img;
	}





	private class Setviewtask extends AsyncTask<String, Integer, Spanned> {

		private Context context;
		private String str;
		private Editor editor;

		public Setviewtask(Context context,String str) {
			this.context = context;
			this.str=str;
		}

		@Override
		protected Spanned doInBackground(String... sUrl) {
			long timefirst = System.currentTimeMillis();
			ArrayList<String> temp= dbase.Query_Array(str);
			Log.d("Check timefisrt: ",String.valueOf(System.currentTimeMillis() - timefirst));

			long time = System.currentTimeMillis();
			int size=temp.size();
			Log.d("Check timefisrt: ",String.valueOf(size));
			for(int i=0;i<size;++i){
				if(Value!=" ")
					Value= Value + "<br/>"+ temp.get(i);
				else Value=temp.get(i);
			}
			Log.d("Check time: ",String.valueOf(System.currentTimeMillis() - time));

			Spanned htmlview = Html.fromHtml(Value,null,new TextViewHtmlTagHandler());
			return htmlview;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dbase = new Sqlite(Dialog_Activity.this);
			editor = mPrefs.edit();
			editor.putString("Key", str);
			Value = " ";
		}



		@Override
		protected void onPostExecute(Spanned result) {
			super.onPostExecute(result);


			value.setText(result);
			scrollview.scrollTo(0, 0);
			editor.putString("Value", Value);
			editor.commit();

			ClipboardMonitor.Signal = true;
			Toast.makeText(Dialog_Activity.this, str, Toast.LENGTH_SHORT).show();
			Log.d("Check: ","Asyntask Setview Done!");


		}
	}






	private class DownloadTask extends AsyncTask<String, Integer, String> {

		private Context context1;
		private PowerManager.WakeLock mWakeLock1;

		public DownloadTask(Context context) {
			this.context1 = context;
		}

		@Override
		protected String doInBackground(String... sUrl) {
			if(Looper.getMainLooper().getThread()==Thread.currentThread())
				Log.d("Check: ","UI thread");

			else Log.d("Check: ","Background thread");

			if(Isalive == false)
				return "false";
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(sUrl[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					Isalive=false;
					return "Server returned HTTP " + connection.getResponseCode()
							+ " " + connection.getResponseMessage();
				//	Isalive=false;
				//	return null;
				}

				// this will be useful to display download percentage
				// might be -1: server did not report the length
				//int fileLength = connection.getContentLength();
				//List fileLength  = connection.getHeaderFields().get("content-Lenght");
				int fileLength = 5921683;
				// download the file
				input = connection.getInputStream();
				//output = new FileOutputStream("/sdcard/file_name.extension");
				output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/lapdict.zip");
				byte data[] = new byte[65536];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					// allow canceling with back button
					if (isCancelled()) {
						input.close();
						return null;
					}
					total += count;
					Log.d("Check: ",String.valueOf(total));
					// publishing the progress....
					if (fileLength > 0) // only if total length is known
						publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
			} catch (Exception e) {
				return e.toString();
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}

				if (connection != null)
					connection.disconnect();
			}
			return null;



		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// instantiate it within the onCreate method90
			//Log.d("Check: ",String.valueOf(Isalive));
			// take CPU lock to prevent CPU from going off if the user
			// presses the power button during download
			PowerManager pm = (PowerManager) context1.getSystemService(Context.POWER_SERVICE);
			mWakeLock1 = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					getClass().getName());
			mWakeLock1.acquire();
			mProgressDialog1 = new ProgressDialog(Dialog_Activity.this);
			mProgressDialog1.setMessage("Downloading...");
			mProgressDialog1.setIndeterminate(true);
			mProgressDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog1.setCancelable(true);
			mProgressDialog1.show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			// if we get here, length is known, now set indeterminate to false
			mProgressDialog1.setIndeterminate(false);
			mProgressDialog1.setMax(100);
			mProgressDialog1.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
		/*	if(Looper.getMainLooper().getThread()==Thread.currentThread())
				Log.d("Check: ","UI thread");
			else Log.d("Check: ","Background thread");*/
			super.onPostExecute(result);
			mWakeLock1.release();
			mProgressDialog1.dismiss();
			Log.d("Check: ", "Dismiss1!!!");

			if (result != null) {
				Toast.makeText(context1, "Download error: " + result, Toast.LENGTH_LONG).show();
				Log.d("Check: ", "Download error!!!");
			//	Isalive = false;
			}
			else {
				Toast.makeText(context1, "Download done, start processing files!", Toast.LENGTH_SHORT).show();
			}


			mProgressDialog2.show();

		}
	}








	private class UnzipTask extends AsyncTask<String, Integer, String> {

		private Context context2;
		private PowerManager.WakeLock mWakeLock2;

		public UnzipTask(Context context) {
			this.context2 = context;
		}

		@Override
		protected String doInBackground(String... sUrl) {
			if(Isalive == false){
				Log.d("Check: ", "Is blocked?");
				return null;}
			unpackZip(Path,Filename+".zip");
			return null;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// instantiate it within the onCreate method
			// take CPU lock to prevent CPU from going off if the user
			// presses the power button during download
			PowerManager pm = (PowerManager) context2.getSystemService(Context.POWER_SERVICE);
			mWakeLock2 = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					getClass().getName());
			mWakeLock2.acquire();

			mProgressDialog2 = new ProgressDialog(Dialog_Activity.this);
			mProgressDialog2.setMessage("Unzipping...");
			mProgressDialog2.setIndeterminate(true);
			mProgressDialog2.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
			mProgressDialog2.setCancelable(true);
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			// if we get here, length is known, now set indeterminate to false
			mProgressDialog2.setIndeterminate(false);
			mProgressDialog2.setMax(100);
			mProgressDialog2.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		/*	if(Looper.getMainLooper().getThread()==Thread.currentThread())
				Log.d("Check: ","UI thread");
			else Log.d("Check: ","Background thread");*/
			mWakeLock2.release();
			mProgressDialog2.dismiss();
			Log.d("Check: ", "Dismiss2!!!");


			mProgressDialog.show();

		}
	}





	public class Insert_Dictionary extends AsyncTask<Void, Integer, Integer> {

		private Context context;
		private PowerManager.WakeLock mWakeLock;

		public Insert_Dictionary(Context context) {
			this.context = context;
		}

		@SuppressLint("NewApi") @Override
		protected void onPreExecute() {
			super.onPreExecute();
		//	Toast.makeText(Dialog_Activity.this, "Starting update dictionary, it will take few seconds. please wait!!", Toast.LENGTH_SHORT).show();
			key.setFocusable(false);
			// Displays the progress bar for the first time.
			mBuilder.setProgress(0, 0, true);
			mNotifyManager.notify(id, mBuilder.build());

			// instantiate it within the onCreate method90
			// take CPU lock to prevent CPU from going off if the user
			// presses the power button during download
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					getClass().getName());
			mWakeLock.acquire();

			mProgressDialog = new ProgressDialog(Dialog_Activity.this);

			mProgressDialog.setMessage("Inserting...\nThis may take several minnutes, please wait!");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);


		}



	/*	@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			// if we get here, length is known, now set indeterminate to false
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgress(progress[0]);
		}*/

		@Override
		protected Integer doInBackground(Void... params) {


				// Sets the progress indicator completion percentage	
			if(Isalive == false)
				return 0;
			try {
				dbase.Insert_Dictionary("E_V.txt");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}

		/*	try {
				deleteFile(Path,Filename+".zip");
				deleteFile(Path,"E_V.txt");
				Log.d("Check: ", "File deleted!");
			} catch (FileNotFoundException e) {
				//e.printStackTrace();
				Log.d("Check: ", "Not found!");
			}*/

			return null;
		}

		@SuppressLint("NewApi") @Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(Isalive == false) {
				mWakeLock.release();
				mProgressDialog.dismiss();
				mBuilder.setContentText("Update was not successful")
						// Removes the progress bar
						.setProgress(0, 0, false);
				mNotifyManager.notify(id, mBuilder.build());
				key.setFocusableInTouchMode(true);
				Toast.makeText(Dialog_Activity.this, "Update was incompleted!", Toast.LENGTH_SHORT).show();
				return;
			}
			mBuilder.setContentText("completed")
			// Removes the progress bar
			.setProgress(0, 0, false);
			mNotifyManager.notify(id, mBuilder.build());
			key.setFocusableInTouchMode(true);
			Toast.makeText(Dialog_Activity.this, "Update completed!", Toast.LENGTH_SHORT).show();
			mWakeLock.release();
			mProgressDialog.dismiss();

			Log.d("Check: ", "Dismiss!!!");



		}
	}
	
	/*public class Insert_Dictionary implements Runnable{
		Sqlite dbase;
		
		Insert_Dictionary(Sqlite dbase)
		{
			this.dbase=dbase;
			new Thread(this,"Insertion").start();
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			dbase.Insert_Dictionary("E_V.txt");
			Log.d(TAG,"Insert successfully!");
		}
		
	}*/
	
	public boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	
	public void AppendList(ArrayList<String> str)
    {
        //setListAdapter(new bsAdapter(this));
		//setListAdapter(new MyAdapter(this));
		//MyListView newlv = (MyListView) findViewById(R.id.listview);
		adapter = new MyAdapter(this,array_sort);
		//simdynamic.onUpdate(0);
		lv.setSelection(0);
		lv.setAdapter(adapter);

    }
     

	
    public static class TextViewHtmlTagHandler implements TagHandler
    {
    /**
     * Keeps track of lists (ol, ul). On bottom of Stack is the outermost list
     * and on top of Stack is the most nested list
     */
    Stack<String>                   lists          =new Stack<String>();
    /**
     * Tracks indexes of ordered lists so that after a nested list ends
     * we can continue with correct index of outer list
     */
    Stack<Integer>                  olNextIndex    =new Stack<Integer>();
    /**
     * List indentation in pixels. Nested lists use multiple of this.
     */
    private static final int        indent         =10;
    private static final int        listItemIndent =indent*2;
    private static final BulletSpan bullet         =new BulletSpan(indent);

    @Override
    public void handleTag(final boolean opening,final String tag,final Editable output,final XMLReader xmlReader)
      {
      if(tag.equalsIgnoreCase("ul"))
        {
        if(opening)
          lists.push(tag);
        else lists.pop();
        }
      else if(tag.equalsIgnoreCase("ol"))
        {
        if(opening)
          {
          lists.push(tag);
          olNextIndex.push(Integer.valueOf(1)).toString();// TODO: add support for lists starting other index than 1
          }
        else
          {
          lists.pop();
          olNextIndex.pop().toString();
          }
        }
      else if(tag.equalsIgnoreCase("li"))
        {
        if(opening)
          {
          if(output.length()>0&&output.charAt(output.length()-1)!='\n')
            output.append("\n");
          final String parentList=lists.peek();
          if(parentList.equalsIgnoreCase("ol"))
            {
            start(output,new Ol());
            output.append(olNextIndex.peek().toString()+". ");
            olNextIndex.push(Integer.valueOf(olNextIndex.pop().intValue()+1));
            }
          else if(parentList.equalsIgnoreCase("ul"))
            start(output,new Ul());
          }
        else if(lists.peek().equalsIgnoreCase("ul"))
          {
          if(output.charAt(output.length()-1)!='\n')
            output.append("\n");
          // Nested BulletSpans increases distance between bullet and text, so we must prevent it.
          int bulletMargin=indent;
          if(lists.size()>1)
            {
            bulletMargin=indent-bullet.getLeadingMargin(true);
            if(lists.size()>2)
              // This get's more complicated when we add a LeadingMarginSpan into the same line:
              // we have also counter it's effect to BulletSpan
              bulletMargin-=(lists.size()-2)*listItemIndent;
            }
          final BulletSpan newBullet=new BulletSpan(bulletMargin);
          end(output,Ul.class,new LeadingMarginSpan.Standard(listItemIndent*(lists.size()-1)),newBullet);
          }
        else if(lists.peek().equalsIgnoreCase("ol"))
          {
          if(output.charAt(output.length()-1)!='\n')
            output.append("\n");
          int numberMargin=listItemIndent*(lists.size()-1);
          if(lists.size()>2)
            // Same as in ordered lists: counter the effect of nested Spans
            numberMargin-=(lists.size()-2)*listItemIndent;
          end(output,Ol.class,new LeadingMarginSpan.Standard(numberMargin));
          }
        }
      else if(opening)
        Log.d("TagHandler","Found an unsupported tag "+tag);
      }

    private static void start(final Editable text,final Object mark)
      {
      final int len=text.length();
      text.setSpan(mark,len,len,Spanned.SPAN_MARK_MARK);
      }

    private static void end(final Editable text,final Class<?> kind,final Object... replaces)
      {
      final int len=text.length();
      final Object obj=getLast(text,kind);
      final int where=text.getSpanStart(obj);
      text.removeSpan(obj);
      if(where!=len)
        for(final Object replace : replaces)
          text.setSpan(replace,where,len,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      return;
      }

    private static Object getLast(final Spanned text,final Class<?> kind)
      {
      /*
       * This knows that the last returned object from getSpans()
       * will be the most recently added.
       */
      final Object[] objs=text.getSpans(0,text.length(),kind);
      if(objs.length==0)
        return null;
      return objs[objs.length-1];
      }

    private static class Ul
      {
      }

    private static class Ol
      {
      }
    }


	public boolean unpackZip(String path, String zipname)
	{
		InputStream is;
		ZipInputStream zis;
		try
		{
			String filename;
			is = new FileInputStream(path + zipname);
			zis = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry ze;
			byte[] buffer = new byte[65536];
			int count;

			while ((ze = zis.getNextEntry()) != null)
			{
				// zapis do souboru
				filename = ze.getName();

				// Need to create directories if not exists, or
				// it will generate an Exception...
				if (ze.isDirectory()) {
					File fmd = new File(path + filename);
					fmd.mkdirs();
					continue;
				}

				FileOutputStream fout = new FileOutputStream(path + filename);
				int total=0;
				// cteni zipu a zapis
				count = zis.read(buffer);
				while ((count != -1))
				{
					fout.write(buffer, 0, count);
					//Log.d("Check1: ",String.valueOf(count));
					count = zis.read(buffer);
					total=total+count;
					Log.d("Check1: ",String.valueOf(total));
				}

				fout.close();
				zis.closeEntry();
			}

			zis.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}


	public void deleteFile(String inputPath, String inputFile) throws FileNotFoundException {
		try {
			// delete the original file
			new File(inputPath + inputFile).delete();


		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		}
	}




	private static class MyAdapter extends ArrayAdapter<String> {

		/** Re-usable contact image drawable */
		//private final Drawable contactImage;

		/**
		 * Constructor
		 *
		 * @param context The context
		 *
		 */
		public MyAdapter(final Context context,ArrayList<String> array) {
			super(context, 0,array);
		//	contactImage = context.getResources().getDrawable(R.drawable.images);
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			Log.d("Check: ","getView called");
			View view = convertView;
			if (view == null) {
				view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
			}

            final TextView name = (TextView)view.findViewById(R.id.title);
          	name.setText(array_sort.get(position));

        //    final TextView number = (TextView)view.findViewById(R.id.contact_number);
        //    number.setText(getItem(position).mNumber);

		//	final ImageView photo = (ImageView)view.findViewById(R.id.contact_photo);
		//	photo.setImageDrawable(contactImage);

			return view;
		}
	}

	/**
	 * A very simple dynamics implementation with spring-like behavior
	 */
	class SimpleDynamics extends Dynamics {

		/** The friction factor */
		private float mFrictionFactor;

		/** The snap to factor */
		private float mSnapToFactor;

		/**
		 * Creates a SimpleDynamics object
		 *
		 * @param frictionFactor The friction factor. Should be between 0 and 1.
		 *            A higher number means a slower dissipating speed.
		 * @param snapToFactor The snap to factor. Should be between 0 and 1. A
		 *            higher number means a stronger snap.
		 */
		public SimpleDynamics(final float frictionFactor, final float snapToFactor) {
			mFrictionFactor = frictionFactor;
			mSnapToFactor = snapToFactor;
		}

		@Override
		protected void onUpdate(final int dt) {
			// update the velocity based on how far we are from the snap point
			mVelocity += getDistanceToLimit() * mSnapToFactor;

			// then update the position based on the current velocity
			mPosition += mVelocity * dt / 1000;

			// and finally, apply some friction to slow it down
			mVelocity *= mFrictionFactor;
		}
	}



	@Override
	protected void onResume() {
		super.onResume();
		Log.d("Check1: ", "It has been started again!!!");
		Isdisplayed = true;
	}

	@Override
	protected void onPause() {
		super.onPause();

		Log.d("Check1: ", "It has been paused!!!");
		Isdisplayed = false;
	}

}

