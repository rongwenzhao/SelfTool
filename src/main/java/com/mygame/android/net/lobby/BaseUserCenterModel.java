package com.mygame.android.net.lobby;

import com.mygame.android.model.Model;

public class BaseUserCenterModel<T> extends Model<T> {

	private int resultCode;

	private String desc;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
