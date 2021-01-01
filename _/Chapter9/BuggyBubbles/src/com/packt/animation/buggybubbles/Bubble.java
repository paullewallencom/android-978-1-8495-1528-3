package com.packt.animation.buggybubbles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

class Bubble {
	private float x, y, speed;
	private float amountOfWobble = 0;
	private Paint bubblePaint = new Paint();
	static {
	}
	public static final int RADIUS = 10;
	public static final int WOBBLE_AMOUNT = 3;
	public static final float WOBBLE_RATE = 1f/40f;

	public static final int MAX_SPEED = 10;
	public static final int MIN_SPEED = 1;
	public Bubble (float x, float y, float speed) {
		this.x = x;
		this.y = y;
		this.speed = Math.max(speed, MIN_SPEED);
		bubblePaint.setStyle(Paint.Style.FILL);
		bubblePaint.setAntiAlias(true);
		float randColor = (float)Math.random();
		if (randColor >0.67)
			bubblePaint.setColor(Color.RED);
		else if (randColor > 0.33)
			bubblePaint.setColor(Color.CYAN);
		else
			bubblePaint.setColor(Color.GREEN);
		bubblePaint.setAlpha(196);
	}
	public void draw(Canvas c) {
		c.drawRoundRect(new RectF(
				x - RADIUS - WOBBLE_AMOUNT*amountOfWobble,
				y - RADIUS - WOBBLE_AMOUNT*amountOfWobble,
				x + RADIUS + WOBBLE_AMOUNT*amountOfWobble,
				y + RADIUS + WOBBLE_AMOUNT*amountOfWobble
				), RADIUS - 5, RADIUS -5, bubblePaint);
	}
	public void move() {
		y -= speed;
		amountOfWobble = (float)Math.sin (y*WOBBLE_RATE);
	}
	public boolean outOfRange() {
		return ((y+RADIUS) < 0);
	}
}