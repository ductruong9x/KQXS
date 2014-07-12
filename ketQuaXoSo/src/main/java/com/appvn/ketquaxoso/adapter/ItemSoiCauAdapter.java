package com.appvn.ketquaxoso.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appvn.ketquaxoso.R;
import com.appvn.ketquaxoso.model.ItemNewFeed;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class ItemSoiCauAdapter extends ArrayAdapter<ItemNewFeed> {

	private Context context;
	private ArrayList<ItemNewFeed> listItem = null;
	private Typeface font_light;

	public ItemSoiCauAdapter(Context context, int resource,
			ArrayList<ItemNewFeed> listItem) {
		super(context, resource, listItem);
		this.context = context;
		this.listItem = listItem;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_layout_soicau, null);
			viewHolder = new ViewHolder();

			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tvDate);
			viewHolder.btnView = (RelativeLayout) convertView
					.findViewById(R.id.btnView);

			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tvContent);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final ItemNewFeed item = getItem(position);
		viewHolder.tvContent.setTypeface(font_light);
		viewHolder.tvContent.setText(item.getMessage() + "");

		Linkify.addLinks(viewHolder.tvContent, Linkify.ALL);
		viewHolder.tvTime.setText(item.getTime());
		viewHolder.btnView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle params = new Bundle();
				params.putString("name", "Kết quả xổ số-MB");
				params.putString("caption", "Xem thêm");
				params.putString("description", item.getMessage());
				params.putString("link",
						"https://play.google.com/store/apps/details?id=com.truongtvd.ketquaxoso");

				WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(
						context, Session.getActiveSession(), params))
						.setOnCompleteListener(new OnCompleteListener() {

							@Override
							public void onComplete(Bundle values,
									FacebookException error) {
								if (error == null) {
									// When the story is posted, echo the
									// success
									// and the post Id.
									final String postId = values
											.getString("post_id");
									if (postId != null) {
										Toast.makeText(context,
												"Posted story, id: " + postId,
												Toast.LENGTH_SHORT).show();
									} else {
										// User clicked the Cancel button
										Toast.makeText(context,
												"Publish cancelled",
												Toast.LENGTH_SHORT).show();
									}
								} else if (error instanceof FacebookOperationCanceledException) {
									// User clicked the "x" button
									Toast.makeText(context,
											"Publish cancelled",
											Toast.LENGTH_SHORT).show();
								} else {
									// Generic, ex: network error
									Toast.makeText(context,
											"Error posting story",
											Toast.LENGTH_SHORT).show();
								}
							}

						}).build();
				feedDialog.show();
			}
		});

		return convertView;
	}

	private class ViewHolder {
		private TextView tvTime, tvContent;
		private RelativeLayout btnView;

	}

}
