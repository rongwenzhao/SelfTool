package com.mygame.android.activity.base;

import java.util.ArrayList;
import java.util.List;

import com.mygame.android.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public abstract class BaseBottomMenuActivity extends BaseActivity{
	protected LinearLayout bottomMenuView;
	protected List<View> menus = new ArrayList<View>();
	protected List<BaseMenuItem> menuItems = new ArrayList<BaseBottomMenuActivity.BaseMenuItem>();
	protected OnBaseMenuItemClickListener l;
	
	public interface BaseMenuItem{
		public int getId();
		public int getLayout();
		public int getShowBackGround();
		public int getBaseBackGround();
	}
	
	public static class MenuItem implements BaseMenuItem{
		private int id;
		private int layout;
		private int showBackGround;
		private int baseBackGround;
		
		public MenuItem(int id, int layout, int showBackGround,
				int baseBackGround) {
			super();
			this.id = id;
			this.layout = layout;
			this.showBackGround = showBackGround;
			this.baseBackGround = baseBackGround;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getLayout() {
			return layout;
		}
		public void setLayout(int layout) {
			this.layout = layout;
		}
		public int getShowBackGround() {
			return showBackGround;
		}
		public void setShowBackGround(int showBackGround) {
			this.showBackGround = showBackGround;
		}
		public int getBaseBackGround() {
			return baseBackGround;
		}
		public void setBaseBackGround(int baseBackGround) {
			this.baseBackGround = baseBackGround;
		}
	}

	public LinearLayout getBottomMenuView() {
		return bottomMenuView;
	}
	public void setBottomMenuView(LinearLayout bottomMenuView) {
		this.bottomMenuView = bottomMenuView;
	}
	public List<View> getMenus() {
		return menus;
	}
	public void setMenus(List<View> menus) {
		this.menus = menus;
	}
	public List<BaseMenuItem> getMenuItems() {
		return menuItems;
	}
	public void setMenuItems(List<BaseMenuItem> menuItems) {
		this.menuItems = menuItems;
	}
	protected void preInitView(){
		RelativeLayout contextView = (RelativeLayout)LayoutInflater.from(this).inflate(getContextLayout(), null);
		bottomMenuView = new LinearLayout(this);
		//bottomMenuView.setLayoutParams(params);
		bottomMenuView.setGravity(Gravity.CENTER);
		onBottomMenuInit(bottomMenuView);
		int scrollViewLength = 0;
		for(View v : menus){
			scrollViewLength += Utils.dip2px(this, 80);
		}
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();//屏幕高度
		RelativeLayout.LayoutParams params = null;
		if(scrollViewLength <= wm.getDefaultDisplay().getWidth()){
			for(View menu : menus){
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				param.weight = 1;
				param.gravity = Gravity.CENTER;
				menu.setLayoutParams(param);
			}
			params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			bottomMenuView.setLayoutParams(params);
			contextView.addView(bottomMenuView);
		}else{
			params = new RelativeLayout.LayoutParams(scrollViewLength, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			HorizontalScrollView scrollView = new HorizontalScrollView(this);
			scrollView.setLayoutParams(params);
			scrollView.addView(bottomMenuView);
			contextView.addView(scrollView);
		}		
		setContentView(contextView);
		l = onButtomMenuItemClick();
		super.preInitView();
	}
	
	public interface OnBaseMenuItemClickListener{
		public void onClick(Activity context,BaseMenuItem item,View v);
	}
	
	public abstract OnBaseMenuItemClickListener onButtomMenuItemClick();
	public void addShowButtomMenu(BaseMenuItem item){
		if(null == item){
			return;
		}
		View v = LayoutInflater.from(this).inflate(item.getLayout(), null);
		v.setTag(item);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		param.weight = 1;
		param.gravity = Gravity.CENTER;
		v.setLayoutParams(param);
		v.setBackgroundResource(item.getShowBackGround());
		bottomMenuView.addView(v);
		v.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BaseMenuItem item = (BaseMenuItem)v.getTag();
				if(l != null){
					l.onClick(BaseBottomMenuActivity.this,item, v);
				}
			}
		});
	}
	public void addButtomMenu(BaseMenuItem item){
		if(null == item){
			return;
		}
		this.menuItems.add(item);
		View v = LayoutInflater.from(this).inflate(item.getLayout(), null);
		v.setTag(item);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Utils.dip2px(this, 80), Utils.dip2px(this, 60));
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();//屏幕高度
		//param.weight = 1;
		param.gravity = Gravity.CENTER;
		param.height = Utils.dip2px(this, 55);
		v.setLayoutParams(param);
		v.setBackgroundResource(item.getBaseBackGround());
		bottomMenuView.addView(v);
		menus.add(v);
		v.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BaseMenuItem item = (BaseMenuItem)v.getTag();
				if(l != null){
					l.onClick(BaseBottomMenuActivity.this,item, v);
				}
			}
		});
	}
	public abstract void onBottomMenuInit(View bottomMenuView);
}
