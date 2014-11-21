package com.mario.elements;

import com.mario.panjtutorial.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class Lives {
	
	private int lives;
	private Bitmap picture;
	private Paint paint;
	private float density;
	
	public static final int MAXLIVES = 10;
	

	public Lives(Resources res, int livePoints, float density) {
		try {
			this.lives = livePoints;
			this.density = density;
			picture = BitmapFactory.decodeResource(res,R.drawable.lives1);
			
			final int fontSize = (int) (30 * density);
			Typeface font = Typeface.create("Arial", Typeface.NORMAL);
			paint.setTypeface(font);
			paint.setTextSize(fontSize);
			paint.setAntiAlias(true);
		} catch (Exception e) {
			System.out.println("Could not initial Live Object with values: (" + res + "|" + livePoints +"|" + density + ")");
			e.printStackTrace();
		}
	}
	
	public void drawLives(Canvas canvas, float densitiy){
		
		int yHeart = (int)(picture.getHeight() * densitiy);
		
		for(int i = 1; i <= lives; i++) {
			int xHeart = canvas.getWidth() - (int)(12 * density) - i * (int)(picture.getWidth() * density + (1 * i));
			canvas.drawBitmap(picture ,xHeart, yHeart, paint);		
		}	
	}
	
	public void addLive() {
		lives++;
	}
	
	public void removeLive() {
		lives--;
	}
	
	public boolean hasLives() {
		return (lives > 0);
	}
	
	public boolean reachedMaxLives(){
		return (lives >= MAXLIVES);
	}
}

