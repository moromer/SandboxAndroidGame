package com.mario.panjtutorial;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameLoopThread extends Thread{
	private GameView theView;
	private boolean isRunning = false;
	private long FPS = 60;
	private long sleepTime;
	
	GameLoopThread(GameView theView) {
		this.theView = theView;
	}
	
	public void setRunning(boolean run) {
		isRunning = run;
	}
	
	@SuppressLint("WrongCall")
	@Override
	public void run(){
		long TPS = 2500/FPS;
		long startTime;
		
		while(isRunning) {
			Canvas theCanvas = null;
			startTime = System.currentTimeMillis();
			try {
				theCanvas = theView.getHolder().lockCanvas();
				synchronized (theView.getHolder()) {
					theView.onDraw(theCanvas);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				if(theCanvas != null) {
					theView.getHolder().unlockCanvasAndPost(theCanvas);
				}
			}
			sleepTime = TPS-(System.currentTimeMillis() - startTime);
			try {
				if(sleepTime > 0) {
					sleep(sleepTime);
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("error in sleep");
				e.printStackTrace();
			}
		}
	}
	
	public long getSleepTime(){
		return sleepTime;
	}
	
	public long getFPS(){
		return FPS;
	}
	
	public void setFPS(long FPS) {
		if(FPS > 0) {
			this.FPS = FPS;
		} else {
			this.FPS = 1;
		}
	}
}
