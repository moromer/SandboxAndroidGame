package com.mario.elements;

import android.graphics.Canvas;
import android.graphics.Paint;

public class TimeBar {
	private Paint paint;
	private long restWidth;
	private long maxWidth;
	public int timeToModify;
	private static final int FPS = 100;
	
	public TimeBar(long maxWidth) {
		paint = new Paint();
		paint.setARGB(255, 51, 0, 25);
		
		restWidth = maxWidth;
		this.maxWidth  = maxWidth;
		
		timeToModify = (int)(FPS/60);
	}
	
	public void setWidth(long width) {
		restWidth = width;
	}
	
	public void reset() {
		restWidth = maxWidth;
	}
	
	public boolean timeOver(){
		return (restWidth <= 0);
	}
	
	public void drawTimeBar(Canvas canvas, float density) {
		canvas.drawRect(0, 0, restWidth, (int)(10 * density), paint);
		
	}
	
	public void degreeseWidth() {
		restWidth -= (int)maxWidth/(FPS * (int)(FPS/60));
	}
}
