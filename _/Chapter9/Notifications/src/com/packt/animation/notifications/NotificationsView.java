package com.packt.animation.notifications;

import java.util.LinkedList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


public class NotificationsView extends TextView implements Runnable {

	public static final long NOTIFICATION_TIME = 3000;
	private boolean running = false;
	private Thread notificationsThread = null;
	private Animation getNotificationAnimation (TextView houseView) {
		AnimationSet mailAnimation = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(0.1f, 1, 0.1f, 1);
		float letterboxX = houseView.getX()+27;
		float letterboxY = houseView.getY()+27;
		TranslateAnimation translate = new TranslateAnimation(
				Animation.ABSOLUTE, letterboxX - getX(),
				Animation.RELATIVE_TO_SELF, 0,
				Animation.ABSOLUTE, letterboxY - getY(),
				Animation.RELATIVE_TO_SELF, 0);
		mailAnimation.addAnimation(scale);
		mailAnimation.addAnimation(translate);
		mailAnimation.setDuration(1000);
		return mailAnimation;
	}


	/*
	 * Solely used for queueing notifications within this class.
	 */
	 private class Notification {
		 public String owner;
		 public String message;
		 public TextView ownerView;
		 public Notification (String owner, TextView ownerView, String message) {
			 this.message=message;
			 this.owner = owner;
			 this.ownerView = ownerView;
		 }
	 }
	 private LinkedList<Notification> pendingNotifications = new LinkedList<Notification>();

	 public NotificationsView(Context context, AttributeSet attrs) {
		 super(context, attrs);
	 }

	 public void queueNotification(String owner, TextView ownerView, String message) {
		 synchronized (pendingNotifications) {
			 pendingNotifications.addLast(new Notification(owner, ownerView, message));
		 }
	 }


	 public void run() {
		 running = true;
		 while (running) {
			 post( new Runnable() {
				 public void run() {
					 NotificationsView.this.setVisibility(View.INVISIBLE);
				 }
			 });
			 synchronized (pendingNotifications) {
				 if (!pendingNotifications.isEmpty()) {
					 final Notification currentNotification = pendingNotifications.removeFirst();
					 currentNotification.ownerView.setText(
							 currentNotification.owner
							 + " - "
							 + currentNotification.message);
					 NotificationsView.this.setVisibility(
							 View.VISIBLE);
					 NotificationsView.this.startAnimation(
							 getNotificationAnimation(
									 currentNotification.ownerView));
					 NotificationsView.this.setText(
							 currentNotification.owner
							 + "\n"
							 + currentNotification.message);

				 }
			 }
			 try {
				 Thread.sleep(NOTIFICATION_TIME);
			 } catch (InterruptedException e) {

			 }
		 }
	 }

	 /*
	  * Starts the thread that takes care of notification messages.
	  */
	 public void startThread() {
		 synchronized (this) {
			 if (notificationsThread != null) return;
			 notificationsThread = new Thread(this);
			 notificationsThread.start();
		 }
	 }

	 /* 
	  * Carefully halts the thread that takes care of notification messages.
	  */
	 public void stopThread() {
		 synchronized (this) {
			 if (notificationsThread == null) return;
			 running = false;
			 boolean retry = true;
			 while (retry) {
				 try {
					 notificationsThread.join();
					 retry = false;
				 } catch (InterruptedException e) {
				 }
			 }
			 notificationsThread = null;
		 }
	 }

	 @Override
	 public void finalize() {
		 stopThread();
	 }
}
