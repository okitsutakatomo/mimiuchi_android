package com.mimiuchi.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class C2DMReceiver extends BroadcastReceiver { // �u���[�h�L���X�g���V�[�o��M

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(
				"com.google.android.c2dm.intent.REGISTRATION")) { // �f�o�C�X�o�^ID�̎�M�C���e���g
			android.util.Log
					.d("C2DMReceiver",
							"com.google.android.c2dm.intent.REGISTRATION intent ACCEPTED");
			handleRegistration(context, intent);
		} else if (intent.getAction().equals(
				"com.google.android.c2dm.intent.RECEIVE")) {
			// C2DM����̃f�[�^��M�C���e���g
			android.util.Log.d("C2DMReceiver",
					"com.google.android.c2dm.intent.RECEIVE intent ACCEPTED");
			android.util.Log.d("C2DMReceiver",
					"message=" + intent.getStringExtra("message")); // �擾����f�[�^

			// �ʒm����B
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			Notification notification = new Notification(
					android.R.drawable.btn_default, "�݂݂������炨�m�点���͂��܂����B",
					System.currentTimeMillis());
			notification.icon = R.drawable.ic_menu_notifications;

			// intent�̐ݒ�
			Intent i = new Intent(context.getApplicationContext(),
					WebViewActivity.class);
			PendingIntent pi = PendingIntent.getActivity(
					context.getApplicationContext(), 0, i, 0);
			// notification.setLatestEventInfo(context.getApplicationContext(),
			// "�݂݂���", intent.getStringExtra("message"), pi);

			// �ЂƂ܂�Notice���b�Z�[�W�͌Œ�Ƃ���
			notification.setLatestEventInfo(context.getApplicationContext(),
					"�݂݂���", "�݂݂�����肨�m�点���͂��Ă��܂�", pi);

			notificationManager.notify(R.string.app_name, notification);
		}
	}

	private void handleRegistration(Context context, Intent intent) {
		String registration_id = intent.getStringExtra("registration_id"); // �f�o�C�X�o�^ID�̎擾
		android.util.Log.d("C2DMReceiver",
				"handleRegistration start registration_id=" + registration_id);
		if (intent.getStringExtra("error") != null) {
			android.util.Log.d("C2DMReceiver",
					"handleRegistration error try again");
			// �o�^�̎��s�A��ōēx�g���C�̕K�v����B
		} else if (intent.getStringExtra("unregistered") != null) {
			android.util.Log
					.d("C2DMReceiver", "handleRegistration unregisterd");
			// �o�^�����������A�F�؍ς݃Z���_�[����̐V�������b�Z�[�W�͋��ۂ����B
		} else if (registration_id != null) {
			android.util.Log.d("C2DMReceiver", "handleRegistration registerd");
			// �o�^ID�̎擾�����BWebView�ɓn���AWebView���ŃT�[�o�ɑ��M����B
			WebViewActivity.setRegistrationId(registration_id);
		}
		android.util.Log.d("C2DMReceiver", "handleRegistration end");
	}
}