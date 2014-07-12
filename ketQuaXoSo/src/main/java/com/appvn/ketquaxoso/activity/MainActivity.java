package com.appvn.ketquaxoso.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.appvn.ketquaxoso.R;
import com.appvn.ketquaxoso.adapter.ViewPagerHomeAdapter;
import com.appvn.ketquaxoso.service.LocalPushService;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.viewpagerindicator.PagerSlidingTabStrip;

import java.util.Calendar;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {
	private ActionBar actionBar;
	private PagerSlidingTabStrip mIndicator;
	private ViewPagerHomeAdapter adapter;
	private ViewPager vpMain;
	private InterstitialAd interstitialAd;
	private String UNIT_ID="ca-app-pub-1857950562418699/1374631568";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.app_color);
			tintManager.setNavigationBarTintResource(R.color.app_color);
		}
		setContentView(R.layout.main);
		pushLocal();

		actionBar = getActionBar();
		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setTitle(Html.fromHtml("<font color='#ffffff' size='25'>"
				+ getString(R.string.app_name) + "</font>"));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#950a11")));
		interstitialAd=new InterstitialAd(this);
		interstitialAd.setAdUnitId(UNIT_ID);
		interstitialAd.loadAd(new AdRequest.Builder().build());
		
		
		vpMain = (ViewPager) findViewById(R.id.vpMain);
		mIndicator = (PagerSlidingTabStrip) findViewById(R.id.indicatorTabHome);
        mIndicator.setBackgroundResource(R.color.app_color);
		mIndicator.setIndicatorColor(Color.WHITE);
		mIndicator.setTextColor(Color.WHITE);
		adapter = new ViewPagerHomeAdapter(MainActivity.this,
				getSupportFragmentManager());
		vpMain.setAdapter(adapter);
		vpMain.setCurrentItem(0);
		mIndicator.setAllCaps(false);
		mIndicator.setIndicatorHeight(6);
		mIndicator.setViewPager(vpMain);
		danhGia();
	}

	public void pushLocal() {
		Intent intent = new Intent(MainActivity.this, LocalPushService.class);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getService(
				MainActivity.this, 0, intent, 0);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 18);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 00);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case R.id.invate:
			sendRequestDialog();
			Toast.makeText(MainActivity.this, "Invate friend from Facebook",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.rate:
			Toast.makeText(MainActivity.this, "Thank you", Toast.LENGTH_SHORT)
					.show();
			Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri
					.parse("market://details?id=" + getPackageName()));
			startActivity(goToMarket);
			break;
		case R.id.devinfo:

			Intent goMoreApp = new Intent(Intent.ACTION_VIEW)
					.setData(Uri
							.parse("https://play.google.com/store/apps/developer?id=App+Entertainment"));
			startActivity(goMoreApp);
			break;
		case R.id.search:
			MyDatePicker dialog = new MyDatePicker();
			dialog.show(getSupportFragmentManager(), "datePicker");
			break;
		default:
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	/*
	 * date time picker
	 */

	@SuppressLint("ValidFragment")
	private class MyDatePicker extends DialogFragment {
		int pyear, pmonth, pday;
		DatePickerDialog dialog;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub

			Calendar cal = Calendar.getInstance();
			pyear = cal.get(Calendar.YEAR);
			pmonth = cal.get(Calendar.MONTH);
			pday = cal.get(Calendar.DAY_OF_MONTH);
			dialog = new DatePickerDialog(MainActivity.this, mCallback, pyear,
					pmonth, pday);
			dialog.setCancelable(false);

			dialog.setCanceledOnTouchOutside(false);
			dialog.setButton(dialog.BUTTON_NEUTRAL, "Xong",
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							updateDisplay();
						}
					});
			dialog.setButton(dialog.BUTTON_POSITIVE, "Hủy",
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});

			return dialog;
		}

		OnDateSetListener mCallback = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub

				pday = dayOfMonth;
				pmonth = monthOfYear;
				pyear = year;

			}
		};

		private void updateDisplay() {

			StringBuilder time = null;
			if (dialog.getDatePicker().getMonth() >= 9) {
				time = new StringBuilder()
						.append(dialog.getDatePicker().getDayOfMonth())
						.append("/")
						.append(dialog.getDatePicker().getMonth() + 1)
						.append("/").append(dialog.getDatePicker().getYear());
			} else {
				time = new StringBuilder()
						.append(dialog.getDatePicker().getDayOfMonth())
						.append("/").append("0")
						.append(dialog.getDatePicker().getMonth() + 1)
						.append("/").append(dialog.getDatePicker().getYear());
			}

			Intent intent = new Intent(MainActivity.this, SearchActivity.class);
			intent.putExtra("TIME_SEARCH", time.toString());
			startActivity(intent);

		}
	}

	private void sendRequestDialog() {
		Bundle params = new Bundle();
		params.putString("message",
				"Kết quả xổ số nhanh chính xác. Soi cầu hàng ngày");

		WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(
				MainActivity.this, Session.getActiveSession(), params))
				.setOnCompleteListener(new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error != null) {
							if (error instanceof FacebookOperationCanceledException) {
								Toast.makeText(
										MainActivity.this
												.getApplicationContext(),
										"Request cancelled", Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(
										MainActivity.this
												.getApplicationContext(),
										"Network Error", Toast.LENGTH_SHORT)
										.show();
							}
						} else {
							final String requestId = values
									.getString("request");
							if (requestId != null) {
								Toast.makeText(
										MainActivity.this
												.getApplicationContext(),
										"Request sent", Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(
										MainActivity.this
												.getApplicationContext(),
										"Request cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						}
					}

				}).build();
		requestsDialog.show();
	}

	public void danhGia() {
		SharedPreferences getPre = getSharedPreferences("SAVE", MODE_PRIVATE);
		int i = getPre.getInt("VOTE", 0);
		SharedPreferences pre;
		SharedPreferences.Editor edit;
		switch (i) {
		case 0:
			pre = getSharedPreferences("SAVE", MODE_PRIVATE);
			edit = pre.edit();
			edit.putInt("VOTE", 1);
			edit.commit();
			break;
		case 1:
			pre = getSharedPreferences("SAVE", MODE_PRIVATE);
			edit = pre.edit();
			edit.putInt("VOTE", i + 1);
			edit.commit();
			break;
		case 2:
			pre = getSharedPreferences("SAVE", MODE_PRIVATE);
			edit = pre.edit();
			edit.putInt("VOTE", i + 1);
			edit.commit();
			break;
		case 3:
			pre = getSharedPreferences("SAVE", MODE_PRIVATE);
			edit = pre.edit();
			edit.putInt("VOTE", i + 1);
			edit.commit();
			break;
		case 4:
			pre = getSharedPreferences("SAVE", MODE_PRIVATE);
			edit = pre.edit();
			edit.putInt("VOTE", i + 1);
			edit.commit();
			break;
		case 5:
			DialogVote dialog = new DialogVote(MainActivity.this);
			dialog.show();
			pre = getSharedPreferences("SAVE", MODE_PRIVATE);
			edit = pre.edit();
			edit.putInt("VOTE", 5);
			edit.commit();
			break;
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		interstitialAd.show();
		super.onBackPressed();
	}
}
