package com.mygame.android.ui.api;

import java.io.Serializable;

import com.mygame.android.activity.base.BaseBottomMenuActivity.OnBaseMenuItemClickListener;

public class UIResource implements Serializable {

	private int id;
	private String layout;
	private String showBack;
	private String baseBack;
	private String clickListener;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getShowBack() {
		return showBack;
	}

	public void setShowBack(String showBack) {
		this.showBack = showBack;
	}

	public String getBaseBack() {
		return baseBack;
	}

	public void setBaseBack(String baseBack) {
		this.baseBack = baseBack;
	}

	public String getClickListener() {
		return clickListener;
	}

	public void setClickListener(String clickListener) {
		this.clickListener = clickListener;
	}

}
