package com.biztrender.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.biztrender.dto.Review;
import com.biztrender.filter.ReviewFilter;
import com.mongodb.client.model.Filters;

public class ReviewMapper implements MongoMapper<Review, ReviewFilter> {

	public static final String REVIEW_ID = "review_id";
	public static final String USER_ID = "user_id";
	public static final String BUSINESS_ID = "business_id";
	public static final String STARS = "stars";
	public static final String USEFUL = "useful";
	public static final String FUNNY = "funny";
	public static final String COOL = "cool";
	public static final String TEXT = "text";
	public static final String DATE = "date";
	public static final String CATEGORIES = "categories";

	@Override
	public Bson convertToDao(ReviewFilter filter) {
		Collection<Bson> filters = new ArrayList<>();
		if (filter.getBusiness_id() != null) {
			filters.add(Filters.eq(BUSINESS_ID, filter.getBusiness_id()));
		}

		if (filter.getCategory() != null) {
			List<String> categories = new ArrayList<String>(Arrays.asList(filter.getCategory().split(",")));
			Collection<Bson> filters_categories = new ArrayList<>();
			categories
					.forEach(item -> filters_categories.add(Filters.regex(CATEGORIES.toLowerCase(), "." + item + ".")));
			filters.add(Filters.or(filters_categories));
		}

		return Filters.and(filters);
	}

	@Override
	public Review convertToDto(Document doc) {
		Review dto = new Review();
		if (doc.containsKey(BUSINESS_ID)) {
			dto.setBusiness_id((String) doc.getString(BUSINESS_ID));
		}
		if (doc.containsKey(USER_ID)) {
			dto.setUser_id((String) doc.get(USER_ID));
		}
		if (doc.containsKey(REVIEW_ID)) {
			dto.setReview_id(((String) doc.get(REVIEW_ID)));
		}
		if (doc.containsKey(STARS)) {
			dto.setStars((Double) doc.get(STARS));
		}
		if (doc.containsKey(USEFUL)) {
			dto.setUseful((Integer) doc.get(USEFUL));
		}
		if (doc.containsKey(FUNNY)) {
			dto.setFunny(((Integer) doc.get(FUNNY)));
		}
		if (doc.containsKey(COOL)) {
			dto.setCool((Integer) doc.get(COOL));
		}
		if (doc.containsKey(TEXT)) {
			dto.setText((String) doc.get(TEXT));
		}
		if (doc.containsKey(DATE)) {
			dto.setDate((String) doc.get(DATE));
		}

		return dto;
	}
}
