package com.mygame.android.activity.base;

import java.lang.reflect.Field;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.mygame.android.ui.util.OnClickSet;

public abstract class BaseActivityOnClickImpl extends BaseActivity implements
		OnClickListener {
	private static final String TAG = "BaseActivityOnClickImpl";
	protected void initListener() {
		Field[] fields = this.getClass().getDeclaredFields();
		if(fields == null||fields.length==0){
			return;
		}
		for(int i = 0; i < fields.length;i ++){
			//UISet
			Field field = fields[i];
			field.setAccessible(true);
			if (field.isAnnotationPresent(OnClickSet.class)){
				try {
					Object fieldValue = field.get(this);
					if(fieldValue instanceof View){
						View thisView = (View)fieldValue;
						thisView.setOnClickListener(this);
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e(TAG, "Error field is " + field.getName() + "\n" + e.getMessage());
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e(TAG, "Error field is " + field.getName() + "\n" + e.getMessage());
				}
			}
		}
	}
}
