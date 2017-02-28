package com.mygame.android.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.Message;
import android.text.TextUtils;

import com.mygame.android.net.NetPostAsynTask.NetPostTask;
import com.mygame.android.net.handle.INetResponseDataParse;

public class UrlPostAsynTask {
	/**
	 * post 请求方式
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	protected static NetPostHandler handler = new NetPostHandler();

	protected static void sendMessage(int code, NetPostTask task) {
		Message msg = Message.obtain(handler);
		msg.what = code;
		msg.obj = task;
		msg.sendToTarget();
	}
	
	public static void doNetPost(String url, NetRequest request,
			Class<? extends NetResponse> responseClass) {
		if (null == request || null == url
				|| "".equals(url.trim())) {
			return;
		}
		NetPostTask task = new NetPostTask();
		task.url = url;
		task.request = request;
		task.responseClass = responseClass;
		sendMessage(NetConst.HANDLE_MESSAGE_FLAG_START_TASK, task);
		NetPostThread thread = new NetPostThread(task);
		thread.start();
	}
	@Deprecated
	public static void doNetPost(String url, NetRequest request,String responseKeyName,
			Class<? extends NetResponse> responseClass) {
		if (null == request || null == responseClass || null == url
				|| "".equals(url.trim())) {
			return;
		}
		NetPostTask task = new NetPostTask();
		task.url = url;
		task.responseKeyName = responseKeyName;
		task.request = request;
		task.responseClass = responseClass;
		sendMessage(NetConst.HANDLE_MESSAGE_FLAG_START_TASK, task);
		NetPostThread thread = new NetPostThread(task);
		thread.start();
	}


	private static class NetPostThread extends Thread {
		protected NetPostTask task;

		public NetPostThread(NetPostTask task) {
			super();
			this.task = task;
		}

		public void run() {
			String result = null;
			//HttpResponse httpResponse = null;
			String httpparams = task.request.complieGet();
			
			try {
				URL url = new URL(task.url);
				URLConnection rulConnection = url.openConnection();
				HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection; 
				httpUrlConnection.setDoOutput(true);   
				httpUrlConnection.setDoInput(true);   
				httpUrlConnection.setUseCaches(false);
				httpUrlConnection.setConnectTimeout(10000);
				httpUrlConnection.setReadTimeout(10000);
				httpUrlConnection.setRequestMethod("POST");
				
				if(TextUtils.isEmpty(httpparams)){
					httpUrlConnection.connect(); 
				}else{
					OutputStream outStrm = httpUrlConnection.getOutputStream();
					outStrm.write(httpparams.getBytes());	
				}
				
				InputStream inStrm = httpUrlConnection.getInputStream();
				
				int responseStatusCode = httpUrlConnection.getResponseCode();
				if (responseStatusCode == 200) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inStrm));
					String line = null;
					String content = "";
					while((line = reader.readLine()) != null){
						content += line;
					}
					result = content;
					System.out.println("URL:"+task.url);
					System.out.println("result:" + result);
					try {
						INetResponseDataParse dataParse = task.request.dataParseHandle.newInstance();
						task.response = dataParse.responseDataParse(task.request, result, task.responseClass);
					} catch (Exception e) {
						e.printStackTrace();
						sendMessage(NetConst.HANDLE_MESSAGE_FLAG_ERROR, task);
					}
					sendMessage(NetConst.HANDLE_MESSAGE_FLAG_200, task);
				} else if (responseStatusCode == 500) {
					sendMessage(NetConst.HANDLE_MESSAGE_FLAG_500, task);
				} else if (responseStatusCode == 400
						|| responseStatusCode == 404) {
					sendMessage(NetConst.HANDLE_MESSAGE_FLAG_400, task);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				sendMessage(NetConst.HANDLE_MESSAGE_FLAG_ERROR, task);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				sendMessage(NetConst.HANDLE_MESSAGE_FLAG_URL_ERROR, task);
			} catch (ParseException e) {
				e.printStackTrace();
				sendMessage(NetConst.HANDLE_MESSAGE_FLAG_URL_ERROR, task);
			} catch (IOException e) {
				e.printStackTrace();
				sendMessage(NetConst.HANDLE_MESSAGE_FLAG_NETWORK_ERROR, task);
			}
		}
	}
}
