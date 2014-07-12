package com.appvn.ketquaxoso.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.appvn.ketquaxoso.database.DatabaseHelper;
import com.appvn.ketquaxoso.model.CommentInfo;
import com.appvn.ketquaxoso.model.ItemNewFeed;
import com.facebook.Response;
import com.facebook.model.GraphObject;

public class JsonUtils {
	public static JSONObject parseResponToJson(Response response) {
		JSONObject json = new JSONObject();
		try {
			GraphObject go = response.getGraphObject();
			json = go.getInnerJSONObject();

		} catch (Exception e) {

		}
		return json;
	}

	public static ArrayList<ItemNewFeed> getListItem(JSONObject json,
			ArrayList<ItemNewFeed> list) {
		try {
			JSONArray data = json.getJSONArray("data");
			ItemNewFeed item;
			for (int i = 0; i < data.length(); i++) {
				JSONObject content = data.getJSONObject(i);
				item = new ItemNewFeed();
//				String message = content.getString("message");
//				item.setMessage(message + "");
//
//				if (TextUtils.isEmpty(message)) {
//					continue;
//				}
				JSONObject like_info = content.getJSONObject("like_info");
				if (like_info.has("like_count")) {
					item.setLike_count(like_info.getInt("like_count"));
				}
				item.setTime(Util.convertDate(content.getInt("created_time")));
				item.setPost_id(content.getString("post_id") + "");
				JSONObject attachment = content.getJSONObject("attachment");
				if (attachment.has("media")) {
					JSONArray mediaArray = attachment.getJSONArray("media");
					if (mediaArray.length() > 0) {
						JSONObject media = mediaArray.getJSONObject(0);
						if (media.has("src")) {
							item.setImageBig(media.getString("src"));
						}
					}

				}
				list.add(item);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public static ArrayList<CommentInfo> getCommentInfo(JSONObject json,
			ArrayList<CommentInfo> list) {
		CommentInfo comment;

		try {
			JSONArray data = json.getJSONArray("data");
			JSONObject jcomment = data.getJSONObject(0);
			JSONObject juser = data.getJSONObject(1);
			JSONArray arrayComment = jcomment.getJSONArray("fql_result_set");
			JSONArray arrayUser = juser.getJSONArray("fql_result_set");
			for (int i = 0; i < arrayComment.length(); i++) {
				JSONObject com = arrayComment.getJSONObject(i);
				JSONObject user = arrayUser.getJSONObject(i);
				comment = new CommentInfo();
				comment.setAvatar(user.getString("pic"));
				comment.setComment(com.getString("text"));
				comment.setTime(com.getInt("time"));
				comment.setUid(user.getString("uid"));
				comment.setUsername(user.getString("name"));
				list.add(comment);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public static ArrayList<ItemNewFeed> getNewFeedPhoto(JSONObject json,
			ArrayList<ItemNewFeed> list) {
		ItemNewFeed item = null;

		try {
			JSONArray data = json.getJSONArray("data");
			for (int i = 0; i < data.length(); i++) {
				JSONObject content = data.getJSONObject(i);
				item = new ItemNewFeed();
				item.setMessage(content.getString("caption"));
				item.setImageSmall(content.getString("src"));
				item.setTime(Util.convertDate(content.getInt("created")));
				item.setImageBig(content.getString("src_big"));
				item.setPost_id(content.getString("object_id"));
				item.setLink(content.getString("link"));
				list.add(item);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

}
