package com.biztrender.action;

import com.biztrender.exception.BizTrenderException;

public interface Action<T extends Object> {

	T perform() throws BizTrenderException;

}