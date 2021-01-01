package com.packt.animation.bubbles1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.RectF;

class Bubble
{
   private float x, y, speed;
   private static final Paint bubblePaint = new Paint();
   static
{
	      bubblePaint.setStyle(Paint.Style.FILL);
	      bubblePaint.setColor(Color.CYAN);
	      bubblePaint.setAlpha(66);
	      bubblePaint.setAntiAlias(true);

}

   public static final int RADIUS = 10;
   public static final int MAX_SPEED = 10;
   public static final int MIN_SPEED = 1;
   private Bitmap bubbleBitmap;
   private float amountOfWobble = 0;
   public static final float WOBBLE_RATE = 1/40;
   public static final int WOBBLE_AMOUNT = 3;

   public Bubble (float x, float y, float speed,Bitmap bubbleBitmap)
{
      this.x = x;
      this.y = y;
      this.speed = Math.max(speed, MIN_SPEED);
      this.bubbleBitmap = bubbleBitmap;
}

   public void draw(Canvas c)
{
	      c.drawOval(
	    	         new RectF(
	    	            x-RADIUS-WOBBLE_AMOUNT*amountOfWobble,
	    	            y-RADIUS+WOBBLE_AMOUNT*amountOfWobble,
	    	            x+RADIUS+WOBBLE_AMOUNT*amountOfWobble,
	    	            y+RADIUS-WOBBLE_AMOUNT*amountOfWobble),
	    	         bubblePaint);


}

   public void move(float numFrames)
{
	      
		y -= speed*numFrames;
	      amountOfWobble = (float)Math.sin (y*WOBBLE_RATE);
}

   public boolean outOfRange()
{
      return (y+RADIUS < 0);
}
}
