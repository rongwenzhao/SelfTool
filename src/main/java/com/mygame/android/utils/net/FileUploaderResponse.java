package com.mygame.android.utils.net;

import com.mygame.android.json.IJson;
import com.mygame.android.json.templete.annotation.JsonField;

public class FileUploaderResponse implements IJson {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonField(name = "targetPath",type=String.class)
	private String targetPath;

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}
	
	
}
