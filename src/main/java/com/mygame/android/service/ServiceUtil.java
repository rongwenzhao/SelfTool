package com.mygame.android.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class ServiceUtil {
	
	private static final String TAG = ServiceUtil.class.getName();
	
	public static final String SCHEDULE_TIME = "scheduleTime";

	public static void startSchedule(Service service, String action, long delay) {
		stopSchedulePrivate(service, action);
		
		long now = System.currentTimeMillis();
		Log.d(TAG, "ServiceUtil: Scheduling Action \"" + action + "\" with delay " + (delay/1000) + "sec");
		Intent i = new Intent();
		i.setClass(service, service.getClass());
		i.setAction(action);
		i.putExtra(SCHEDULE_TIME, android.os.SystemClock.elapsedRealtime());
		PendingIntent pi = PendingIntent.getService(service, 0, i, 0);
		AlarmManager alarmMgr = (AlarmManager)service.getSystemService(Context.ALARM_SERVICE);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, now + delay, pi);
	}

	public static void repeatSchedule(Service service, String action, long interval) {
		repeatSchedule(service, action, AlarmManager.RTC_WAKEUP, interval);
	}

	public static void repeatSchedule(Service service, String action, int type, long interval) {
		stopSchedulePrivate(service, action);
		
		Log.d(TAG, "ServiceUtil: Starting Action \"" + action + "\", with interval " + (interval/1000) + "sec");
		long now = System.currentTimeMillis();
		
		Intent i = new Intent();
		i.setClass(service, service.getClass());
		i.setAction(action);
		i.putExtra(SCHEDULE_TIME, android.os.SystemClock.elapsedRealtime());
		PendingIntent pi = PendingIntent.getService(service, 0, i, 0);
		AlarmManager alarmMgr = (AlarmManager)service.getSystemService(Context.ALARM_SERVICE);
		alarmMgr.setRepeating(type,  now + interval, interval, pi);
	}
	
	public static void stopSchedule(Service service, String action) {
		Log.d(TAG, "ServiceUtil: Stopping Action \"" + action + "\"");
		stopSchedulePrivate(service, action);
	}
	
	static void stopSchedulePrivate(Service service, String action) {
		Intent i = new Intent();
		i.setClass(service, service.getClass());
		i.setAction(action);
		PendingIntent pi = PendingIntent.getService(service, 0, i, 0);
		AlarmManager alarmMgr = (AlarmManager)service.getSystemService(Context.ALARM_SERVICE);
		alarmMgr.cancel(pi);
	}
	
	public static boolean isRunning(Context context, Class<? extends Service> serviceClass)
	throws SecurityException {
		
		String serviceName = serviceClass.getName();	
		if(serviceName != null) {
			ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningServiceInfo> infos = manager.getRunningServices(Integer.MAX_VALUE);	
			if(infos != null) {
				for (RunningServiceInfo info : infos) {
					if(info == null || info.service == null) continue;
					if (serviceName.equals(info.service.getClassName())) {	
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
