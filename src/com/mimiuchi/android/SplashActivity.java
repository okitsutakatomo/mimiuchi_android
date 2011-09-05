package com.mimiuchi.android;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends Activity {

	// C2DMのためのインテント
	public static Intent registrationIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		Handler hdl = new Handler();
		hdl.postDelayed(new splashHandler(), 2000);

		// C2DMのためのインテント
		registrationIntent = new Intent(
				"com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app",
				PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", "troopergreen.mobile@gmail.com");
		// C2DM登録しているアカウントを指定する
		startService(registrationIntent);
	}

	class splashHandler implements Runnable {
		public void run() {
			Intent i = new Intent(getApplication(),
					WebViewActivity.class);
			startActivity(i);
			SplashActivity.this.finish();
		}
	}
}