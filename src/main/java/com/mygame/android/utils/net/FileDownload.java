package com.mygame.android.utils.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Environment;

@SuppressWarnings("deprecation")
public class FileDownload {
	public interface IFileDownloadCallback {

		public static final int STATUS_FAIL = -1;
		public static final int STATUS_START = 0;
		public static final int STATUS_CONNECTED = 1;
		public static final int STATUS_PAUSE = 2;
		public static final int STATUS_PROGRESS = 3;
		public static final int STATUS_COMPLETE = 4;
        
		
		public static final int ERR_NET_CUT = 0;
		public static final int ERR_USER_CANCEL = 1;

		void onDownloadStart(int dID);

		void onConnected(int dID, long total, boolean isRangeSupport);
		
		void onPaused(int dID);

		void onPercentage(int dID, float total, float curr);

		void onComplete(int dID, String url, String localFile);

		void onFaild(int dID, float percent, int errCode);
	}

	static FileDownload _inst = null;

	public static FileDownload instance() {
		if (_inst == null) {
			_inst = new FileDownload();
		}
		return _inst;
	}

	Map<String, DownloadThread> downloadThreads;
	int taskID;
	String downloadDir;

	boolean mBreakThread = false; // 是否停止线程

	public void setBreakThread(boolean breakThread) {
		mBreakThread = breakThread;
	}

