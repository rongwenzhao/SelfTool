package com.mygame.android.utils.net;

import java.util.List;

import android.os.Handler;
import android.os.Message;

public abstract class FileUploaderHandler extends Handler {

	public static class FileUploaderResult{
		public int resultFlag;
		public String message;
		public Throwable exception;
		public List<FormFile> file;
		public Object resultObject;
		public FileUploaderResult(int resultFlag, String message,
				Throwable exception, List<FormFile> file) {
			super();
			this.resultFlag = resultFlag;
			this.message = message;
			this.exception = exception;
			this.file = file;
		}
		public FileUploaderResult() {
			super();
			// TODO Auto-generated constructor stub
		}
		
	}
	public abstract void handleMessage(Message msg);
	
}
