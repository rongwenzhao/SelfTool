package com.mygame.android.service;

import java.util.Map;
import java.util.TreeMap;

import android.content.Intent;

public abstract class IntentRunningService extends WorkerService {

	private Object mapLock = new Object();
	private IntentRunnable mForNullAction = null;
	private Map<String, IntentRunnable> mMap = new TreeMap<String, IntentRunnable>();

	public IntentRunnable addIntentRunnable(IntentRunnable IntentRunnable) {
		IntentRunnable reVal = null;
		synchronized (mapLock) {
			if (IntentRunnable.mAction == null) {
				reVal = mForNullAction;
				mForNullAction = IntentRunnable;
			} else {
				reVal = mMap.put(IntentRunnable.mAction, IntentRunnable);
			}
		}
		return reVal;
	}

	public IntentRunnable removeIntentRunnable(String action) {
		IntentRunnable reVal = null;
		synchronized (mapLock) {
			if (action == null) {
				reVal = mForNullAction;
				mForNullAction = null;
			} else {
				reVal = mMap.remove(action);
			}
		}
		return reVal;
	}

	public void resetIntentRunnables() {
		synchronized (mapLock) {
			mForNullAction = null;
			mMap.clear();
		}
	}

	public boolean processIntent(Intent intent) {
		try {
			if (intent == null) {
				return false;
			}
			String action = intent.getAction();
			synchronized (mapLock) {
				if (action == null && mForNullAction != null) {
					mForNullAction.run(intent);
					return true;
				} else {
					IntentRunnable IntentRunnable = mMap.get(action);
					if (IntentRunnable != null) {
						IntentRunnable.run(intent);
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onWorkerRequest(Intent intent, int startId) {
		processIntent(intent);
	}

}
