package com.jeevan.tdt4260;



import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import android.os.Vibrator;

import io.socket.SocketIOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.jeevan.tdt4260.R;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class DrawActivity extends Activity {
	
	
	static DrawView m;
	static Handler handler, Intenthandler = null;
	Dialog dialog;
	boolean pause = false;

	
	public void guessword (View v) {
		EditText mybox = (EditText)findViewById(R.id.widget39);
		DrawView.SendWord(mybox.getText().toString());
	}
	
	
    public static void ReceiveCord (String inntekst) {
    	
    	Point point = new Point();
    	JSONObject minJson = null;
    	try {
			minJson = new JSONObject (inntekst);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try { 
			point.x = minJson.getLong("x");
			point.y = minJson.getLong("y");
			point.state= minJson.getString("type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.addpoint(point);
		m.postInvalidate();
    	}
	
    
    
    public static void ShowWinner (String inntekst) {
    	
		Message msg = handler.obtainMessage();
		Bundle b = new Bundle();
		b.putString("chat", inntekst);
		msg.setData(b);
		handler.sendMessage(msg);
	
    	}
    
    
    
    public static void StartDraw (String inntekst) {
    	 
    	
		Message msg = handler.obtainMessage();
		Bundle b = new Bundle();
		b.putString("chat", "THE WORD YOU MUST DRAW IS : " +inntekst);
		msg.setData(b);
		handler.sendMessage(msg);
    	  
    	
		Message msg2 = Intenthandler.obtainMessage();
		Bundle b2 = new Bundle();
		b2.putString("chat", "start");
		msg2.setData(b2);
		Intenthandler.sendMessage(msg2);
		m.points.clear();
		m.postInvalidate();
	
    	}
    
    
     
    public static void SetupChat (String inntekst) {
    	
		Message msg = Intenthandler.obtainMessage();
		Bundle b = new Bundle();
		b.putString("chat", inntekst);
		msg.setData(b);
		Intenthandler.sendMessage(msg);
		m.points.clear();
		m.postInvalidate();
	
    	}
    
	static IOCallback callback = new IOCallback() {
			
			@Override
			public void onMessage(JSONObject json, IOAcknowledge ack) {
				
			 

			}
		@Override
			public void onMessage(String data, IOAcknowledge ack) {
		
			
		
			}
			
			@Override
			public void onError(SocketIOException socketIOException) {
				System.out.println("Error!!!!!!!");
				socketIOException.printStackTrace();
				
			}
			
			@Override
			public void onDisconnect() {
				
			
				
			} 
			
			@Override
			public void onConnect() {
			
				
			}
			 
			@Override
			public void on(String event, IOAcknowledge ack, Object... args) {
				
				if (event.equals("draw")){
					 for (Object arg : args)
					 ReceiveCord(arg.toString());
						}
				
				if (event.equals("winner")){
					 for (Object arg : args)
						ShowWinner(arg.toString());
						}
				
				
//				if (event.equals("newround")){
//					 for (Object arg : args)
//					 SetupChat(arg.toString());
//						}
//				
//				
//				if (event.equals("newword")){
//					 for (Object arg : args)
//					 StartDraw(arg.toString());
//						}
//				
				
				}
			};
	
			
			
		
	@Override
	public void onPause()
	{
	   dialog.dismiss();
	   pause = true;
	   super.onPause();

	}
	
	@Override
	public void onResume()
	{
	pause = false;
	    super.onResume();
	}


	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    	//we add the Drawingboard to the screenview.
        m = new DrawView(this);
   	 	super.onCreate(savedInstanceState);
        setContentView(m);
     
        
        
//       Intent explicitIntent = new Intent(this, noenytt.class);
//       startActivity(explicitIntent);

    	 dialog = new Dialog(this,R.style.Theme_Transparent);
    	
    	
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	      WindowManager.LayoutParams wlp = this.getWindow().getAttributes();
	      wlp.gravity= Gravity.BOTTOM;
	      dialog.getWindow().setAttributes(wlp);
	      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	      
		//dialog.setTitle("This is my custom dialog box");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mydialog);
        Button dialog_btn = (Button) dialog.findViewById(R.id.widget37);
        final EditText mybox = (EditText)dialog.findViewById(R.id.widget39);
        dialog_btn.setOnClickListener(new View.OnClickListener() 
        {

			@Override
			public void onClick(View v) {
				
				DrawView.SendWord(mybox.getText().toString());
				
			}
            // Perform button logic
        });
 

       // dialog.show();
        
        
        
        handler = new Handler() {
        	public void handleMessage(Message msg) {
        		Toast.makeText(getApplicationContext(),msg.getData().getString("chat"), 
                        Toast.LENGTH_LONG).show();
     
        	
        	}
        	
        	};
        	
        	
        	
            Intenthandler = new Handler() {
            	public void handleMessage(Message msg) {
            		
            		if (msg.getData().getString("chat").equals("start") && pause==false) {
            		
            		dialog.hide();
            	    Vibrator v = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            	    v.vibrate(300); 
            		   
            		} 
            		else {
            		if (pause==false)
              	      dialog.show(); 
            			
            		}
            		
            	
            	}
            	
            	};
        	
        
        // createChooser is a convenience method to create
        // an Chooser Intent with a Title
    //    startActivity(Intent.createChooser(sharingIntent,"Share this using"));
        
        //we establish a websocket connection to our server
        try {   
        	DrawView.socket = new SocketIO();
        	DrawView.socket.connect("http://173.246.41.243:80", callback);
        	DrawView.socket.emit("adduser", "Jekyll");
        	DrawView.socket.emit("joinroom", "room1");
        
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}