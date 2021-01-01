package com.packt.animation.notifications;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple class to randomly create notifications about things, and call a handler.
 * This simulates a system where notification information is coming in from an
 * external source at regular intervals;
 * 
 * @author alex
 *
 */
public class Notifier extends Thread {
	public static final long TIME_BETWEEN_EVENTS = NotificationsView.NOTIFICATION_TIME;
	public static final double CHANCE_OF_ENTRY = 0.1;
	public static final double CHANCE_OF_EXIT = 0.1;
	public static final double CHANCE_OF_ELVIS = 0.002;
	public interface NotificationReceiver {
		public void onNotification(String notification);
	}
	private NotificationReceiver receiver;
	private boolean running = false;
	public Notifier(NotificationReceiver receiver) {
		this.receiver = receiver;
	}
	public void run() {
		running = true;
		while (running) {
			try {
				Thread.sleep(TIME_BETWEEN_EVENTS);
			} catch (InterruptedException e) {
			}
			double choiceValue = Math.random();
			Date now = new Date();
			String nowString = "(" + DateFormat.getTimeInstance().format(now)+") ";
			if (choiceValue < CHANCE_OF_ENTRY) {
				receiver.onNotification(nowString + "Someone entered the building");
			} else if (choiceValue < CHANCE_OF_ENTRY+ CHANCE_OF_EXIT) {
				receiver.onNotification(nowString + "Someone left the building");
			} else if (choiceValue < CHANCE_OF_ENTRY+ CHANCE_OF_EXIT +CHANCE_OF_ELVIS) {
				receiver.onNotification(nowString + "Elvis has left the building");
			}
		}
	}

	/* 
	 * Carefully halts the thread that generates notification messages.
	 */
	public void stopThread() {
		synchronized (this) {
			running = false;
			boolean retry = true;
			while (retry) {
				try {
					join();
					retry = false;
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
