package com.mimiuchi.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class WebViewActivity extends Activity {

	private WebView webView;
	private String baseUrl = "http://mimiuchi.com";
	// private String baseUrl = "http://10.0.1.13:3000";

	private static String registrationId;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeLogo(R.drawable.header_logo_m);
		actionBar.setTitle("                        ");
		actionBar
				.addAction(new ClickAction(R.drawable.ic_title_refresh_default) {
					@Override
					public void onClick() {
						webView.reload();
					}
				});
		actionBar.addAction(new ClickAction(R.drawable.tools_inv) {
			@Override
			public void onClick() {
				webView.loadUrl(baseUrl + "/account");
			}
		});

		actionBar.setOnTitleClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				webView.loadUrl(baseUrl);
			}
		});

		webView = (WebView) findViewById(R.id.webview);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setPluginsEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				actionBar.setProgressBarVisibility(View.GONE);

				// C2DM用のRegistratioIDが存在し、かつ自ドメイン内の場合は、サーバにRegistratioIDをポストする。
				if (registrationId != null && url.indexOf(baseUrl) != -1) {
					webView.loadUrl("javascript:send_android_registration_id(\""
							+ registrationId + "\");");
				}
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				actionBar.setProgressBarVisibility(View.VISIBLE);
			}
		});
		webView.loadUrl(baseUrl + "/?app=android");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		} else {
			HistoryManager historyManager = new HistoryManager();
			historyManager.goBack();
			return false;
		}
	}

	// オプションメニューアイテムを生成します。
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// MenuItemの生成
		MenuItem homeItem = menu.add(0, 3, 1, "お知らせ");
		homeItem.setIcon(R.drawable.ic_menu_notifications);

		MenuItem settingItem = menu.add(1, 0, 2, "設定");
		settingItem.setIcon(android.R.drawable.ic_menu_preferences);

		MenuItem helpItem = menu.add(1, 1, 2, "ヘルプ");
		helpItem.setIcon(android.R.drawable.ic_menu_help);

		MenuItem aboutItem = menu.add(1, 2, 2, "みみうちとは");
		aboutItem.setIcon(android.R.drawable.ic_menu_info_details);
		return true;
	}

	// オプションメニューが選択されると呼び出されます。
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			webView.loadUrl(baseUrl + "/account");
			return true;
		case 1:
			webView.loadUrl(baseUrl + "/help");
			return true;
		case 2:
			webView.loadUrl(baseUrl + "/about");
			return true;
		case 3:
			webView.loadUrl(baseUrl + "/home");
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * @author okitsu
	 * 
	 */
	public abstract class ClickAction implements Action {

		final private int mDrawable;

		public ClickAction(int drawable) {
			mDrawable = drawable;
		}

		@Override
		public int getDrawable() {
			return mDrawable;
		}

		@Override
		public void performAction(View view) {
			this.onClick();
		}

		public abstract void onClick();

	}

	public class HistoryManager {
		public HistoryManager() {
		}

		public void goBack() {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				finish();
			}
		}
	}

	/**
	 * C2DM用のRegistrationIdを設定する。
	 */
	public static void setRegistrationId(String regid) {
		registrationId = regid;
	}

}