package com.mygame.android.view;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.text.format.Time;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimeSelector implements OnDateSetListener,OnTimeSetListener,GodenDatePickerDialog.OnDateSetListener {
	int year;
	int mounth;
	int day;
	int hour;
	int second;
	private Context context;
	private TextView v;
	private DatePickerDialog picker = null;
	private TimePickerDialog timePicker = null;
	
	private String spanString(int i){
		String t = "" + i;
		if(t.length() < 2){
			t = "0" + t;
		}
		return t;
	}
	
	public TimeSelector(Context context, TextView v) {
		super();
		this.context = context;
		this.v = v;
	}
	
	public void showNow(){
		/*Time time = new Time();
		time.setToNow();
		picker = new DatePickerDialog(context,this,time.year,time.month,time.monthDay);
		picker.show();*/
		GodenDatePickerDialog dialog = new GodenDatePickerDialog.Builder(this.context,this).create();
		dialog.show();
	}

	public void show(){
			picker = new DatePickerDialog(context,this,year,mounth-1,day);
			picker.show();
	}
	
	public void showTime(int hour,int minute){
		timePicker = new TimePickerDialog(context, this, hour, minute, true);
		timePicker.show();
	}
	
	public void showTimeNow(){
		Time time = new Time();
		time.setToNow();
		timePicker = new TimePickerDialog(context, this, time.hour, time.minute, true);
		timePicker.show();
	}

	public TimeSelector(Context context, TextView v,int year, int mounth, int day) {
		super();
		this.context = context;
		this.v = v;
		this.year = year;
		this.mounth = mounth;
		this.day = day;
	}

	public void setPreDate(Context context, TextView v,int year, int mounth, int day){
		this.context = context;
		this.v = v;
		this.year = year;
		this.mounth = mounth;
		this.day = day;
	}

	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		this.year = year;
		this.mounth = monthOfYear + 1;
		this.day = dayOfMonth;
		
		String date = year + "-" + spanString(mounth) + "-" + spanString(day);
		v.setTag(date);
		v.setText(date);
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		
		v.setTag(spanString(hourOfDay) + ":" + spanString(minute));
		v.setText(spanString(hourOfDay) + ":" + spanString(minute));
	}

	@Override
	public void onDateSet(GodenDatePickerDialog dialog, int year, int mounth,
			int day) {
		this.year = year;
		this.mounth = mounth + 1;
		this.day = day;
		
		String date = this.year + "-" + spanString(this.mounth) + "-" + spanString(this.day);
		v.setTag(date);
		v.setText(date);
	}

}
