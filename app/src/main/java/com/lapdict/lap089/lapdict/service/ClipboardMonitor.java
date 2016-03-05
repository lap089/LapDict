package com.lapdict.lap089.lapdict.service;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.FileObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lapdict.lap089.lapdict.Dialog_Activity;
import com.lapdict.lap089.lapdict.LogTag;
import com.lapdict.lap089.lapdict.NotificationPanel;
import com.lapdict.lap089.lapdict.R;
import com.lapdict.lap089.lapdict.Sqlite;

import org.xml.sax.XMLReader;

@SuppressWarnings("deprecation")
public class ClipboardMonitor extends Service implements LogTag {

	public static boolean Signal = true;
  
   private NotificationManager mNM;
   private Sqlite dbase;
    private MonitorTask mTask = new MonitorTask();
    private ClipboardManager mCM;
    private TextView dialog;
    private SharedPreferences mPrefs;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private String temp="NULL";
   
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    
    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    //    showNotification();
        mCM = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
       mPrefs= getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        
        mTask.start();
    }

  /*  private void showNotification() {
        Notification notif = new Notification(R.drawable.ic_launcher,"Dictionary monitor is started",System.currentTimeMillis());
        notif.flags |= (Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR | Notification.FLAG_FOREGROUND_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Dialog_Activity.class), 0);
        notif.setLatestEventInfo(this, getText(R.string.clip_monitor_service),
                "Press here to search new words", contentIntent);
        // Use layout id because it's unique
        startForeground(R.string.clip_monitor_service, notif);
     //   mNM.notify(R.string.clip_monitor_service, notif);
    }*/
    
    @Override
    public void onDestroy() {
        mNM.cancel(R.string.clip_monitor_service);
        if(DictHead.signal_head)
        {
            DictHead.signal_head = false;
            stopService(new Intent(this,DictHead.class));
        }

        Dialog_Activity.Isservicerunning = false;
        if(Dialog_Activity.mPanel != null)
            Dialog_Activity.mPanel.notificationCancel();
        Dialog_Activity.mPanel = new NotificationPanel(getApplication());
        mTask.cancel();
        
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
    }

    /**
     * Monitor task: monitor new text clips in global system clipboard and
     * new image clips in browser download directory
     */
    public class MonitorTask extends Thread {

        private volatile boolean mKeepRunning = false;
        public String mOldClip = null;
        public MonitorTask() {
        	super("ClipboardMonitor");
        }

       // /** Cancel task */
        public void cancel() {
            mKeepRunning = false;
            interrupt();
        }
        
       
        
        @Override
        public void run() {
          ///  mKeepRunning = true;
           
            while (true) {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                doTask();
               
           //     if (!mKeepRunning) {
             //       break;
             //   }
            }
          
        }
        
        private void doTask() {
            if (mCM.hasText()) {
            	
            	String newClip = mCM.getText().toString().trim();
                mOldClip = mPrefs.getString("Key", "Has been null");
                if (!newClip.equals(mOldClip) && Signal == true ) {
                	newClip=newClip.trim();
                	if(newClip.length()>20 || newClip.length()==0)
                		return;
                	
                    Log.i(TAG, "detect new text clip: " + newClip.toString());
                    Log.i(TAG,"Text: " + newClip.toString());
                 //  Toast.makeText(getApplicationContext(),"Start searching !" , Toast.LENGTH_LONG).show();
                    
                    Editor editor = mPrefs.edit();
                    editor.putString("Key",newClip);
                    editor.commit();
                    
                    new Dictionary_Search().execute(newClip);
                    
                   // Log.i(TAG, "new text clip inserted: " + newClip.toString());
                   /* Log.i(TAG, "Starting handler!!");
                    Handler h1 = new Handler(ClipboardMonitor.this.getMainLooper());
    				h1.post(new Runnable() {
    				    @Override
    				    public void run() {
    				    	
    				    Toast.makeText(ClipboardMonitor.this,"Text: "+ newClip.toString(), Toast.LENGTH_SHORT).show();
    				    dbase = new Sqlite(ClipboardMonitor.this);
    				//    dbase.Insert_Dictionary();
    				   // Toast.makeText(ClipboardMonitor.this,"Insert done!!!!!", Toast.LENGTH_SHORT).show();
    				    
    				    Log.i(TAG,"Start searching!");
    				    //String[] a = {newClip.toString()};
    				        String temp="NULL";
    				    //temp= dbase.Querry(a);
    				        Toast.makeText(ClipboardMonitor.this,temp, Toast.LENGTH_LONG).show();
    				        Log.i(TAG,"Search done!!");
    				        
    				        Editor editor = mPrefs.edit();
    	                    editor.putString("Value",temp);
    	                    editor.commit();
    				        
    				        //Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "Hello world!!!");
    						//ClipboardMonitor.this.sendBroadcast(i);
    				        Toast.makeText(ClipboardMonitor.this,"Starting open dialog!!!", Toast.LENGTH_SHORT).show();
    				        
    				        Intent inten =new Intent(ClipboardMonitor.this, Dialog_Activity.class);
    				        inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				        inten.putExtra("Key",newClip.toString());
    				        
    				        if(temp!=null)
    				        inten.putExtra("Value",temp);
    				        
    				        startActivity(inten);
    				      //  Toast.makeText(ClipboardMonitor.this,"Has sent!!!", Toast.LENGTH_LONG).show();
    				        
    				        
    				     
    				    					}
    									});*/
    								
                				}
                
            }
            
            
        }
        
        
        private class Dictionary_Search extends AsyncTask<String, Void, String> {
        	String key;
        	
        	 @Override
        	    protected void onPreExecute() {
        		 dbase = new Sqlite(getApplicationContext());
     	     //   Toast.makeText(getApplicationContext(),"Start searching !" , Toast.LENGTH_LONG).show();
        	        super.onPreExecute();
        	    }
        	
    		@Override
    		protected String doInBackground(String... urls) {
    			key = urls[0];
    			 Log.i(TAG,"Start searching!");
    			// if(key.trim().length()==0)
    			//	 return null;
				 //   String[] a = {key};        
				    temp= dbase.Querry(key);
				     //   Toast.makeText(ClipboardMonitor.this,temp, Toast.LENGTH_LONG).show();
				        Log.i(TAG,"Search done!!");
				        	
    			
    			return temp;
    		}

    		@Override
    		protected void onPostExecute(String result) {
    			//Toast.makeText(getApplicationContext(),"Starting open dialog!!!", Toast.LENGTH_SHORT).show();
    			if(result == "Not found!"){
    				Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
    				return;
    			}
    			
    			Editor editor = mPrefs.edit();
                editor.putString("Value", result);
                editor.commit();

                if(Dialog_Activity.Isdisplayed == true)
                {
                    Dialog_Activity.key.setText("");
                    Dialog_Activity.value.setText(Html.fromHtml(result, null, new TextViewHtmlTagHandler()));
                    Log.d("Check: ","Search itself!");
                    return;
                }
		        Intent inten =new Intent(getApplicationContext(), Dialog_Activity.class);
		        
		      //  inten.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		      inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        inten.putExtra("Key", key);
		        
    			
		        if(result!=null)
		        inten.putExtra("Value",result);
		        
		        startActivity(inten);
    			
    			
    		}
    	}



        
        
        /**
         * Monitor change of download directory of browser. It listens two
         * events: <tt>CREATE</tt> and <tt>CLOSE_WRITE</tt>. <tt>CREATE</tt>
         * event occurs when new file created in download directory. If this
         * file is image, new image clip will be inserted into database when 
         * receiving <tt>CLOSE_WRITE</tt> event, meaning file is sucessfully
         * downloaded.
         */
       
        /*public class MyLoadView extends View {

            private Paint mPaint;

            public MyLoadView(Context context) {
                super(context);
                mPaint = new Paint();
                mPaint.setTextSize(30);
                mPaint.setARGB(200, 200, 200, 200);
            }

            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                canvas.drawText("test test test", 0, 100, mPaint);
            }

            @Override
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
            }

            @Override
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
            }

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }*/
        }


    public static class TextViewHtmlTagHandler implements Html.TagHandler
    {
        /**
         * Keeps track of lists (ol, ul). On bottom of Stack is the outermost list
         * and on top of Stack is the most nested list
         */
        Stack<String> lists          =new Stack<String>();
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
            text.setSpan(mark, len, len, Spanned.SPAN_MARK_MARK);
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

    
    }



