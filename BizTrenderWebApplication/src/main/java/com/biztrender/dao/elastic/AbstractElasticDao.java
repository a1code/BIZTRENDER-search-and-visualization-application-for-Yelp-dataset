package com.biztrender.dao.elastic;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.biztrender.dto.AbstractDTO;
import com.biztrender.dto.PagedList;
import com.biztrender.dto.Pagination;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.factory.ElasticFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractElasticDao<T extends AbstractDTO> {

	private RestHighLevelClient client;
	private String indexName;
	private Class<T> clazz;

	public AbstractElasticDao(ElasticFactory elasticFactory, String indexName, Class<T> clazz) throws BizTrenderException {
		super();
		try {
			this.client = elasticFactory.getESClient();
			this.indexName = indexName;
			this.clazz = clazz;
		} catch (Exception e) {
			throw new BizTrenderException(e);
		}
	}

	public RestHighLevelClient getClient() {
		return client;
	}

	public String getIndexName() {
		return indexName;
	}

	public PagedList<T> search(String keyword, Integer offset, Integer limit) throws Exception {
		PagedList<T> pagedList = new PagedList<>();

		SearchRequest searchRequest = new SearchRequest(getIndexName());
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryStringQuery(keyword));
		searchSourceBuilder.from(offset);
		searchSourceBuilder.size(limit);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = getClient().search(searchRequest, RequestOptions.DEFAULT);
		SearchHits searchHits = searchResponse.getHits();
		ObjectMapper mapper = new ObjectMapper();
		List<T> objects = new ArrayList<>();
		for (SearchHit searchHit : searchHits.getHits()) {
			objects.add(mapper.readValue(searchHit.getSourceAsString(), clazz));
		}
		pagedList.setObjects(objects);
		Pagination pagination = new Pagination();
		pagination.setOffset(-1);
		pagination.setLimit(-1);
		pagination.setCount(objects.size());
		pagination.setTotal(searchHits.getTotalHits());
		pagedList.setPagination(pagination);
		return pagedList;
	}
}
