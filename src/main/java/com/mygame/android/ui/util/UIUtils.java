package com.mygame.android.ui.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

public class UIUtils {

	private static final String TAG = "com.mygame.android.ui.util.UIUtils";
	
	public static void initViewByAnnotation(Activity context,View containerView,Object containerHolder){
		Field[] fields = containerHolder.getClass().getDeclaredFields();
		if(fields == null||fields.length==0){
			return;
		}
		for(int i = 0; i < fields.length;i ++){
			//UISet
			Field field = fields[i];
			field.setAccessible(true);
			if (field.isAnnotationPresent(UISet.class)){
				UISet uiSet = field.getAnnotation(UISet.class);
				String name = uiSet.name();
				UIElement uiElement = uiSet.elementType();
				switch(uiElement){
				case UI_WEGHT:
					View v = containerView.findViewById(context.getResources().getIdentifier(name, "id",context.getApplication().getPackageName()));
					try {
						field.set(containerHolder, v);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e(TAG, e.getMessage());
					} 
					break;
				case UI_COLOR:
					int color = context.getResources().getColor(context.getResources().getIdentifier(name, "color", context.getApplication().getPackageName()));
					try {
						field.set(containerHolder, color);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e(TAG, e.getMessage());
					} 
					break;
				case UI_STRING:
					String string = context.getResources().getString(context.getResources().getIdentifier(name, "string", context.getApplication().getPackageName()));
					try {
						field.set(containerHolder, string);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e(TAG, e.getMessage());
					} 
					break;
				case UI_DRAWABLE:
					Drawable d = context.getResources().getDrawable(context.getResources().getIdentifier(name, "drawable", context.getApplication().getPackageName()));
					try {
						field.set(containerHolder, d);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("Error Field is " + field.getName());
						e.printStackTrace();
						Log.e(TAG, e.getMessage());
					} 
					break;
				default:
					break;
				}

			}
		}
	}

	public static void initActivityViewByAnnotation(Activity context,Object containerHolder){
		Field[] fields = containerHolder.getClass().getDeclaredFields();
		if(fields == null||fields.length==0){
			return;
		}
		for(int i = 0; i < fields.length;i ++){
			//UISet
			Field field = fields[i];
			field.setAccessible(true);
			if (field.isAnnotationPresent(UISet.class)){
				UISet uiSet = field.getAnnotation(UISet.class);
				String name = uiSet.name();
				UIElement uiElement = uiSet.elementType();
				switch(uiElement){
					case UI_WEGHT:
						View v = context.findViewById(context.getResources().getIdentifier(name, "id",context.getApplication().getPackageName()));
						try {
							field.set(containerHolder, v);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.e(TAG, e.getMessage());
						}
						break;
					case UI_COLOR:
						int color = context.getResources().getColor(context.getResources().getIdentifier(name, "color", context.getApplication().getPackageName()));
						try {
							field.set(containerHolder, color);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.e(TAG, e.getMessage());
						}
						break;
					case UI_STRING:
						String string = context.getResources().getString(context.getResources().getIdentifier(name, "string", context.getApplication().getPackageName()));
						try {
							field.set(containerHolder, string);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.e(TAG, e.getMessage());
						}
						break;
					case UI_DRAWABLE:
						Drawable d = context.getResources().getDrawable(context.getResources().getIdentifier(name, "drawable", context.getApplication().getPackageName()));
						try {
							field.set(containerHolder, d);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("Error Field is " + field.getName());
							e.printStackTrace();
							Log.e(TAG, e.getMessage());
						}
						break;
					default:
						break;
				}

			}
		}
	}
	
}
