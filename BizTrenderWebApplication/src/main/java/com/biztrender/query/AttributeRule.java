package com.biztrender.query;

import com.biztrender.query.Attribute;

public class AttributeRule<T> extends Attribute<T> {
	private String rule;

	public AttributeRule() {
		// default constructor
	}

	public AttributeRule(String fieldName, T value, String rule) {
		super(fieldName, value);
		this.rule = rule;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

}
