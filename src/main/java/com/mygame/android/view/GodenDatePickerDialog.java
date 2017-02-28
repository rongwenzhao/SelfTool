package com.mygame.android.view;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.mygame.android.library.R;
import com.mygame.android.view.widget.NumericWheelAdapter;
import com.mygame.android.view.widget.OnWheelChangedListener;
import com.mygame.android.view.widget.WheelView;
 
/**
 * 
 * Create custom Dialog windows for your application
 * Custom dialogs rely on custom layouts wich allow you to 
 * create and use your own look & feel.
 * 
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 * 
 * @author antoine vianey
 *
 */
public class GodenDatePickerDialog extends Dialog {
 
    public GodenDatePickerDialog(Context context, int theme) {
        super(context, theme);
    }
 
    public GodenDatePickerDialog(Context context) {
        super(context);
    }
    
    public static interface OnDateSetListener{
    	 public abstract void onDateSet(GodenDatePickerDialog dialog,int year,int mounth,int day);
    }
    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {
 
        private Context context;
        private WheelView year,mounth,day;
        private int year_,mounth_,day_;
        OnDateSetListener onDateSetListener;
        
        OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(mounth.getId() == wheel.getId()){
					mounth_ = newValue;
					int days = getMonthLastDay(year_,mounth_);
					day.setAdapter(new NumericWheelAdapter(1, days, "%02d"));
				}else if(year.getId() == wheel.getId()){
					int y = Calendar.getInstance().get(Calendar.YEAR);
					year_ = y + newValue - 50;
					int days = getMonthLastDay(year_,mounth_);
					day.setAdapter(new NumericWheelAdapter(1, days, "%02d"));
				}else if(day.getId() == wheel.getId()){
					day_ = newValue+1;
				}
			}
		};
        public Builder(Context context,OnDateSetListener onDateSetListener) {
            this.context = context;
            this.onDateSetListener = onDateSetListener;
        }

		public GodenDatePickerDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final GodenDatePickerDialog dialog = new GodenDatePickerDialog(context, 
            		R.style.Dialog);
            View layout = inflater.inflate(R.layout.date_picker_dialog_layout, null);
            int width = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
            LayoutParams layoutParams = new LinearLayout.LayoutParams(
            		width / 4 * 3, LayoutParams.WRAP_CONTENT);
            dialog.addContentView(layout, layoutParams);
            year = (WheelView)layout.findViewById(R.id.year);
            mounth = (WheelView)layout.findViewById(R.id.mounth);
            day = (WheelView)layout.findViewById(R.id.day);
            Calendar cal = Calendar.getInstance();
            int y = cal.get(Calendar.YEAR);
            year.setAdapter(new NumericWheelAdapter(y - 50, y+ 50, "%04d"));
            year.setCyclic(true);
            year.setVisibleItems(5);
            mounth.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
            mounth.setCyclic(true);
            mounth.setVisibleItems(5);
            day.setAdapter(new NumericWheelAdapter(1, getCurrentMonthLastDay(), "%02d"));
            day.setCyclic(true);
            day.setVisibleItems(5);
            
            year.setCurrentItem(50);
            mounth.setCurrentItem(cal.get(Calendar.MONTH));
            day.setCurrentItem(cal.get(Calendar.DAY_OF_MONTH)-1);
            year_ = cal.get(Calendar.YEAR);
            mounth_ = cal.get(Calendar.MONTH);
            day_ = cal.get(Calendar.DAY_OF_MONTH);
            dialog.setContentView(layout);
            layout.findViewById(R.id.negativeButton).setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					if(onDateSetListener == null){
						dialog.dismiss();
					}else{
						onDateSetListener.onDateSet(dialog,year_, mounth_, day_);
						dialog.dismiss();
					}
				}
			});
            layout.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
						dialog.dismiss();
				}
			});
            year.addChangingListener(wheelListener);
            mounth.addChangingListener(wheelListener);
            day.addChangingListener(wheelListener);
            
            return dialog;
        }
 
    }

	public static int getCurrentMonthLastDay()
	{
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public static int getMonthLastDay(int year, int month)
	{
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}
}
