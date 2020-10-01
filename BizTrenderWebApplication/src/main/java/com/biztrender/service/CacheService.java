package com.biztrender.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biztrender.action.Action;
import com.biztrender.dao.mongo.CacheMongoDao;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.factory.MongoFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CacheService {

	@Autowired
	private MongoFactory mongoFactory;

	private CacheMongoDao cacheMongoDao;

	private CacheMongoDao getCacheMongoDao() throws BizTrenderException {
		if (cacheMongoDao == null) {
			cacheMongoDao = new CacheMongoDao(mongoFactory);
		}
		return cacheMongoDao;
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> T find(String serviceKey, String cacheKey,
			Action<T> action, Class<T> clazz) throws BizTrenderException {
		String data = getCacheMongoDao().find(serviceKey, cacheKey);
		T result = null;
		if (data != null) {
			try {
				if (data != null) {
					result = String.class == clazz ? (T) data
							: new ObjectMapper().readValue(
									data, clazz);
				}
			} catch (IOException e) {
				throw new BizTrenderException(e);
			}
		} else {
			result = action.perform();
			if (result != null) {
				getCacheMongoDao().save(serviceKey, cacheKey, result.toString());
			}
		}
		return result;
	}

	public void clear() throws BizTrenderException {
		getCacheMongoDao().clear();
	}

}
