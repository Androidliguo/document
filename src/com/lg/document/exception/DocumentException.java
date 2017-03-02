package com.lg.document.exception;

public class DocumentException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 336034492866457416L;

	public DocumentException() {
		super();
	}

	public DocumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DocumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentException(String message) {
		super(message);
	}

	public DocumentException(Throwable cause) {
		super(cause);
	}
	
	
	

}
