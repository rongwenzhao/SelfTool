package com.mygame.android.encode.util;

public class MYGameDesUtil {
	private static final String COMMON_KEY = "25618035";

	public static final String encryptDes(String text) throws Exception {
		return Des.encryptDES(text, COMMON_KEY);
	}

	public static final String decryptDes(String desText) throws Exception {
		return Des.decryptDES(desText, COMMON_KEY);
	}
}
