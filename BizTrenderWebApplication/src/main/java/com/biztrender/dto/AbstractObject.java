package com.biztrender.dto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.biztrender.exception.BizTrenderException;
import com.biztrender.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AbstractObject {

	/**
	 * This function is responsible for updating field in map
	 * 
	 * @param methodName
	 * @param method
	 * @param fields
	 * @param map
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void updateFieldInMap(String methodName, Method method,
			Set<String> fields, Map<String, Object> map)
			throws IllegalAccessException, InvocationTargetException {
		String field = methodName.substring(0, 1).toLowerCase()
				+ methodName.substring(1);
		if (fields.contains(field)) {
			method.setAccessible(true);
			Object obj = method.invoke(this);
			if (obj == null
					|| (obj instanceof String && StringUtils
							.isBlank((String) obj))) {
				return;
			}
			map.put(field, obj);
		} else {
			field = methodName.substring(0, 1) + methodName.substring(1);
			if (fields.contains(field)) {
				Object obj = method.invoke(this);
				if (obj == null
						|| (obj instanceof String && StringUtils
								.isBlank((String) obj))) {
					return;
				}
				map.put(field, obj);
			}
		}
	}

	/**
	 * This function is responsible for updating fields in map
	 * 
	 * @param clazz
	 * @param fields
	 * @param map
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void updateFieldsInMap(Class clazz, Set<String> fields,
			Map<String, Object> map) throws IllegalAccessException,
			InvocationTargetException {
		for (Method method : clazz.getDeclaredMethods()) {
			String methodName = method.getName();
			if (methodName.startsWith("get")) {
				methodName = methodName.substring(3);
			} else if (methodName.startsWith("is")) {
				methodName = methodName.substring(2);
			} else {
				continue;
			}
			updateFieldInMap(methodName, method, fields, map);
		}
	}

	/**
	 * This function is responsible for converting current object to map
	 * 
	 * @return map
	 * @throws BizTrenderException
	 */
	public Map<String, Object> toMap() throws BizTrenderException {
		Map<String, Object> map = new TreeMap<>();
		try {
			Class clazz = this.getClass();
			while (clazz != null) {
				Set<String> fields = getFields(clazz);
				updateFieldsInMap(clazz, fields, map);
				clazz = clazz.getSuperclass();
			}
		} catch (Exception e) {
			throw new BizTrenderException(e);
		}
		return map;
	}

	@Override
	public String toString() {
		try {
			return JsonUtils.getJsonString(toMap());
		} catch (BizTrenderException e) {
			return "{}";
		}
	}

	/**
	 * This function is responsible for populating specified object info current
	 * object
	 * 
	 * @param obj
	 */
	public void populate(Object obj) {
		BeanUtils.copyProperties(obj, this);
	}

	/**
	 * This function is responsible for providing fields of a class
	 * 
	 * @param clazz
	 * @return fields
	 */
	public Set<String> getFields(Class clazz) {
		Set<String> fields = new TreeSet<>();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(JsonIgnore.class)) {
				continue;
			}
			fields.add(field.getName());
		}
		return fields;
	}

}