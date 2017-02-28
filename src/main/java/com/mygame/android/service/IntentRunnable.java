package com.mygame.android.service;

import android.content.Intent;

public abstract class IntentRunnable implements Comparable<IntentRunnable> {

	String mAction = null;

	public IntentRunnable(String action) {
		mAction = action;
	}
	
	public String getAction() {
		return mAction;
	}

	@Override
	public int compareTo(IntentRunnable another) {
		return mAction.compareTo(another.mAction);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (this == o) {
			return true;
		}
		if (! (o instanceof IntentRunnable)) {//if (this.getClass() != o.getClass()) {
			return false;
		}
		String oAction = ((IntentRunnable) o).mAction;
		if (mAction == null) {
			if (oAction == null) {
				return true;
			} else {
				return false;
			}
		} else {
			if (oAction == null) {
				return false;
			} else {
				if (mAction.equals(oAction)) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	public abstract void run(Intent intent);

}
