package com.mario.panjtutorial;

import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

@SuppressLint("DrawAllocation")
public class Sprite {
	static final int BMP_COLUMNS = 4;
	static final int BMP_ROWS    = 4;
	private int[] DIRECTION_TO_SPRITE_SHEET = {2, 0, 3, 1};

	private int x;
	private int y;
	private int xSpeed;
	private int ySpeed;
	private int width;   //width of the image
	private int height;  //height of the image
	private Bitmap bmp;
	private GameView theGameView;
	private int currentFrame = 0;
	
	
	public Sprite(GameView theGameView, Bitmap bmp) {
		this.theGameView = theGameView;
		this.bmp         = bmp;		
		this.width       = bmp.getWidth() / BMP_COLUMNS;
		this.height      = bmp.getHeight() / BMP_ROWS;
		
		Random rnd = new Random();
		x = (int) (30 * theGameView.getDensity() + rnd.nextInt((int) (theGameView
                .getWidth() - width - 50 * theGameView.getDensity())));
        y = (int) (30 * theGameView.getDensity() + rnd.nextInt((int) (theGameView
                .getHeight() - height - 50 * theGameView.getDensity())));
        ySpeed = (int) ((rnd.nextInt(10) - 4)*theGameView.getDensity());
        xSpeed = (int) ((rnd.nextInt(10) - 4)*theGameView.getDensity());
	}
	
	//direction = 0 above, 1 left, 2 bottom, 3 right
	//sprite sheet = 2 above, 0 left, 3 bottom, 1 right
	private int getAnimationRow() {
		double directionDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
		int spriteDir = (int) Math.round(directionDouble) % BMP_ROWS;
		
		return DIRECTION_TO_SPRITE_SHEET[spriteDir];
	}
	
	
	private void bounceOff() {
		if (x + 10 *theGameView.getDensity() > theGameView.getWidth() - width - xSpeed || x + xSpeed < 10* theGameView.getDensity()) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        if (y + 10* theGameView.getDensity()> theGameView.getHeight() - height - ySpeed || y + ySpeed < 0) {
            ySpeed = -ySpeed;
        }
		y = y + ySpeed;
		
		currentFrame = ++currentFrame % BMP_COLUMNS;

	}
	
	public void draw(Canvas canvas) {
		bounceOff();
		int sourceX = currentFrame * width; 
		int sourceY = getAnimationRow() * height;
		
		Rect source = new Rect(sourceX, sourceY, sourceX + width, sourceY + height);
		Rect dest   = new Rect(x, y, x+width, y+height);
		
		canvas.drawBitmap(bmp, source, dest, null);
	}
	
	public boolean isTouched(float x2, float y2){
		return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
	}
	
}
