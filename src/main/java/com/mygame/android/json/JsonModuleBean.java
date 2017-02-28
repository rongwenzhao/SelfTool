package com.mygame.android.json;

import java.io.Serializable;

import com.mygame.android.json.templete.annotation.JsonField;
import com.mygame.android.model.Model;

public class JsonModuleBean<T> extends Model<T> implements Serializable {
	// {"success":false;"error":null;"errorCode":0;"data":null}
	@JsonField(name = "error", type = String.class)
	private String error;

	@JsonField(name = "success", type = boolean.class)
	private boolean success;

	private T data;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
