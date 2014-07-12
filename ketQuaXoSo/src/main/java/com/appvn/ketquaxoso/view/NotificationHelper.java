package com.appvn.ketquaxoso.view;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.appvn.ketquaxoso.R;
import com.appvn.ketquaxoso.activity.MainActivity;

public class NotificationHelper {
	private Context context;
	private Notification mNotification;
	private NotificationManager mNotificationManager;
	private PendingIntent mContentIntent;
	private int mNOTIFICATION_ID = 10012;

	public NotificationHelper(Context context) {
		this.context = context;

	}

	@SuppressWarnings("deprecation")
	public void onCreateNotificaion() {

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String contentText = "Xem kết quả ngày " + day + "/" + month + "/"
				+ year;
		String title = context.getString(R.string.app_name);
		long when = System.currentTimeMillis();
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);
		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotification = new Notification(R.drawable.ic_launcher, contentText,
				when);
		Intent notificationIntent = new Intent();
		mContentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		mNotification.setLatestEventInfo(context, title, contentText,
				mContentIntent);
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotification.contentIntent = pIntent;
		mNotificationManager.notify(mNOTIFICATION_ID, mNotification);

	}
}
