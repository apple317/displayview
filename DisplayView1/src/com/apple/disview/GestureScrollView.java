package com.apple.disview;



import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class GestureScrollView extends ScrollView {

	GestureDetector myGesture;

	public GestureScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public GestureScrollView(Context context, GestureDetector gest) {
		super(context);
		myGesture = gest;
	}

	public GestureScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(myGesture.onTouchEvent(ev))return true;
		else return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(myGesture.onTouchEvent(ev))return true;
		else return super.onTouchEvent(ev);
	}
	public void setGesture(GestureDetector gest){
		myGesture=gest;
	}
}
