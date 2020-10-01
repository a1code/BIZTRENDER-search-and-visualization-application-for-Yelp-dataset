package com.biztrender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biztrender.dao.mongo.CheckinMongoDao;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.factory.MongoFactory;

@Component
public class CheckinService {

	@Autowired
	private MongoFactory mongoFactory;

	private CheckinMongoDao checkinMongoDao;

	public CheckinMongoDao getCheckinMongoDao() throws BizTrenderException {
		if (checkinMongoDao == null) {
			checkinMongoDao = new CheckinMongoDao(mongoFactory);
		}
		return checkinMongoDao;
	}
}