package com.biztrender.dao.mongo;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.biztrender.dto.Business;
import com.biztrender.dto.Checkin;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.factory.MongoFactory;
import com.biztrender.filter.BusinessFilter;
import com.biztrender.mapper.BusinessMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;

public class BusinessMongoDao extends AbstractMongoDao<Business, BusinessFilter, BusinessMapper> {

	public BusinessMongoDao(MongoFactory factory) throws BizTrenderException {
		super(factory, "business", BusinessMapper.class);
	}

	public List<Business> findByCategoryAndRating(BusinessFilter filter) throws BizTrenderException, ParseException {
		List<Business> result = new ArrayList<>();

		List<Bson> pipeline = new ArrayList<>();

		Bson findObj = getMapper().convertToDao(filter);
		pipeline.add(Aggregates.match(findObj));

		MongoCursor<Document> cur = getCollection().aggregate(pipeline).allowDiskUse(true).iterator();
		while (cur.hasNext()) {
			Document doc = (Document) cur.next();
			result.add(getMapper().convertToDto(doc));
		}

		return result;
	}

	public List<Business> findWithAllRatingsByCategory(BusinessFilter filter)
			throws BizTrenderException, ParseException {
		List<Business> result = new ArrayList<>();

		String[] categories = filter.getCategory().split(",");

		List<Bson> pipeline = new ArrayList<>();

		Bson findObj = getMapper().convertToDao(filter);
		pipeline.add(Aggregates.match(findObj));

		MongoCursor<Document> cur = getCollection().aggregate(pipeline).allowDiskUse(true).iterator();
		while (cur.hasNext()) {
			Document doc = (Document) cur.next();

			Business business = getMapper().convertToDto(doc);

			for (int i = 0; i < categories.length; i++) {
				if (business.getCategories().contains(categories[i])) {
					business.setCategories(categories[i]);
					break;
				}
			}
			business.setStars(Math.floor(business.getStars()) - 1.0);
			result.add(business);
		}

		return result;
	}

	public List<Business> findWithCheckinsByCategory(BusinessFilter filter) throws BizTrenderException, ParseException {
		List<Business> result = new ArrayList<>();

		String[] categories = filter.getCategory().split(",");

		List<Bson> pipeline = new ArrayList<>();

		Bson findObj = getMapper().convertToDao(filter);
		pipeline.add(Aggregates.match(findObj));
		pipeline.add(Aggregates.lookup("checkin", "business_id", "business_id", "checkin"));
		pipeline.add(Aggregates.unwind("$checkin"));

		MongoCursor<Document> cur = getCollection().aggregate(pipeline).allowDiskUse(true).iterator();
		while (cur.hasNext()) {
			Document doc = (Document) cur.next();

			Business business = getMapper().convertToDto(doc);

			Document checkin = (Document) doc.get("checkin");
			business.setCheckinCount((((String) checkin.get("date")).split(", ").length));

			for (int i = 0; i < categories.length; i++) {
				if (business.getCategories().contains(categories[i])) {
					business.setCategories(categories[i]);
					break;
				}
			}

			result.add(business);
		}

		return result;
	}

