package com.biztrender.dao.elastic;

import com.biztrender.dto.Business;
import com.biztrender.exception.BizTrenderException;
import com.biztrender.factory.ElasticFactory;

public class BusinessElasticDao extends AbstractElasticDao<Business> {

	public BusinessElasticDao(ElasticFactory factory) throws BizTrenderException {
		super(factory, "biztrender_business", Business.class);
	}

}
