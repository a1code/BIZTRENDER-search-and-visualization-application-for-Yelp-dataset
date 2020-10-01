package com.biztrender.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biztrender.config.MongoConfiguration;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

@Component
public class MongoFactory {

	private MongoClient client;
	private MongoDatabase db;

	@Autowired
	private MongoConfiguration config;

	public MongoClient getMongoClient() throws Exception {
		if (client == null) {
			client = new MongoClient(config.getHost(), config.getPort());
		}
		return client;
	}

	public MongoConfiguration getConfig() {
		return config;
	}

	public MongoDatabase getDb() throws Exception {
		if (db == null) {
			db = getMongoClient().getDatabase(config.getDb());
		}
		return db;
	}

	@Override
	protected void finalize() throws Throwable {
		getMongoClient().close();
		super.finalize();
	}

}
