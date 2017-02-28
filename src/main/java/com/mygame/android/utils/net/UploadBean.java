package com.mygame.android.utils.net;

/**
 * 创建时间 2013年10月24日上午9:16:06
 *
 * @author WangJin
 */
public class UploadBean {
	
	private String fileName;
	private String contentType;
	private int fileSize;
	private int filePartSize;
	private int filePartCount;
	private String imageNum;
	
	public int getFilePartSize() {
		return filePartSize;
	}
	public void setFilePartSize(int filePartSize) {
		this.filePartSize = filePartSize;
	}
	public int getFilePartCount() {
		return filePartCount;
	}
	public void setFilePartCount(int filePartCount) {
		this.filePartCount = filePartCount;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getImageNum() {
		return imageNum;
	}
	public void setImageNum(String imageNum) {
		this.imageNum = imageNum;
	}
	
}
