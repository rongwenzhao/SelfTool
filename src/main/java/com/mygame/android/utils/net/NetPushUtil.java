package com.mygame.android.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * netpush从网络获取json url
 * 
 * @author ASUS S400
 * 
 */
public class NetPushUtil {
	private static NetPushUtil st = new NetPushUtil();

	private NetPushUtil() {
	}

	public static NetPushUtil getInstance() {
		return st;
	}

	/**
	 * 根据url和参数获取json数据
	 * 
	 * @param url
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public String load(String url, String query) throws Exception {
		URL restURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setAllowUserInteraction(false);
		PrintStream ps = new PrintStream(conn.getOutputStream());
		ps.print(query);
		ps.close();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line, resultStr = "";
		while (null != (line = bReader.readLine())) {
			resultStr += line;
		}
		bReader.close();
		return resultStr;
	}

	/**
	 * post 请求方式
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public String GetPost(String url, List<NameValuePair> params) {

		// 第一步，创建HttpPost对象
		HttpPost httpPost = new HttpPost(url);
		// // 设置HTTP POST请求参数必须用NameValuePair对象
		// List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("action", "downloadAndroidApp"));
		// params.add(new BasicNameValuePair("packageId",
		// "89dcb664-50a7-4bf2-aeed-49c08af6a58a"));
		// params.add(new BasicNameValuePair("uuid", "test_ok1"));
		String result = "";
		HttpResponse httpResponse = null;
		try {
			// 设置httpPost请求参数
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// System.out.println(httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 第三步，使用getEntity方法活得返回结果
				result = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("result:" + result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end url...");
		return result;
	}
}
