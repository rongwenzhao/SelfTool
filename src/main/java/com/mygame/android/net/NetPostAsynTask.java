package com.mygame.android.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Message;

import com.mygame.android.json.JsonModuleBean;
import com.mygame.android.json.util.JsonFormatFactory;
import com.mygame.android.net.handle.INetResponseDataParse;

public class NetPostAsynTask {
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

	private static String GetPost(String url, List<NameValuePair> params) {

		HttpPost httpPost = new HttpPost(url);
		String result = null;
		HttpResponse httpResponse = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("result:" + result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
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

	public static class NetPostTask {
		public String url;
		public String responseKeyName;
		public NetRequest request;
		public Class<? extends NetResponse> responseClass;
		public Object response;
	}

	private static class NetPostThread extends Thread {
		protected NetPostTask task;

		public NetPostThread(NetPostTask task) {
			super();
			this.task = task;
		}

		public void run() {
			HttpPost httpPost = new HttpPost(task.url);
			String URL = task.url;
			String result = null;
			HttpResponse httpResponse = null;
			List<NameValuePair> params = task.request.complieParams();
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpParams httpPostParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpPostParams, 10000);
				HttpConnectionParams.setSoTimeout(httpPostParams, 10000);
				System.out.println(URL);
				httpResponse = new DefaultHttpClient(httpPostParams).execute(httpPost);
				int responseStatusCode = httpResponse.getStatusLine()
						.getStatusCode();
				if (responseStatusCode == 200) {
					result = EntityUtils.toString(httpResponse.getEntity());
					System.out.println("result:" + result);
					
					try {
						INetResponseDataParse dataParse = task.request.dataParseHandle.newInstance();
						task.response = dataParse.responseDataParse(task.request, result, task.responseClass);
					} catch (Exception e) {
						// TODO Auto-generated catch block
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
