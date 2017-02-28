package com.mygame.android.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment{

	protected View thisView;
	protected boolean hasLoad;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		thisView = inflater.inflate(getFragmentLayout(), null);
		if(!hasLoad){
			initView();
			initListener();
			initData();
		}else{
			onReloadView();
			initListener();
			onReloadInitData();
		}
		hasLoad = true;
		return thisView;
	}
	
	protected abstract int getFragmentLayout();
	
	protected abstract void initView();
	
	protected abstract void initListener();
	
	protected abstract void initData();
	
	protected abstract void onReloadView();
	
	protected abstract void onReloadInitData();
}
