package com.biztrender.dto;

import java.util.List;
import java.util.Map;

public class PagedList<T> extends AbstractObject {
    private List<T> objects;
    private Pagination pagination;
    private Map<String, Object> additionalData;

    public List<T> getObjects() {
        return objects;
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

	public Map<String, Object> getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(Map<String, Object> additionalData) {
		this.additionalData = additionalData;
	}

}
