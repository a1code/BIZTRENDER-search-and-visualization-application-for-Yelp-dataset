package com.biztrender.sort;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.biztrender.dto.AbstractDTO;
import com.biztrender.exception.BizTrenderException;

public class SortRules extends AbstractDTO {
	private static final Logger LOGGER = LoggerFactory.getLogger(SortRules.class.getName());

	private List<SortRule> rules = new LinkedList<>();

	public SortRules() {
		// default constructor
	}

	public void clear() {
		rules.clear();
	}

	public void add(SortRule sortRule) {
		rules.add(sortRule);
	}

	public void add(List<SortRule> rules) {
		this.rules.addAll(rules);
	}

	public List<SortRule> getRules() {
		return rules;
	}

	public static SortRules parse(String sort) throws BizTrenderException {
		if (StringUtils.isBlank(sort)) {
			return null;
		}
		SortRules sortRules = new SortRules();
		String[] sorts;
		try {
			sorts = URLDecoder.decode(sort, "UTF-8").split(",");
		} catch (UnsupportedEncodingException e) {
			throw new BizTrenderException(e);
		}
		for (String srt : sorts) {
			int lastSpacePos = srt.lastIndexOf(" ");
			String name = srt.substring(0, lastSpacePos);
			String order = srt.substring(lastSpacePos + 1);
			SortOrder sortOrder = SortOrder.ASC;
			try {
				sortOrder = SortOrder.valueOf(order.toUpperCase());
			} catch (IllegalArgumentException | NullPointerException e) {
				LOGGER.warn("Error parsing sort order for " + srt, e);
			}
			if (StringUtils.isNotBlank(name)) {
				sortRules.add(new SortRule(name, sortOrder));
			}
		}
		return sortRules;
	}

}
