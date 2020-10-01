package com.biztrender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biztrender.dto.Checkin;
import com.biztrender.dto.PagedList;
import com.biztrender.filter.CheckinFilter;
import com.biztrender.service.CacheService;
import com.biztrender.service.CheckinService;

@RestController
@RequestMapping("service/checkin")
public class CheckinController extends AbstractController<Checkin, CheckinFilter> {

	@Autowired
	private CheckinService checkinService;

	@Autowired
	private CacheService cacheService;

	public CheckinController() {
		super(Checkin.class, CheckinFilter.class);
	}

	@Override
	public PagedList<Checkin> searchByQuery(String query, Integer offset, Integer limit) throws Exception {
		return null;
	}
}