package com.mygame.android.service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Intent;
import android.os.Message;

import com.mygame.android.utils.PermissionManager;

public abstract class WorkerService extends BaseService {

	private final Lock mWorkerLock = new ReentrantLock();

	public abstract String getWorkerTag();

	@Override
	public void onCreate() {
		super.onCreate();

		startWorker(getWorkerTag());
	}

	@Override
	protected void handleStart(Intent intent, int startId) {

		try {
			Message msg = getWorkerHandler().obtainMessage();
			msg.what = startId;
			msg.obj = intent;
			getWorkerHandler().dispatchMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		endWorker();
	}

	@Override
	protected void onWorkerRequest(Message msg) {
		mWorkerLock.lock();	
		PermissionManager.getWakeLockInstance(this, getWorkerTag()).acquire();
		try {
			onWorkerRequest((Intent) msg.obj, msg.what);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PermissionManager.getWakeLockInstance(this, getWorkerTag()).release();
			mWorkerLock.unlock();
		}
	}

	public abstract void onWorkerRequest(Intent intent, int startId);

}
