package com.appvn.ketquaxoso.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.appvn.ketquaxoso.R;
import com.appvn.ketquaxoso.network.MyVolley;
import com.appvn.ketquaxoso.view.FadeInNetworkImageView;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.startapp.android.publish.StartAppAd;

import java.util.Arrays;

public class DetailActivity extends Activity {

	private FadeInNetworkImageView imgFull;
	private String URL_IMAGE = "";
	private String link = "";
	private ImageButton btnBack, btnShare;
    private StartAppAd startAppAd;
	private ProgressDialog dialog;

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
		setContentView(R.layout.detail_activity);
        startAppAd=new StartAppAd(this);
        startAppAd.loadAd();
		dialog = new ProgressDialog(this);
		dialog.setMessage("Sharing...");
		imgFull = (FadeInNetworkImageView) findViewById(R.id.imgFull);
		Intent intent = getIntent();
		URL_IMAGE = intent.getStringExtra("URL_IMAGE");
		link = intent.getStringExtra("LINK");
		imgFull.setImageUrl(URL_IMAGE, MyVolley.getImageLoader());
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnShare = (ImageButton) findViewById(R.id.btnShare);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();

				
			}
		});
		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// shareWeb();
				// shareIt();

				try {
					if (!Session.getActiveSession().getPermissions()
							.contains("publish_actions")) {
						NewPermissionsRequest request = new NewPermissionsRequest(
								DetailActivity.this, Arrays
										.asList("publish_actions"));
						request.setCallback(new StatusCallback() {

							@Override
							public void call(Session session,
									SessionState state, Exception exception) {
								// TODO Auto-generated method stub

							}
						});

						Session.getActiveSession()
								.requestNewPublishPermissions(request);
						return;
					}
				} catch (Exception e) {

				}
				dialog.show();
				Bundle postParams = new Bundle();
				postParams.putString("name", "Kết quả xổ số");
				postParams
						.putString(
								"message",
								"Link tải ứng dụng kết quả xổ số cho Android: "
										+ "https://play.google.com/store/apps/details?id=com.truongtvd.ketquaxoso");
				postParams.putString("description",
						"Xem kết quả xổ số trên Android");
				postParams.putString("link", link);

				Request.Callback callback = new Request.Callback() {
					public void onCompleted(Response response) {
						dialog.dismiss();
						Toast.makeText(DetailActivity.this,
								"Share successfuly	", Toast.LENGTH_SHORT)
								.show();
					}
				};

				Request request = new Request(Session.getActiveSession(),
						"me/feed", postParams, HttpMethod.POST, callback);

				RequestAsyncTask task = new RequestAsyncTask(request);
				task.execute();
			}
		});


	}

	private void shareWeb() {
		Bundle params = new Bundle();
		params.putString("name", "Kết quả xổ số-MB");
		params.putString("caption", "Xem thêm");
		params.putString("description", MyApplication.getContent());
		params.putString("link",
				"https://play.google.com/store/apps/details?id=com.truongtvd.ketquaxoso");

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(
				DetailActivity.this, Session.getActiveSession(), params))
				.setOnCompleteListener(new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the
							// success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(getApplicationContext(),
										"Posted story, id: " + postId,
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								Toast.makeText(DetailActivity.this,
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							Toast.makeText(getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}
					}

				}).build();
		feedDialog.show();
	}

	private void shareIt() {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Tử vi hàng ngày");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, URL_IMAGE);

		startActivity(Intent.createChooser(sharingIntent,
				MyApplication.getContent()));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(DetailActivity.this,
				requestCode, resultCode, data);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
        startAppAd.onBackPressed();
		super.onBackPressed();
	}
}
