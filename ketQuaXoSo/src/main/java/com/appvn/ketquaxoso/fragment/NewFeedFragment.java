package com.appvn.ketquaxoso.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.appvn.ketquaxoso.R;
import com.appvn.ketquaxoso.adapter.ItemAdapter;
import com.appvn.ketquaxoso.database.DatabaseHelper;
import com.appvn.ketquaxoso.model.ItemNewFeed;
import com.appvn.ketquaxoso.network.MyVolley;
import com.appvn.ketquaxoso.network.NetworkOperator;
import com.appvn.ketquaxoso.util.JsonUtils;
import com.appvn.ketquaxoso.view.FadeInNetworkImageView;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class NewFeedFragment extends Fragment {
	private View mPrent;
	private NetworkOperator operator;
	private View header;
	private FadeInNetworkImageView imgCover;
	private TextView tvTitle, tvMember;
	private String title;
	private int member;
	private String COVER_URL = "";
	private ListView lvList;
	private ProgressBar loading;
	private ItemAdapter adapter;
	private ArrayList<ItemNewFeed> listNew = new ArrayList<ItemNewFeed>();
	private boolean check = false;
	private DatabaseHelper database;
	private AdView adView;
	private Session session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		operator = NetworkOperator.getInstance().init(getActivity());
		database = new DatabaseHelper(getActivity());
		database.getWritableDatabase();
		session=Session.getActiveSession();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mPrent = inflater.inflate(R.layout.activity_main, null);

		header = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.layout_header, null);

		imgCover = (FadeInNetworkImageView) header.findViewById(R.id.imgBanner);
		tvTitle = (TextView) header.findViewById(R.id.tvTitle);
		tvMember = (TextView) header.findViewById(R.id.tvMember);
		lvList = (ListView) mPrent.findViewById(R.id.lvList);
		loading = (ProgressBar) mPrent.findViewById(R.id.loading);
		adView = (AdView) mPrent.findViewById(R.id.ad);
		adView.loadAd(new AdRequest.Builder().build());
		return mPrent;
	}

	public void init() {
		if (check) {
			return;
		}
		getFanpageInfo();
		check = true;
	}

	private void getFanpageInfo() {
		operator.getFanpageInfo(getInfoSuccess(), getInfoError());
	}

	private ErrorListener getInfoError() {
		// TODO Auto-generated method stub
		return new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error, String extraData) {
				// TODO Auto-generated method stub
				error.printStackTrace();

			}
		};
	}

	private Listener<JSONObject> getInfoSuccess() {
		// TODO Auto-generated method stub
		return new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response, String extraData) {
				// TODO Auto-generated method stub
				Log.e("INFO", response.toString());
				try {
					title = response.getString("name");
					member = response.getInt("likes");
					JSONObject cover = response.getJSONObject("cover");
					COVER_URL = cover.getString("source");
					tvTitle.setText("" + title);
					tvMember.setText(getString(R.string.member, member));

					imgCover.setImageUrl(COVER_URL, MyVolley.getImageLoader());
					// imgCover.setImageResource(R.drawable.bg_item);
					getNewFeed();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}

	private void getNewFeed() {
		String fqlQuery = "select object_id,caption,src_big,src,created,link FROM photo WHERE owner = '462803120472407' LIMIT "
				+ 300;
		Bundle params = new Bundle();
		params.putString("q", fqlQuery);

		// session = Session.getActiveSession();
		Request request = new Request(session, "/fql", params, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						JSONObject jso = JsonUtils.parseResponToJson(response);
						Log.e("NEWS", jso.toString());
						loading.setVisibility(View.GONE);
						listNew = JsonUtils.getNewFeedPhoto(jso, listNew);
						//Collections.shuffle(listNew);
						lvList.addHeaderView(header);
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									for (int i = 0; i < listNew.size(); i++) {
										database.addBook(listNew.get(i), listNew
												.get(i).getTime());
									}
								} catch (Exception e) {

								}
							}
						}).start();
						adapter=new ItemAdapter(getActivity(), R.layout.item_layout, listNew);
						lvList.setAdapter(adapter);
						


					}
				});
		Request.executeBatchAsync(request);
	}

	private ErrorListener getError() {
		// TODO Auto-generated method stub
		return new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error, String extraData) {
				// TODO Auto-generated method stub
				error.printStackTrace();
				loading.setVisibility(View.GONE);
			}
		};
	}

	private Listener<JSONObject> getSuccess() {
		// TODO Auto-generated method stub
		return new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response, String extraData) {
				// TODO Auto-generated method stub
				try {
					Log.e("NEWFEED", response.toString());
					loading.setVisibility(View.GONE);
					listNew = JsonUtils.getNewFeedPhoto(response, listNew);
					adapter = new ItemAdapter(getActivity(),
							R.layout.item_layout, listNew);
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								for (int i = 0; i < listNew.size(); i++) {
									database.addBook(listNew.get(i), listNew
											.get(i).getTime());
								}
							} catch (Exception e) {

							}
						}
					}).start();

					lvList.addHeaderView(header);
					lvList.setAdapter(adapter);
				} catch (Exception e) {

				}
			}
		};
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		database.deleteAll();
		super.onDestroy();
	}
}
