package com.biztrender.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.biztrender.dao.elastic.BusinessElasticDao;
import com.biztrender.dao.mongo.BusinessMongoDao;
import com.biztrender.dto.Business;
import com.biztrender.dto.PagedList;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.factory.ElasticFactory;
import com.biztrender.factory.MongoFactory;
import com.biztrender.filter.BusinessFilter;

@Component
public class BusinessService {

	@Autowired
	private MongoFactory mongoFactory;

	@Autowired
	private ElasticFactory elasticFactory;

	private BusinessMongoDao businessMongoDao;

	private BusinessElasticDao businessElasticDao;

	public BusinessMongoDao getBusinessMongoDao() throws BizTrenderException {
		if (businessMongoDao == null) {
			businessMongoDao = new BusinessMongoDao(mongoFactory);
		}
		return businessMongoDao;
	}

	public BusinessElasticDao getBusinessElasticDao() throws BizTrenderException {
		if (businessElasticDao == null) {
			businessElasticDao = new BusinessElasticDao(elasticFactory);
		}
		return businessElasticDao;
	}

	public PagedList<Business> searchBusiness(String keyword, Integer offset, Integer limit) throws Exception {
		return getBusinessElasticDao().search(keyword, offset, limit);
	}

	public List<Business> findByCategoryAndRating(BusinessFilter filter) throws BizTrenderException, ParseException {
		return getBusinessMongoDao().findByCategoryAndRating(filter);
	}

	public List<Business> findWithAllRatingsByCategory(BusinessFilter filter)
			throws BizTrenderException, ParseException {
		return getBusinessMongoDao().findWithAllRatingsByCategory(filter);
	}

	public List<Business> findWithCheckinsByCategory(BusinessFilter filter) throws BizTrenderException, ParseException {
		return getBusinessMongoDao().findWithCheckinsByCategory(filter);
	}

	public StreamingResponseBody findReviewSentimentVsTimeByCategoryAndRating(BusinessFilter filter)
			throws BizTrenderException, ParseException {
		return getBusinessMongoDao().findReviewSentimentVsTimeByCategoryAndRating(filter);
	}

	public StreamingResponseBody findAllCheckinsByCategoryAndRating(BusinessFilter filter)
			throws BizTrenderException, ParseException {
		return getBusinessMongoDao().findAllCheckinsByCategoryAndRating(filter);
	}
}