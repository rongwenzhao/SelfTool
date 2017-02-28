package com.mygame.android.json;

public class JsonFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JsonFormatException() {
		super();
	}

	public JsonFormatException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public JsonFormatException(String detailMessage) {
		super(detailMessage);
	}

	public JsonFormatException(Throwable throwable) {
		super(throwable);
	}
	
	

}
