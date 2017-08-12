package com.hkfs.fundamental.common.utils;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HttpUtils {
	public static int DEFAULT_CONNECT_TIMEOUT = 8000;
	public static int DEFAULT_READ_TIMEOUT = 15000;
	public static String DEFAULT_CHARSET = "UTF-8";
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	
	public static String get(String url) throws Exception {
		return get(url, null, null);
	}
	public static String get(String url, int connectTimeout, int readTimeout) throws Exception {
		return get(url, null, connectTimeout, readTimeout);
	}
	public static String get(String url, String charset) throws Exception {
		return requestUrl(url, METHOD_GET, charset, null, null);
	}
	public static String get(String url, String charset, int connectTimeout, int readTimeout) throws Exception {
		return requestUrl(url, METHOD_GET, charset, null, null, connectTimeout, readTimeout, null);
	}
	public static String requestUrl(String url, String requestMethod, String charset, String data, String contentType) throws Exception {
		return requestUrl(url, requestMethod, charset, data, contentType, 0, 0, null);
	}

	public static String get(String url, List<StrUtils.NamedValue> headers) throws Exception {
		return get(url, null, headers);
	}
	public static String get(String url, int connectTimeout, int readTimeout, List<StrUtils.NamedValue> headers) throws Exception {
		return get(url, null, connectTimeout, readTimeout, headers);
	}
	public static String get(String url, String charset, List<StrUtils.NamedValue> headers) throws Exception {
		return requestUrl(url, METHOD_GET, charset, null, null, headers);
	}
	public static String get(String url, String charset, int connectTimeout, int readTimeout, List<StrUtils.NamedValue> headers) throws Exception {
		return requestUrl(url, METHOD_GET, charset, null, null, connectTimeout, readTimeout, headers);
	}
	public static String requestUrl(String url, String requestMethod, String charset, String data, String contentType, List<StrUtils.NamedValue> headers) throws Exception {
		return requestUrl(url, requestMethod, charset, data, contentType, 0, 0, headers);
	}










	public static String requestUrl(
			String url, String requestMethod, 
			String charset, String data, String contentType,
			int connectTimeout, int readTimeout, List<StrUtils.NamedValue> headers) throws Exception {
		
		if (!METHOD_GET.equalsIgnoreCase(requestMethod) && !METHOD_POST.equalsIgnoreCase(requestMethod)) {
			requestMethod = METHOD_GET;
		}
		if (charset == null) {
			charset = DEFAULT_CHARSET;
		}
		
		URL u = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) u.openConnection();
		connection.setRequestMethod(requestMethod.toUpperCase());
		connection.setConnectTimeout(connectTimeout>0?connectTimeout:DEFAULT_CONNECT_TIMEOUT);
		connection.setReadTimeout(readTimeout>0?readTimeout:DEFAULT_READ_TIMEOUT);
		connection.setUseCaches(false);

		if (headers != null) {
			for (StrUtils.NamedValue header : headers) {
				if (header.value != null) {
					connection.addRequestProperty(header.name, header.value.toString());
				}
			}
		}

		if (data != null) {
			connection.addRequestProperty(HEADER_CONTENT_TYPE, StrUtils.notEmpty(contentType) ? contentType : getPlainTextContentType());
			connection.setDoOutput(true);
			byte[] body = data.getBytes(charset);
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.write(body);
			out.flush();
			out.close();
		}
		else {
			connection.addRequestProperty(HEADER_CONTENT_TYPE, contentType!=null?contentType:getFormContentType(charset));
		}

		try {
			byte[] bytes = IOUtils.readBytes(connection.getInputStream());
			String result = new String(bytes, charset);
			return result;
		}
		finally {
			try {
				connection.disconnect();
			}
			catch (Exception e) {}
		}
	}

	public static String getFormContentType(String charset) {
		return "application/x-www-form-urlencoded; charset=" +charset ;
	}
	
	public static String getPlainTextContentType() {
		return "text/plain";
	}
	
	public static String post(String url) throws Exception {
		return post(url, null, 0, 0);
	}
	public static String post(String url, int connectTimeout, int readTimeout) throws Exception {
		return post(url, null, connectTimeout, readTimeout);
	}
	public static String post(String url, String charset) throws Exception {
		return post(url, charset, 0, 0);
	}
	public static String post(String url, String charset, int connectTimeout, int readTimeout) throws Exception {
		return post(url, charset, null, connectTimeout, readTimeout);
	}
	public static String post(String url, String charset, String data) throws Exception {
		return post(url, charset, data, null, null);
	}
	public static String post(String url, String charset, String data, int connectTimeout, int readTimeout) throws Exception {
		return post(url, charset, data, null, connectTimeout, readTimeout);
	}
	public static String post(String url, String charset, String data, String contentType) throws Exception {
		return requestUrl(url, METHOD_POST, charset, data, contentType);
	}
	public static String post(String url, String charset, String data, String contentType, int connectTimeout, int readTimeout) throws Exception {
		return requestUrl(url, METHOD_POST, charset, data, contentType, connectTimeout, readTimeout, null);
	}


	public static String post(String url, List<StrUtils.NamedValue> headers) throws Exception {
		return post(url, null, headers);
	}
	public static String post(String url, int connectTimeout, int readTimeout, List<StrUtils.NamedValue> headers) throws Exception {
		return post(url, null, connectTimeout, readTimeout, headers);
	}
	public static String post(String url, String charset, List<StrUtils.NamedValue> headers) throws Exception {
		return post(url, charset, null, headers);
	}
	public static String post(String url, String charset, int connectTimeout, int readTimeout, List<StrUtils.NamedValue> headers) throws Exception {
		return post(url, charset, null, connectTimeout, readTimeout, headers);
	}
	public static String post(String url, String charset, String data, List<StrUtils.NamedValue> headers) throws Exception {
		return post(url, charset, data, null, headers);
	}
	public static String post(String url, String charset, String data, int connectTimeout, int readTimeout, List<StrUtils.NamedValue> headers) throws Exception {
		return post(url, charset, data, null, connectTimeout, readTimeout, headers);
	}
	public static String post(String url, String charset, String data, String contentType, List<StrUtils.NamedValue> headers) throws Exception {
		return requestUrl(url, METHOD_POST, charset, data, contentType, headers);
	}
	public static String post(String url, String charset, String data, String contentType, int connectTimeout, int readTimeout, List<StrUtils.NamedValue> headers) throws Exception {
		return requestUrl(url, METHOD_POST, charset, data, contentType, connectTimeout, readTimeout, headers);
	}
}