	public String getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(String dir) {
		downloadDir = dir;
		File file = new File(downloadDir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public void setDownloadDir(File fileDir) {
		if (fileDir == null)
			return;
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		downloadDir = fileDir.getPath();
	}

	private FileDownload() {
		downloadDir = Environment.getExternalStorageDirectory() + "/contDownloadDir/";
		File file = new File(downloadDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		downloadThreads = new HashMap<String, DownloadThread>();
		taskID = 1;
	}

	/**
	 * 开启下载线程
	 * 
	 * @param url
	 *            网络地址
	 * @param callback
	 *            下载回调
	 * @param fileName
	 *            下载后命名文件
	 * @return
	 */
	public synchronized int Download(String url, IFileDownloadCallback callback, String fileName) {
		String strUrl = url;
		int retID = taskID;
		synchronized (downloadThreads) {
			DownloadThread t = downloadThreads.get(strUrl);
			if (t != null) {
				t.startDownload();
				return t.dID;
			}

			t = new DownloadThread();
			t.setParemet(strUrl, fileName, callback, retID);
			taskID++;
			downloadThreads.put(strUrl, t);
			t.startDownload();
		}
		return retID;
	}

	public boolean isFileExist(String fileName) {
		String path = downloadDir + fileName;
		File f = new File(path);
		return f.exists();
	}

	public void DeleteFile(String fileName) {
		String file = downloadDir + fileName;
		synchronized (downloadThreads) {
			Iterator it = downloadThreads.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				DownloadThread value = (DownloadThread) entry.getValue();
				if (value.fileName.endsWith(file))
					return;
			}
			try {
				File f = new File(file);
				if (f.exists())
					f.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void stopDownload(String url) {           //停止下载
		synchronized (downloadThreads) {
			String strUrl = url;
			DownloadThread t = downloadThreads.get(strUrl);
			if (t == null)
				return;
			t.stopDownload();
		}
	}

	/**
	 * 暂停下载，采用pause 2015-11-28
	 */
	public void pauseDownload(String url) {
		synchronized (downloadThreads) {
			String strUrl = url;
			DownloadThread t = downloadThreads.get(strUrl);
			if (t == null)
				return;
			t.pauseDownload();
		} 
	} 
	
	public void clearTask(String url) {
		synchronized (downloadThreads) {
			String strUrl = url;
			DownloadThread t = downloadThreads.get(strUrl);
			if (t == null)
				return;
			downloadThreads.remove(strUrl);
		}
	}

	public float getPercentage(String url) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	public class DownloadThread extends Thread {
		public IFileDownloadCallback callback;
		public String urlStr;
		public int dID;
		public int fileSize;
		public String fileName;
		public String fileNameWithoutPath;
		public boolean bRun = false;

		public void startDownload() {
			try{
				if (bRun)
					return;
				bRun = true; 
				 start();
			}catch(IllegalThreadStateException e){
				e.printStackTrace();
			} 
		}

		/**
		 * @deprecated 停止线程，用join方式不好，
		 */
		public void stopDownload() {
			if (!bRun)
				return;
			bRun = false;
			try {
				this.join();
			}catch (Exception e) {
				e.printStackTrace(); 
			}
		}
 
		/**
		 * 暂停下载，采用pause 2015-11-28
		 */
		public void pauseDownload() {
			if (!bRun)
				return;
			bRun = false; 
		}
		
		
		public synchronized Thread getCurrentThread() {
			return Thread.currentThread();
		}

		public void setParemet(String url, String filename, IFileDownloadCallback iCallBack, int id) {
			urlStr = url;
			callback = iCallBack;
			dID = id;
			fileNameWithoutPath = filename;
			fileName = downloadDir + filename;
		}

		static final int BUFFER_SIZE = 10240;

		boolean getFileSize() {
			try {
				URL url = new URL(urlStr);
				URLConnection conn = url.openConnection();
				conn.setAllowUserInteraction(true);
				fileSize = conn.getContentLength();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		/**
		 * 临时文件名
		 */
		private File iconTmpFile = null;
		/**
		 * 生成具体文件名
		 */
		private File iconFile = null;

		/**
		 * 恢复下载
		 * 
		 * @return
		 */
		private int resumeHttp() {
			int errCode = IFileDownloadCallback.ERR_NET_CUT;
			HttpClient httpClient = null;
			long curPosition = 0;
			int result = -1;
			try {
				getFileSize();
				iconFile = new File(downloadDir, fileNameWithoutPath);
				iconTmpFile = new File(downloadDir, fileNameWithoutPath + "_tmp");              //临时名是，appID.apk_tmp
				iconTmpFile.createNewFile();

				HttpGet httpGet = new HttpGet(urlStr);
				httpGet.addHeader("Range", "bytes=" + iconTmpFile.length() + "-"+fileSize);

				curPosition = iconTmpFile.length();

				httpClient = new DefaultHttpClient(); // 创建http客户端
				HttpEntity httpEntity = null;
				FileOutputStream fos = null;
				InputStream is = null;
				HttpResponse httpResponse = httpClient.execute(httpGet); // 执行GET活动http响应
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_PARTIAL_CONTENT) { // 连接成功

					boolean isRangeSupport = false;
					Header[] hs = httpResponse.getAllHeaders();
					if (hs != null) {
						for (int i = 0; i < hs.length; i++) {
							String headerName = hs[i].getName();
							if ("Accept-Ranges".equalsIgnoreCase(headerName)) {
								isRangeSupport = hs[i].getValue().equals("bytes");
							}
						}
					}
					httpEntity = httpResponse.getEntity();
					long total = httpEntity.getContentLength();
					if (callback!= null) {
						callback.onConnected(dID, total, isRangeSupport);  //2015-12-9打开
					}
					fileSize = (int) total;
					if (httpEntity != null) {
						fos = new FileOutputStream(iconTmpFile, true);
						is = httpEntity.getContent();

						byte[] buff = new byte[64];
						int recved = 0;
						while ((recved = is.read(buff, 0, buff.length)) != -1 && bRun) {
							fos.write(buff, 0, recved);
							curPosition += recved;
							float percent = (float) curPosition / (float) fileSize;
							callback.onPercentage(dID, fileSize, curPosition);
							//LogUtils.e("Download percent=", String.valueOf(percent * 100) + "%");
						}
						fos.flush();
						fos.close();
						is.close();
					}
					if (httpEntity != null) {
						httpEntity.consumeContent();
					}
				}
			} catch (IOException e) {
				errCode = IFileDownloadCallback.ERR_NET_CUT;
				e.printStackTrace();
				result = 1;
			} catch (Exception e) {
				errCode = IFileDownloadCallback.ERR_NET_CUT;
				e.printStackTrace();
				result = 2;
			} finally {
				if (httpClient != null) {
					httpClient.getConnectionManager().shutdown();
				}
			}

			if (!bRun) {                //---不在运行状态
				if (callback!= null) {
					callback.onPaused(dID);
				}
			}else{
				if (curPosition == fileSize) {
					iconTmpFile.renameTo(iconFile);
					callback.onComplete(dID, urlStr, fileName);
					result = 0;
				} else {
					float percent = (float) curPosition / (float) fileSize;
					callback.onPercentage(dID, fileSize, curPosition);
					//callback.onFaild(dID, percent, errCode);                 //2015-12-9
					result = 3;
				}
			} 
			clearTask(urlStr);
			return result;
		}

		@Override
		public void run() {
			if (callback != null) {                                            //---开始下载前
				callback.onDownloadStart(dID);
			}

			int TimeTicker = 5;
			int result = 0;
			while ((result = resumeHttp()) != 0 && TimeTicker-- > 0 && bRun) { //---下载过程中，下载完毕
				try {
					Thread.sleep(2 * 1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//LogUtils.e("TAG", "--下载失败状态result=：--" + result);
				//LogUtils.e("TAG", "--剩余下载次数：--" + TimeTicker);
			}
			                                                                   //---下载出错
			if(TimeTicker<=0 && bRun){
				if(callback!=null){
					callback.onFaild(dID, 0, IFileDownloadCallback.ERR_NET_CUT);
				} 
			}
		}

		public float getPercentage() {
			return 0;
		}
	}

}