package com.appvn.ketquaxoso.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.appvn.ketquaxoso.R;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class StartActivity extends Activity {
	private LoginButton btnLogin;
	private UiLifecycleHelper uiHelper;
	private StatusCallback mCallBack = new StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.app_color);
			tintManager.setNavigationBarTintResource(R.color.app_color);
		}
		setContentView(R.layout.activity_start);
		uiHelper = new UiLifecycleHelper(this, mCallBack);
		uiHelper.onCreate(savedInstanceState);
		btnLogin = (LoginButton) findViewById(R.id.authButton);
		//btnLogin.setReadPermissions(Arrays.asList("user_likes", "user_status"));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			Intent intent = new Intent(StartActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

		uiHelper.onResume();
	}
}
