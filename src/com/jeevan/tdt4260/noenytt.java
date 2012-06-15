package com.jeevan.tdt4260;



import com.jeevan.tdt4260.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class noenytt extends Activity {
	

	
	@Override
	public void onBackPressed() {
	   return;
	}

	
	public void guessword (View v) {
		EditText mybox = (EditText)findViewById(R.id.widget39);
		DrawView.SendWord(mybox.getText().toString());
	}

	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        //Remove title bar
		      this.requestWindowFeature(Window.FEATURE_NO_TITLE);	      
		      //Remove notification bar
		      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		      WindowManager.LayoutParams wlp = this.getWindow().getAttributes();
		      wlp.gravity= Gravity.BOTTOM;
		      this.getWindow().setAttributes(wlp);
		     
	       super.onCreate(savedInstanceState);
	      setContentView(R.layout.mydialog);
	     getWindow().setBackgroundDrawableResource(R.drawable.ic_launcher);
	     
         		
            	
       
	     


	 
	        }
	
}
