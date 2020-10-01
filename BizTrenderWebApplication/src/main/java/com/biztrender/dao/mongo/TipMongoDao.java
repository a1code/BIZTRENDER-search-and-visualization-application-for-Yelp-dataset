package com.biztrender.dao.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.biztrender.dto.Tip;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.factory.MongoFactory;
import com.biztrender.filter.TipFilter;
import com.biztrender.mapper.TipMapper;
import com.biztrender.utils.CommonUtils;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;

public class TipMongoDao extends AbstractMongoDao<Tip, TipFilter, TipMapper> {

	public TipMongoDao(MongoFactory factory) throws BizTrenderException {
		super(factory, "tip", TipMapper.class);
	}

	public String getTipsTextByBusiness(TipFilter filter) {
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
}
