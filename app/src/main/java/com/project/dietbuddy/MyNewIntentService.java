package com.project.dietbuddy;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyNewIntentService extends IntentService {
	private int NOTIFICATION_ID = 3;

	public MyNewIntentService() {
		super("MyNewIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		createNotificationChannel();

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_ID + "")
				.setSmallIcon(R.drawable.ic_launcher_background)
				.setContentTitle("식사하실 시간입니다.")
				.setContentText("건강을 위한 첫 걸음을 시작하세요!");

		Intent notifyIntent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		//to be able to launch your activity from the notification
		builder.setContentIntent(pendingIntent);
		Notification notificationCompat = builder.build();
		NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
		managerCompat.notify(NOTIFICATION_ID, notificationCompat);
	}
	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = getString(R.string.channel_name);
			String description = getString(R.string.channel_description);
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID + "", name, importance);
			channel.setDescription(description);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}
}