package com.mygame.android.utils.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;

import com.alibaba.fastjson.JSONArray;
import com.mygame.android.json.JsonFormatException;
import com.mygame.android.json.JsonModuleBean;
import com.mygame.android.json.util.JsonFormatFactory;
import com.mygame.android.utils.net.FileUploaderHandler.FileUploaderResult;

public class FileUploader {

	private FileUploaderHandler uploaderHandler;

	public static final int FILE_UPLOADER_RESULT_SUCCESS = 100;
	public static final int FILE_UPLOADER_RESULT_ERROR = 101;
	public static final int FILE_UPLOADER_START = 102;
	
	public FileUploader(FileUploaderHandler uploaderHandler) {
		super();
		this.uploaderHandler = uploaderHandler;
	}

	/**
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.xxx.cn或http://192.168.1.10:8080这样的路径测试)
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 */
	public void post(String path, Map<String, String> params,
			FormFile uploadFile,Object obj) {
		final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
		final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志

		int fileDataLength = 0;
		// 得到文件类型数据的总长度
		StringBuilder fileExplain = new StringBuilder();
		fileExplain.append("--");
		fileExplain.append(BOUNDARY);
		fileExplain.append("\r\n");
		fileExplain.append("Content-Disposition: form-data;name=\""
				+ uploadFile.getParameterName() + "\";filename=\""
				+ uploadFile.getFilname() + "\"\r\n");
		fileExplain.append("Content-Type: " + uploadFile.getContentType()
				+ "\r\n\r\n");
		fileExplain.append("\r\n");
		fileDataLength += fileExplain.length();
		if (uploadFile.getInStream() != null) {
			fileDataLength += uploadFile.getFile().length();
		} else {
			fileDataLength += uploadFile.getData().length;
		}
		StringBuilder textEntity = new StringBuilder("");
		if(params != null){
			for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
				textEntity.append("--");
				textEntity.append(BOUNDARY);
				textEntity.append("\r\n");
				textEntity.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"\r\n\r\n");
				textEntity.append(entry.getValue());
				textEntity.append("\r\n");
			}
		}
		
		// 计算传输给服务器的实体数据总长度
		int dataLength = textEntity.toString().getBytes().length
				+ fileDataLength + endline.getBytes().length;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(30 * 1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			String request_context = "";
			OutputStream outStream = conn.getOutputStream();
			String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg\r\n";
			//outStream.write(accept.getBytes());
			request_context += accept;
			String language = "Accept-Language: utf-8\r\n";
			request_context += language;
			//outStream.write(language.getBytes());
			String contenttype = "Content-Type: multipart/form-data; boundary="
					+ BOUNDARY + "\r\n";
			request_context += contenttype;
			//outStream.write(contenttype.getBytes());
			String contentlength = "Content-Length: " + dataLength + "\r\n";
			request_context += contentlength;
			//outStream.write(contentlength.getBytes());
			String alive = "Connection: Keep-Alive\r\n";
			request_context += alive;
			//outStream.write(alive.getBytes());
			// 写完HTTP请求头后根据HTTP协议再写一个回车换行
			//outStream.write("\r\n".getBytes());
			// 把所有文本类型的实体数据发送出来
			//outStream.write(textEntity.toString().getBytes());
			// 把所有文件类型的实体数据发送出来
			request_context += "\r\n";
			request_context += textEntity.toString();
			StringBuilder fileEntity = new StringBuilder();
			fileEntity.append("--");
			fileEntity.append(BOUNDARY);
			fileEntity.append("\r\n");
			fileEntity.append("Content-Disposition: form-data;name=\""
					+ uploadFile.getParameterName() + "\";filename=\""
					+ uploadFile.getFilname() + "\"\r\n");
			fileEntity.append("Content-Type: " + uploadFile.getContentType()
					+ "\r\n\r\n");
			request_context += fileEntity.toString();
			outStream.write(request_context.getBytes());
			if (uploadFile.getInStream() != null) {
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
					outStream.write(buffer, 0, len);
				}
				uploadFile.getInStream().close();
			} else {
				outStream.write(uploadFile.getData(), 0,
						uploadFile.getData().length);
			}
			outStream.write("\r\n".getBytes());
			// 下面发送数据结束标志，表示数据已经结束
			outStream.write(endline.getBytes());
			if (conn.getResponseCode() >= 400) {
				// 读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
				//sendMessage(FILE_UPLOADER_RESULT_ERROR, "地址请求出错", null,
				//		uploadFile);
				return;
			}else{
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String result = "";
				String line = "";
				while((line = reader.readLine() ) != null){
					result += line;
				}
				JSONObject jsonObj = new JSONObject(result);
				JsonModuleBean<FileUploaderResponse> return_result =  (JsonModuleBean<FileUploaderResponse>) JsonFormatFactory.getJsonModuleBean(jsonObj, null,FileUploaderResponse.class);
				//sendMessage(FILE_UPLOADER_RESULT_SUCCESS, "文件上传成功", null, uploadFile);
				
			}
			outStream.flush();
			outStream.close();
			conn.disconnect();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			//sendMessage(FILE_UPLOADER_RESULT_ERROR, "无法解析到远程主机", e, uploadFile);
		} catch (IOException e) {
			e.printStackTrace();
			//sendMessage(FILE_UPLOADER_RESULT_ERROR, "数据交互出错", e, uploadFile);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//sendMessage(FILE_UPLOADER_RESULT_ERROR, "解析返回数据出错", e, uploadFile);
		} catch (JsonFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//sendMessage(FILE_UPLOADER_RESULT_ERROR, "解析返回数据出错", e, uploadFile);
		}
		return;
	}

	private void sendMessage(int code, String message, Throwable exception,
			List<FormFile> file) {
		Message msg = Message.obtain();
		msg.obj = new FileUploaderResult(code, message,
				exception, file);;
		msg.setTarget(uploaderHandler);
		msg.sendToTarget();
	}
	
	private void sendMessage(int code, String message, Throwable exception,
			List<FormFile> file,Object resultObject) {
		Message msg = Message.obtain();
		FileUploaderResult result = new FileUploaderResult(code, message,
				exception, file);
		result.resultObject = resultObject;
		msg.obj =result;
		
		msg.setTarget(uploaderHandler);
		msg.sendToTarget();
	}
	
	public void uploaderFile(String url, Map<String, String> params,
			List<FormFile> uploadFiles) {
		new UploaderThread(url, params, uploadFiles).start();
	}
	
	public boolean post(String path, Map<String, String> params, List<FormFile> files){    
		sendMessage(FILE_UPLOADER_START, "数据处理开始", null, files);
        final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志
        
        int fileDataLength = 0;
        for(FormFile uploadFile : files){//得到文件类型数据的总长度
            StringBuilder fileExplain = new StringBuilder();
             fileExplain.append("--");
             fileExplain.append(BOUNDARY);
             fileExplain.append("\r\n");
             fileExplain.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
             fileExplain.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");
             fileExplain.append("\r\n");
             fileDataLength += fileExplain.length();
            if(uploadFile.getInStream()!=null){
                fileDataLength += uploadFile.getFile().length();
             }else{
                 fileDataLength += uploadFile.getData().length;
             }
        }
        StringBuilder textEntity = new StringBuilder();

        if(params != null){
	        for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
	            textEntity.append("--");
	            textEntity.append(BOUNDARY);
	            textEntity.append("\r\n");
	            textEntity.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");
	            textEntity.append(entry.getValue());
	            textEntity.append("\r\n");
	        }
        }
        //计算传输给服务器的实体数据总长度
        int dataLength = textEntity.toString().getBytes().length + fileDataLength +  endline.getBytes().length;
        
        try {
			URL url = new URL(path);
			int port = url.getPort()==-1 ? 80 : url.getPort();
			Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);           
			OutputStream outStream = socket.getOutputStream();
			//下面完成HTTP请求头的发送
			String requestmethod = "POST "+ url.getPath()+" HTTP/1.1\r\n";
			outStream.write(requestmethod.getBytes());
			String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
			outStream.write(accept.getBytes());
			String language = "Accept-Language: zh-CN\r\n";
			outStream.write(language.getBytes());
			String contenttype = "Content-Type: multipart/form-data; boundary="+ BOUNDARY+ "\r\n";
			outStream.write(contenttype.getBytes());
			String contentlength = "Content-Length: "+ dataLength + "\r\n";
			outStream.write(contentlength.getBytes());
			String alive = "Connection: Keep-Alive\r\n";
			outStream.write(alive.getBytes());
			String host = "Host: "+ url.getHost() +":"+ port +"\r\n";
			outStream.write(host.getBytes());
			//写完HTTP请求头后根据HTTP协议再写一个回车换行
			outStream.write("\r\n".getBytes());
			//把所有文本类型的实体数据发送出来
			outStream.write(textEntity.toString().getBytes());         
			System.out.println(textEntity);
			//把所有文件类型的实体数据发送出来
			for(FormFile uploadFile : files){
			    StringBuilder fileEntity = new StringBuilder();
			     fileEntity.append("--");
			     fileEntity.append(BOUNDARY);
			     fileEntity.append("\r\n");
			     fileEntity.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
			     fileEntity.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");
			     outStream.write(fileEntity.toString().getBytes());
			     System.out.println(fileEntity);
			     if(uploadFile.getInStream()!=null){
			         byte[] buffer = new byte[1024];
			         int len = 0;
			         while((len = uploadFile.getInStream().read(buffer, 0, 1024))!=-1){
			             outStream.write(buffer, 0, len);
			         }
			         uploadFile.getInStream().close();
			     }else{
			         outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
			     }
			     outStream.write("\r\n".getBytes());
			}
			//下面发送数据结束标志，表示数据已经结束
			outStream.write(endline.getBytes());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String jsonString = null;
			while((jsonString = reader.readLine()) != null){
				if(jsonString.trim().equals("")){
					jsonString = reader.readLine();
					jsonString = reader.readLine();
					break;
				}
			}
			sendMessage(FILE_UPLOADER_RESULT_SUCCESS, "数据传送完成",null,files,jsonString);
			outStream.flush();
			outStream.close();
			reader.close();
			socket.close();
        } catch (UnknownHostException e) {
			e.printStackTrace();
			sendMessage(FILE_UPLOADER_RESULT_ERROR, "无法解析到远程主机", e, files);
		} catch (IOException e) {
			e.printStackTrace();
			sendMessage(FILE_UPLOADER_RESULT_ERROR, "数据交互出错", e, files);
		} 
        return true;
    }
	
	public static String post(String actionUrl, Map<String, String> params,

            Map<String, File> files) throws IOException {

        String BOUNDARY = java.util.UUID.randomUUID().toString();

        String PREFIX = "--", LINEND = "\r\n";

        String MULTIPART_FROM_DATA = "multipart/form-data";

        String CHARSET = "UTF-8";

        URL uri = new URL(actionUrl);

        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();        

        

        conn.setReadTimeout(5 * 1000); 

        conn.setDoInput(true);// 允许输入

        conn.setDoOutput(true);// 允许输出

        conn.setUseCaches(false); 

        conn.setRequestMethod("POST");  //Post方式

        conn.setRequestProperty("connection", "keep-alive");

        conn.setRequestProperty("Charsert", "UTF-8");

        

        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA

                + ";boundary=" + BOUNDARY);

        

        // 首先组拼文本类型的参数

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {

            sb.append(PREFIX);

            sb.append(BOUNDARY);

            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\""

                    + entry.getKey() + "\"" + LINEND);

            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);

            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);

            sb.append(LINEND);

            sb.append(entry.getValue());

            sb.append(LINEND);

        }    

        

        DataOutputStream outStream = new DataOutputStream(conn

                .getOutputStream());

        outStream.write(sb.toString().getBytes());

        // 发送文件数据

        if (files != null)

            for (Map.Entry<String, File> file : files.entrySet()) {

                StringBuilder sb1 = new StringBuilder();

                sb1.append(PREFIX);

                sb1.append(BOUNDARY);

                sb1.append(LINEND);

                sb1

                        .append("Content-Disposition: form-data; name=\"file\"; filename=\""

                                + file.getKey() + "\"" + LINEND);

                sb1.append("Content-Type: application/octet-stream; charset="

                        + CHARSET + LINEND);

                sb1.append(LINEND);

                outStream.write(sb1.toString().getBytes());

                InputStream is = new FileInputStream(file.getValue());

                byte[] buffer = new byte[1024];

                int len = 0;

                while ((len = is.read(buffer)) != -1) {

                    outStream.write(buffer, 0, len);

                }

                is.close();

                outStream.write(LINEND.getBytes());

            }

        

        // 请求结束标志

        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();

        outStream.write(end_data);

        outStream.flush();

        // 得到响应码

        int res = conn.getResponseCode(); 

        InputStream in = conn.getInputStream();

        InputStreamReader isReader = new InputStreamReader(in);

        

        BufferedReader bufReader = new BufferedReader(isReader);

        

        String line = null;

        String data = "OK";

        while((line = bufReader.readLine())==null)

            data += line;

        

        if (res == 200) {

            int ch;

            StringBuilder sb2 = new StringBuilder();

            while ((ch = in.read()) != -1) {

                sb2.append((char) ch);

            }

        }

        outStream.close();

        conn.disconnect();

        return  in.toString();

    }
    
    /**
     * 提交数据到服务器
     * @param path 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param file 上传文件
     */
    public static boolean post(String path, Map<String, String> params, FormFile file) throws Exception{
       //return post(path, params, new FormFile[]{file});
    	return true;
    }
    public void uploadFile(final String url,final String filePath,final String onSpotFid,final String fid,final String personId,final int imageNum){
    	Thread t = new Thread(new Runnable() {
			public void run() {
				post(url,filePath,onSpotFid,fid,personId,imageNum);
			}
		});
    	t.start();
    }
    public boolean post(String url,String filePath,String onSpotFid,String fid,String personId,int imageNum){
		try {
			FileInputStream in = new FileInputStream(filePath);
			
			UploadBean uploadBean = new UploadBean();
			uploadBean.setFileSize(in.available());
			int count = 0;
			int ret = -1;
			List<UploadPart> parts = new ArrayList<UploadPart>();
			while(true){
				byte[] content = new byte[uploadBean.getFileSize()];
				if((ret = in.read(content)) > 0){
					UploadPart part = new UploadPart();
					part.setParameter("part" + count);
					count ++;
					part.setData(content);
					parts.add(part);
				}else{
					break;
				}
			}
			in.close();
			uploadBean.setFileName(onSpotFid+new File(filePath).getName());
			uploadBean.setFilePartSize(uploadBean.getFileSize());
			uploadBean.setFilePartCount(parts.size());
			uploadBean.setContentType("image/jpeg");
			uploadBean.setImageNum(imageNum + "");
			System.out.println(imageNum);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("fid",fid));
			params.add(new BasicNameValuePair("personId",personId));
			params.add(new BasicNameValuePair("uploadFileString", com.alibaba.fastjson.JSONObject.toJSONString(uploadBean)));
			for(UploadPart part : parts){
				params.add(new BasicNameValuePair(part.getParameter(), JSONArray.toJSONString(part.getData())));
			}
			String returnString = NetPushUtil.getInstance().GetPost(url, params);
			FormFile file = new FormFile();
			file.setFile(new File(filePath));
			if(null == returnString || "".equals(returnString.trim())){
				//sendMessage(FILE_UPLOADER_RESULT_ERROR, "文件上传出错", null, file);
			}else{
				JsonModuleBean<FileUploadResponseBean> result = (JsonModuleBean<FileUploadResponseBean>) JsonFormatFactory.getJsonModuleBean(new JSONObject(returnString), null,FileUploadResponseBean.class);
				FormFile formFile = new FormFile();
				formFile.setFile(new File(filePath));
				//sendMessage(FILE_UPLOADER_RESULT_SUCCESS, "文件上传成功", null, file);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FormFile file = new FormFile();
			file.setFile(new File(filePath));
			//sendMessage(FILE_UPLOADER_RESULT_ERROR, "未找到指定文件", e, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FormFile file = new FormFile();
			file.setFile(new File(filePath));
			//sendMessage(FILE_UPLOADER_RESULT_ERROR, "文件读取失败", e, file);
		} catch (JsonFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FormFile file = new FormFile();
			file.setFile(new File(filePath));
			//sendMessage(FILE_UPLOADER_RESULT_ERROR, "返回格式不正确", e, file);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FormFile file = new FormFile();
			file.setFile(new File(filePath));
			//sendMessage(FILE_UPLOADER_RESULT_ERROR, "返回格式不正确", e,file);
		}
		
    	return true;
    }

	private class UploaderThread extends Thread {
		private String url;
		private Map<String, String> params;
		private List<FormFile> uploadFile;

		public UploaderThread(String url, Map<String, String> params,
				List<FormFile> uploadFile) {
			super();
			this.url = url;
			this.params = params;
			this.uploadFile = uploadFile;
		}

		public void run() {
			post(url,params,uploadFile);
		}
	}
}
