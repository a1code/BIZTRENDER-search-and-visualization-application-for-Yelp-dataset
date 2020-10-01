package com.biztrender.mapper;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.biztrender.dto.AbstractDTO;
import com.biztrender.filter.SearchFilter;

public interface MongoMapper<T extends AbstractDTO, E extends SearchFilter> {

	Bson convertToDao(E filter);

	T convertToDto(Document doc);

}
