package com.mygame.android.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

/**
 * 
 * @author Arngard, Hovan
 */
public abstract class BaseService extends Service {

	@SuppressWarnings("rawtypes")
	private static final Class[] mStartForegroundSignature = new Class[] {int.class, Notification.class};
	@SuppressWarnings("rawtypes")
	private static final Class[] mStopForegroundSignature = new Class[] {boolean.class};
	private Method mStartForeground;
	private Method mStopForeground;
	private Object[] mStartForegroundArgs = new Object[2];
	private Object[] mStopForegroundArgs = new Object[1];

	@Override
	public void onCreate() {
		try {
			mStartForeground = getClass().getMethod("startForeground", mStartForegroundSignature);
			mStopForeground = getClass().getMethod("stopForeground", mStopForegroundSignature);
		} catch (NoSuchMethodException e) {
			// Running on an older platform.
			mStartForeground = mStopForeground = null;
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		handleStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handleStart(intent, startId);
		return START_STICKY;
	}

	abstract protected void handleStart(Intent intent, int startId);

	/**
	 * This is a wrapper around the new startForeground method, using the older APIs if it is not available.
	 * @param id
	 * @param notification
	 * @see Service#startForeground(int, Notification)
	 * @see Service#setForeground(boolean)
	 */
	public final void startForegroundCompat(int id, Notification notification) {
		// If we have the new startForeground API, then use it.
		if (mStartForeground != null) {
			mStartForegroundArgs[0] = Integer.valueOf(id);
			mStartForegroundArgs[1] = notification;
			try {
				mStartForeground.invoke(this, mStartForegroundArgs);
			} catch (InvocationTargetException e) {
				// Should not happen.
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// Should not happen.
				e.printStackTrace();
			}
			return;
		}

		// Fall back on the old API.
		try {
			mStopForegroundArgs[0] = Boolean.TRUE;
			getClass().getMethod("setForeground",new Class[] {boolean.class}).invoke(this, mStopForegroundArgs);
		} catch (Throwable e) {
			// Should not happen.
			e.printStackTrace();
		}
	}

	/**
	 * This is a wrapper around the new stopForeground method, using the older APIs if it is not available.
	 * @param id
	 * @see Service#stopForeground(boolean)
	 * @see Service#setForeground(boolean)
	 */
	public final void stopForegroundCompat(int id) {
		// If we have the new stopForeground API, then use it.
		if (mStopForeground != null) {
			mStopForegroundArgs[0] = Boolean.TRUE;
			try {
				mStopForeground.invoke(this, mStopForegroundArgs);
			} catch (InvocationTargetException e) {
				// Should not happen.
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// Should not happen.
				e.printStackTrace();
			}
			return;
		}

		// Fall back on the old API.  Note to cancel BEFORE changing the
		// foreground state, since we could be killed at that point.
		try {
			mStopForegroundArgs[0] = Boolean.FALSE;
			getClass().getMethod("setForeground",new Class[] {boolean.class}).invoke(this, mStopForegroundArgs);
		} catch (Throwable e) {
			// Should not happen.
			e.printStackTrace();
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	/**
	 * Service 내부의 Worker Thread의 Looper와 통신하는 Handler.<br>
	 * 생성자에서 Worker thread 의 looper 를 넘긴다.
	 * @author namkhoh
	 */
	protected final class WorkerHandler extends Handler {
		public WorkerHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			onWorkerRequest(msg);
		}
	}

	protected HandlerThread mWorker = null;
	protected WorkerHandler mHandler = null;

	protected void startWorker(String tag) {
		// 작업 스레드를 시작한다. 중복 호출을 허용하는 안전 시작.
		if (mWorker == null) {
			mWorker = null;
			mWorker = new HandlerThread(tag);
			mWorker.start();
			mHandler = null;
			mHandler = new WorkerHandler(mWorker.getLooper());
		} else if (mWorker.getState() == Thread.State.NEW) {
			mWorker.start();
			mHandler = null;
			mHandler = new WorkerHandler(mWorker.getLooper());
		} else if (mWorker.getState() == Thread.State.WAITING) {
			mHandler = null;
			mHandler = new WorkerHandler(mWorker.getLooper());
		} else if (mWorker.getState() == Thread.State.TERMINATED) {
			mWorker = null;
			mWorker = new HandlerThread(tag);
			mWorker.start();
			mHandler = null;
			mHandler = new WorkerHandler(mWorker.getLooper());
		}
	}

	protected void endWorker() {
		mHandler = null;
		HandlerThread snap = mWorker;
		mWorker = null;
		snap.quit();
		snap.interrupt();
	}

	protected WorkerHandler getWorkerHandler() {
		return mHandler;
	}

	abstract protected void onWorkerRequest(Message msg);

}
