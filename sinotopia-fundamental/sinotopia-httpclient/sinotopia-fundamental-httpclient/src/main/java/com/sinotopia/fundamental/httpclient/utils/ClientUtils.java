package com.sinotopia.fundamental.httpclient.utils;

import com.sinotopia.fundamental.common.utils.HttpUtils;
import com.sinotopia.fundamental.httpclient.ClientEx;
import com.sinotopia.fundamental.httpclient.http.CoreRedirectStrategy;
import com.sinotopia.fundamental.httpclient.http.CoreRetryHandler;
import com.sinotopia.fundamental.httpclient.http.CoreTrustStrategy;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class ClientUtils {
	private static Logger logger = LoggerFactory.getLogger(ClientEx.class);
	public static final String COOKIE_DIVIDER = "; ";
	public static final String RESPONSE_COOKIE_NAME = "Set-Cookie";

	public static void printHttpResponse(HttpResponse response, String charset) {
		try {
			printStatusLine(response);
			printHeaders(response);
			printEntity(response, charset);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void printCookieStore(CookieStore cookieStore) {
		if (cookieStore != null) {
			List<Cookie> cookieList = cookieStore.getCookies();
			if (cookieList != null) {
				for (Cookie cookie : cookieList) {
					logger.debug(cookie.toString());
				}
			}
		}
	}

	public static void printHeaders(HttpResponse response) {
		try {
			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				logger.debug(header.toString());
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void printStatusLine(HttpResponse response) {
		try {
			logger.debug(response.getStatusLine().toString());
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void printEntity(HttpResponse response, String charset) {
		logger.debug(getContentFromResponse(response, charset));
	}

	public static HttpEntity decodeGZipEntity(HttpEntity entity) {
		return new InflatingEntity(entity);
	}

	public static class InflatingEntity extends HttpEntityWrapper {
		public InflatingEntity(HttpEntity wrapped) {
			super(wrapped);
		}
		@Override
		public InputStream getContent() throws IOException {
			return new GZIPInputStream(super.wrappedEntity.getContent());
		}
		@Override
		public long getContentLength() {
			return -1;
		}
	}

	/**
	 * 判断HttpResponse返回的HttpEntity是否为gzip类型
	 */
	public static boolean isGZIPEntity(HttpResponse response) {
		Header[] headers = response.getAllHeaders();
		if (headers != null && headers.length > 0) {
			String headerName = null;
			for (Header header : headers) {
				headerName = header.getName();
				if (headerName != null && "Content-Encoding".equalsIgnoreCase(headerName)) {
					return "gzip".equalsIgnoreCase(header.getValue());
				}
			}
		}
		return false;
	}

	public static StringEntity getStringEntity(String data) {
		return getStringEntity(data, null);
	}

	public static StringEntity getStringEntity(String data, String charset) {
		StringEntity entity = null;
		if (data != null) {
			try {
				if (charset == null) {
					charset = "UTF-8";
				}
				entity = new StringEntity(data, charset);
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return entity;
	}

	public static String getContentFromResponse(HttpResponse response) {
		return getContentFromResponse(response, null);
	}

	public static String getContentFromResponse(HttpResponse response, String charset) {
		String data = null;
		try {
			return getContentFromResponseEx(response, charset);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return data;
	}

	public static String getContentFromResponseEx(HttpResponse response) throws Exception {
		return getContentFromResponseEx(response, null);
	}
	public static String getContentFromResponseEx(HttpResponse response, String charset) throws Exception {
		String data = null;
		HttpEntity entity = response.getEntity();
		if (isGZIPEntity(response)) {
			entity = decodeGZIPEntity(entity);
		}

		if (charset != null) {
			data = toString(entity, charset);
		}
		else {
			data = toString(entity, "UTF-8");
		}
		return data;
	}
	public static String toString(HttpEntity entity) {
		return toString(entity, (String)null);
	}

	public static String toString(HttpEntity entity, String charset) {
		try {
			if (charset == null) {
				charset = "UTF-8";
			}
			return toString(entity, charset != null ? Charset.forName(charset) : null);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static String toString(
			final HttpEntity entity, final Charset defaultCharset) throws IOException, ParseException {
		Args.notNull(entity, "Entity");
		final InputStream instream = entity.getContent();
		if (instream == null) {
			return null;
		}
		try {
			Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
					"HTTP entity too large to be buffered in memory");
			int i = (int)entity.getContentLength();
			if (i < 0) {
				i = 4096;
			}
			Charset charset = null;
			try {
				final ContentType contentType = ContentType.get(entity);
				if (contentType != null) {
					charset = contentType.getCharset();
				}
			} catch (final UnsupportedCharsetException ex) {
				if (defaultCharset == null) {
					throw new UnsupportedEncodingException(ex.getMessage());
				}
			}
			if (charset == null) {
				charset = defaultCharset;
			}
			if (charset == null) {
				charset = HTTP.DEF_CONTENT_CHARSET;
			}
			final Reader reader = new InputStreamReader(instream, charset);
			final CharArrayBuffer buffer = new CharArrayBuffer(i);
			final char[] tmp = new char[1024];
			int l;
			try {
				while ((l = reader.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}
			}
			catch (EOFException e) {
			}
			return buffer.toString();
		} finally {
			instream.close();
		}
	}

	public static HttpResponse execute(HttpClient client, HttpUriRequest request) {
		try {
			return client.execute(request);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 将gzip编码的HttpEntity转换为普通的HttpEntity
	 */
	public static HttpEntity decodeGZIPEntity(HttpEntity gzipEntity) {
		if (gzipEntity != null) {
			return new HttpEntityWrapper(gzipEntity) {
				@Override
				public InputStream getContent() throws IOException {
					if (super.wrappedEntity != null) {
						return new GZIPInputStream(super.wrappedEntity.getContent(), 8192);
					}
					return super.getContent();
				}
			};
		}
		return null;
	}


	/**
	 * 关闭链接
	 * @param client HttpClient对象
	 */
	public static void shutdown(HttpClient client) {
		if (client != null) {
			try {
				client.getConnectionManager().shutdown();
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 销毁HttpResponse，保证实体内容被完全消耗而且低层的流被关闭。
	 * @param response HttpResponse对象
	 */
	public static void consume(HttpResponse response) {
		if (response != null) {
			try {
				EntityUtils.consume(response.getEntity());
			}
			catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public static String getHost(String url) {
		try {
			URL u = new URL(url);
			return u.getHost();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 从HttpClient中获取服务器返回的Cookie
	 */
	public static String getCookie(HttpClient httpClient) {
		if (httpClient instanceof DefaultHttpClient) {
			return getCookie(((DefaultHttpClient)httpClient).getCookieStore());
		}
		return null;
	}

	/**
	 * 从cookiestore中获取服务器返回的cookie
	 */
	public static String getCookie(CookieStore cookieStore) {
		if (cookieStore != null) {
			List<Cookie> cookies = cookieStore.getCookies();
			if (cookies != null && cookies.size() > 0) {
				String name;
				String value;
				StringBuilder sb = new StringBuilder();
				for (Cookie cookie : cookies) {
					name = cookie.getName();
					value = cookie.getValue();
					if (name != null && value != null) {
						sb.append(name+"="+value+"; ");//cookie项之间用分号+空格隔开
					}
				}
				if (sb.length() > 1) {
					return sb.substring(0, sb.length()-2);
				}
			}
		}
		return null;
	}

	public static String formatCookieValue(String cookieValue) {
		String[] cookies = cookieValue.split(COOKIE_DIVIDER);
		StringBuilder sb = new StringBuilder();
		if (cookies.length > 0) {
			for (String cookie : cookies) {
				String lowerCookie = cookie.toLowerCase();
				if (lowerCookie.startsWith("path=") || lowerCookie.startsWith("expires=")
						|| lowerCookie.startsWith("domain=") || lowerCookie.startsWith("max-age=")
						|| lowerCookie.startsWith("httponly")) {
					continue;
				}
				sb.append(cookie.trim()).append(COOKIE_DIVIDER);
			}
		}
		String newCookie = sb.toString();
		if (newCookie.endsWith(COOKIE_DIVIDER)) {
			return newCookie.substring(0, sb.length()-COOKIE_DIVIDER.length());
		}
		return newCookie;
	}

	public static String getCookie(Header[] headers) {
		StringBuilder sb = new StringBuilder();
		if (headers != null && headers.length > 0) {
			for (Header header : headers) {
				if (RESPONSE_COOKIE_NAME.equalsIgnoreCase(header.getName())) {
					String value = ClientUtils.formatCookieValue(header.getValue());
					if (value != null && value.length() > 0) {
						sb.append(value).append(COOKIE_DIVIDER);
					}
				}
			}
		}
		String cookieText = sb.toString();
		if (cookieText.endsWith(COOKIE_DIVIDER)) {
			return cookieText.substring(0, cookieText.length()-COOKIE_DIVIDER.length());
		}
		return cookieText;
	}


	/**
	 * 创建简单的支持SSL的HttpClient
	 */
	public static HttpClient createSslHttpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		return  createSslHttpClientBuilder().build();
	}
	/**
	 * 创建简单的支持SSL的HttpClient
	 */
	public static HttpClient createSslHttpClient(RequestConfig requestConfig) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		return  createSslHttpClientBuilder(requestConfig).build();
	}
	/**
	 * 创建简单的支持SSL的HttpClient的Builder
	 */
	public static HttpClientBuilder createSslHttpClientBuilder() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		return createSslHttpClientBuilder(createRequestConfig());
	}
	/**
	 * 创建简单的支持SSL的HttpClient的Builder
	 */
	public static HttpClientBuilder createSslHttpClientBuilder(RequestConfig requestConfig) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new CoreTrustStrategy()).build();
		return HttpClients.custom()
				.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
				.setRedirectStrategy(new CoreRedirectStrategy())
				.setRetryHandler(new CoreRetryHandler())
				.setDefaultRequestConfig(requestConfig);
	}
	/**
	 * 创建简单的支持SSL的HttpClient的Builder
	 */
	public static HttpClientBuilder createSslHttpClientBuilder(int connectTimeout, int socketTimeout) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		return createSslHttpClientBuilder(createRequestConfig(connectTimeout, socketTimeout));
	}
	/**
	 * 创建请求配置
	 */
	public static RequestConfig createRequestConfig() {
		return createRequestConfigBuilder().build();
	}
	/**
	 * 创建请求配置
	 */
	public static RequestConfig createRequestConfig(int connectTimeout, int socketTimeout) {
		return createRequestConfigBuilder(connectTimeout, socketTimeout).build();
	}
	/**
	 * 创建请求配置
	 */
	public static RequestConfig.Builder createRequestConfigBuilder() {
		return createRequestConfigBuilder(HttpUtils.DEFAULT_CONNECT_TIMEOUT, HttpUtils.DEFAULT_READ_TIMEOUT);
	}
	/**
	 * 创建请求配置
	 */
	public static RequestConfig.Builder createRequestConfigBuilder(int connectTimeout, int socketTimeout) {
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();

		requestConfigBuilder.setConnectTimeout(connectTimeout);
		requestConfigBuilder.setSocketTimeout(socketTimeout);

		return requestConfigBuilder;
	}
	/**
	 * 创建HttpContext
	 */
	public static HttpContext createHttpContext(CookieStore cookieStore) {
		HttpContext httpContext = HttpClientContext.create();
		if (cookieStore != null) {
			httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
		}
		return httpContext;
	}
	
	/**
	 * 创建HttpContext
	 */
	public static HttpContext createHttpContext() {
		return createHttpContext(new BasicCookieStore());
	}
}
