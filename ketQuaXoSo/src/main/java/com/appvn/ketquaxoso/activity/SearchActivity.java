package com.appvn.ketquaxoso.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appvn.ketquaxoso.R;
import com.appvn.ketquaxoso.adapter.ItemAdapter;
import com.appvn.ketquaxoso.database.DatabaseHelper;
import com.appvn.ketquaxoso.model.ItemNewFeed;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class SearchActivity extends Activity {
	private ActionBar actionBar;
	private String time;
	private ArrayList<ItemNewFeed> listSearch = new ArrayList<ItemNewFeed>();
	private ItemAdapter adapter;
	private ListView lvSearch;
	private DatabaseHelper database;
	private ProgressBar loading;
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.app_color);
			tintManager.setNavigationBarTintResource(R.color.app_color);
		}
		setContentView(R.layout.activity_main);
		actionBar = getActionBar();
		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setTitle(Html.fromHtml("<font color='#ffffff' size='25'>"
				+ getString(R.string.app_name) + "</font>"));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#950a11")));
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		lvSearch = (ListView) findViewById(R.id.lvList);
		loading = (ProgressBar) findViewById(R.id.loading);
		adView = (AdView) findViewById(R.id.ad);
		adView.loadAd(new AdRequest.Builder().build());
		Intent intent = getIntent();
		time = intent.getStringExtra("TIME_SEARCH");

		database = new DatabaseHelper(this);
		listSearch = database.getSearchData(listSearch, time);
		adapter = new ItemAdapter(this, R.layout.item_layout, listSearch);
		lvSearch.setAdapter(adapter);

		loading.setVisibility(View.GONE);

		if (listSearch.size() == 0) {
			Toast.makeText(this, time, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
