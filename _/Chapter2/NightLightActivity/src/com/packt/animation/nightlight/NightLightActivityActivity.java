package com.packt.animation.nightlight;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;

public class NightLightActivityActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		View mainScreen = findViewById(R.id.mainscreen);
		mainScreen.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// We're going to fill this in in a moment
				ImageView scene = 
						(ImageView) findViewById(R.id.lightgraphic);
				TransitionDrawable sceneDrawable = 
						(TransitionDrawable) scene.getDrawable();
				sceneDrawable.startTransition(3000);

			}
		});

	}
}
