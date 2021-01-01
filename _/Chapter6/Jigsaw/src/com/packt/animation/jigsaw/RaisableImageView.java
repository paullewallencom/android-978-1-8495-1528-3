package com.packt.animation.jigsaw;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class RaisableImageView extends FrameLayout {

	public ImageView image;
	private ImageView shadow;
	private float depth=0;
	
	private void init(Context context,AttributeSet attrs) {
		shadow = new ImageView(context,attrs);
		addView(shadow);
		image = new ImageView(context,attrs);
		addView(image); 
		ShapeDrawable shadowDrawable = new ShapeDrawable(new RectShape());
		shadowDrawable.getPaint().setColor(Color.BLACK);
		shadowDrawable.getPaint().setAlpha(88);
		shadowDrawable.setIntrinsicWidth(image.getDrawable().getIntrinsicWidth());
		shadowDrawable.setIntrinsicHeight(image.getDrawable().getIntrinsicHeight());
		shadow.setImageDrawable(shadowDrawable);
	}

	public RaisableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}

	public RaisableImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs);
	}

	public void setDepth(float depth) {
		this.depth = depth;
	    ShapeDrawable shadowDrawable = (ShapeDrawable)shadow.getDrawable(); 
		shadowDrawable.getPaint().setAlpha(88);
		image.setAlpha(1f);
		FrameLayout.LayoutParams imageLayout = (FrameLayout.LayoutParams) image.getLayoutParams();
		imageLayout.setMargins(
				(int) (getMeasuredWidth()/4*depth), 
				(int) (-getMeasuredHeight()/4*depth), 
				(int) (-getMeasuredWidth()/4*depth), 
				(int) (getMeasuredHeight()/4*depth));
		image.setLayoutParams(imageLayout);
		FrameLayout.LayoutParams shadowLayout = (FrameLayout.LayoutParams) shadow.getLayoutParams();
		shadowLayout.setMargins(
				(int) (-getMeasuredWidth()/4*depth), 
				(int) (getMeasuredHeight()/4*depth), 
				(int) (getMeasuredWidth()/4*depth), 
				(int) (-getMeasuredHeight()/4*depth));
		shadow.setLayoutParams(shadowLayout);
		setPivotX(getWidth()/2);
		setPivotY(getHeight()/2);
		setScaleX(depth+1);
		setScaleY(depth+1);
	}
	
	public void setFocus (float focus) {
	    if (depth>0) return;
	    ShapeDrawable shadowDrawable = (ShapeDrawable)shadow.getDrawable(); 
		shadowDrawable.getPaint().setAlpha(255);
		image.setAlpha(focus);		
	}
	public Drawable getDrawable() {
		return image.getDrawable();
	}
	public void setDrawable(Drawable drawable) {
		image.setImageDrawable(drawable);
	}
}
