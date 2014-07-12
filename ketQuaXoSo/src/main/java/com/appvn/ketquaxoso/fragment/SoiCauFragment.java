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
import com.appvn.ketquaxoso.adapter.ItemSoiCauAdapter;
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

public class SoiCauFragment extends Fragment {
	private View mParent;
	private NetworkOperator operator;
	private View header;
	private FadeInNetworkImageView imgCover;
	private TextView tvTitle, tvMember;
	private String title;
	private int member;
	private String COVER_URL = "";
	private ListView lvList;
	private ProgressBar loading;
	private ItemSoiCauAdapter adapter;
	private ArrayList<ItemNewFeed> listNew = new ArrayList<ItemNewFeed>();
	private boolean check = false;
	private Session session;
	private AdView adView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		operator = NetworkOperator.getInstance().init(getActivity());
		session = Session.getActiveSession();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mParent = inflater.inflate(R.layout.activity_main, null);

		header = ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.layout_header, null);
		imgCover = (FadeInNetworkImageView) header.findViewById(R.id.imgBanner);
		tvTitle = (TextView) header.findViewById(R.id.tvTitle);
		tvMember = (TextView) header.findViewById(R.id.tvMember);
		lvList = (ListView) mParent.findViewById(R.id.lvList);
		loading = (ProgressBar) mParent.findViewById(R.id.loading);
		adView = (AdView) mParent.findViewById(R.id.ad);
		adView.loadAd(new AdRequest.Builder().build());
		return mParent;
	}

	public void init() {
		if (check) {
			return;
		}
		getFanpageInfo();
		check = true;
	}

	private void getFanpageInfo() {
		operator.getFanpageInfoSoiCau(getInfoSuccess(), getInfoError());
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
					getSoiCau();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}

	private void getSoiCau() {

		String fqlQuery = "SELECT post_id, message, attachment,created_time,like_info FROM stream WHERE source_id = '268593233235338' LIMIT " + 300;
		// String fqlQuery = Constants.QUERY_INFO+limit;
		Bundle params = new Bundle();
		params.putString("q", fqlQuery);

		// session = Session.getActiveSession();
		Request request = new Request(session, "/fql", params, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						JSONObject jso = JsonUtils.parseResponToJson(response);
						Log.e("SOICAI", jso.toString());
						// Util.writetoFile(jso.toString(), "TUVI");
						loading.setVisibility(View.GONE);
						listNew = JsonUtils.getListItem(jso, listNew);
						adapter = new ItemSoiCauAdapter(getActivity(),
								R.layout.item_layout_soicau, listNew);
						lvList.addHeaderView(header);
						lvList.setAdapter(adapter);
						// Log.e("LIST_SIZE", listItem.size() + "");

						// Log.e("NEW", jso.toString());

					}
				});
		Request.executeBatchAsync(request);
	}

}
