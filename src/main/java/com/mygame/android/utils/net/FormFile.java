package com.mygame.android.utils.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FormFile {
	/** 上传文件的数据 */
	private byte[] data;
	private InputStream inStream;
	private File file;
	/** 文件名称 */
	private String filname;
	/** 请求参数名称 */
	private String parameterName;
	/** 内容类型 */
	private String contentType = "application/octet-stream";

	public FormFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param filname
	 *            文件名称
	 * @param data
	 *            上传的文件数据
	 * @param parameterName
	 *            参数
	 * @param contentType
	 *            内容类型
	 */
	public FormFile(String filname, byte[] data, String parameterName,
			String contentType) {
		this.data = data;
		this.filname = filname;
		this.parameterName = parameterName;
		if (contentType != null)
			this.contentType = contentType;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * 
	 * @param filname
	 *            文件名
	 * @param file
	 *            上传的文件
	 * @param parameterName
	 *            参数
	 * @param contentType
	 *            内容内容类型
	 */
	public FormFile(String filname, File file, String parameterName,
			String contentType) {
		this.filname = filname;
		this.parameterName = parameterName;
		this.file = file;
		try {
			this.inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (contentType != null)
			this.contentType = contentType;
	}

	public File getFile() {
		return file;
	}

	public InputStream getInStream() {
		return inStream;
	}

	public byte[] getData() {
		return data;
	}

	public String getFilname() {
		return filname;
	}

	public void setFilname(String filname) {
		this.filname = filname;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
