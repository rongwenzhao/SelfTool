package com.mygame.android.activity.base;

import android.view.View;

import com.mygame.android.application.base.MYGameApplication;
import com.mygame.android.ui.api.UIResource;

public abstract class CommonBaseBottomMenuActivity extends BaseBottomMenuActivity {

	public OnBaseMenuItemClickListener onButtomMenuItemClick() {
		String clickClassString = MYGameApplication.resources.get(0).getClickListener();
		Class<OnBaseMenuItemClickListener> clz;
		try {
			clz = (Class<OnBaseMenuItemClickListener>) Class.forName(clickClassString);
			OnBaseMenuItemClickListener listener = clz.newInstance();
			return listener;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void onBottomMenuInit(View bottomMenuView) {
		for(UIResource resource : MYGameApplication.resources){
			int menuLayout = getResources().getIdentifier(resource.getLayout(), "layout", getApplication().getPackageName());
			int menuShowBack = getResources().getIdentifier(resource.getShowBack(), "drawable", getApplication().getPackageName());
			int menuBaseBack = getResources().getIdentifier(resource.getBaseBack(), "drawable", getApplication().getPackageName());
			addButtomMenu(new MenuItem(resource.getId(), menuLayout, menuShowBack, menuBaseBack));
		}
		View v = menus.get(0);
		if(v != null){
			MenuItem item = (MenuItem)v.getTag();
			v.setBackgroundResource(item.getShowBackGround());
		}
	}

}
