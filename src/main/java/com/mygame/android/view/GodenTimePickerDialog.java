package com.mygame.android.view;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mygame.android.library.R;
import com.mygame.android.view.widget.NumericWheelAdapter;
import com.mygame.android.view.widget.OnWheelChangedListener;
import com.mygame.android.view.widget.OnWheelScrollListener;
import com.mygame.android.view.widget.WheelView;

public class GodenTimePickerDialog extends PopupWindow {

	private View view;
	private LayoutInflater inflater;
	private Button setting_btn;
	private Button cancel_btn;
	private WheelView hours;
	private WheelView mins;

	// Time changed flag
	private boolean timeChanged = false;
	//
	private boolean timeScrolled = false;

	private TextView tv;

	public GodenTimePickerDialog(Activity context, final TextView tv) {
		super(context);
		this.tv = tv;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.time_layout, null);
		setting_btn = (Button) view.findViewById(R.id.time_set);
		cancel_btn = (Button) view.findViewById(R.id.time_cancel);

		cancel_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});

		setting_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int a=hours.getCurrentItem();
				int b=mins.getCurrentItem();
				if(a>9&&b>9){
					tv.setText(a+":"+b);
				}
				else if(a>9&&b<10){
					tv.setText(a+":"+"0"+b);
				}
				else if(b>9&&a<10){
					tv.setText("0"+a+":"+b);
				}
				else{
					tv.setText("0"+a+":"+"0"+b);
				}
				
				dismiss();
			}
		});

		this.setContentView(view);
		this.setWidth(WindowManager.LayoutParams.FILL_PARENT);
		this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);// 返回键关闭,必须
		this.setAnimationStyle(R.style.PopAnimation);
		this.setBackgroundDrawable(null);
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = view.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_DOWN) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

		// 点击返回键关闭对话框
		view.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dismiss();
					return true;
				}
				return false;
			}
		});

		hours = (WheelView) view.findViewById(R.id.hour);
		mins = (WheelView) view.findViewById(R.id.mins);
		hours.setAdapter(new NumericWheelAdapter(0, 23));
		hours.setVisibleItems(5);
		mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		mins.setVisibleItems(5);
		mins.setCyclic(true);
		Calendar calendar = Calendar.getInstance();
		int Hours = calendar.get(Calendar.HOUR_OF_DAY);
		int Mins = calendar.get(Calendar.MINUTE);
		hours.setCurrentItem(Hours);
		mins.setCurrentItem(Mins);

		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!timeScrolled) {
					timeChanged = true;
					timeChanged = false;
				}
			}
		};

		hours.addChangingListener(wheelListener);
		mins.addChangingListener(wheelListener);

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}

			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
				timeChanged = true;
				timeChanged = false;
			}
		};

		hours.addScrollingListener(scrollListener);
		mins.addScrollingListener(scrollListener);

	}

	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				wheel.setLabel(label);
			}
		});
	}

}
