package com.mygame.android.net.handle;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.mygame.android.net.NetRequest;
import com.mygame.android.net.handle.util.DataParseUtil;
import com.mygame.android.net.lobby.BaseGateModel;
import com.mygame.android.net.lobby.BasePhpModel;
import com.mygame.android.net.lobby.HttpPhpGetRequest;

public class PhpModelNetResponseParse implements INetResponseDataParse {

	private static final String DEFAULT_DATA_KEY = "data";
	
	@Override
	public <T> BasePhpModel<T> responseDataParse(NetRequest request, String result,Class responseClass) throws Exception {
		if(!(request instanceof HttpPhpGetRequest)){
			return null;
		}
		if(responseClass == null){
			BasePhpModel ret = new BasePhpModel();
			ret.setStatus(0);
			ret.setData(result);
			ret.setErrorCode(0);
			ret.setSuccess(true);
			ret.setErrorMessage(null);
			return ret;
		}
		BasePhpModel<Object> model = JSON.parseObject(result, BasePhpModel.class);
		JSONObject json = new JSONObject(result);

		DataParseUtil.complieDataParse(DEFAULT_DATA_KEY, json, model, responseClass);
		return (BasePhpModel<T>) model;
	}

}
