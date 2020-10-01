package com.biztrender.filter;

import com.biztrender.dto.AbstractObject;

public class SearchFilter extends AbstractObject {

	private Long id;

	public Long getId() {
		return id;
	}

	public SearchFilter setId(Long id) {
		this.id = id;
		return this;
	}

}
