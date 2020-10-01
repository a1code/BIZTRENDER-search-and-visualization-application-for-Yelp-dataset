package com.biztrender.dto;

public class Tip extends AbstractDTO {
	private String user_id;
	private String business_id;
	private String text;
	private String date;
	private Integer compliment_count;

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

	public Integer getCompliment_count() {
		return compliment_count;
	}

	public void setCompliment_count(Integer compliment_count) {
		this.compliment_count = compliment_count;
	}
}