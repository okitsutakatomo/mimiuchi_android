package com.mimiuchi.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class C2DMReceiver extends BroadcastReceiver { // ブロードキャストレシーバ受信

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(
				"com.google.android.c2dm.intent.REGISTRATION")) { // デバイス登録IDの受信インテント
			android.util.Log
					.d("C2DMReceiver",
							"com.google.android.c2dm.intent.REGISTRATION intent ACCEPTED");
			handleRegistration(context, intent);
		} else if (intent.getAction().equals(
				"com.google.android.c2dm.intent.RECEIVE")) {
			// C2DMからのデータ受信インテント
			android.util.Log.d("C2DMReceiver",
					"com.google.android.c2dm.intent.RECEIVE intent ACCEPTED");
			android.util.Log.d("C2DMReceiver",
					"message=" + intent.getStringExtra("message")); // 取得するデータ

			// 通知する。
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			Notification notification = new Notification(
					android.R.drawable.btn_default, "みみうちからお知らせが届きました。",
					System.currentTimeMillis());
			notification.icon = R.drawable.ic_menu_notifications;

			// intentの設定
			Intent i = new Intent(context.getApplicationContext(),
					WebViewActivity.class);
			PendingIntent pi = PendingIntent.getActivity(
					context.getApplicationContext(), 0, i, 0);
			// notification.setLatestEventInfo(context.getApplicationContext(),
			// "みみうち", intent.getStringExtra("message"), pi);

			// ひとまずNoticeメッセージは固定とする
			notification.setLatestEventInfo(context.getApplicationContext(),
					"みみうち", "みみうちよりお知らせが届いています", pi);

			notificationManager.notify(R.string.app_name, notification);
		}
	}

	private void handleRegistration(Context context, Intent intent) {
		String registration_id = intent.getStringExtra("registration_id"); // デバイス登録IDの取得
		android.util.Log.d("C2DMReceiver",
				"handleRegistration start registration_id=" + registration_id);
		if (intent.getStringExtra("error") != null) {
			android.util.Log.d("C2DMReceiver",
					"handleRegistration error try again");
			// 登録の失敗、後で再度トライの必要あり。
		} else if (intent.getStringExtra("unregistered") != null) {
			android.util.Log
					.d("C2DMReceiver", "handleRegistration unregisterd");
			// 登録解除が完了、認証済みセンダーからの新しいメッセージは拒否される。
		} else if (registration_id != null) {
			android.util.Log.d("C2DMReceiver", "handleRegistration registerd");
			// 登録IDの取得成功。WebViewに渡し、WebView側でサーバに送信する。
			WebViewActivity.setRegistrationId(registration_id);
		}
		android.util.Log.d("C2DMReceiver", "handleRegistration end");
	}
}