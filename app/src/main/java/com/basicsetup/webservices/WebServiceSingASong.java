package com.basicsetup.webservices;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import android.util.Log;

public class WebServiceSingASong {

	private static final String TAG = WebServiceSingASong.class.getSimpleName();
	private static final String GET = "GET";
	private static final String POST = "POST";
	
	private static final int CONNECTION_TIMEOUT = 30000;
	private static final int READ_TIME_OUT = 30000;

	public String execute(WebserviceModel model) {

		HttpURLConnection conn = null;
		String response = null;
		int status = -1;

		try {
			String finalUrlString = null;
			String urlWithUrlParams = addUrlParam(
					WebserviceUtils.getUrl(model), model.getUrlParams());

			switch (model.getMethodeType()) {
			case WebserviceConstants.METHODE_TYPE_GET:
				finalUrlString = addUrlParam(urlWithUrlParams,
						model.getUrlParams());
				conn = getHttpUrlConnection(finalUrlString);
				conn.setDoOutput(false);
				conn.setDoInput(true);
				conn.setRequestMethod(GET);
				break;

			case WebserviceConstants.METHODE_TYPE_POST:
				finalUrlString = urlWithUrlParams;
				conn = getHttpUrlConnection(finalUrlString);
				conn.setRequestMethod(POST);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);

				String boundary = "AaB03x";
				conn.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + boundary);
				conn.setRequestProperty("Accept-Charset", "UTF-8");

				DataOutputStream out = new DataOutputStream(
						conn.getOutputStream());

				List<ParcelableNameValuePair> params = model
						.getOptionalParams();

				for (ParcelableNameValuePair param : params) {
					if (!param.getName().equals(
							WebserviceConstants.PARAM_REC_FILE)) {
						Log.d("singsongresponse", "recorded file  + " + param.getName()
								+ " : " + param.getValue());
						writeParam(param.getName(), param.getValue(), out,
								boundary);
					} else {
						Log.d("singsongresponse", "recorded file  + " + param.getName()
								+ " : " + param.getValue());
						File file = new File(param.getValue());
						Log.d("singsongresponse", "file exists : " + file.exists());
						writeFile(param.getName(), file, out, boundary);
					}
				}

				out.writeBytes("--" + boundary +"--"+ "\r\n");
				out.flush();
				out.close();

				break;
			}

			conn.setConnectTimeout(CONNECTION_TIMEOUT);
			conn.setReadTimeout(READ_TIME_OUT);
			conn.connect();

			Log.d(TAG, "fetching code : ");
			status = conn.getResponseCode();
			Log.d(TAG, "code : " + status);

			//if (conn.getContentLength() > 0) {
				BufferedInputStream in = new BufferedInputStream(
						conn.getInputStream());
				byte[] body = readStream(in);
				response = new String(body);
				
				Log.d(TAG, "code : " + response);
			//}

		} catch (IOException e) {
			Log.d(TAG, Log.getStackTraceString(e));
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.disconnect();
		}

		return response;
	}

	private HttpURLConnection getHttpUrlConnection(String finalUrlString)
			throws IOException {
		URL url = new URL(finalUrlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		return conn;
	}

	private String addUrlParam(String url, List<String> urlParams) {
		if (url == null) {
			return null;
		}

		if (urlParams == null) {
			return url;
		} else {
			for (String urlParam : urlParams) {
				url = MessageFormat.format("{0}/{1}", url, urlParam);
			}

			return url;
		}
	}

	private static byte[] readStream(InputStream in) throws IOException {
		byte[] buf = new byte[1024];
		int count = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		while ((count = in.read(buf)) != -1)
			out.write(buf, 0, count);
		return out.toByteArray();
	}

	private static void writeParam(String name, String value,
			DataOutputStream out, String boundary) {
		try {
			out.writeBytes("--" + boundary + "\r\n");
			out.writeBytes("content-disposition: form-data; name=\"" + name
					+ "\"\r\n\r\n");
			out.writeBytes(value+"\r\n");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private static void writeFile(String name, File file, DataOutputStream out,
			String boundary) {
		try {
			Log.d("AUDIO", "writing file + _" + file.exists());
			if (!file.exists()) {
				return;
			}
			Log.d("AUDIO", "writing file +++++ ");

			out.writeBytes("--" + boundary + "\r\n");
			out.writeBytes("content-disposition: form-data; name=\"" + name
					+ "\";filename=\"" + file.getName() + "\"\r\n");
			out.writeBytes("content-type: video/mp4\r\n\r\n");
//			out.writeBytes("Hi Shain Padamajan"+"\r\n");

			FileInputStream fis = new FileInputStream(file);
			while (true) {
				synchronized (buffer) {
					int amountRead = fis.read(buffer);
					if (amountRead == -1) {
						break;
					}
					out.write(buffer, 0, amountRead);
				}
			}
			fis.close();
			
			out.writeBytes("\r\n");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	static final int BUFF_SIZE = 1024*100;		//1KB *100
	static final byte[] buffer = new byte[BUFF_SIZE];

}
