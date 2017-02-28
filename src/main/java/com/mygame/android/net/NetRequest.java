package com.mygame.android.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.mygame.android.model.Model;
import com.mygame.android.net.handle.DefaultNetResponseDataParse;
import com.mygame.android.net.handle.INetResponseDataParse;
public class NetRequest {
	
	protected Map<String,String> requestParams = new HashMap<String, String>();
	protected Activity hostActivity;
	protected CallBackAsync callBackAsync;
	protected String responseCharacterSet = null;
	protected Class<? extends INetResponseDataParse> dataParseHandle = DefaultNetResponseDataParse.class;
	public interface CallBackAsync<T>{
		void callBackAsync(Model<T> response);
	}

	public NetRequest(Activity hostActivity,CallBackAsync callBackAsync) {
		super();
		this.hostActivity = hostActivity;
		this.callBackAsync = callBackAsync;
	}

	public void setRequestParams(Map<String, String> requestParams) {
		this.requestParams = requestParams;
	}
	public Map<String,String> getRequestParams(){
		return requestParams;
	}
	
	public String getParameter(String attrname){
		return requestParams.get(attrname);
	}
	
	public void addParameter(String attrname,String attrvalue){
		requestParams.put(attrname, attrvalue);
	}
	public Activity getHostActivity() {
		return hostActivity;
	}
	public void setHostActivity(Activity hostActivity) {
		this.hostActivity = hostActivity;
	}

	public void clearParams(){
		requestParams.clear();
	}
	
	public String complieGet(){
		
		List<NameValuePair> params = complieParams();
		
		if(params.size() <= 0){
			return null;
		}
		
		String httpparams = "";
		for (NameValuePair valuePair : params) {
			try {
				httpparams += valuePair.getName();
				httpparams += "=";
				httpparams += URLEncoder.encode(valuePair.getValue(),"UTF-8")+"&";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}		
		httpparams = httpparams.substring(0,httpparams.length() - 1);
		return httpparams;
	}
	
	public List<NameValuePair> complieParams(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Set<String> keySet = getRequestParams().keySet();
		if (null != keySet && 0 != keySet.size()) {
			for (String key : keySet) {
				if(null != getParameter(key)){
					params.add(new BasicNameValuePair(key,getParameter(key)));
				}
			}
		}
		return params;
	}
	
	public String getResponseCharacterSet() {
		return responseCharacterSet;
	}

	public void setResponseCharacterSet(String responseCharacterSet) {
		this.responseCharacterSet = responseCharacterSet;
	}




	// 十六进制下数字到字符的映射数组
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };
	
	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param originString
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	protected static String encodeByMD5(String originString) {
		if (originString != null) {
			try {
				// 创建具有指定算法名称的信息摘要
				MessageDigest md = MessageDigest.getInstance("MD5");
				// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
				byte[] results = md.digest(originString.getBytes());
				// 将得到的字节数组变成字符串返回
				String resultString = byteArrayToHexString(results);
				return resultString.toLowerCase();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 转换字节数组为十六进制字符串
	 * 
	 * @param 字节数组
	 * @return 十六进制字符串
	 */
	protected static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}
	
	/** 将一个字节转化成十六进制形式的字符串 */
	protected static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
}
