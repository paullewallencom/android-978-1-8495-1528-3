package com.packt.animation.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * House security notification app for Android
 * 
 * @author alex
 *
 */
public class NotificationsActivity extends Activity {

	private static final int[] houses = {
		R.id.house1,
		R.id.house2,
		R.id.house3,
		R.id.house4
	};
	private Notifier[] notifiers;
	private NotificationsView notificationsView = null;

	/*
	 * Takes care of notifications received against a particular house
	 */
	private class HouseNotificationReceiver implements Notifier.NotificationReceiver {
		private String houseName;
		private TextView houseView;
		public HouseNotificationReceiver (String houseName, TextView houseView) {
			this.houseName = houseName;
			this.houseView = houseView;
		}

		public void onNotification(String notification) {
			queueNotification (houseName, houseView,notification);
		}

	}

	private void queueNotification (String origin, TextView originView, String message) {
		if (notificationsView != null)
			notificationsView.queueNotification(origin, originView, message);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//View torch = findViewById(R.id.torch);
		//Animation torchAnim = AnimationUtils.loadAnimation(NotificationsActivity.this, R.anim.torchanim);
		//torch.startAnimation(torchAnim);
		notificationsView = (NotificationsView) findViewById(R.id.notificationsView);
		notificationsView.startThread();
		// Add a random notifier for each house
		notifiers = new Notifier[houses.length];
		for (int i = 0; i< houses.length; ++i) {
			TextView house = (TextView) findViewById(houses[i]);
			String houseName = house.getText().toString();
			HouseNotificationReceiver houseReceiver = new HouseNotificationReceiver(houseName, house);
			notifiers[i] = new Notifier(houseReceiver);
			notifiers[i].start();
		}
	}

	private void stopThreads() {
		notificationsView.stopThread();
		for (Notifier notifier : notifiers) {
			notifier.stopThread();
		}
	}

	public void onStop() {
		super.onStop();
		stopThreads();
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		stopThreads();
	}
}