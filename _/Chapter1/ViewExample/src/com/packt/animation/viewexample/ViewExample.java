package com.packt.animation.viewexample;

import android.app.Activity;
import android.os.Bundle;

public class ViewExample extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyTextView helloView = new MyTextView(this);
		helloView.setText("Hello Views!");
		setContentView(R.layout.main);
	}
}