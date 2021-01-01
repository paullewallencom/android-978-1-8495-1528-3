package com.packt.animation.countdroid;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class BallField extends SurfaceView implements Callback {

	private static final int ballGreen = 0xffaaddbb;
	private int count = 0;
	private int toAdd = 0;
	private static final int maxRows = 4, maxCols = 5;

	private static final float timePerFrame = 5;
	private class Ball {
		private static final int numberOfBounces = 3;
		private int numberOfBouncesLeft=numberOfBounces;
		private float groundResistance = 0.9f;
		private float ballWidth = 10;
		private float groundLineMinusRadius = 0;
		private float gravity = 1;
		private final float radius = Math.min(getWidth(), getHeight());
		private float x = 0, y = 0-radius, velocityX = 1, velocityY = 0;
		public boolean isMoving = true;
		public void nextFrame() {
			x = x + velocityX*timePerFrame;
			y = y + velocityY;
			if (y>groundLineMinusRadius) {
				y = 2*groundLineMinusRadius - y;
				velocityY = -velocityY*groundResistance;
				isMoving = (--numberOfBouncesLeft > 0);
				if (!isMoving) y = groundLineMinusRadius;  //correct for bounce
			}
			// v = u + a t
			velocityY = velocityY + 0.5f* gravity * timePerFrame * timePerFrame;
		}
		public void onDraw(Canvas c) {
			c.drawCircle(x, y, ballWidth, ballPaint);			
		}

		private float bounceTime (float distanceToBounce ) {
			return (float) (2f*Math.sqrt(2*distanceToBounce/gravity));
		}

		private float getDurationUntilStop (float distanceToBounce) {
			float bounceHeight = distanceToBounce;
			float duration = 0;
			duration += bounceTime(bounceHeight)/2;
			for (int bounceNumber = 1; bounceNumber < numberOfBounces; ++bounceNumber) {
				bounceHeight *= groundResistance;
				duration += bounceTime(bounceHeight);
			}
			return duration;
		}

		public Ball(float row,float col) {
			float startHeight = row*getHeight()/maxRows;
			float groundLine = (row+1)*getHeight()/maxRows;
			float targetX = (float)(col+1)*getWidth()/(float)(maxCols+1);
			ballWidth = Math.min(getWidth()/maxCols, getHeight()/maxRows)/3 ;
			x = -ballWidth;
			y = startHeight;
			groundLineMinusRadius = groundLine-ballWidth;
			velocityX=targetX/getDurationUntilStop(getWidth()*2/(maxCols));
		}
	}

	private Paint ballPaint = new Paint();
	private Updater updater = new Updater();
	private Ball hoppingBall = null;
	private Vector<Ball> landedBalls = new Vector<Ball>();

	private void init() {
		ballPaint.setColor(Color.RED);
		getHolder().addCallback(this);
	}

	public BallField(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BallField(Context context) {
		super(context);
		init();
	}

	public BallField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void addBalls (int n) {
		toAdd += n;
	}

	private void addBall () {
		int row = count / maxCols;
		int col = count % maxCols;
		hoppingBall = new Ball(row,col);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	public void surfaceCreated(SurfaceHolder holder) {
		updater.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		updater.isRunning = false;
		try {
			updater.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDraw(Canvas c) {
		c.drawColor(ballGreen);
		if (hoppingBall != null) hoppingBall.onDraw(c);
		synchronized (landedBalls) {
			for (Ball ball : landedBalls) ball.onDraw(c);
		}
	}

	public void updateWorld() {
		if (hoppingBall==null) {
			if (toAdd >0) {
				addBall();
				--toAdd;
				++count;
			} else return; // Nothing is moving, no animation needed
		}
		if (!hoppingBall.isMoving ) {
			synchronized (landedBalls) {
				landedBalls.add(hoppingBall);
			}
			hoppingBall = null;
		} else {
			hoppingBall.nextFrame();
		}
	}

	@Override
	public void finalize() {
		if (updater!=null) updater.isRunning = false;
		try {
			updater.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class Updater extends Thread {
		public boolean isRunning = false;
		@Override
		public void run() {
			Canvas c;
			isRunning = true;
			while (isRunning) {
				c = null;
				SurfaceHolder holder = null;
				try {
					holder = getHolder();
					synchronized(holder) {
						c = holder.lockCanvas(null);
						updateWorld();
						postInvalidate();
					}
					sleep(50);
				} catch (InterruptedException e) {
					isRunning = false;
					e.printStackTrace();
				} finally {
					if (c!=null && holder!=null) holder.unlockCanvasAndPost(c);
				}
			}
		}
	}
}
