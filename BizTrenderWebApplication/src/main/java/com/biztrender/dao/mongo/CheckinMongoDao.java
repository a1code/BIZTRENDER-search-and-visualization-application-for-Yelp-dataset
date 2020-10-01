package com.biztrender.dao.mongo;

import com.biztrender.dto.Checkin;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.factory.MongoFactory;
import com.biztrender.filter.CheckinFilter;
import com.biztrender.mapper.CheckinMapper;

public class CheckinMongoDao extends AbstractMongoDao<Checkin, CheckinFilter, CheckinMapper> {
	public CheckinMongoDao(MongoFactory factory) throws BizTrenderException {
		super(factory, "checkin", CheckinMapper.class);
	}
}
