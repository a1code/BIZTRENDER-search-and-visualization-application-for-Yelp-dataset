package com.biztrender.dto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for holding fields to be returned in response.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class BizTrenderResponse extends AbstractObject {
    private String errorCode;
    private Object data;
    private Pagination pagination;
    private List<String> errors;
    private Map<String, Object> additionalData;

    public String getErrorCode() {
        return errorCode;
    }

    public BizTrenderResponse setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Object getData() {
        return data;
    }

    public BizTrenderResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public BizTrenderResponse setPagination(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }

    public List<String> getErrors() {
        return errors;
    }

    public BizTrenderResponse setErrors(List<String> errors) {
        this.errors = errors;
        return this;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public BizTrenderResponse setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
        return this;
    }

	public BizTrenderResponse addAdditionalData(String key, Object value) {
        if (additionalData == null) {
            additionalData = new HashMap<>();
        }
        additionalData.put(key, value);
        return this;
    }

    public Object getAdditionalData(String key) {
        if (additionalData == null) {
            additionalData = new HashMap<>();
        }
        return additionalData.get(key);
    }

    public void removeAdditionalData(String key) {
        if (additionalData != null) {
            additionalData.remove(key);
        }
    }

    public void clearAdditionalData() {
        if (additionalData != null) {
            additionalData.clear();
        }
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        if (errorCode != null) {
            map.put("errorCode", errorCode);
        }
        if (data != null) {
            map.put("data", data);
        }
        if (pagination != null) {
            map.put("pagination", pagination);
        }
        if (errors != null && !errors.isEmpty()) {
            map.put("errors", errors);
        }
        if (additionalData != null && !additionalData.isEmpty()) {
            map.putAll(additionalData);
        }
        return map;
    }

}
