package com.biztrender.mapper;

import java.util.ArrayList;
import java.util.Collection;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.biztrender.dto.Checkin;
import com.biztrender.filter.CheckinFilter;
import com.mongodb.client.model.Filters;

public class CheckinMapper implements MongoMapper<Checkin, CheckinFilter> {

	public static final String BUSINESS_ID = "business_id";

	@Override
	public Bson convertToDao(CheckinFilter filter) {
		Collection<Bson> filters = new ArrayList<>();

		return Filters.and(filters);
	}

	@Override
	public Checkin convertToDto(Document doc) {
		Checkin dto = new Checkin();
		if (doc.containsKey(BUSINESS_ID)) {
			dto.setBusinessId((String) doc.get(BUSINESS_ID));
		}
		return dto;
	}
}
