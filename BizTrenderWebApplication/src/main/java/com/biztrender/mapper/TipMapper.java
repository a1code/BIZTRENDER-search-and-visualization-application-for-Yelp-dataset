package com.biztrender.mapper;

import java.util.ArrayList;
import java.util.Collection;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.biztrender.dto.Tip;
import com.biztrender.filter.TipFilter;
import com.mongodb.client.model.Filters;

public class TipMapper implements MongoMapper<Tip, TipFilter> {
	public static final String USER_ID = "user_id";
	public static final String BUSINESS_ID = "business_id";
	public static final String TEXT = "text";
	public static final String DATE = "date";
	public static final String COMPLIMENT_COUNT = "compliment_count";

	@Override
	public Bson convertToDao(TipFilter filter) {
		Collection<Bson> filters = new ArrayList<>();
		if (filter.getBusiness_id() != null) {
			filters.add(Filters.eq(BUSINESS_ID, filter.getBusiness_id()));
		}

		return Filters.and(filters);
	}

	@Override
	public Tip convertToDto(Document doc) {
		Tip dto = new Tip();
		if (doc.containsKey(BUSINESS_ID)) {
			dto.setBusiness_id((String) doc.getString(BUSINESS_ID));
		}
		if (doc.containsKey(USER_ID)) {
			dto.setUser_id((String) doc.get(USER_ID));
		}
		if (doc.containsKey(TEXT)) {
			dto.setText((String) doc.get(TEXT));
		}
		if (doc.containsKey(DATE)) {
			dto.setDate((String) doc.get(DATE));
		}
		if (doc.containsKey(COMPLIMENT_COUNT)) {
			dto.setCompliment_count(((Integer) doc.get(COMPLIMENT_COUNT)));
		}

		return dto;
	}
}
