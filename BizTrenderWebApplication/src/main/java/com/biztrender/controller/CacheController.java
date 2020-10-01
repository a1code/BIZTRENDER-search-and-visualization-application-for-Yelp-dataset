package com.biztrender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biztrender.dto.BizTrenderResponse;
import com.biztrender.service.CacheService;

@RestController
@RequestMapping("service/cache")
public class CacheController {

	@Autowired
	private CacheService cacheService;

	@RequestMapping(value = "/clear", method = RequestMethod.GET)
	public String clear(@RequestParam(required = false) String serviceKey)
			throws Exception {
		cacheService.clear();
		return new BizTrenderResponse().setData("success").toString();
	}

}
