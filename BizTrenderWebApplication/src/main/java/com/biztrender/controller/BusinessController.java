package com.biztrender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.biztrender.action.Action;
import com.biztrender.dto.BizTrenderResponse;
import com.biztrender.dto.Business;
import com.biztrender.dto.PagedList;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.filter.BusinessFilter;
import com.biztrender.service.BusinessService;
import com.biztrender.service.CacheService;
import com.biztrender.utils.CommonUtils;

@RestController
@RequestMapping("service/business")
public class BusinessController extends AbstractController<Business, BusinessFilter> {
	private static final String BUSINESS_DATA_BY_CATEGORY_AND_RATING = "BUSINESS_DATA_BY_CATEGORY_AND_RATING";
	private static final String BUSINESS_DATA_WITH_ALL_RATINGS_BY_CATEGORY = "BUSINESS_DATA_WITH_ALL_RATINGS_BY_CATEGORY";
	private static final String BUSINESS_DATA_WITH_CHECKINS_BY_CATEGORY = "BUSINESS_DATA_WITH_CHECKINS_BY_CATEGORY";

	@Autowired
	private CacheService cacheService;

	@Autowired
	private BusinessService service;

	public BusinessController() {
		super(Business.class, BusinessFilter.class);
	}

	@Override
	public PagedList<Business> searchByQuery(String query, Integer offset, Integer limit) throws Exception {
		return service.searchBusiness(query, offset, limit);
	}

	@RequestMapping(value = "/findByCategoryAndRating", method = RequestMethod.GET)
	public String findByCategoryAndRating(@RequestParam String category, @RequestParam String rating) throws Exception {
		String cacheKey = CommonUtils.getCacheKey(category, rating);
		String analysisResponse = cacheService.find(BUSINESS_DATA_BY_CATEGORY_AND_RATING, cacheKey,
				new Action<String>() {
					@Override
					public String perform() throws BizTrenderException {
						try {
							BusinessFilter businessFilter = new BusinessFilter();
							businessFilter.setCategory(category);
							businessFilter.setRating(rating);
							return new BizTrenderResponse().setData(service.findByCategoryAndRating(businessFilter))
									.toString();
						} catch (Exception e) {
							throw new BizTrenderException(e);
						}
					}
				}, String.class);
		return analysisResponse;
	}

	@RequestMapping(value = "/findWithAllRatingsByCategory", method = RequestMethod.GET)
	public String findWithAllRatingsByCategory(@RequestParam String category) throws Exception {
		String cacheKey = CommonUtils.getCacheKey(category);
		String analysisResponse = cacheService.find(BUSINESS_DATA_WITH_ALL_RATINGS_BY_CATEGORY, cacheKey,
				new Action<String>() {
					@Override
					public String perform() throws BizTrenderException {
						try {
							BusinessFilter businessFilter = new BusinessFilter();
							businessFilter.setCategory(category);
							return new BizTrenderResponse()
									.setData(service.findWithAllRatingsByCategory(businessFilter)).toString();
						} catch (Exception e) {
							throw new BizTrenderException(e);
						}
					}
				}, String.class);
		return analysisResponse;
	}

	@RequestMapping(value = "/findWithCheckinsByCategory", method = RequestMethod.GET)
	public String findWithCheckinsByCategoryAndRating(@RequestParam String category) throws Exception {
		String cacheKey = CommonUtils.getCacheKey(category);
		String analysisResponse = cacheService.find(BUSINESS_DATA_WITH_CHECKINS_BY_CATEGORY, cacheKey,
				new Action<String>() {
					@Override
					public String perform() throws BizTrenderException {
						try {
							BusinessFilter businessFilter = new BusinessFilter();
							businessFilter.setCategory(category);
							return new BizTrenderResponse().setData(service.findWithCheckinsByCategory(businessFilter))
									.toString();
						} catch (Exception e) {
							throw new BizTrenderException(e);
						}
					}
				}, String.class);
		return analysisResponse;
	}

	@RequestMapping(value = "/findReviewSentimentVsTimeByCategoryAndRating", method = RequestMethod.GET)
	public StreamingResponseBody findReviewSentimentVsTimeByCategoryAndRating(@RequestParam String category,
			@RequestParam String rating) throws Exception {
		BusinessFilter businessFilter = new BusinessFilter();
		businessFilter.setCategory(category);
		businessFilter.setRating(rating);
		return service.findReviewSentimentVsTimeByCategoryAndRating(businessFilter);
	}

	@RequestMapping(value = "/findAllCheckinsByCategoryAndRating", method = RequestMethod.GET)
	public StreamingResponseBody findAllCheckinsByCategoryAndRating(@RequestParam String category,
			@RequestParam String rating) throws Exception {
		BusinessFilter businessFilter = new BusinessFilter();
		businessFilter.setCategory(category);
		businessFilter.setRating(rating);
		return service.findAllCheckinsByCategoryAndRating(businessFilter);
	}
}