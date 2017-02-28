package com.mygame.android.view.widget;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {

	private int maxProgress = 100;
	private int progress = 30;
	private int progressStrokeWidth = 4;
	RectF oval;
	Paint paint;

	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		oval = new RectF();
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int width = this.getWidth() / 2;
		int height = this.getHeight() / 2;
		int len = this.getWidth() / 4;

		paint.setAntiAlias(true); 
		paint.setColor(Color.WHITE);
		canvas.drawColor(Color.TRANSPARENT); 
		paint.setStrokeWidth(progressStrokeWidth);
		paint.setStyle(Style.STROKE);

		oval.left = width - len;
		oval.top = height - len;
		oval.right = width + len;
		oval.bottom = height + len;

		paint.setColor(0xFFFF7777);
		canvas.drawArc(oval, -90, 360, false, paint);
		paint.setColor(0xFF8BE4E0);
		canvas.drawArc(oval, -90, ((float) progress / maxProgress) * 360, false, paint);

		paint.setStrokeWidth(1);
		String text = progress + "%";
		int textHeight = height / 5;
		paint.setTextSize(textHeight);
		int textWidth = (int) paint.measureText(text, 0, text.length());
		paint.setStyle(Style.FILL);
		canvas.drawText(text, this.getWidth() / 2 - textWidth / 2, this.getHeight() / 2 + textHeight / 2, paint);
	}

	public int getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		this.invalidate();
	}

	public void setProgressNotInUiThread(int progress) {
		this.progress = progress;
		this.postInvalidate();
	}

}