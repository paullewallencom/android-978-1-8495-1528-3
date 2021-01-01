package com.packt.animation.bubbles;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BubblesView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder surfaceHolder;
	private GameLoop gameLoop;
	private LinkedList<Bubble> bubbles = new LinkedList<Bubble>();
	private Paint backgroundPaint = new Paint();
	private float BUBBLE_FREQUENCY = 0.3f;
	
	private class GameLoop extends Thread {
		private long msPerFrame = 1000/25;
		public boolean running = true;
		long frameTime = 0;
		@Override
		public void run() {
			Canvas canvas = null;
			final SurfaceHolder surfaceHolder = BubblesView.this.surfaceHolder;
			while (running) {
				try {
					canvas = surfaceHolder.lockCanvas();
					synchronized (surfaceHolder) {
						if (canvas != null) {
							drawScreen(canvas);
							calculateDisplay(canvas);
						}
					}
				} finally {
					if (canvas != null)
						surfaceHolder.unlockCanvasAndPost(canvas);
				}
				waitTillNextFrame();
			}
		}
		
		private void waitTillNextFrame() {
			long nextSleep = 0;
			frameTime += msPerFrame;
			nextSleep = frameTime - System.currentTimeMillis();
			if (nextSleep > 0) {
				try {
					sleep(nextSleep);
				} catch (InterruptedException e) {
				}
			}
		}
		
	}
	
	public void randomlyAddBubbles(int screenWidth, int screenHeight) {
		if (Math.random()>BUBBLE_FREQUENCY) return;
		bubbles.add(
				new Bubble(
						(int) (screenWidth*Math.random()),
						screenHeight+Bubble.RADIUS, 
						(int) ((Bubble.MAX_SPEED-0.1)*Math.random()+0.1)));
	}
	
	private void init() {
		getHolder().addCallback(this);
		backgroundPaint.setColor(Color.BLUE);
	}
	
	public BubblesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public BubblesView(Context context) {
		super(context);
		init();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		surfaceDestroyed(holder);
		surfaceCreated(holder);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		surfaceHolder = holder;
		gameLoop = new GameLoop();
		gameLoop.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		gameLoop.running = false;
		while (retry) {
			try {
				gameLoop.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void drawScreen(Canvas c) {
		c.drawPaint(backgroundPaint);
		
		for (Bubble bubble : bubbles) {
			bubble.draw(c);
		}
	}

	private void calculateDisplay(Canvas c) {
		randomlyAddBubbles(c.getWidth(),c.getHeight());
		LinkedList<Bubble> bubblesToRemove = new LinkedList<Bubble>();
		for (Bubble bubble : bubbles) {
			bubble.move();
			if (bubble.outOfRange())
				bubblesToRemove.add(bubble);
		}
		for (Bubble bubble : bubblesToRemove) {
			bubbles.remove(bubble);
		}
	}


	public void startAnimation() {
		synchronized (this) {
			if (gameLoop == null) {
				gameLoop = new GameLoop();
				gameLoop.start();
			}
		}
	}
	
	public void stopAnimation() {
		boolean retry = true;
		if (gameLoop != null) {
			gameLoop.running = false;
			while (retry) {
				try {
					gameLoop.join();
					retry = false;
				} catch (InterruptedException e) {
				}
			}
		}
		gameLoop = null;
	}
}
