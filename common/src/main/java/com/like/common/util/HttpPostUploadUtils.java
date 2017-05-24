package com.like.common.util;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.like.logger.Logger;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 表单形式上传图片工具类
 */
public class HttpPostUploadUtils {

	/**
	 * 表单方式上传文件
	 * 
	 * @param activity
	 * @param uploadUrl
	 * @param filePath
	 */
	public static void formUpload(final String uploadUrl, final String filePath, final OnUploadListener listener) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String media_id = null;

				if (TextUtils.isEmpty(filePath))
					return null;

				File file = new File(filePath);
				if (!file.exists())
					return null;
				Logger.v("上传图片的大小为：" + (file.length() / 1024) + " kb");

				String BOUNDARY = "---------------------------" + System.currentTimeMillis(); // boundary就是request头和上传文件内容的分隔符
				try {
					// 创建一个URL对象
					URL url = new URL(uploadUrl);
					// 利用HttpURLConnection对象从网络中获取网页数据
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(30000);
					conn.setDoOutput(true);
					conn.setDoInput(true);
					conn.setUseCaches(false);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
					conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

					OutputStream out = new DataOutputStream(conn.getOutputStream());
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"userfile\";" + " filename=\"" + file.getName() + "\"\r\n");
					strBuf.append("Content-Type:image/png" + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
					byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
					out.write(endData);
					out.flush();
					out.close();

					// 服务器返回的响应吗
					int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
					// 对响应码进行判断
					if (code == 200) {// 返回的响应码200,是成功
						// 读取返回数据
						StringBuffer sb = new StringBuffer();
						BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String line = null;
						while ((line = reader.readLine()) != null) {
							sb.append(line).append("\n");
						}
						String res = sb.toString();
						reader.close();
						reader = null;
						media_id = new JSONObject(res).getString("media_id");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return media_id;
			}

			protected void onPostExecute(String result) {
				if (TextUtils.isEmpty(result)) {
					Logger.e("图片上传失败");
					return;
				}
				if (listener != null) {
					listener.onSuccess(result);
				}
			};

		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public interface OnUploadListener {
		void onSuccess(String result);

		void onFailure();
	}

}
