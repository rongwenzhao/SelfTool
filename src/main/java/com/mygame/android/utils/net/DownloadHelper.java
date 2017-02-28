package com.mygame.android.utils.net;

import com.mygame.android.utils.net.FileDownload.IFileDownloadCallback;

class DownLoadCallback implements IFileDownloadCallback
{
	public static final int RET_SUCC = 0;
	public static final int RET_FAIL = -1;
	
	@Override
	public void onPercentage(int dID, float total, float curr) {
		 
	}

	@Override
	public void onComplete(int dID, String url, String localFile) {
		 
	}

	@Override
	public void onFaild(int dID, float percent, int errCode) {
		 
	}

	@Override
	public void onConnected(int dID, long total, boolean isRangeSupport) {
		 
		
	}

	@Override
	public void onDownloadStart(int dID) {
		 
		
	}

	@Override
	public void onPaused(int dID) {
		 
		
	}
	
}

public class DownloadHelper
{
	
	static DownloadHelper _inst = null;
	public static DownloadHelper instance()
	{
		if(_inst == null)
			_inst = new DownloadHelper();
		return _inst;
		
	}
	
	//获取http起始ID
	public int taskId = 2048;
	
	//http获取配置文件
	public int getHttpString(String url)
	{
		++taskId;
		 
		return taskId;
	}
	
	//http下载文件
	public int httpDownload(String url, String path, String filename, boolean isPercentage)
	{
		FileDownload.instance().setDownloadDir(path);
		int taskId = FileDownload.instance().Download(url, new DownLoadCallback(), filename);
		return taskId;
	}
}