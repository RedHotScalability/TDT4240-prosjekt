package com.jeevan.tdt4260;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener {
	
  
	public static SocketIO socket;
	List<Point> points = new ArrayList<Point>();
	
	
	
    Paint paint = new Paint();
    Paint paint2 = new Paint();
    Path minpath = new Path();
    Path path = new Path();
   // static Handler handler = null;
    
    public static void SendWord(String myword)
    {    	
    socket.emit("guessword", myword);	
    }
   

		
 
    public DrawView(Context context) {
        super(context);
        setFocusable(true);
        
     
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        
        paint.setColor(Color.BLACK);
		
        paint2.setColor(Color.RED);
		paint.setStrokeWidth(5);

		paint.setStyle(Paint.Style.STROKE);
     
      
    }

    
    public void addpoint (Point p){
    	points.add(p);
    	
    }
   
    
    @Override
    public void onDraw(Canvas canvas) {
    	 path = new Path();
    
    	try {
    
    		
			for (Point point : points)  {
			   // canvas.drawCircle(point.x, point.y, 10, paint);
				if (point.state.equalsIgnoreCase("start")) {
				 	path.moveTo(point.x, point.y); //at
					canvas.drawCircle(point.x, point.y, 3, paint2);
			
				}
				path.lineTo(point.x, point.y);
			  		 
			}
		} catch (Exception e) {
			invalidate();
			e.printStackTrace();
			
		}
    	canvas.drawColor(Color.WHITE);
    	canvas.drawPath(path, paint);
}
    
    
    
 

    public boolean onTouch(View view, MotionEvent event) {
       
      
    	
    if (event.getAction() == event.ACTION_DOWN)
       {  
    	Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        point.state= "start";
        points.add(point); 
        invalidate();
          	try {
          		
    			socket.emit("drawClick", new JSONObject().put("x", event.getX()).put("y", event.getY()).put("type", "dragstart"));
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace(); 
    		}
           
    	   return true;
       }
    
    
    else if (event.getAction() == event.ACTION_UP)
    {  
 	Point point = new Point();
     point.x = event.getX();
     point.y = event.getY();
     point.state= "drag";
     points.add(point); 
     invalidate();
       	try {
 			socket.emit("drawClick", new JSONObject().put("x", event.getX()).put("y", event.getY()).put("type", "dragend"));
 		} catch (JSONException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace(); 
 		}
        
 	   return true;
    } 
    
    Point point = new Point();  
    point.x = event.getX();
    point.y = event.getY();
    point.state= "draw";
    points.add(point);
    invalidate();
    	try {
			socket.emit("drawClick", new JSONObject().put("x", event.getX()).put("y", event.getY()).put("type", "drag"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      // invalidate();   
        return true;
        
    }
   
}


class Point {
    float x, y;
    String state;
} 


