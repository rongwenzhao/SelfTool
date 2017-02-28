package com.mygame.android.utils.encode;

public class MyGameDesUtil {
	private static final String COMMON_KEY = "~9(A%y#!";

	public static final String encryptDes(String text) throws Exception {
		return Des.encryptDES(text, COMMON_KEY);
	}

	public static final String decryptDes(String desText) throws Exception {
		return Des.decryptDES(desText, COMMON_KEY);
	}

}
