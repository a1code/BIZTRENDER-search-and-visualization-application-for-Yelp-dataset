package com.biztrender.sort;

import com.biztrender.query.AttributeRule;

public class SortRule extends AttributeRule<SortOrder> {

	public SortRule(String fieldName, SortOrder value) {
		super(fieldName, value, "SORT");
	}

}
