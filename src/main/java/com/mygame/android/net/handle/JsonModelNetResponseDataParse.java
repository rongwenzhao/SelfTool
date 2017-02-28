package com.mygame.android.net.handle;

import org.json.JSONException;
import org.json.JSONObject;

import com.mygame.android.json.IJson;
import com.mygame.android.json.JsonFormatException;
import com.mygame.android.json.JsonModuleBean;
import com.mygame.android.json.util.JsonFormatFactory;
import com.mygame.android.net.NetRequest;

public class JsonModelNetResponseDataParse implements INetResponseDataParse{

	@Override
	public <T> JsonModuleBean<T> responseDataParse(NetRequest request, String result,Class responseClass) throws Exception {
		JsonModuleBean<T> response = (JsonModuleBean<T>) JsonFormatFactory.getJsonModuleBeanParse(new JSONObject(result), "data", (Class<? extends IJson>) responseClass);
		return response;

	}

}
