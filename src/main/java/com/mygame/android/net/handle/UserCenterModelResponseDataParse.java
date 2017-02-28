package com.mygame.android.net.handle;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.mygame.android.net.NetRequest;
import com.mygame.android.net.handle.util.DataParseUtil;
import com.mygame.android.net.lobby.BaseUserCenterModel;
import com.mygame.android.net.lobby.HttpUserCenterRequest;

public class UserCenterModelResponseDataParse implements INetResponseDataParse {

	private static final String DEFAULT_DATA_KEY = "data";
	
	@Override
	public <T> BaseUserCenterModel<T> responseDataParse(NetRequest request, String result,Class responseClass) throws Exception {
		if(!(request instanceof HttpUserCenterRequest)){
			return null;
		}
		
		BaseUserCenterModel<Object> model = JSON.parseObject(result, BaseUserCenterModel.class);
		JSONObject json = new JSONObject(result);

		DataParseUtil.complieDataParse(DEFAULT_DATA_KEY, json, model, responseClass);
		return (BaseUserCenterModel<T>) model;
	}

}
