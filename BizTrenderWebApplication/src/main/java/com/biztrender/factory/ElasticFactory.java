package com.biztrender.factory;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biztrender.config.ElasticConfiguration;

@Component
public class ElasticFactory {

	private RestHighLevelClient esClient;

	@Autowired
	private ElasticConfiguration config;

	public RestHighLevelClient getESClient() throws Exception {
		if (esClient == null) {
			esClient = new RestHighLevelClient(RestClient.builder(
					new HttpHost(config.getHost(), config.getPort())));
		}
		return esClient;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void finalize() throws Throwable {
		getESClient().close();
		super.finalize();
	}

}
