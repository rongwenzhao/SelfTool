package com.mygame.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.mygame.android.library.R;
import com.mygame.android.view.widget.CircleProcessView;

public class GodenProcessDialog extends Dialog {
	View contentView;
	Context context;
	public GodenProcessDialog(Context context) {
		super(context,R.style.Dialog);
		this.context = context;
		initView();
	}

	public GodenProcessDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context,R.style.Dialog);
		this.context = context;
		initView();
	}

	public GodenProcessDialog(Context context, int theme) {
		super(context,R.style.Dialog);
		this.context = context;
		initView();
	}
	
	private void initView(){
		//contentView = LayoutInflater.from(getContext()).inflate(R.layout.goden_process_dialog, null);
/*		contentView.setBackgroundColor(getContext().getResources().getColor(R.color.ban_transparent));
*/		int width = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
		//contentView.setLayoutParams(new LayoutParams(width / 2, LayoutParams.WRAP_CONTENT));
		//setContentView(contentView);
		setContentView(R.layout.goden_process_dialog);
	}
	
	@Override
	public void show() {
		super.show();
		CircleProcessView circleView = (CircleProcessView)findViewById(R.id.circleView);
		circleView.setMaxProgress(100);
		circleView.show();
	}
	
	@Override
	public void dismiss() {
		CircleProcessView circleView = (CircleProcessView)findViewById(R.id.circleView);
		circleView.stop();
		super.dismiss();
	}
}
