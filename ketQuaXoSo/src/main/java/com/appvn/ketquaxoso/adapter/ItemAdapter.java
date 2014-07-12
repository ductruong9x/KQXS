package com.appvn.ketquaxoso.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appvn.ketquaxoso.R;
import com.appvn.ketquaxoso.activity.DetailActivity;
import com.appvn.ketquaxoso.model.ItemNewFeed;
import com.appvn.ketquaxoso.network.MyVolley;
import com.appvn.ketquaxoso.view.FadeInNetworkImageView;

public class ItemAdapter extends ArrayAdapter<ItemNewFeed> {

	private Context context;
	private ArrayList<ItemNewFeed> listItem = null;
	private Typeface font_light;

	public ItemAdapter(Context context, int resource,
			ArrayList<ItemNewFeed> listItem) {
		super(context, resource, listItem);
		this.context = context;
		this.listItem = listItem;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.imgItem = (FadeInNetworkImageView) convertView
					.findViewById(R.id.imgItem);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tvDate);
			viewHolder.btnView = (Button) convertView
					.findViewById(R.id.btnView);
			viewHolder.loading = (ProgressBar) convertView
					.findViewById(R.id.loadingItem);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tvContent);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.loading.setVisibility(View.GONE);
		final ItemNewFeed item = getItem(position);
		viewHolder.btnView.setTypeface(font_light);
		viewHolder.tvContent.setTypeface(font_light);
		viewHolder.tvContent.setText(item.getMessage() + "");
		Linkify.addLinks(viewHolder.tvContent, Linkify.ALL);
		viewHolder.tvTime.setText(item.getTime());
		viewHolder.imgItem.setImageUrl(item.getImageBig(),
				MyVolley.getImageLoader());
		viewHolder.btnView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context, DetailActivity.class);
				i.putExtra("URL_IMAGE", item.getImageBig());
				i.putExtra("LINK", getItem(position).getLink());
				context.startActivity(i);
			}
		});
		viewHolder.imgItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context, DetailActivity.class);
				i.putExtra("URL_IMAGE", item.getImageBig());
				i.putExtra("LINK", getItem(position).getLink());
				context.startActivity(i);
			}
		});
		return convertView;
	}

	private class ViewHolder {
		private TextView tvTime, tvContent;
		private FadeInNetworkImageView imgItem;
		private Button btnView;
		private ProgressBar loading;

	}

}
