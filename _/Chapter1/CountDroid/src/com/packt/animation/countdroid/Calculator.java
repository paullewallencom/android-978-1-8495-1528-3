package com.packt.animation.countdroid;

public class Calculator {
	private static final int UNSET = -1;
	private int inputNumbers[] = new int[2];
	private NumberHandler[] inputNumberReceiver = new NumberHandler[2];

	public static class Error extends Exception {
		public Error(String string) {
			super(string);
		}

		private static final long serialVersionUID = 3761542637105335103L;
	}

	public static interface NumberHandler {
		public void receiveNumber(int n);
	}

	public Calculator() {
		clear();
	}
	public void clear() {
		for (int i =0; i<inputNumbers.length; ++i) {
			inputNumbers[i] = UNSET;
		}
	}
	public int result() throws Error {
		int sum = 0;
		for (int n : inputNumbers) {
			if (n == UNSET) {
				throw new Error("You haven't finished typing in the sum!");
			}
			sum += n;
		}
		return sum;
	}
	public boolean validInput(int value) {
		return (value>0 && value<10);
	}
	public int inputValue(int value) throws Error {
		if (!validInput(value)) {
			throw new Error("I can only add numbers I understand!");
		}
		int i=0;
		for (i=0; i<inputNumbers.length;++i) {
			if (inputNumbers[i] == UNSET) {
				inputNumbers[i] = value;
				inputNumberReceiver[i].receiveNumber(value);
				break;
			}
		}
		if (i == inputNumbers.length) {
			throw new Error("I don't need any more numbers!");
		} 
		return i;
	}
	public void setNumberReceiver( int i, NumberHandler receiver ) {
		inputNumberReceiver[i]=receiver;
	}
}
