package com.mygame.android.net.lobby;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;

import com.mygame.android.net.NetRequest;
import com.mygame.android.net.handle.PhpModelNetResponseParse;

/**
 * php接口请求类
 * */
public class HttpPhpGetRequest extends NetRequest{
	
	private static final String SECRET = "2e3aa31da2886b5be5cbf7c104a0da37";	

	public HttpPhpGetRequest(Activity hostActivity, CallBackAsync callBackAsync) {
		super(hostActivity, callBackAsync);
		dataParseHandle = PhpModelNetResponseParse.class;
	}

	@Override
	public List<NameValuePair> complieParams() {
		// TODO Auto-generated method stub
		long time = System.currentTimeMillis() / 1000;
		addParameter("format", "json");
		addParameter("op", String.valueOf(time));
		String origin = "format=jsonop=" + time
				+ "secret="+SECRET;
		addParameter("sign",encodeByMD5(origin));
		return super.complieParams();
	}
	
}
