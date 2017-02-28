package com.mygame.android.net.lobby;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

import com.mygame.android.net.NetRequest;
import com.mygame.android.net.handle.PhpModelNetResponseParse;
import com.mygame.android.net.handle.UserCenterModelResponseDataParse;

/**
 * 用户中心相关接口请求类
 * */
public class HttpUserCenterRequest extends NetRequest {

	public static final String secretKey = "6a1494cf07452af16b70a5ca39664619";
	
	public HttpUserCenterRequest(Activity hostActivity,
			CallBackAsync callBackAsync) {
		super(hostActivity, callBackAsync);
		// TODO Auto-generated constructor stub
		dataParseHandle = UserCenterModelResponseDataParse.class;
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
			sb.append("&");
		}

		StringBuilder origin = sb.deleteCharAt(sb.length() - 1);
		params.add(new BasicNameValuePair("sign", encodeByMD5(origin.toString()+secretKey)));
		return params;
	}
	
}
