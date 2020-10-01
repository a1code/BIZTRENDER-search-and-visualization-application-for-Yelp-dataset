package com.biztrender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biztrender.action.Action;
import com.biztrender.dto.BizTrenderResponse;
import com.biztrender.dto.PagedList;
import com.biztrender.dto.Review;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.filter.ReviewFilter;
import com.biztrender.service.CacheService;
import com.biztrender.service.ReviewService;
import com.biztrender.utils.CommonUtils;

@RestController
@RequestMapping("service/review")
public class ReviewController extends AbstractController<Review, ReviewFilter> {
	private static final String USER_TIPS_AND_REVIEWS_BY_BUSINESS = "USER_TIPS_AND_REVIEWS_BY_BUSINESS";
	private static final String USER_REVIEWS_SENTIMENT_BY_BUSINESS = "USER_REVIEWS_SENTIMENT_BY_BUSINESS";
	private static final String USER_AVERAGE_RATINGS_VS_ACTUAL_BY_BUSINESS = "USER_AVERAGE_RATINGS_VS_ACTUAL_BY_BUSINESS";

	@Autowired
	private CacheService cacheService;

	@Autowired
	private ReviewService service;

	public ReviewController() {
		super(Review.class, ReviewFilter.class);
	}

	@RequestMapping(value = "/getTipsAndReviewsTextByBusiness", method = RequestMethod.GET)
	public String getTipsAndReviewsTextByBusiness(@RequestParam String business_id) throws Exception {
		String cacheKey = CommonUtils.getCacheKey(business_id);
		String analysisResponse = cacheService.find(USER_TIPS_AND_REVIEWS_BY_BUSINESS, cacheKey, new Action<String>() {
			@Override
			public String perform() throws BizTrenderException {
				try {
					return new BizTrenderResponse().setData(service.getTipsAndReviewsTextByBusiness(business_id))
							.toString();
				} catch (Exception e) {
					throw new BizTrenderException(e);
				}
			}
		}, String.class);
		return analysisResponse;
	}

	@RequestMapping(value = "/getReviewSentimentByBusiness", method = RequestMethod.GET)
	public String getReviewSentimentByBusiness(@RequestParam String business_id) throws Exception {
		String cacheKey = CommonUtils.getCacheKey(business_id);
		String analysisResponse = cacheService.find(USER_REVIEWS_SENTIMENT_BY_BUSINESS, cacheKey, new Action<String>() {
			@Override
			public String perform() throws BizTrenderException {
				try {
					ReviewFilter reviewFilter = new ReviewFilter();
					reviewFilter.setBusiness_id(business_id);
					return new BizTrenderResponse().setData(service.getReviewSentimentByBusiness(reviewFilter)).toString();
				} catch (Exception e) {
					throw new BizTrenderException(e);
				}
			}
		}, String.class);
		return analysisResponse;
	}

	@RequestMapping(value = "/getUsersAndRatingsByBusiness", method = RequestMethod.GET)
	public String getUsersAndRatingsByBusiness(@RequestParam String business_id) throws Exception {
		String cacheKey = CommonUtils.getCacheKey(business_id);
		String analysisResponse = cacheService.find(USER_AVERAGE_RATINGS_VS_ACTUAL_BY_BUSINESS, cacheKey,
				new Action<String>() {
					@Override
					public String perform() throws BizTrenderException {
						try {
							ReviewFilter reviewFilter = new ReviewFilter();
							reviewFilter.setBusiness_id(business_id);
							return new BizTrenderResponse().setData(service.getUsersAndRatingsByBusiness(reviewFilter))
									.toString();
						} catch (Exception e) {
							throw new BizTrenderException(e);
						}
					}
				}, String.class);
		return analysisResponse;
	}

	@Override
	public PagedList<Review> searchByQuery(String query, Integer offset, Integer limit) throws Exception {
		return null;
	}
}