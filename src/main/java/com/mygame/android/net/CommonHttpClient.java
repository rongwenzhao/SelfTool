package com.mygame.android.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.net.ParseException;
import android.util.Log;

import com.mygame.android.application.base.MYGameApplication;
import com.mygame.android.encode.util.MYGameDesUtil;
import com.mygame.android.json.util.JsonFormatFactory;
import com.mygame.android.net.NetConst;
import com.mygame.android.net.NetPostAsynTask;
import com.mygame.android.net.NetResponse;

public class CommonHttpClient extends NetPostAsynTask {

	private static final String responseModel = "responseModel";

	public static void doNetPost(CommonHttpRequest request,
			Class<? extends NetResponse> responseClass) {
		String url = request.getRequestUrl();
		if (null == request || null == responseClass || null == url
				|| "".equals(url.trim())) {
			return;
		}
		if(null != request.getHostActivity()){
			((MYGameApplication)request.getHostActivity().getApplication()).setLongParameter(MYGameApplication.LAST_REQUEST_TIME, System.currentTimeMillis());
		}
		NetPostTask task = new NetPostTask();
		task.url = url;
		request.execute();
		task.request = request;
		task.responseClass = responseClass;
		sendMessage(NetConst.HANDLE_MESSAGE_FLAG_START_TASK, task);
		NetPostThreadDes thread = new NetPostThreadDes(task);
		thread.start();
	}
	
	public static void doNetPost(CommonHttpRequest request,String responseKeyName,
			Class<? extends NetResponse> responseClass) {
		String url = request.getRequestUrl();
		if (null == request || null == responseClass || null == url
				|| "".equals(url.trim())) {
			return;
		}
		if(null != request.getHostActivity()){
			((MYGameApplication)request.getHostActivity().getApplication()).setLongParameter(MYGameApplication.LAST_REQUEST_TIME, System.currentTimeMillis());
		}
		NetPostTask task = new NetPostTask();
		task.url = url;
		task.responseKeyName = responseKeyName;
		request.execute();
		task.request = request;
		task.responseClass = responseClass;
		sendMessage(NetConst.HANDLE_MESSAGE_FLAG_START_TASK, task);
		NetPostThreadDes thread = new NetPostThreadDes(task);
		thread.start();
	}

	private static class NetPostThreadDes extends Thread {
		protected NetPostTask task;

		public NetPostThreadDes(NetPostTask task) {
			super();
			this.task = task;
		}

		public void run() {
			HttpPost httpPost = new HttpPost(task.url);
			Log.i("Url", task.url);
			String result = null;
			HttpResponse httpResponse = null;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Set<String> keySet = task.request.getRequestParams().keySet();
			if (null != keySet && 0 != keySet.size()) {
				for (String key : keySet) {
					if (!CommonHttpRequest.IGNORE_DES.equals(key)) {
						params.add(new BasicNameValuePair(key, task.request
								.getParameter(key)));
					}
				}
			}
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				Log.i("URl", httpPost.getURI() + "");
				httpResponse = new DefaultHttpClient().execute(httpPost);
				int responseStatusCode = httpResponse.getStatusLine()
						.getStatusCode();
				if (responseStatusCode == 200) {
					result = EntityUtils.toString(httpResponse.getEntity());
					System.out.println("result:" + result);
					Log.i("URl", httpPost.getURI() + "" + result);
					try {
						String responseDesString = new JSONObject(result)
								.getString(responseModel);
						if ("false".equals(task.request
								.getParameter(CommonHttpRequest.IGNORE_DES))) {
							String jsonString = MYGameDesUtil
									.decryptDes(responseDesString);
							Log.i("CommonHttpClient", jsonString);

							task.response = JsonFormatFactory
									.getJsonModuleBeanParse(
											new org.json.JSONObject(jsonString),task.responseKeyName,
											task.responseClass);
						} else {
							task.response = JsonFormatFactory
									.getJsonModuleBeanParse(
											new org.json.JSONObject(
													responseDesString),task.responseKeyName,
											task.responseClass);
						}
					} catch (Throwable e) {
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
