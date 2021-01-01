package com.packt.animation.countdroid;

import java.util.Vector;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class CountDroid extends Activity {
	Calculator calculator = new Calculator();
	CalculatorDelegator calculatorDelegator;

	int[] numberButtonIDs = {
			R.id.num_1,
			R.id.num_2,
			R.id.num_3,
			R.id.num_4,
			R.id.num_5,
			R.id.num_6,
			R.id.num_7,
			R.id.num_8,
			R.id.num_9,
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		calculatorDelegator = new CalculatorDelegator(calculator);
		associateNumberButtons();
		associateCalculatorDisplay();
		associateBallField();
		showSplash();
	}

	@Override
	public void onPause() {
		System.exit(0); // Lazy but efficient way of destroying state
	}

	private void associateNumberButtons() {
		Vector<Button> buttons = new Vector<Button>();
		for (int id : numberButtonIDs) {
			buttons.add((Button) findViewById(id));
		}
		calculatorDelegator.setNumberButtons(buttons);
	}

	private void associateCalculatorDisplay() {
		TextView lhs, rhs, plus, equals, result;
		lhs = (TextView) findViewById(R.id.display_lhs); 
		plus = (TextView) findViewById(R.id.display_plus);
		rhs = (TextView) findViewById(R.id.display_rhs);
		equals = (TextView) findViewById(R.id.display_equals);
		result = (TextView) findViewById(R.id.display_result);
		calculatorDelegator.setDisplayElements(lhs, plus, rhs, equals, result);
	}

	private void associateBallField() {
		final BallField bf = (BallField) findViewById(R.id.ball_field);
		calculatorDelegator.setBallField(bf);
	}

	private void showSplash() {
		ImageView splashContainer = (ImageView) findViewById(R.id.splash_container);
		final AnimationDrawable splash = (AnimationDrawable) splashContainer.getBackground();
		splashContainer.post(new Runnable() {
			public void run() {
				splash.start();
				showCalculatorAfterSplash();
			}
		});
		int splashDuration = 0;
		for (int i=0;i<splash.getNumberOfFrames();++i) {
			splashDuration += splash.getDuration(i);
		}
	}

	private void showCalculatorAfterSplash() {
		ImageView splashContainer = (ImageView) findViewById(R.id.splash_container);
		final AnimationDrawable splash = (AnimationDrawable) splashContainer.getBackground();
		final ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper);
		flipper.postDelayed(new Runnable() {
			public void run() {
				if ( splash.getCurrent() == splash.getFrame(splash.getNumberOfFrames()-1))
					flipper.showNext();
				else
					showCalculatorAfterSplash();

			}
		}, 1000);    	
	}
}