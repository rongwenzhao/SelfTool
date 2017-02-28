package com.mygame.android.net.lobby;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

import com.mygame.android.net.NetRequest;
import com.mygame.android.net.handle.GateModelNetResponseParse;

/**
 * C++ http网关服务器请求类
 * */
public class HttpGateRequest extends NetRequest{
	
	public HttpGateRequest(Activity hostActivity, CallBackAsync callBackAsync) {
		super(hostActivity, callBackAsync);
		dataParseHandle = GateModelNetResponseParse.class;
		responseCharacterSet = "ISO-8859-1";
	}

	@Override
	public List<NameValuePair> complieParams() {
		// TODO Auto-generated method stub
		List<NameValuePair> params =  super.complieParams();
		StringBuilder sb = new StringBuilder();

		for (NameValuePair param : params) {
			sb.append(param.getName());
			sb.append("=");
			sb.append(param.getValue());
			sb.append(",");
		}

		StringBuilder origin = sb.deleteCharAt(sb.length() - 1);
		params.add(new BasicNameValuePair("sign", encodeByMD5(origin.toString())));
		return params;
	}	
	
}
