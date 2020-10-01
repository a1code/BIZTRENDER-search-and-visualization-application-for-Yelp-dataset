package com.biztrender.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.biztrender.dto.Business;
import com.biztrender.filter.BusinessFilter;
import com.mongodb.client.model.Filters;

public class BusinessMapper implements MongoMapper<Business, BusinessFilter> {

	public static final String BUSINESS_ID = "business_id";
	public static final String CITY = "city";
	public static final String NAME = "name";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String STATE = "state";
	public static final String POSTAL_CODE = "postal_code";
	public static final String STARS = "stars";
	public static final String ADDRESS = "address";
	public static final String IS_OPEN = "is_open";
	public static final String CATEGORIES = "categories";

	@Override
	public Bson convertToDao(BusinessFilter filter) {
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

		if (filter.getRating() != null) {
			List<String> ratings = new ArrayList<String>(Arrays.asList(filter.getRating().split(",")));
			ratings.forEach(item -> Double.parseDouble(item));
			String max = Collections.max(ratings);
			String min = Collections.min(ratings);
			filters.add(Filters.lte(STARS, Double.parseDouble(max) + 1.0));
			filters.add(Filters.gt(STARS, Double.parseDouble(min)));
		}

		return Filters.and(filters);
	}

	@Override
	public Business convertToDto(Document doc) {
		Business dto = new Business();
		if (doc.containsKey(BUSINESS_ID)) {
			dto.setBusiness_id((String) doc.get(BUSINESS_ID));
		}
		if (doc.containsKey(CITY)) {
			dto.setCity((String) doc.get(CITY));
		}
		if (doc.containsKey(NAME)) {
			dto.setName((String) doc.get(NAME));
		}
		if (doc.containsKey(LATITUDE)) {
			dto.setLatitude(((Double) doc.get(LATITUDE)));
		}
		if (doc.containsKey(LONGITUDE)) {
			dto.setLongitude((Double) doc.get(LONGITUDE));
		}
		if (doc.containsKey(STATE)) {
			dto.setState((String) doc.get(STATE));
		}
		if (doc.containsKey(POSTAL_CODE)) {
			dto.setPostal_code((String) doc.get(POSTAL_CODE));
		}
		if (doc.containsKey(STARS)) {
			dto.setStars((Double) doc.get(STARS));
		}
		if (doc.containsKey(ADDRESS)) {
			dto.setAddress((String) doc.get(ADDRESS));
		}
		if (doc.containsKey(IS_OPEN)) {
			dto.setIs_open((Integer) doc.get(IS_OPEN));
		}
		if (doc.containsKey(CATEGORIES)) {
			dto.setCategories((String) doc.get(CATEGORIES));
		}

		return dto;
	}
}
