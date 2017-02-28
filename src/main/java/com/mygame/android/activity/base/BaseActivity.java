package com.mygame.android.activity.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.mygame.android.application.base.MYGameApplication;
import com.mygame.android.ui.util.UIElement;
import com.mygame.android.ui.util.UISet;
import com.mygame.android.view.TopBarView;

public abstract class BaseActivity extends Activity {
	
	private static final String TAG = BaseActivity.class.getName();
	private static Map<String,Object> caches = 
			new HashMap<String,Object>();
	public static final List<Activity> activityGroup = new ArrayList<Activity>();
	protected TopBarView topbar;
	protected MYGameApplication application;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getContextLayout());
		application = (MYGameApplication)getApplication();
		preInitView();
		initViewByAnnotation();
		initView();
		topbar.getTopTitle().setGravity(Gravity.CENTER);
		topbar.getTopLeftButtonView().setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
		initListener();
		initData();
		activityGroup.add(this);
	}
	
	protected void onStart() {
		super.onStart();
		
	}
	
	public void setCacheParams(String activity,Object value){
		caches.put(activity, value);
	}
	
	public Object getCacheParams(String activity){
		return caches.get(activity);
	}
	
	protected void preInitView(){
		topbar = (TopBarView)findViewById(getResources().getIdentifier("top_bar", "id", getApplication().getPackageName()));
	}
	
	private void initViewByAnnotation(){
		Field[] fields = this.getClass().getDeclaredFields();
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
					View v = findViewById(getResources().getIdentifier(name, "id", getApplication().getPackageName()));
					try {
						field.set(this, v);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e(TAG, e.getMessage());
					} 
					break;
				case UI_COLOR:
					int color = getResources().getColor(getResources().getIdentifier(name, "color", getApplication().getPackageName()));
					try {
						field.set(this, color);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e(TAG, e.getMessage());
					} 
					break;
				case UI_STRING:
					String string = getResources().getString(getResources().getIdentifier(name, "string", getApplication().getPackageName()));
					try {
						field.set(this, string);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e(TAG, e.getMessage());
					} 
					break;
				case UI_DRAWABLE:
					Drawable d = getResources().getDrawable(getResources().getIdentifier(name, "drawable", getApplication().getPackageName()));
					try {
						field.set(this, d);
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
	
	protected void setTitle(String title){
		if(null != topbar){
			topbar.getTopTitle().setText(title);
		}
	}
	
	protected void goneLeftButton(){
		if(null != topbar){
			topbar.getTopLeftButton().setVisibility(View.GONE);
		}
	}

	
	protected void goneRightButton(){
		if(null != topbar){
			topbar.getTopRightButton().setVisibility(View.GONE);
		}
	}
	
	protected void visibleLeftButton(){
		if(null != topbar){
			topbar.getTopLeftButton().setVisibility(View.VISIBLE);
		}
	}
	
	protected void visibleRightButton(){
		if(null != topbar){
			topbar.getTopRightButton().setVisibility(View.VISIBLE);
		}
	}
	
	protected void onDestroy() {
		super.onDestroy();
		activityGroup.remove(this);
	}
	protected abstract int getContextLayout();
	protected abstract void initView();
	protected abstract void initListener();
	protected abstract void initData();
	
	
}
