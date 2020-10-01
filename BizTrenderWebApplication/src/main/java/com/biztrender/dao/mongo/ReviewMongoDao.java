package com.biztrender.dao.mongo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.biztrender.dto.Review;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.factory.MongoFactory;
import com.biztrender.filter.ReviewFilter;
import com.biztrender.mapper.ReviewMapper;
import com.biztrender.utils.CommonUtils;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;

public class ReviewMongoDao extends AbstractMongoDao<Review, ReviewFilter, ReviewMapper> {

	public ReviewMongoDao(MongoFactory factory) throws BizTrenderException {
		super(factory, "review", ReviewMapper.class);
	}

	public String getReviewsTextByBusiness(ReviewFilter filter) {
		List<String> output = new ArrayList<>();

		List<Bson> pipeline = new ArrayList<>();

		Bson findObj = getMapper().convertToDao(filter);
		pipeline.add(Aggregates.match(findObj));

		MongoCursor<Document> cur = getCollection().aggregate(pipeline).allowDiskUse(true).iterator();
		while (cur.hasNext()) {
			Document doc = (Document) cur.next();
			output.add((String) doc.get("text"));
		}

		String resultString = output.stream().map(Object::toString).collect(Collectors.joining(" "));

		ArrayList<String> allWords = Stream.of(resultString.toLowerCase().split(" "))
				.collect(Collectors.toCollection(ArrayList<String>::new));
		allWords.removeAll(CommonUtils.getEnglishStopWords());

		String result = allWords.stream().collect(Collectors.joining(" "));

		return result;
	}

	public List<Double> getReviewSentimentByBusiness(ReviewFilter filter) throws BizTrenderException, ParseException {
		List<Double> output = new ArrayList<>();

		List<Bson> pipeline = new ArrayList<>();

		Bson findObj = getMapper().convertToDao(filter);
		pipeline.add(Aggregates.match(findObj));

		MongoCursor<Document> cur = getCollection().aggregate(pipeline).allowDiskUse(true).iterator();
		while (cur.hasNext()) {
			Document doc = (Document) cur.next();
			output.add((Double) doc.get("stars"));
		}

		Double average = output.stream().mapToDouble(a -> a).average().getAsDouble();

		List<Double> result = output.stream().map(x -> Double.parseDouble(String.format("%.2f", (x - average))))
				.collect(Collectors.toList());
		return result;
	}

	public Map<String, Object> getUsersAndRatingsByBusiness(ReviewFilter filter) {
		Map<String, Object> output = new HashMap<>();

		List<String> users = new ArrayList<>();
		List<Double> ratings_business = new ArrayList<>();
		List<Double> ratings_average = new ArrayList<>();
		List<String> user_names = new ArrayList<>();

		List<Bson> pipeline = new ArrayList<>();

		Bson findObj = getMapper().convertToDao(filter);
		pipeline.add(Aggregates.match(findObj));
		pipeline.add(Aggregates.lookup("user", "user_id", "user_id", "user"));
		pipeline.add(Aggregates.unwind("$user"));

		MongoCursor<Document> cur = getCollection().aggregate(pipeline).allowDiskUse(true).iterator();
		while (cur.hasNext()) {
			Document doc = (Document) cur.next();
			users.add(((String) doc.get("user_id")));
			ratings_business.add((Double) doc.get("stars"));
			Document user = (Document) doc.get("user");
			ratings_average.add((Double) user.get("average_stars"));
			user_names.add((String) user.get("name"));
		}

		output.put("users", users);
		output.put("ratings_business", ratings_business);
		output.put("ratings_average", ratings_average);
		output.put("user_names", user_names);

		return output;
	}
}
