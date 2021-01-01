package com.packt.animation.countdroid;

import java.util.Vector;
import com.packt.animation.countdroid.Calculator.Error;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CalculatorDelegator {
	private Calculator calculator;
	private Vector<Button> buttons;
	private AnimatedDisplayElement lhs, plus, rhs, equals, result;
	private BallField ballField;

	public CalculatorDelegator(Calculator calculator) {
		this.calculator = calculator;
	}

	public void setNumberButtons(Vector<Button> buttons) {
		this.buttons = buttons;
		for (Button calculatorKey: buttons) {
			final int keyNumber = Integer.parseInt(calculatorKey.getText().toString());
			calculatorKey.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					inputValue(keyNumber);
				}
			});
		}
	}

	public void setDisplayElements(TextView lhs, TextView plus, TextView rhs, TextView equals, TextView result) {
		this.lhs = new AnimatedDisplayElement (lhs);
		this.rhs = new AnimatedDisplayElement (rhs);
		this.plus = new AnimatedDisplayElement (plus);
		this.equals = new AnimatedDisplayElement (equals);
		this.result = new AnimatedDisplayElement (result);

		this.lhs.setOnAnimationFinished( new Runnable() {
			public void run() {
				CalculatorDelegator.this.plus.setVisibility(View.VISIBLE);
				CalculatorDelegator.this.plus.animate();
			}
		});

		this.plus.setOnAnimationFinished ( new Runnable() {
			public void run() {
				CalculatorDelegator.this.setInteractive(true);
			}
		});

		this.rhs.setOnAnimationFinished ( new Runnable() {
			public void run() {
				CalculatorDelegator.this.equals.setVisibility(View.VISIBLE);
				CalculatorDelegator.this.equals.animate();
			}
		});

		this.equals.setOnAnimationFinished ( new Runnable() {
			public void run() {
				try {
					int result = CalculatorDelegator.this.calculator.result();
					CalculatorDelegator.this.result.setText(Integer.toString(result));
				} catch (Calculator.Error e) {
					CalculatorDelegator.this.result.setText("?");
				}
				CalculatorDelegator.this.result.animate();
				CalculatorDelegator.this.setInteractive(true);
			}
		});

		calculator.setNumberReceiver(0, this.lhs);
		calculator.setNumberReceiver(1, this.rhs);
	}

	public void setBallField (BallField ballField) {
		this.ballField = ballField; 
	}

	public void inputValue(int keyNumber) {
		setInteractive(false);
		try {
			calculator.inputValue(keyNumber);
			ballField.addBalls(keyNumber);
		} catch (Error e) {
			e.printStackTrace();
		}
	}

	private void setInteractive(boolean isInteractive) {
		for (Button b: buttons) b.setClickable(isInteractive);
	}
}
