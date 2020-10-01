package com.biztrender.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biztrender.dao.mongo.BusinessMongoDao;
import com.biztrender.dao.mongo.ReviewMongoDao;
import com.biztrender.dao.mongo.TipMongoDao;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.factory.MongoFactory;
import com.biztrender.filter.ReviewFilter;
import com.biztrender.filter.TipFilter;

@Component
public class ReviewService {

	@Autowired
	private MongoFactory mongoFactory;

	private ReviewMongoDao reviewMongoDao;

	private TipMongoDao tipMongoDao;

	private BusinessMongoDao businessMongoDao;

	public ReviewMongoDao getReviewMongoDao() throws BizTrenderException {
		if (reviewMongoDao == null) {
			reviewMongoDao = new ReviewMongoDao(mongoFactory);
		}
		return reviewMongoDao;
	}

	public TipMongoDao getTipMongoDao() throws BizTrenderException {
		if (tipMongoDao == null) {
			tipMongoDao = new TipMongoDao(mongoFactory);
		}
		return tipMongoDao;
	}

	public BusinessMongoDao getBusinessMongoDao() throws BizTrenderException {
		if (businessMongoDao == null) {
			businessMongoDao = new BusinessMongoDao(mongoFactory);
		}
		return businessMongoDao;
	}

	public String getTipsAndReviewsTextByBusiness(String businessId) throws BizTrenderException {
		ReviewFilter reviewFilter = new ReviewFilter();
		TipFilter tipFilter = new TipFilter();

		reviewFilter.setBusiness_id(businessId);
		tipFilter.setBusiness_id(businessId);

		return getReviewMongoDao().getReviewsTextByBusiness(reviewFilter)
				.concat(getTipMongoDao().getTipsTextByBusiness(tipFilter));
	}

	public List<Double> getReviewSentimentByBusiness(ReviewFilter filter) throws BizTrenderException, ParseException {
		return getReviewMongoDao().getReviewSentimentByBusiness(filter);
	}

	public Map<String, Object> getUsersAndRatingsByBusiness(ReviewFilter reviewFilter)
			throws BizTrenderException, ParseException {
		return getReviewMongoDao().getUsersAndRatingsByBusiness(reviewFilter);
	}
}