package com.mygame.android.net;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.mygame.android.encode.util.MYGameDesUtil;
import com.mygame.android.json.IJson;
import com.mygame.android.json.JsonModuleBean;
import com.mygame.android.net.NetRequest;

public class CommonHttpRequest extends NetRequest {

	private String requestUrl;
	private String requestMethod;
	private String irequest;
	public static final String IGNORE_DES = "IGNORE_DES";

	public CommonHttpRequest(Activity hostActivity, CallBackAsync callBackAsync) {
		super(hostActivity, callBackAsync);
		addParameter(IGNORE_DES, "false");
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public void setIgnoreDes(boolean ignore) {
		if (ignore) {
			addParameter(IGNORE_DES, "true");
		} else {
			addParameter(IGNORE_DES, "false");
		}
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public void setRequestParamers(IRequest requestParams) {
		if ("true".equals(getParameter(CommonHttpRequest.IGNORE_DES))) {
			this.irequest = JSONObject.toJSONString(requestParams);
		} else {
			try {
				String jsonString = JSONObject.toJSONString(requestParams);
				jsonString = new String(jsonString.getBytes(), "UTF-8");
				this.irequest = MYGameDesUtil.encryptDes(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
				JsonModuleBean<IJson> error = new JsonModuleBean<IJson>();
				error.setError("初始值设置失败");
				error.setSuccess(false);
				error.setErrorCode(-1);
				callBackAsync.callBackAsync(error);
			}
		}

	}

	protected void execute() {
		this.addParameter("requestMethod", requestMethod);
		System.out.println(requestUrl + "?requestMethod=" + requestMethod + "&"
				+ "jrequest=" + irequest);
		this.addParameter("jrequest", irequest);
	}

}
