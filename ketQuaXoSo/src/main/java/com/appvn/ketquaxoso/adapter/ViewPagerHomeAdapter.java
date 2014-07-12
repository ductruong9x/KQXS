package com.appvn.ketquaxoso.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.appvn.ketquaxoso.R;
import com.appvn.ketquaxoso.fragment.NewFeedFragment;
import com.appvn.ketquaxoso.fragment.SoiCauFragment;

public class ViewPagerHomeAdapter extends FragmentPagerAdapter {
	private Context context;
	private String[] CONTENT;
	private String time, content, post_id;

	public ViewPagerHomeAdapter(Context context, FragmentManager fm) {

		super(fm);
		this.context = context;
		CONTENT = new String[] { context.getString(R.string.ketqua),
				context.getString(R.string.soicau) };
	}

	Fragment fragment;

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.setPrimaryItem(container, position, object);
		if (position == 0) {
			NewFeedFragment detialFragment = (NewFeedFragment) object;
			detialFragment.init();
		} else if (position == 1) {
			SoiCauFragment commentFragment = (SoiCauFragment) object;
			commentFragment.init();
		}

	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			fragment = new NewFeedFragment();

			break;
		case 1:
			fragment = new SoiCauFragment();
			break;

		default:
			break;
		}
		return fragment;

	}

	@Override
	public CharSequence getPageTitle(int position) {
		return CONTENT[position];
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return CONTENT.length;
	}

}