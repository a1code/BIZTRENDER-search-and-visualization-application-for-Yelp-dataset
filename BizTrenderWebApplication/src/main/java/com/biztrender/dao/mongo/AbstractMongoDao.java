package com.biztrender.dao.mongo;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import com.biztrender.dto.AbstractDTO;
import com.biztrender.dto.Paging;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.factory.MongoFactory;
import com.biztrender.filter.SearchFilter;
import com.biztrender.mapper.MongoMapper;
import com.biztrender.sort.SortOrder;
import com.biztrender.sort.SortRule;
import com.biztrender.sort.SortRules;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public abstract class AbstractMongoDao<T extends AbstractDTO, E extends SearchFilter, Z extends MongoMapper<T, E>> {

	public static final String MONGO_ID = "_id";
	public static final String SET_OPERATOR = "$set";
	public static final String SORT_OPERATOR = "$sort";
	public static final String UNSET_OPERATOR = "$unset";
	public static final String EXIST_OPERATOR = "$exists";
	public static final String MATCH_OPERATOR = "$match";
	public static final String GRAPH_LOOKUP_OPERATOR = "$graphLookup";
	public static final String UNWIND_OPERATOR = "$unwind";
	public static final String DOT = ".";
	public static final String FROM = "from";
	public static final String PROJECT_OPERATOR = "$project";
	public static final String DEPTH = "depth";
	public static final String DEPTH_FIELD = "depthField";
	public static final String AS = "as";
	public static final String CONNECT_TO_FIELD = "connectToField";
	public static final String CONNECT_FROM_FIELD = "connectFromField";
	public static final String START_WITH = "startWith";
	public static final String $ = "$";

	private MongoCollection<Document> collection;
	private Z instance;

	public AbstractMongoDao(MongoFactory mongoFactory, String collectionName,
			Class<Z> mapper) throws BizTrenderException {
		super();
		try {
			instance = mapper.newInstance();
			collection = mongoFactory.getDb().getCollection(collectionName);
		} catch (Exception e) {
			throw new BizTrenderException(e);
		}
	}

	public MongoCollection<Document> getCollection() {
		return collection;
	}

	public Z getMapper() {
		return instance;
	}

	public T findById(String id) {
		Document obj = getCollection().find(new BasicDBObject(MONGO_ID, id))
				.first();
		return obj == null ? null : instance.convertToDto(obj);
	}

	public T findOne(E filter) {
		Document obj = getCollection().find(instance.convertToDao(filter))
				.first();
		return obj == null ? null : instance.convertToDto(obj);
	}

	private BasicDBObject getSortObj(SortRules sortRules) {
		BasicDBObject sortObj = null;
		if (sortRules != null) {
			List<SortRule> sortRule = sortRules.getRules();
			if (sortRule != null && !sortRule.isEmpty()) {
				sortObj = new BasicDBObject();
				for (SortRule sort : sortRule) {
					sortObj.put(
							sort.getFieldName(),
							SortOrder.DESC.toString().equalsIgnoreCase(
									String.valueOf(sort.getValue())) ? -1 : 1);
				}
			}
		}
		if (sortObj == null) {
			sortObj = new BasicDBObject(MONGO_ID, 1);
		}
		return sortObj;
	}

	public List<T> list(E filter, Paging paging, SortRules sortRules) {
		BasicDBObject sortObj = getSortObj(sortRules);
		List<T> results = new LinkedList<>();
		MongoCursor<Document> cur = paging == null || paging.isNotReqd() ? getCollection()
				.find(instance.convertToDao(filter)).sort(sortObj).iterator()
				: getCollection().find(instance.convertToDao(filter))
						.sort(sortObj).skip(paging.getOffset())
						.limit(paging.getLimit()).iterator();
		while (cur.hasNext()) {
			results.add(instance.convertToDto(cur.next()));
		}
		cur.close();
		return results;
	}

	public long count(E filter) {
		return getCollection().count(instance.convertToDao(filter));
	}

}
