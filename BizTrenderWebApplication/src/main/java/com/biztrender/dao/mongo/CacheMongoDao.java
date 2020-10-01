package com.biztrender.dao.mongo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.biztrender.exception.BizTrenderException;
import com.biztrender.factory.MongoFactory;
import com.biztrender.utils.CommonUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

/**
 * This class is intended to persist service responses for caching.
 * 
 * @author Hemant Kumar
 * @version 1.6.6O
 */
public class CacheMongoDao {

	private GridFSBucket bucket;

	public CacheMongoDao(MongoFactory mongoFactory) throws BizTrenderException {
		try {
			bucket = GridFSBuckets.create(mongoFactory.getDb(), "cache");
		} catch (Exception e) {
			throw new BizTrenderException(e);
		}
	}

	public void save(String serviceKey, String cacheKey, String data) {
		String id = CommonUtils.getMD5Hash(serviceKey + cacheKey);
		bucket.uploadFromStream(id, new ByteArrayInputStream(data.getBytes()));
	}

	public String find(String serviceKey, String cacheKey) {
		String id = CommonUtils.getMD5Hash(serviceKey + cacheKey);
		String res = null;
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
			bucket.downloadToStream(id, os);
			res = os.toString();
		} catch (Exception e) {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return res;
	}

	public void clear() {
		bucket.drop();
	}

}
