package com.mygame.android.ui.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by msgli on 16/9/10.
 */
public final class StatusBarUtils {

    private static final String TITLE_BAR_ID = "title_bar";

    /**
     * Activity 中使用
     * */
    public static void setStatusBarColor(Activity context){
        setStatusBarColor(context,TITLE_BAR_ID,context.getResources().getColor(android.R.color.transparent));
    }

    /**
     * Activity 中使用
     * */
    public static void setStatusBarColor(Activity context,String id,int color){
        int titleId = context.getResources().getIdentifier(id, "id",context.getApplication().getPackageName());
        if(titleId <= 0){
            return;
        }
        setStatusBarColor(context,titleId,color);
    }

    /**
     * Activity 中使用
     * */
    public static void setStatusBarColor(Activity context,int id,int color){

        View titleView = context.findViewById(id);

        if(titleView == null){
            return;
        }
        setStatusBarColor(context,titleView,color);
    }

    /**
     * 此方法可用于fragment及Activity中状态栏透明
     * */
    public static void setStatusBarColor(Activity context,View titleView,int color){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.getWindow().setStatusBarColor(color);
            }

            int statusBarHeight = getStatusBarHeight(context);
            ViewGroup.LayoutParams layoutParams = titleView.getLayoutParams();
            if (layoutParams == null){
                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                titleView.measure(w, h);
                int height = titleView.getMeasuredHeight();
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
                //titleView.setLayoutParams(layoutParams);
            }
            layoutParams.height = layoutParams.height + statusBarHeight;
            titleView.setLayoutParams(layoutParams);
            titleView.setPadding(0,statusBarHeight,0,0);
        }
    }

    public static void setStatusBarTranslucent(Activity context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.getWindow().setStatusBarColor(context.getResources().getColor(android.R.color.transparent));
            }

        }
    }

    /**
     * 获取状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
