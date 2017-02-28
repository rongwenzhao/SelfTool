package com.mygame.android.net.handle;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.mygame.android.json.templete.annotation.JsonClass;
import com.mygame.android.json.templete.annotation.JsonKey;
import com.mygame.android.json.templete.annotation.JsonType;
import com.mygame.android.model.Model;
import com.mygame.android.net.NetRequest;
import com.mygame.android.net.handle.util.DataParseUtil;
import com.mygame.android.net.lobby.BaseGateModel;
import com.mygame.android.net.lobby.HttpGateRequest;

public class GateModelNetResponseParse implements INetResponseDataParse {

	private static final String DEFAULT_DATA_KEY = "data";
	
	@Override
	public <T> BaseGateModel<T> responseDataParse(NetRequest request, String result,Class responseClass) throws Exception {
		if(!(request instanceof HttpGateRequest)){
			return null;
		}
		if(responseClass == null){
			BaseGateModel ret = new BaseGateModel();
			ret.setCode(0);
			ret.setData(result);
			ret.setErrorCode(0);
			ret.setSuccess(true);
			ret.setMsg(null);
			ret.setErrorMessage(null);
			return ret;
		}
		
		if(null != request.getResponseCharacterSet()){
			result = new String(result.getBytes(request.getResponseCharacterSet()),"UTF-8");
		}
		
		BaseGateModel<Object> model = JSON.parseObject(result, BaseGateModel.class);
		JSONObject json = new JSONObject(result);

		DataParseUtil.complieDataParse(DEFAULT_DATA_KEY, json, model, responseClass);
		return (BaseGateModel<T>) model;
	}

}
