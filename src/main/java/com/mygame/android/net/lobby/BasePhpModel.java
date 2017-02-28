package com.mygame.android.net.lobby;

import com.mygame.android.model.Model;

public class BasePhpModel<T> extends Model<T> {

	private int status;

	private String statusnote;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusnote() {
		return statusnote;
	}

	public void setStatusnote(String statusnote) {
		this.statusnote = statusnote;
	}

}
