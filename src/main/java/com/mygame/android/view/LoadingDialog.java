package com.mygame.android.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.mygame.android.library.R;

public class LoadingDialog extends Dialog  {
	private Context context;
	private TextView loading_text;
	private CharSequence loadingText;
	public LoadingDialog(Context context){
		this(context,R.style.Dialog);
		this.context = context;
	}
	public LoadingDialog(Context context,int theme) {
		super(context,theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_dialog);
		loading_text = (TextView) findViewById(R.id.loading_text);
		if(null != loadingText){
			loading_text.setText(loadingText);
		}
	}
	
	public LoadingDialog setLoadingText(CharSequence loadingText){
		this.loadingText = loadingText;
		return this;
	}
}
