package com.appvn.ketquaxoso.model;

public class ItemNewFeed {
	private String message;
	private String post_id;
	private String imageBig;
	private String imageSmall;
	private String defaut_image;
	private String time;
	private int like_count;
	private String link;

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImageSmall() {
		return imageSmall;
	}

	public void setImageSmall(String imageSmall) {
		this.imageSmall = imageSmall;
	}

	public int getLike_count() {
		return like_count;
	}

	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public String getImageBig() {
		return imageBig;
	}

	public void setImageBig(String image) {
		this.imageBig = image;
	}

	public String getDefaut_image() {
		return defaut_image;
	}

	public void setDefaut_image(String defaut_image) {
		this.defaut_image = defaut_image;
	}

}
