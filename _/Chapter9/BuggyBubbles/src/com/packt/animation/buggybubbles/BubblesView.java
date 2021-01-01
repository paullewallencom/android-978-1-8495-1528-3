package com.packt.animation.buggybubbles;

import java.util.LinkedList;
import android.graphics.Canvas;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BubblesView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder surfaceHolder;
	private GameLoop gameLoop=null;
	private LinkedList<Bubble> bubbles = new LinkedList<Bubble>();
	private Paint backgroundPaint = new Paint();
	private float BUBBLE_FREQUENCY = 0.03f;
	private float BUBBLE_TOUCH_RADIUS = 30;
	private int BUBBLE_TOUCH_QUANTITY = 7;

	private class GameLoop extends Thread {
		private long msPerFrame = 1000/12;
		public boolean running = true;
		long frameTime = 0;

		public void run() {
			setName("GameLoop");
			Canvas canvas = null;
			final SurfaceHolder surfaceHolder = BubblesView.this.surfaceHolder;
			frameTime = System.currentTimeMillis();
			while (running) {
				try {
					canvas = surfaceHolder.lockCanvas();
					if (canvas != null) {
						synchronized (surfaceHolder) {
							drawScreen(canvas);
						}
						calculateDisplay(canvas);
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
			// HAHA THEY WILL NEVER FIND ME!
			// gremlin(nextSleep);


			if (nextSleep > 0) {
				try {
					sleep(nextSleep);
				} catch (InterruptedException e) {
				}
			}
		}

		private void gremlin(long nextSleep) {
			while (nextSleep>0) {
				double[] pointlessMemoryAllocation = new double[1337];
				double pointlessCalculationValue = Math.random()*66666.66;
				for (int i = 0; i< pointlessMemoryAllocation.length; ++i) {
					pointlessMemoryAllocation[i] = pointlessCalculationValue / (1+i);  
				}
				nextSleep = frameTime - System.currentTimeMillis();
			}
		}	
	}


	public void randomlyAddBubbles(int screenWidth, int screenHeight) {
		if (Math.random()>BUBBLE_FREQUENCY) return;
		synchronized (bubbles) {
			bubbles.add(
					new Bubble(
							(int) (screenWidth*Math.random()),
							screenHeight+Bubble.RADIUS, 
							(int) ((Bubble.MAX_SPEED-0.1)*Math.random()+0.1)));
		}
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


	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}


	public void surfaceCreated(SurfaceHolder holder) {
		surfaceHolder = holder;
		startAnimation();
	}


	public void surfaceDestroyed(SurfaceHolder holder) {
		stopAnimation();
	}

	private void drawScreen(Canvas c) {
		c.drawPaint(backgroundPaint);
		synchronized (bubbles) {
			for (Bubble bubble : bubbles) {
				bubble.draw(c);
			}
		}
	}

	private void calculateDisplay(Canvas c) {
		randomlyAddBubbles(c.getWidth(),c.getHeight());
		LinkedList<Bubble> bubblesToRemove = new LinkedList<Bubble>();
		synchronized (bubbles) {
			for (Bubble bubble : bubbles) {
				bubble.move();
				if (bubble.outOfRange())
					bubblesToRemove.add(bubble);
			}
			for (Bubble bubble : bubblesToRemove) {
				bubbles.remove(bubble);
			}
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
		synchronized (this) {
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


	public boolean onTouchEvent(MotionEvent event) {
		boolean handled = false;
		if (event.getAction()==MotionEvent.ACTION_DOWN) {
			createSomeBubbles(event.getX(),event.getY());
			handled = true;
		}
		return handled;
	}
	private void createSomeBubbles(float x, float y) {
		for (int numBubbles = 0; numBubbles < BUBBLE_TOUCH_QUANTITY; ++numBubbles) {
			synchronized(bubbles) {
				bubbles.add(
						new Bubble(
								(int) (2*BUBBLE_TOUCH_RADIUS*Math.random() - BUBBLE_TOUCH_RADIUS + x),
								(int) (2*BUBBLE_TOUCH_RADIUS*Math.random() - BUBBLE_TOUCH_RADIUS + y), 
								(int) ((Bubble.MAX_SPEED-0.1)*Math.random()+0.1)));
			}
		}
	}


	public void finalize() {
		stopAnimation();
	}
	public void setFrequency(float bubbleFrequency) {
		BUBBLE_FREQUENCY = bubbleFrequency;
	}

}
