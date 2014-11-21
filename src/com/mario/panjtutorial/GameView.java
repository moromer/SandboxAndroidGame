package com.mario.panjtutorial;

import java.util.Random;

import com.mario.elements.Lives;
import com.mario.elements.TimeBar;
import com.mario.lists.SpriteList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {

	private SpriteList sprites = new SpriteList(12);

	private SurfaceHolder surfaceHolder;

	private boolean createSprites = true;

	private GameLoopThread theGameLoopThread;
	private long lastClick;

	private int currentColorNum; // number assignment to the corresponding color {0,1,2,3}
	
	private Paint paintRed, paintBlue, paintGreen, paintYellow;

	private int score;
	private int correctClicks = 0;
	
	private Lives lives = null;
	
	private Random rnd;
	
	private float densitiy;
	
	
	private long time;
	private TimeBar timeBar;
	
	private boolean addedNewSprite = false;
	
	@SuppressLint("WrongCall")
	public GameView(Context context) {
		super(context);
		
		setColors();

		rnd = new Random(System.currentTimeMillis());
		currentColorNum = rnd.nextInt(4);

		surfaceHolder     = getHolder();
		theGameLoopThread = new GameLoopThread(this);
		
		time = System.currentTimeMillis();
		

		surfaceHolder.addCallback(new SurfaceHolder.Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
				boolean retry = true;
				theGameLoopThread.setRunning(false);
				while (retry) {
					try {
						theGameLoopThread.join();
						retry = false;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}

			public void surfaceCreated(SurfaceHolder holder) {
				theGameLoopThread.setRunning(true);
				theGameLoopThread.start();
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {

			}

		});
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.DKGRAY);
		
		if(lives == null) {
			lives = new Lives(getResources(), 3, getDensity());
			timeBar = new TimeBar(getWidth());
		}
		
		if (createSprites) {
			sprites.initialSprite(getResources(), this);
			createSprites = false;
		}
		
		if(correctClicks > 0 && addedNewSprite == false && sprites.getCount() < 40 && correctClicks % 5 == 0) {
			theGameLoopThread.setFPS(theGameLoopThread.getFPS()-10);
			sprites.rndCreateSprite(getResources(), this);
			sprites.rndCreateSprite(getResources(), this);
			addedNewSprite = true;
		}

		
		if(lives.hasLives()) {
			sprites.drawSprites(canvas);

			
		} else {
			writeGameOverScreen(canvas);
			timeBar.setWidth(0);
			timeBar.drawTimeBar(canvas, getDensity());
			theGameLoopThread.setRunning(false);
		}
		
		if(System.currentTimeMillis() - time >= timeBar.timeToModify){
			timeBar.degreeseWidth();
			timeBar.drawTimeBar(canvas, getDensity());
			time = System.currentTimeMillis();
		}
		
		if(timeBar.timeOver()) {
			changeColor();
			timeBar.setWidth(getWidth());
			lives.removeLive();
		}
		
		switch (currentColorNum) {
		case 0:
			drawLines(paintBlue, canvas);
			break;
		case 1:
			drawLines(paintRed, canvas);
			break;
		case 2:
			drawLines(paintGreen, canvas);
			break;
		case 3:
			drawLines(paintYellow, canvas);
			break;
		}
		writeScore(canvas);
		
		lives.drawLives(canvas, getDensity());
		
	}
	
	private void writeScore(Canvas canvas) {
		
		String ScoreString;
		final int fontSize = (int)(25 * getDensity());
		Typeface font = Typeface.create("Arial", Typeface.NORMAL);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTypeface(font);
		paint.setTextSize(fontSize);
		paint.setAntiAlias(true);
		ScoreString = "Score: " + String.valueOf(score);

		int x = (int)(15 * getDensity());
		canvas.drawText(ScoreString, x, 40, paint);
	}
	
	private void writeGameOverScreen(Canvas canvas) {
		String gameOverString = "GAME OVER!";
		final int fontSize = (int) (30 * getDensity());
		Typeface font = Typeface.create("Arial", Typeface.BOLD);
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTypeface(font);
		paint.setTextSize(fontSize);
		paint.setAntiAlias(true);
		paint.setTextAlign(Paint.Align.CENTER);
		
		int x = (canvas.getWidth() / 2) - (int)(paint.getTextSize()/2);
		int y = (canvas.getHeight()/2) - (int)(paint.getTextSize()/2);
		canvas.drawText(gameOverString, x, y, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		synchronized (getHolder()) {
			if (System.currentTimeMillis() - lastClick > 300) {
				System.out.println(sprites.getCount());

				for (int i = sprites.getCount() - 1; i >= 0; i--) {
					Sprite sprite = sprites.get(i);
					System.out.println(sprite);
					
					if (sprite.isTouched(event.getX(), event.getY())) {
						System.out.println("spriteColornum: "+ sprites.getCurrentColorNum(i) + "currentcolorNum: "+ currentColorNum);
						if(sprites.getCurrentColorNum(i) == currentColorNum) {
							score += 5;
							correctClicks++;
							timeBar.reset();
							addedNewSprite = false;
						} else {
							lives.removeLive();
							score -=10;
							timeBar.reset();
							addedNewSprite = false;
						}
						
						if(score < 0) {
							score = 0;
						}
						
						if(correctClicks != 0 && (correctClicks % 10) == 0 && !lives.reachedMaxLives()){
							correctClicks = 0;
							lives.addLive();
						}
						sprites.removeSprite(i);
						sprites.rndCreateSprite(getResources(), this);
						changeColor();
						lastClick = System.currentTimeMillis();
						break;
					}
				}
			}
		}
		return true;
	}

	public void setColors() {
		Paint paintRed = new Paint();
		paintRed.setARGB(255, 236, 37, 36);
		this.paintRed = paintRed;

		Paint paintBlue = new Paint();
		paintBlue.setARGB(255, 36, 72, 204);
		this.paintBlue = paintBlue;

		Paint paintYellow = new Paint();
		paintYellow.setARGB(255, 255, 242, 0);
		this.paintYellow = paintYellow;

		Paint paintGreen = new Paint();
		paintGreen.setARGB(255, 34, 177, 76);
		this.paintGreen = paintGreen;
	}

	public void drawLines(Paint lineColor, Canvas canvas) {
		int lineWidth = 10;
		int screenHeight = getHeight();
		int screenWidth = getWidth();

		canvas.drawRect(0, 0, lineWidth, getHeight(), lineColor);
		canvas.drawRect(0, getHeight() - lineWidth, screenWidth, screenHeight,
				lineColor);
		canvas.drawRect(screenWidth - lineWidth, 0, screenWidth, screenHeight,
				lineColor);
	}

	public void changeColor() {
		int index = rnd.nextInt(sprites.getCount());
		this.currentColorNum = sprites.getCurrentColorNum(index);
	}
	
	public float getDensity() {
		densitiy = getResources().getDisplayMetrics().density;
		return densitiy;
	}
}
