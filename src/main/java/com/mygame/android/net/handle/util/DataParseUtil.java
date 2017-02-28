package com.mygame.android.net.handle.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.mygame.android.json.templete.annotation.JsonClass;
import com.mygame.android.json.templete.annotation.JsonKey;
import com.mygame.android.json.templete.annotation.JsonType;
import com.mygame.android.model.Model;

public class DataParseUtil {

	public static String getJsonKey(Class responseClass,String defaultKey){
		String dataKey = defaultKey;
		if(responseClass.getAnnotation(JsonKey.class) != null){
			JsonKey jsonKey = (JsonKey) responseClass.getAnnotation(JsonKey.class);
			dataKey = jsonKey.key();
			if(dataKey == null){
				dataKey = defaultKey;
			}
		}
		return dataKey;
	}
	
	public static void complieDataParse(String defaultDataKey,JSONObject json,Model<Object> model,Class responseClass) throws JSONException{
		if(responseClass.getAnnotation(JsonClass.class) != null){
			JsonClass jsonClass = (JsonClass) responseClass.getAnnotation(JsonClass.class);
			String dataKey = DataParseUtil.getJsonKey(responseClass, defaultDataKey);
			if(json.isNull(dataKey)){
				return;
			}
			if(jsonClass.type() == JsonType.JSONOBJECT){
				model.setData(JSON.parseObject(String.valueOf(json.getJSONObject(dataKey)), responseClass));
			}else if(jsonClass.type() == JsonType.JSONLIST){
				model.setData(JSON.parseArray(String.valueOf(json.getJSONArray(dataKey)), responseClass));
			}
		}else{
			model.setData(JSON.parseObject(String.valueOf(json.getJSONObject(defaultDataKey)), responseClass));
		}
	}
	
}
