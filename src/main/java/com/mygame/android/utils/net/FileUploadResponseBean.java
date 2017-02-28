package com.mygame.android.utils.net;

import com.mygame.android.json.IJson;
import com.mygame.android.json.templete.annotation.JsonField;

public class FileUploadResponseBean implements IJson {
	@JsonField(name="filePath",type=String.class)
	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
