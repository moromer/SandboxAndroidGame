package com.mario.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.mario.panjtutorial.GameView;
import com.mario.panjtutorial.R;
import com.mario.panjtutorial.Sprite;

public class SpriteList {
	private List<Sprite> spriteList;
	private List<Integer> spriteListNum;
	private int initialCount;
	
	
	public SpriteList(int count) {
		this.initialCount = count;
		spriteList = new ArrayList<Sprite>();
		spriteListNum = new ArrayList<Integer>();
	}
	
	public void initialSprite(Resources res, GameView view) {
		int spriteCount = (int) (initialCount/3);
		for (int i = 0; i < spriteCount; i++) {
			for (int j = 0; j < 3; j++) {
				createSprite(res, i, view);
			}
		}
	}
	
	private void createSprite(Resources res,  int index, GameView view) {
		Bitmap bmp = null;

		switch (index) {
		case 0:
			bmp = BitmapFactory.decodeResource(res,
					R.drawable.alienspriteblue);
			break;
		case 1:
			bmp = BitmapFactory.decodeResource(res,
					R.drawable.alienspritered);
			break;
		case 2:
			bmp = BitmapFactory.decodeResource(res,
					R.drawable.alienspritegreen);
			break;
		case 3:
			bmp = BitmapFactory.decodeResource(res,
					R.drawable.alienspriteyellow);
			break;
		}

		Sprite sprite = new Sprite(view, bmp);
		spriteList.add(sprite);
		spriteListNum.add(index);
	}
	
	public void rndCreateSprite(Resources res, GameView view) {
		Random rnd = new Random(System.currentTimeMillis());
		int i = rnd.nextInt(4);
		createSprite(res, i, view);
	}
	
	public int getCount() {
		if(spriteListNum != null) {
			return spriteListNum.size();
		} 
		return 0;
	}
	
	public void drawSprites(Canvas canvas) {
		for (Sprite sprite : spriteList) {
			try {
				sprite.draw(canvas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void removeSprite(int index) {
		System.out.println(index);
		spriteList.remove(index);
		spriteListNum.remove(index);
	}
	
	public Sprite get(int index) {
		return spriteList.get(index);
	}
	
	public int getCurrentColorNum(int index) {
		return spriteListNum.get(index);
	}

}
