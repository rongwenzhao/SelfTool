package com.mygame.android.net.lobby;

import com.mygame.android.model.Model;


public class BaseGateModel<T> extends Model<T> {

	private int code;

	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
