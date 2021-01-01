package com.packt.animation.funky;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;




public class FunkyActivity extends Activity {
	/** Called when the activity is first created. */
	private AnimationDrawable jumpAnim;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final ImageView animImage = (ImageView) 
				findViewById(R.id.stickman);
		final AnimationDrawable animDrawable = (AnimationDrawable) animImage.getDrawable();
		animImage.post(
				new Runnable() {
					public void run() { 
						animDrawable.start();
					}
				}
				);
		Button danceLeftButton = 
				(Button) findViewById(R.id.danceleft);
		danceLeftButton.setOnClickListener(
				new View.OnClickListener(){
					public void onClick(View v) {
						// We'll fill this in in a minute
						AnimationDrawable danceLeftAnim = 
						(AnimationDrawable)
						getResources().getDrawable(R.anim.dance_left);
						animImage.setImageDrawable(danceLeftAnim);
						danceLeftAnim.start();
					}
				}
				);
		Button danceRightButton = 
				(Button) findViewById(R.id.danceright);
		danceRightButton.setOnClickListener(
				new View.OnClickListener(){
					public void onClick(View v) {
						// We'll fill this in in a minute too!
						AnimationDrawable danceRightAnim = 
								(AnimationDrawable) 
								getResources().getDrawable(R.anim.dance_right);
						animImage.setImageDrawable(danceRightAnim);
						danceRightAnim.start();
					}
				});
		jumpAnim = subAnimation( animDrawable, 24, 36 );
		Drawable standingStill = 
				getResources()
				.getDrawable(R.drawable.stickman_frame_00);
		jumpAnim.addFrame(standingStill, 83);
		jumpAnim.setOneShot(true);
		Button jumpButton = (Button) findViewById(R.id.jump);
		jumpButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				animImage.setImageDrawable(jumpAnim);
				jumpAnim.start();
				jumpAnim.setVisible(true,true);
			}
		});


	}

	private AnimationDrawable subAnimation( 
			AnimationDrawable src, int start, int end ) {
		AnimationDrawable subAnim = new AnimationDrawable();
		subAnim.setOneShot( src.isOneShot() );
		for (int i = start; i<end; ++i) {
			subAnim.addFrame ( src.getFrame (i), src.getDuration (i) 
					);
		}
		return subAnim;
	}




}
