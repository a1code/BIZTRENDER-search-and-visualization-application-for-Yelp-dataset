package com.biztrender.exception;

/**
 * This class is intended for holding information for the exceptions generated
 * in fintech.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class BizTrenderException extends Exception {
	private static final long serialVersionUID = -3462664213354567053L;

	public BizTrenderException(String message, Throwable cause) {
		super(message, cause);
	}

	public BizTrenderException(String message) {
		super(message);
	}

	public BizTrenderException(Throwable cause) {
		super(cause);
	}

}
