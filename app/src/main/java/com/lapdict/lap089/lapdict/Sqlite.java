package com.lapdict.lap089.lapdict;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class Sqlite extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "MyDBName.db";
	public static final String CONTACTS_TABLE_NAME = "Dictionary";
	public static final String CONTACTS_COLUMN_ID = "id";
	public static final String CONTACTS_COLUMN_NAME = "Key";
	public static final String CONTACTS_COLUMN_EMAIL = "Value";
	public Context context;
	public String Filename="lapdict";
	public String Path=Environment.getExternalStorageDirectory().toString() +"/";

	private SharedPreferences mPrefs;
	private static SQLiteDatabase ab;

	private HashMap hp;

	public Sqlite(Context context) {

		super(context, DATABASE_NAME, null, 1);
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL(
				"create table Dictionary " +
						"(id integer primary key autoincrement, Key text, Value text)"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS Dictionary");
		onCreate(db);
	}

	public boolean insertContact(String Key, String Value) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put("Key", Key);
		contentValues.put("Value", Value);

		db.insert("Dictionary", null, contentValues);
		return true;
	}

	public Cursor getData(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from Dictionary where id=" + id + "", null);
		return res;
	}

	public int numberOfRows() {
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
		return numRows;
	}

	public boolean updateContact(Integer id, String key, String value) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("Key", key);
		contentValues.put("Value", value);

		db.update("Dictionary", contentValues, "id = ? ", new String[]{Integer.toString(id)});
		return true;
	}

	public Integer deleteContact(Integer id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete("Dictionary",
				"id = ? ",
				new String[]{Integer.toString(id)});
	}


	 boolean isTableExists() {
		ab = this.getReadableDatabase();
		Cursor callInitCheck = ab.rawQuery("select count(*) from Dictionary", null);
		callInitCheck.moveToFirst();
		// ab.close();
		if (Integer.parseInt(callInitCheck.getString(0)) != 0) {
			ab.close();
				return true;
		}
		else return false;
	}


	public void Insert_Dictionary(String dict) throws InterruptedException, ExecutionException, TimeoutException {
		//ab = this.getReadableDatabase();

		//if (isTableExists(ab) == true)
		//	return;
		/*if(Looper.getMainLooper().getThread()==Thread.currentThread())
			Log.d("Check: ","UI thread");

		else Log.d("Check: ","Background thread");
		*/
// execute this when the downloader must be fired

		int i;
		String key, value;
		File file = new File(Path + "E_V.txt");
		//File file = new File("/Removable/MicroSD/LapDict/Dictionary/" + "E_V.txt");
		//  File file = new File(sdcard+"/Download/" + "test.txt");

		StringBuilder text = new StringBuilder();
		Log.d("Check: ", "Start transaction");
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			String sql = "INSERT INTO " + "Dictionary" + " VALUES (?,?,?);";
			SQLiteStatement statement = ab.compileStatement(sql);
			ab.beginTransaction();
	//		DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(ab, "Dictionary");
			try {
				ab.setLockingEnabled(false);
				int count = 0;
			//	char a, b;
				long time = System.currentTimeMillis();
				while ((line = br.readLine()) != null) {

					 //size = line.length();
					//for (i = 0; i < size - 1; ++i) {
					//	a = line.charAt(i);
					//	b = line.charAt(i + 1);
					//	if ((a == ' ' || a == '\t') && b == '<' || b == '=')
					//		break;
					//}

					i = line.indexOf("<");

					key = line.substring(0, i).trim();
					value = line.substring(i);
				//	value = value.replace("|n|", "\n");
					Log.d("Key: ", key);

				//	key="";
				//	value= "";
					statement.clearBindings();
					//statement.bindString(1, String.valueOf(count));
					statement.bindString(2, key);
					statement.bindString(3, value);

					// insertContact(key,value);

					statement.execute();
				/*	ih.prepareForInsert();
					ih.bind(1, String.valueOf(count));
					ih.bind(2, key);
					ih.bind(3, value);
					ih.execute();*/



					//++count;
					//Log.d("Key: ", String.valueOf(count));

				}
				ab.setTransactionSuccessful();// marks a commit
				long time1 = System.currentTimeMillis() - time;
				Log.d("Time: ", String.valueOf(time1));
			//	Dialog_Activity.key.setText(String.valueOf(time1));
			} finally {
				ab.endTransaction();
				ab.close();
			//	ih.close();
				ab.setLockingEnabled(true);

			}

			Log.d("value: ", "Insert done!!!!!!");
			br.close();
		} catch (IOException e) {
			Log.e("value: ", "error during insertion!!!");
			//You'll need to add proper error handling here
			//9884 -> 16566 ->  29981  ->  38639 E -> 45601 F -> 51950 G -> 56629 H  -> 61268 I
			// 67511 J  -> 68598 K 	-> 69681 L -> 73766 M  -> 79696 N	->  81905 o -> 86016 p -> 98667 q  ->  99429 r -> 106004 s -> 122392 t
			// 128656 u ->  133627 v 	-> w 135848	-> 138636 x -> 138699 y -> 138968 z -> 139200

		}

	}

	public ArrayList<String> Query_Array(String key) {
		ArrayList<String> array = new ArrayList<String>();

		Cursor cursor;
		//   if(key.trim().length()==0){
		//	   array.add("NULL");
		//	   return array;
		//   }
		String[] a = {key};
		cursor = Get_Cursor(a);


		if (cursor != null && cursor.moveToFirst()) {


			//while(cursor.isAfterLast()==false)
			for (int i = 0; i < 3 && cursor.isAfterLast() == false; ++i) {
				//temp = cursor.getString(1).toLowerCase();
				// if(array.size()<=5 && temp.contains(key+ " ")==true || temp.contains(key+"ed") || temp.contains(key+"-") || temp.contains(key+ "s") || temp.contains(key+ "es")){
				array.add(cursor.getString(2));
				cursor.moveToNext();
				//}
				//else return array;
			}
		}

		if (cursor != null && !cursor.isClosed())
			cursor.close();

		if (cursor == null) {
			array.add("NULL");
			return array;
		}

		return array;
	}


	public String Querry(String key) {
		Cursor cursor;
		//   if(key.trim().length()==0){
		//   return "NULL";
		//}
		String[] a = {key};
		cursor = Get_Cursor(a);

		if (cursor != null && cursor.moveToFirst()) {
			String value = cursor.getString(2);
			//return cursor.getString(2);
			if (!cursor.isClosed())
				cursor.close();
			return value;
		}
		else{
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		 return "Not found!";
			}
	}


	@SuppressLint("DefaultLocale")
	public Cursor Get_Cursor(String[] Key) {
		Cursor cursor;
		SQLiteDatabase db = this.getReadableDatabase();
		Key[0] = Key[0];
		char temp = Key[0].charAt(0);
		if (temp >= 65 && temp <= 90) {
			temp = (char) (temp + 32);
			Key[0] = Key[0].toLowerCase();
		}
		Key[0] = Key[0] + "%";

		switch (temp) {
			case 'a':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 0 AND 9884", Key);
				break;

			case 'b':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 9884 AND 16566", Key);
				break;


			case 'c':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 16566 AND  29981", Key);
				break;

			case 'd':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 29981  AND  38639", Key);
				break;

			case 'e':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 38639 AND 45601", Key);
				break;

			case 'f':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 45601 AND 51950", Key);
				break;

			case 'g':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 51950 AND 56629", Key);
				break;

			case 'h':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 56629 AND 61268", Key);
				break;

			case 'i':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 61268 AND 67511", Key);
				break;

			case 'j':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 67511 AND 68598", Key);
				break;

			case 'k':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 68598 AND 69681", Key);
				break;

			case 'l':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 69681 AND 73766", Key);
				break;

			case 'm':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 73766 AND 79696", Key);
				break;

			case 'n':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 79696 AND 81905", Key);
				break;

			case 'o':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 81905 AND 86016", Key);
				break;

			case 'p':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 86016 AND 98667", Key);
				break;

			case 'q':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 98667 AND 99429", Key);
				break;

			case 'r':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 99429 AND 106004", Key);
				break;

			case 's':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 106004 AND 122392", Key);
				break;

			case 't':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 122392 AND 128656", Key);
				break;

			case 'u':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 128656 AND 133627", Key);
				break;

			case 'v':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 133627 AND 135848", Key);
				break;

			case 'w':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 135848	AND 138636", Key);
				break;

			case 'x':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 138636 AND 138699", Key);
				break;

			case 'y':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 138699 AND 138968", Key);
				break;

			case 'z':
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ? AND id BETWEEN 138968 AND 139200", Key);
				break;
			default:
				cursor = db.rawQuery("SELECT * FROM Dictionary WHERE Key LIKE ?", Key);
				break;
		}

		if (cursor != null && cursor.moveToFirst()) {
	//	Log.d("Check: ",cursor.getString(0));
			db.close();
				return cursor;//return cursor.getString(2);
		}
		else {
			db.close();
			return null;
		}
	}

	public ArrayList<String> getArraywords(String key) {
		ArrayList<String> array = new ArrayList<String>();
		String[] a = {key};
		Cursor cursor = Get_Cursor(a);
		if (cursor != null && cursor.moveToFirst()) {
			for (int i = 0; i < 5 && !cursor.isAfterLast(); ++i) {
				array.add(cursor.getString(1));
				cursor.moveToNext();
			}
		} else
			array.add(key);

		if (cursor != null && !cursor.isClosed())
			cursor.close();

		return array;
	}


	public ArrayList getAllDictionary() {
		ArrayList array_list = new ArrayList();
		//hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from Dictionary", null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
			res.moveToNext();
		}

		if (res != null && !res.isClosed())
			res.close();
		db.close();
		return array_list;
	}







	/*private class DownloadTask extends AsyncTask<String, Integer, String> {

		private Context context;
		private PowerManager.WakeLock mWakeLock;

		public DownloadTask(Context context) {
			this.context = context;
		}

		@Override
		protected String doInBackground(String... sUrl) {
			if(Looper.getMainLooper().getThread()==Thread.currentThread())
				Log.d("Check: ","UI thread");

			else Log.d("Check: ","Background thread");

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
					return "Server returned HTTP " + connection.getResponseCode()
							+ " " + connection.getResponseMessage();
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
				byte data[] = new byte[4096];
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

			mWakeLock.release();
			mProgressDialog.dismiss();
			Log.d("Check: ", "Dismiss!!!");

			*//*boolean check = unpackZip(Path,Filename+".zip");
			if(check==true) {
				//	Toast.makeText(context, "unzip done!", Toast.LENGTH_SHORT).show();
				Log.d("Check: ", "unzip done!");
				Log.d("Check: ", "Start inserting dictionary!");


				int i;
				String key, value;
				File file = new File(Path + "E_V.txt");
				//File file = new File("/Removable/MicroSD/LapDict/Dictionary/" + "E_V.txt");
				//  File file = new File(sdcard+"/Download/" + "test.txt");

				StringBuilder text = new StringBuilder();

				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line;

					String sql = "INSERT INTO " + "Dictionary" + " VALUES (?,?,?);";
					SQLiteStatement statement = ab.compileStatement(sql);
					ab.beginTransaction();
					try {
						int count = 0;
						char a, b;
						while ((line = br.readLine()) != null) {
							int size = line.length();
							for (i = 0; i < size - 1; ++i) {
								a = line.charAt(i);
								b = line.charAt(i + 1);
								if ((a == ' ' || a == '\t') && b == '<' || b == '=')
									break;
							}
							key = line.substring(0, i);
							value = line.substring(i);
							value = value.replace("|n|", "\n");
							Log.d("Key: ", key);

							statement.clearBindings();
							statement.bindString(1, String.valueOf(count));
							statement.bindString(2, key);
							statement.bindString(3, value);

							// insertContact(key,value);

							statement.execute();
							++count;
						}
						ab.setTransactionSuccessful();// marks a commit
					} finally {
						ab.endTransaction();
					}

					Log.d("value: ", "Insert done!!!!!!");
					br.close();
				} catch (IOException e) {
					Log.e("value: ", "error during insertion!!!");
					//You'll need to add proper error handling here
					//9884 -> 16566 ->  29981  ->  38639 E -> 45601 F -> 51950 G -> 56629 H  -> 61268 I
					// 67511 J  -> 68598 K 	-> 69681 L -> 73766 M  -> 79696 N	->  81905 o -> 86016 p -> 98667 q  ->  99429 r -> 106004 s -> 122392 t
					// 128656 u ->  133627 v 	-> w 135848	-> 138636 x -> 138699 y -> 138968 z -> 139200

				}

			}
			else
				Log.d("Check: ","Unzip failed!!!");*//*
			return null;



		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// instantiate it within the onCreate method90


			// take CPU lock to prevent CPU from going off if the user
			// presses the power button during download
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					getClass().getName());
			mWakeLock.acquire();

		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			// if we get here, length is known, now set indeterminate to false
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			if(Looper.getMainLooper().getThread()==Thread.currentThread())
				Log.d("Check: ","UI thread");

			else Log.d("Check: ","Background thread");

			if (result != null)
				Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
			else {
				Toast.makeText(context, "Download done, start processing files!", Toast.LENGTH_SHORT).show();


				//	Toast.makeText(context, "unzip failed!", Toast.LENGTH_SHORT).show();

			}

			}
		}*/

	}

