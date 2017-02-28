package com.mygame.android.application.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.mygame.android.ui.api.UIResource;

public class MYGameApplication extends Application {

	private static final String TAG = "CloudloongApplication";
	public static List<UIResource> resources = new ArrayList<UIResource>();
	public static FinalBitmap imageLoader;
	private SharedPreferences sp;
	public static final String LAST_REQUEST_TIME = "lastrequesttime";
	
	public void onCreate() {
		super.onCreate();
//		initUIResource();
		imageLoader = FinalBitmap.create(getApplicationContext());
		sp = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
	}
	
	public static FinalBitmap getFinalBitmap() {
		return imageLoader;
	}

	private void initUIResource() {
		int configId = getResources().getIdentifier("ui_config", "xml",
				getPackageName());
		if (configId == 0) {
			pluginConfigurationMissing();
			return;
		}
		XmlResourceParser xml = getResources().getXml(configId);
		int eventType = -1;
		String service = "", pluginClass = "", paramType = "";
		boolean onload = false;
		boolean insideFeature = false;
		UIResource currentResource = null;
		while (eventType != XmlResourceParser.END_DOCUMENT) {
			try {
				if (eventType == XmlResourceParser.START_TAG) {
					String name = xml.getName();
					if (name.equalsIgnoreCase("UIResource")) {
						currentResource = new UIResource();
						currentResource.setId(Integer.parseInt(xml.getAttributeValue(null, "id")));
					} else if (currentResource != null) {
						if (name.equalsIgnoreCase("layout")) {
							currentResource.setLayout(xml.nextText());
						} else if (name.equalsIgnoreCase("showBack")) {
							currentResource.setShowBack(xml.nextText());
						} else if (name.equalsIgnoreCase("baseBack")) {
							currentResource.setBaseBack(xml.nextText());
						} else if (name.equalsIgnoreCase("clickListener")) {
							currentResource.setClickListener(xml.nextText());
						}
					}
				} else if (eventType == XmlResourceParser.END_TAG) {
					if (xml.getName().equalsIgnoreCase("UIResource")
							&& currentResource != null) {
						resources.add(currentResource);
						currentResource = null;
					}
				}
				eventType = xml.next();

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}
		}
	}
	
	public boolean isFirstRun(){
    	/*long first = getLongParameter("FIRST");
    	if(first == 1){
    		return false;
    	}else{
    		setLongParameter("FIRST", 1);
    		return true;
    	}*/
		String lastVer = getStringParamter("LAST_VER");
		try {
			if(lastVer == null || lastVer.equals("") || !lastVer.equals(getVersionName())){
				setStringParamter("LAST_VER", getVersionName());
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }

	private void pluginConfigurationMissing() {
		Log.e(TAG,
				"=====================================================================================");
		Log.e(TAG,
				"ERROR: ui_config.xml is missing.  Add res/xml/ui_config.xml to your project.");
		Log.e(TAG,
				"=====================================================================================");
	}
	
	public String getStringParamter(String key) {
		return sp.getString(key, "");
	}

	public void setStringParamter(String key, String value) {
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	public String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
       // packInfo.versionCode;
        String version = packInfo.versionName;
        return version;
}

	public long getLongParameter(String key) {
		return sp.getLong(key, -1);
	}

	public void setLongParameter(String key, long value) {
		Editor edit = sp.edit();
		edit.putLong(key, value);
		edit.commit();
	}
	
	private SharedPreferences getSharedPreferences(){
		return sp;
	}
	
}