	public StreamingResponseBody findReviewSentimentVsTimeByCategoryAndRating(BusinessFilter filter)
			throws BizTrenderException, ParseException {
		List<Bson> pipeline = new ArrayList<>();

		Bson findObj = getMapper().convertToDao(filter);
		pipeline.add(Aggregates.match(findObj));

		pipeline.add(Aggregates.lookup("review", "business_id", "business_id", "review"));

		MongoCursor<Document> cur = getCollection().aggregate(pipeline).allowDiskUse(true).cursor();

		return new StreamingResponseBody() {
			@Override
			public void writeTo(OutputStream outputStream) throws IOException {
				JsonFactory jsonFactory = new JsonFactory();
				JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
				ObjectMapper jsonMapper = Jackson2ObjectMapperBuilder.json().build();
				jsonGenerator.writeStartArray();

				while (cur.hasNext()) {
					Document doc = (Document) cur.next();
					List<Document> list_review_res = ((ArrayList<Document>) doc.get("review"));

					if (list_review_res != null && !list_review_res.isEmpty()) {
						list_review_res.forEach(x -> {
							Double stars = (Double) x.get("stars");
							String dateString = (String) x.get("date");

							if (dateString != null && !dateString.isEmpty()) {
								Map<String, String> scoreVsTime = new HashMap<>();
								scoreVsTime.put(dateString, String.format("%.2f", stars));

								try {
									jsonMapper.writeValue(jsonGenerator, scoreVsTime);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});
					}

					jsonGenerator.flush();
				}
				jsonGenerator.writeEndArray();
			}
		};
	}

	public StreamingResponseBody findAllCheckinsByCategoryAndRating(BusinessFilter filter)
			throws BizTrenderException, ParseException {

		Map<Integer, String> days = new HashMap<>();
		days.putIfAbsent(0, "Sunday");
		days.putIfAbsent(1, "Monday");
		days.putIfAbsent(2, "Tuesday");
		days.putIfAbsent(3, "Wednesday");
		days.putIfAbsent(4, "Thursday");
		days.putIfAbsent(5, "Friday");
		days.putIfAbsent(6, "Saturday");

		Map<Integer, String> times = new HashMap<>();
		times.putIfAbsent(0, "12:00 to 12:59 AM");
		times.putIfAbsent(1, "01:00 to 01:59 AM");
		times.putIfAbsent(2, "02:00 to 02:59 AM");
		times.putIfAbsent(3, "03:00 to 03:59 AM");
		times.putIfAbsent(4, "04:00 to 04:59 AM");
		times.putIfAbsent(5, "05:00 to 05:59 AM");
		times.putIfAbsent(6, "06:00 to 06:59 AM");
		times.putIfAbsent(7, "07:00 to 07:59 AM");
		times.putIfAbsent(8, "08:00 to 08:59 AM");
		times.putIfAbsent(9, "09:00 to 09:59 AM");
		times.putIfAbsent(10, "10:00 to 10:59 AM");
		times.putIfAbsent(11, "11:00 to 11:59 AM");
		times.putIfAbsent(12, "12:00 to 12:59 PM");
		times.putIfAbsent(13, "01:00 to 01:59 PM");
		times.putIfAbsent(14, "02:00 to 02:59 PM");
		times.putIfAbsent(15, "03:00 to 03:59 PM");
		times.putIfAbsent(16, "04:00 to 04:59 PM");
		times.putIfAbsent(17, "05:00 to 05:59 PM");
		times.putIfAbsent(18, "06:00 to 06:59 PM");
		times.putIfAbsent(19, "07:00 to 07:59 PM");
		times.putIfAbsent(20, "08:00 to 08:59 PM");
		times.putIfAbsent(21, "09:00 to 09:59 PM");
		times.putIfAbsent(22, "10:00 to 10:59 PM");
		times.putIfAbsent(23, "11:00 to 11:59 PM");

		String[] categories = filter.getCategory().split(",");

		List<Bson> pipeline = new ArrayList<>();

		Bson findObj = getMapper().convertToDao(filter);
		pipeline.add(Aggregates.match(findObj));
		pipeline.add(Aggregates.lookup("checkin", "business_id", "business_id", "checkin"));

		MongoCursor<Document> cur = getCollection().aggregate(pipeline).allowDiskUse(true).iterator();

		return new StreamingResponseBody() {
			@SuppressWarnings("deprecation")
			@Override
			public void writeTo(OutputStream outputStream) throws IOException {
				JsonFactory jsonFactory = new JsonFactory();
				JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
				ObjectMapper jsonMapper = Jackson2ObjectMapperBuilder.json().build();
				jsonGenerator.writeStartArray();

				while (cur.hasNext()) {
					Document doc = (Document) cur.next();
					List<Document> list_checkin_res = ((ArrayList<Document>) doc.get("checkin"));

					if (list_checkin_res != null && !list_checkin_res.isEmpty()) {
						Document checkin_res = list_checkin_res.get(0);

						String businessId = (String) checkin_res.get("business_id");

						String dateString = (String) checkin_res.get("date");

						if (dateString != null && !dateString.isEmpty()) {
							List<String> dates = Arrays.asList(dateString.split(", "));
							dates.forEach(x -> {
								Date date = null;
								try {
									date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(x);
								} catch (ParseException e1) {
									e1.printStackTrace();
								}

								if (date != null) {
									Checkin checkin = new Checkin();
									checkin.setBusinessId(businessId);
									checkin.setDay(days.get(date.getDay()).toString());
									checkin.setHour(times.get(date.getHours()).toString());

									for (int i = 0; i < categories.length; i++) {
										if (checkin.getCategories() == null
												|| checkin.getCategories().contains(categories[i])) {
											checkin.setCategories(categories[i]);
											break;
										}
									}

									try {
										jsonMapper.writeValue(jsonGenerator, checkin.toMap());
									} catch (IOException | BizTrenderException e) {
										e.printStackTrace();
									}
								}
							});
						}
					}

					jsonGenerator.flush();
				}
				jsonGenerator.writeEndArray();
			}
		};
	}
}
