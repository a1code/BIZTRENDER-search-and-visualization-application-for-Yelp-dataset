package com.biztrender.dto;

public class Review extends AbstractDTO {
	private String review_id;
	private String user_id;
	private String business_id;
	private Double stars;
	private Integer useful;
	private Integer funny;
	private Integer cool;
	private String text;
	private String date;

	public String getReview_id() {
		return review_id;
	}

	public void setReview_id(String review_id) {
		this.review_id = review_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public Double getStars() {
		return stars;
	}

	public void setStars(Double stars) {
		this.stars = stars;
	}

	public Integer getUseful() {
		return useful;
	}

	public void setUseful(Integer useful) {
		this.useful = useful;
	}

	public Integer getFunny() {
		return funny;
	}

	public void setFunny(Integer funny) {
		this.funny = funny;
	}

	public Integer getCool() {
		return cool;
	}

	public void setCool(Integer cool) {
		this.cool = cool;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}