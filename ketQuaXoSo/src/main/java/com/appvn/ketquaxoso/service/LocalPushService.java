package com.appvn.ketquaxoso.service;

import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.appvn.ketquaxoso.view.NotificationHelper;

public class LocalPushService extends Service {

	private NotificationHelper notificationHelper;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		notificationHelper = new NotificationHelper(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (checkTimeLimit()) {

		} else {
			notificationHelper.onCreateNotificaion();

		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean checkTimeLimit() {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.HOUR_OF_DAY) == 18
				&& calendar.get(Calendar.MINUTE) == 30) {
			return false;
		}

		return true;
	}
}
