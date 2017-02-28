package com.mygame.android.utils.net.pay;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;




public class AlipayUtil {

	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;

	private static final String NOTIFY_URL = "http://202.102.83.54:8081/BangbangServices/notify_url.jsp";
	
	private static final String RETURN_URL = "";
	
	private Activity context;
	
	private Handler handler;
	
	public AlipayUtil(Activity context,Handler handler){
		this.context = context;
		this.handler = handler;
	}
	
	/**
	 * 
	 * @param out_trade_no 商品编号
	 * @param subject 商品标题
	 * @param body 商品简介
	 * @param price 商品价格
	 */
	public  void pay(String out_trade_no,String subject,String body,String price){
		try {
			String info = getNewOrderInfo(out_trade_no,subject,body,price);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i("ExternalPartner", "开始支付");
			// start the pay.

			final String orderInfo = info;
			new Thread() {
				public void run() {
					//AliPay alipay = new AliPay(context,handler);
					
					//设置为沙箱模式，不设置默认为线上环境
					//alipay.setSandBox(false);
					/*String result = alipay.pay(orderInfo);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					handler.sendMessage(msg);*/
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	private  String getNewOrderInfo(String out_trade_no,String subject,String body,String price) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(out_trade_no);
		sb.append("\"&subject=\"");
		sb.append(subject);
		sb.append("\"&body=\"");
		sb.append(body);
		sb.append("\"&total_fee=\"");
		sb.append(price);
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode(NOTIFY_URL));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}
	
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		return key;
	}
	
	private  String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
