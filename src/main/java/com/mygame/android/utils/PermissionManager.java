package com.mygame.android.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class PermissionManager {
	private PermissionManager() {}
	
	private static Map<String, WakeLockWrapper> mWakeLockMap = new ConcurrentHashMap<String, WakeLockWrapper>();

	synchronized public static WakeLockWrapper getWakeLockInstance(Context context, String tag) {
		return getWakeLockInstance(context, WakeLockWrapper.PARTIAL_WAKE_LOCK, tag);
	}

	synchronized public static WakeLockWrapper getWakeLockInstance(Context context) {
		return getWakeLockInstance(context, WakeLockWrapper.PARTIAL_WAKE_LOCK, context.getApplicationInfo().packageName);
	}
	
	synchronized public static WakeLockWrapper getWakeLockInstance(Context context, int flags) {
		return getWakeLockInstance(context, flags, context.getApplicationInfo().packageName);
	}
	
	synchronized public static WakeLockWrapper getWakeLockInstance(Context context, int flags, String tag) {
		//LogByCodeLab.v("WakeLockWrapper.getWakeLockInstance(), tag: " + tag);
		WakeLockWrapper found = mWakeLockMap.get(tag);
		if (found == null) {
			found = new WakeLockWrapper(context, tag, flags);
			mWakeLockMap.put(tag, found);
		}
		return found;
	}

	synchronized public static int releaseAllWakeLocks() {
		int reVal = 0;
		for (WakeLockWrapper locker : mWakeLockMap.values()) {
			if (locker.release()) {
				++reVal;
			}
		}
		return reVal;
	}

	synchronized public static int freeAllWakeLocks() {
		int reVal = mWakeLockMap.size();
		releaseAllWakeLocks();
		mWakeLockMap.clear();
		return reVal;
	}

	public static class WakeLockWrapper {

		private static boolean isPermissionChecked = false;
		private static boolean isPermissionGranted = false;

		volatile private WakeLock wakeLock = null;

		/** @see PowerManager#PARTIAL_WAKE_LOCK */
		public static final int PARTIAL_WAKE_LOCK = PowerManager.PARTIAL_WAKE_LOCK;
		/** @see PowerManager#SCREEN_DIM_WAKE_LOCK */
		@SuppressWarnings("deprecation")
		public static final int SCREEN_DIM_WAKE_LOCK = PowerManager.SCREEN_DIM_WAKE_LOCK;
		/** @see PowerManager#FULL_WAKE_LOCK */
		@SuppressWarnings("deprecation")
		public static final int FULL_WAKE_LOCK = PowerManager.FULL_WAKE_LOCK;

		WakeLockWrapper(Context context, String tag, int flags) {
			if (isAvailable(context)) {
				wakeLock = ((PowerManager)context.getSystemService(Context.POWER_SERVICE))
				.newWakeLock(flags, tag);
			}
		}

		public boolean acquire() {
			if (wakeLock != null && wakeLock.isHeld() == false) {
				//LogByCodeLab.v("WakeLockWrapper.acquire()");
				wakeLock.acquire();
				return true;
			}
			return false;
		}

		public boolean acquire(long timeout) {
			if (wakeLock != null /*&& wakeLock.isHeld() == false*/) {	
				//LogByCodeLab.v("WakeLockWrapper.acquire() timeout: "+timeout);
				wakeLock.acquire(timeout);
				return true;
			}
			return false;
		}

		public boolean release() {
			if (wakeLock != null && wakeLock.isHeld() == true) {
				//LogByCodeLab.v("WakeLockWrapper.release()");
				wakeLock.release();
				return true;
			}
			return false;
		}

		synchronized public static boolean isAvailable(Context context) {
			if (isPermissionChecked == false) {
				isPermissionChecked = true;
				PackageManager packageManager = context.getPackageManager();
				if (PackageManager.PERMISSION_GRANTED
						== packageManager.checkPermission(android.Manifest.permission.WAKE_LOCK, context.getPackageName())) {
					isPermissionGranted = true;
				}
			}
			//LogByCodeLab.v("WakeLockWrapper.isAvailable() result: "+isPermissionGranted);
			return isPermissionGranted;
		}
	}
}
