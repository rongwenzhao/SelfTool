package com.mygame.android.json.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.mygame.android.json.IJson;
import com.mygame.android.json.JsonFormatException;
import com.mygame.android.json.JsonModuleBean;
import com.mygame.android.json.templete.annotation.JsonClass;
import com.mygame.android.json.templete.annotation.JsonField;
import com.mygame.android.json.templete.annotation.JsonType;

public class JsonFormatFactory {

	private static final String DATA_SECTION = "data";

	public static String validateString(JSONObject jsonObj, String name)
			throws JSONException {
		if (jsonObj.isNull(name)) {
			return null;
		} else {
			return jsonObj.getString(name);
		}
	}

	public static int validateInt(JSONObject jsonObj, String name)
			throws JSONException {
		if (jsonObj.isNull(name)) {
			return 0;
		} else {
			return jsonObj.getInt(name);
		}
	}

	public static long validateLong(JSONObject jsonObj, String name)
			throws JSONException {
		if (jsonObj.isNull(name)) {
			return 0;
		} else {
			return jsonObj.getLong(name);
		}
	}

	public static boolean validateBoolean(JSONObject jsonObj, String name)
			throws JSONException {
		if (jsonObj.isNull(name)) {
			return false;
		} else {
			return jsonObj.getBoolean(name);
		}
	}

	public static double validateDouble(JSONObject jsonObj, String name)
			throws JSONException {
		if (jsonObj.isNull(name)) {
			return 0.0;
		} else {
			return jsonObj.getDouble(name);
		}
	}
	
	private static IJson getBeanWithFastJson(JSONObject jsonObj,
			Class<? extends IJson> class_) throws JsonFormatException {
		return JSON.parseObject(jsonObj.toString(), class_);
	}

	private static IJson getBean(JSONObject jsonObj,
			Class<? extends IJson> class_) throws JsonFormatException {
		if (jsonObj == null || class_ == null) {
			return null;
		}

		Field[] fields = class_.getDeclaredFields();
		IJson instance;
		try {
			instance = class_.newInstance();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new JsonFormatException(e1);
		}
		for (Field f : fields) {
			f.setAccessible(true);
			if (f.isAnnotationPresent(JsonField.class)) {
				JsonField field_def = f.getAnnotation(JsonField.class);
				if (field_def != null) {
					String name = field_def.name();
					Class type = field_def.type();
					if(name == null || name.equals("")){
						name = f.getName();
					}
					if(type == Void.class ){
						type = f.getType();
					}
					try {
						if (type == String.class) {
							f.set(instance, validateString(jsonObj, name));
						} else if (type == int.class || type == Integer.class) {
							f.set(instance, validateInt(jsonObj, name));
						} else if (type == boolean.class || type == Boolean.class) {
							f.set(instance, validateBoolean(jsonObj, name));
						} else if (type == double.class || type == Double.class) {
							f.set(instance, validateDouble(jsonObj, name));
						} else if (type == long.class || type == Long.class) {
							f.set(instance, validateLong(jsonObj, name));
						}
						else if (type == List.class) {
							if (!jsonObj.isNull(name)) {
								JSONArray j = jsonObj.getJSONArray(name);
								f.set(instance, getList(j,field_def.entity()));
							}

						} else if (type == IJson.class) {
							if (!jsonObj.isNull(name)) {
								JSONObject j = jsonObj.getJSONObject(name);
								f.set(instance, getBean(j, field_def.entity()));
							}
						} else {
							if (!jsonObj.isNull(name)) {
								Class[] interfaces = f.getType()
										.getInterfaces();
								if (interfaces != null
										&& 0 != interfaces.length) {
									for (Class t : interfaces) {
										if (t == List.class) {
											JSONArray j = jsonObj
													.getJSONArray(name);
											f.set(instance, getList(j, type));
										} else if (t == IJson.class) {
											if (!jsonObj.isNull(name)) {
												JSONObject j = jsonObj
														.getJSONObject(name);
												f.set(instance,
														getBean(j,
																(Class<? extends IJson>) f
																		.getType()));
											}
										}
									}
								}
							}
						}
					} catch (Exception e) {
						System.out.println("fildname is : " + name);
						e.printStackTrace();
						throw new JsonFormatException(e);
					}
				}
			}
		}
		return instance;
	}
	
	private static List<IJson> getListWithFastJson(JSONArray jsonarray,
			Class<? extends IJson> class_) throws JsonFormatException {
		return (List<IJson>) JSON.parseArray(jsonarray.toString(), class_);
	}

	private static List<IJson> getList(JSONArray jsonarray,
			Class<? extends IJson> class_) throws JsonFormatException {
		if (jsonarray == null || class_ == null) {
			return null;
		}
		List<IJson> retlist = new ArrayList<IJson>();
		for (int i = 0; i < jsonarray.length(); i++) {
			try {
				JSONObject jsonObj = jsonarray.getJSONObject(i);
				IJson instance = getBean(jsonObj, class_);
				retlist.add(instance);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new JsonFormatException(e);
			}
		}

		return retlist;
	}

	public static JsonModuleBean<? extends IJson> getJsonModuleBean(
			JSONObject jsonObj,String keyName, Class<? extends IJson> class_)
			throws JsonFormatException {

		if (jsonObj == null || class_ == null) {
			return null;
		}

		JsonModuleBean<IJson> ret = new JsonModuleBean<IJson>();
		try {
			ret.setError(validateString(jsonObj, "error"));
			ret.setErrorCode(validateInt(jsonObj, "errorCode"));
			ret.setSuccess(validateBoolean(jsonObj, "success"));
			JSONObject jobj = null;
			try {
				if(TextUtils.isEmpty(keyName)){
					keyName = DATA_SECTION;
				}
				if (!jsonObj.isNull(keyName)) {
					jobj = jsonObj.getJSONObject(DATA_SECTION);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ret;
			}
			if (jobj != null) {
				ret.setData(getBeanWithFastJson(jobj, class_));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JsonFormatException(e);
		}
		return ret;
	}

	public static JsonModuleBean<? extends List> getJsonModuleBeanList(
			JSONObject jsonObj, String keyName,Class<? extends IJson> class_)
			throws JsonFormatException {

		if (jsonObj == null || class_ == null) {
			return null;
		}

		JsonModuleBean<List<IJson>> ret = new JsonModuleBean<List<IJson>>();
		try {
			ret.setError(validateString(jsonObj, "error"));
			ret.setErrorCode(validateInt(jsonObj, "errorCode"));
			ret.setSuccess(validateBoolean(jsonObj, "success"));
			if(TextUtils.isEmpty(keyName)){
				keyName = DATA_SECTION;
			}
			if (!jsonObj.isNull(keyName)) {
				JSONArray jobj = jsonObj.getJSONArray(DATA_SECTION);
				if (jobj != null) {
					ret.setData(getListWithFastJson(jobj, class_));

				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JsonFormatException(e);
		}
		return ret;
	}

	public static JsonModuleBean<?> getJsonModuleBeanParse(JSONObject jsonObj,String keyName,
			Class<? extends IJson> class_) throws JsonFormatException {
		if (jsonObj == null || class_ == null) {
			return null;
		}

		JsonClass classType = class_.getAnnotation(JsonClass.class);
		if (null != classType) {
			if (classType.type() == JsonType.JSONLIST) {
				return getJsonModuleBeanList(jsonObj, keyName,class_);
			} else if (classType.type() == JsonType.JSONOBJECT) {
				return getJsonModuleBean(jsonObj, keyName,class_);
			}
		}
		return null;
	}
}
