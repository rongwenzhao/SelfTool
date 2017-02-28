package com.mygame.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mygame.android.library.R;

public class TopBarView extends LinearLayout {
	private Context context;
	private RelativeLayout layout_view;
	private Button left_button;
	private Button right_button;
	private RelativeLayout top_right_button_view;
	private RelativeLayout top_left_button_view;
	private TextView title;

	public TopBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
        // 导入布局
        LayoutInflater.from(context).inflate(R.layout.top_title, this, true);
        initView();
	}

	private void initView(){
		layout_view = (RelativeLayout)findViewById(R.id.top_title_view);
		left_button = (Button)findViewById(R.id.top_left_button);
		right_button = (Button)findViewById(R.id.top_right_button);
		top_right_button_view = (RelativeLayout)findViewById(R.id.top_right_button_view);
		top_left_button_view = (RelativeLayout)findViewById(R.id.top_left_button_view);
		title = (TextView)findViewById(R.id.top_title);
	}
	

	public Button getTopLeftButton(){
		return left_button;
	}
	
	public Button getTopRightButton(){
		return right_button;
	}
	
	public TextView getTopTitle(){
		return title;
	}
	
	public RelativeLayout getTopBarView(){
		return layout_view;
	}
	
	public RelativeLayout getTopLeftButtonView(){
		return top_left_button_view;
	}
	
	public RelativeLayout getTopRightButtonView(){
		return top_right_button_view;
	}
	
}
