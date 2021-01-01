package com.packt.animation.countdroid;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class AnimatedDisplayElement implements Calculator.NumberHandler {
	private TextView view;
	private Runnable animationFinishedHandler;

	public AnimatedDisplayElement (TextView view) {
		this.view = view;
	}

	public void setOnAnimationFinished (Runnable animationFinishedHandler) {
		this.animationFinishedHandler = animationFinishedHandler;
	}

	public void setText(String s) {
		view.setText(s);
	}

	public void setVisibility (int visibility) {
		view.setVisibility(visibility);
	}

	public void animate() {
		view.clearAnimation();
		Animation displayChar = AnimationUtils.loadAnimation(
				view.getContext(), R.anim.displaychar);
		setVisibility(View.VISIBLE);
		view.startAnimation(displayChar);
		if (animationFinishedHandler != null) {
			view.postDelayed(animationFinishedHandler, displayChar.getDuration());
		}
	}

	public void receiveNumber(int n) {
		animate();
		setText(Integer.toString(n));
	}
}
