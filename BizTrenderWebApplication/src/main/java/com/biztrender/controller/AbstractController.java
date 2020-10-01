package com.biztrender.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biztrender.dto.AbstractDTO;
import com.biztrender.dto.BizTrenderResponse;
import com.biztrender.dto.PagedList;
import com.biztrender.filter.SearchFilter;

public abstract class AbstractController<T extends AbstractDTO, E extends SearchFilter> {
	public AbstractController(Class<T> dtoType, Class<E> filterType) {
		super();
	}

	public abstract @ResponseBody PagedList<T> searchByQuery(String query, Integer offset, Integer limit)
			throws Exception;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public @ResponseBody String search(@RequestParam String query, @RequestParam Integer offset,
			@RequestParam Integer limit) throws Exception {
		PagedList<T> pagedList = searchByQuery(query, offset, limit);
		return new BizTrenderResponse().setData(pagedList.getObjects()).setPagination(pagedList.getPagination())
				.setAdditionalData(pagedList.getAdditionalData()).toString();
	}
}