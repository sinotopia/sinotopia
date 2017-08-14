package com.hkfs.fundamental.common.utils;

import org.apache.http.*;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * http请求模拟工具类(HTTP,HTTPS)
 *
 * @Author dzr
 */
public class HttpClientUtil {

	private static final int DEFAULT_SOCKET_TIMEOUT = 30000;

	private static final int DEFAULT_CONNECT_TIMEOUT = 30000;

	private static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 30000;

	private static final int DEFAULT_MAX_CONNTOTAL = 1500;

	private static final int DEFAULT_MAX_CONNPERROUTE = 1000;


	private static Map<String, HttpClient> httpRequestMap = new ConcurrentHashMap<String, HttpClient>();

	private static Lock httpRequestMapLock = new ReentrantLock();


	static {
		init();
	}
	/**
	 * 退出虚拟机时自动关闭
	 */
	private static void init() {
		// 注册关闭钩子
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				httpRequestMapLock.lock();
				try {
					for (HttpClient httpClient : httpRequestMap.values()) {
						httpClient.close();
					}
				} finally {
					httpRequestMapLock.unlock();
				}
			}
		});
	}

	/**
	 * 从缓存里面获取一个HttpClient
	 * @param requestName
	 * @param socketTimeout
	 * @param connectTimeout
	 * @param connectionRequestTimeout
	 * @param maxConnTotal
	 * @param maxConnPerRoute
	 * @return
	 */
	private static HttpClient getHttpClient(String requestName,int socketTimeout,int connectTimeout,int connectionRequestTimeout,int maxConnTotal,int maxConnPerRoute) {
		HttpClient httpClient = httpRequestMap.get(requestName);
		httpRequestMapLock.lock();
		try {
			httpClient = httpRequestMap.get(requestName);
			if (null == httpClient) {
				httpClient = new HttpClient(requestName,socketTimeout,connectTimeout,connectionRequestTimeout,maxConnTotal,maxConnPerRoute);
				httpRequestMap.put(requestName, httpClient);
			}
		} finally {
			httpRequestMapLock.unlock();
		}
		return httpClient;
	}

	/**
	 * 获取httpClient
	 * @return
	 */
	private static HttpClient getDefaultHttpClient(){
		return getDefaultHttpClient("default_httpclient");
	}

	/**
	 * 获取httpClient
	 * @param name
	 * @return
	 */
	private static HttpClient getDefaultHttpClient(String name){
		return getHttpClient(name,DEFAULT_SOCKET_TIMEOUT,DEFAULT_CONNECT_TIMEOUT,DEFAULT_CONNECTION_REQUEST_TIMEOUT,DEFAULT_MAX_CONNTOTAL,DEFAULT_MAX_CONNPERROUTE);
	}

	/**
	 * 通过get请求的方式获取数据
	 *
	 * @param url
	 * @return
	 */
	public static String getData(String url) {
		return getData(url, (Header[])null);
	}

	/**
	 * 通过get请求方式获取数据
	 *
	 * @param url
	 * @param header
	 * @return
	 */
	public static String getData(String url, Header[] header) {
		return getDefaultHttpClient().getData(url,header);
	}

	/**
	 * 表单提交
	 * @param url
	 * @param params
	 * @return
	 */
	public static String postForm(String url,Map<String,Object> params){
		return getDefaultHttpClient().postForm(url,params);
	}

	/**
	 * body提交
	 * @param url
	 * @param rawData
	 * @return
	 */
	public static String postRaw(String url,String rawData){
		return getDefaultHttpClient().postRaw(url, rawData);
	}

	/**
	 * 流提交
	 * @param url
	 * @param is
	 * @return
	 */
	public static String postInputStrean(String url,InputStream is){
		return getDefaultHttpClient().postInputStrean(url, is);
	}

	/**
	 * 文件提交
	 * @param url
	 * @param fileName 文件名称
	 * @param inputStream 文件流
	 * @param params
	 * @return
	 */
	public static String postFile(String url,String fileName,InputStream inputStream,Map<String,Object> params){
		return getDefaultHttpClient().postFile(url, fileName, inputStream, params);
	}

	public static String postData(String url,Header[] header,HttpEntity entity) {
		return getDefaultHttpClient().postData(url, header, entity);
	}

	/**
	 * 通过get请求的方式获取数据
	 *
	 * @param url
	 * @return
	 */
	public static String getData(String name,String url) {
		return getData(name, url, null);
	}

	/**
	 * 通过get请求方式获取数据
	 *
	 * @param url
	 * @param header
	 * @return
	 */
	public static String getData(String name, String url, Header[] header) {
		return getDefaultHttpClient(name).getData(url, header);
	}

	/**
	 * 表单提交
	 * @param url
	 * @param params
	 * @return
	 */
	public static String postForm(String name, String url,Map<String,Object> params){
		return getDefaultHttpClient(name).postForm(url,params);
	}

	/**
	 * body提交
	 * @param url
	 * @param rawData
	 * @return
	 */
	public static String postRaw(String name, String url,String rawData){
		return getDefaultHttpClient(name).postRaw(url, rawData);
	}

	/**
	 * 流提交
	 * @param url
	 * @param is
	 * @return
	 */
	public static String postInputStrean(String name ,String url,InputStream is){
		return getDefaultHttpClient(name).postInputStrean(url, is);
	}

	/**
	 * 文件提交
	 * @param url
	 * @param fileName 文件名称
	 * @param inputStream 文件流
	 * @param params
	 * @return
	 */
	public static String postFile(String name, String url,String fileName,InputStream inputStream,Map<String,Object> params){
		return getDefaultHttpClient(name).postFile(url, fileName, inputStream, params);
	}

	public static String postData(String name, String url,Header[] header,HttpEntity entity) {
		return getDefaultHttpClient(name).postData(url, header, entity);
	}

	/**
	 * 自己定义的httpClient
	 */
	public static class HttpClient {

		private final Logger LOG = LoggerFactory.getLogger("HttpClient");

		private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		private int socketTimeout = 30000;

		private int connectTimeout = 30000;

		private int connectionRequestTimeout = 30000;

		private int maxConnTotal = 1500;

		private int maxConnPerRoute = 1000;

		private org.apache.http.client.HttpClient httpclient = null;

		private String name = "";

		private Map<String,AtomicInteger> dailyRequestStatisticsMap = new ConcurrentHashMap<String,AtomicInteger>();

		private AtomicInteger requestCount = new AtomicInteger(0);

		public HttpClient(String name, int socketTimeout, int connectTimeout, int connectionRequestTimeout, int maxConnTotal, int maxConnPerRoute){
			if(name != null && !name.equals("")){
				this.name = "["+name+"]";
			}
			if(socketTimeout >0) {
				this.socketTimeout = socketTimeout;
			}
			if(connectTimeout >0) {
				this.connectTimeout = connectTimeout;
			}
			if(connectionRequestTimeout >0) {
				this.connectionRequestTimeout = connectionRequestTimeout;
			}
			if(maxConnPerRoute >0) {
				this.maxConnPerRoute = maxConnPerRoute;
			}
			if(maxConnTotal > 0) {
				this.maxConnTotal = maxConnTotal;
			}
			this.initConfig();
		}

		private void initConfig(){
			try {
				SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
					@Override
					public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						return true;
					}
				}).build();

				RequestConfig defaultRequestConfig = RequestConfig.custom().setExpectContinueEnabled(true)
						.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
						.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).setConnectTimeout(connectTimeout)
						.setSocketTimeout(socketTimeout).setConnectionRequestTimeout(connectionRequestTimeout).build();

				httpclient = HttpClientBuilder.create().setSslcontext(sslContext)
						.setSSLHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
						.setDefaultRequestConfig(defaultRequestConfig).setMaxConnTotal(maxConnTotal).setMaxConnPerRoute(maxConnPerRoute).build();
			} catch (Exception ex) {
				LOG.error(this.name+" httpClient4.4 init failed!", ex);
			}
		}

		/**
		 * 通过get请求的方式获取数据
		 *
		 * @param url
		 * @return
		 */
		public String getData(String url) {
			return getData(url, null);
		}

		/**
		 * 通过get请求方式获取数据
		 *
		 * @param url
		 * @param header
		 * @return
		 */
		public String getData(String url, Header[] header) {
			String responseString = null;
			HttpGet get = null;
			HttpEntity responseEntity = null;
			int count = this.getCount().incrementAndGet();
			try {
				LOG.info("[client->server]-"+this.name+"-"+count+" url:" + url);
				get = new HttpGet(url);
				if (header != null) {
					get.setHeaders(header);
				}
				HttpResponse response = httpclient.execute(get);

				int statusCode = response.getStatusLine().getStatusCode();
				responseEntity = response.getEntity();
				responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
				LOG.info("[server->client]-"+this.name+"-"+count+" url:" + url + ", reponse:" + responseString);
				if (statusCode == HttpStatus.SC_OK) {
					return responseString;
				} else {
					LOG.warn(this.name+"-"+count+" getData fail, url:" + url + ", status:" + statusCode);
				}
			} catch (Exception e) {
				LOG.error(this.name+"-"+count+" getData Exception", e);
			} finally {
				if (null != get) {
					get.abort();
				}
				if (null != responseEntity) {
					try {
						EntityUtils.consume(responseEntity);
					} catch (Exception e) {
						LOG.error(this.name+"-"+count+" consume httpentity error", e);
					}
				}
			}
			return responseString;
		}

		/**
		 * 表单提交
		 * @param url
		 * @param params
		 * @return
		 */
		public String postForm(String url,Map<String,Object> params){
			LOG.info("[client->server] [FORM]-"+this.name+ " url:" + url +",params:"+ params);
			HttpEntity httpEntity = createUrlEncodedFormEntity(params);
			return postData(url,null,httpEntity);
		}

		/**
		 * body提交
		 * @param url
		 * @param rawData
		 * @return
		 */
		public String postRaw(String url,String rawData){
			LOG.info("[client->server] [RAW]-"+this.name+" url:" + url +",params:"+ rawData);
			HttpEntity httpEntity = createRawEntity(rawData);
			return postData(url,null,httpEntity);
		}

		/**
		 * 流提交
		 * @param url
		 * @param is
		 * @return
		 */
		public String postInputStrean(String url,InputStream is){
			LOG.info("[client->server] [inputstream]-"+this.name+" url:" + url );
			HttpEntity httpEntity = createInputStreamEntity(is);
			return postData(url,null,httpEntity);
		}

		/**
		 * 文件提交
		 * @param url
		 * @param fileName 文件名称
		 * @param inputStream 文件流
		 * @param params
		 * @return
		 */
		public String postFile(String url,String fileName,InputStream inputStream,Map<String,Object> params){
			LOG.info("[client->server] [FILE]-"+this.name+" url:" + url +",params:"+ params);
			HttpEntity httpEntity = null;
			if (inputStream == null) {
				httpEntity= createMultipartEntity(null, params);
			}else{
				httpEntity = createMultipartEntity(new InputStreamBody(inputStream, fileName), params);
			}
			return postData(url,null,httpEntity);
		}

		public String postData(String url,Header[] header,HttpEntity entity) {
			String reponseString = null;
			HttpPost post = null;
			HttpEntity responseEntity = null;
			int count = this.getCount().incrementAndGet();
			try {
				LOG.info("[client->server]-"+this.name+"-"+count+" url:" + url );
				post = new HttpPost(url);
				post.setEntity(entity);
				if (header != null) {
					post.setHeaders(header);
				}
				HttpResponse response = httpclient.execute(post);
				int statusCode = response.getStatusLine().getStatusCode();
				responseEntity = response.getEntity();
				reponseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
				LOG.info("[server->client]-"+this.name+"-"+count+" url:" + url + ", reponse:" + reponseString);
				if (statusCode == HttpStatus.SC_OK) {
					return reponseString;
				} else {
					LOG.warn(this.name+"-"+count+" postData fail, url:" + url + ", status:" + statusCode);
					return null;
				}
			} catch (Exception e) {
				LOG.error(this.name+"-"+count+" postData Exception", e);
			} finally {
				if (null != post) {
					post.abort();
				}
				if (null != responseEntity) {
					try {
						EntityUtils.consume(responseEntity);
					} catch (Exception e) {
						LOG.error(this.name+"-"+count+" consume httpentity error", e);
					}
				}
			}
			return reponseString;
		}


		private HttpEntity createRawEntity(String rawData) {
			StringEntity entity = new StringEntity(rawData, "UTF-8");
			return entity;
		}

		private HttpEntity createInputStreamEntity(InputStream in) {
			InputStreamEntity entity = new InputStreamEntity(in);
			return entity;
		}

		private HttpEntity createUrlEncodedFormEntity(Map<String, Object> params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			if(params != null) {
				Iterator<Entry<String, Object>> iter = params.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, Object> entry = iter.next();
					pairs.add(new BasicNameValuePair(entry.getKey(), StrUtils.getString(entry.getValue())));
				}
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8);
			return entity;
		}

		private HttpEntity createMultipartEntity(InputStreamBody in, Map<String, Object> params) {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			if (in != null) {
				builder.addBinaryBody(in.getFilename(), in.getInputStream());
			}
			if(params != null) {
				Iterator<Entry<String, Object>> iter = params.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, Object> entry = iter.next();
					builder.addTextBody(entry.getKey(), StrUtils.getString(entry.getValue()));
				}
			}
			return builder.build();
		}

		public void close(){
			if(this.httpclient != null && this.httpclient instanceof CloseableHttpClient){
				try {
					((CloseableHttpClient) this.httpclient).close();
				} catch (IOException e) {
					LOG.error("-"+this.name+" close httpclient error", e);
				}
			}
		}

		private AtomicInteger getCount(){
			String curDate = sdf.format(new Date());
			AtomicInteger requestCount = this.dailyRequestStatisticsMap.get(curDate);
			if(requestCount == null){
				requestCount = new AtomicInteger(0);
				this.dailyRequestStatisticsMap.putIfAbsent(curDate,requestCount);
			}
			return requestCount;
		}
	}
	

	public static void main(String[] args) {

//		System.out.println(999%1000);
//		HttpClientUtil.postRaw("http://192.168.7.61:8180/djd-system-web/system/customer/get_customer_details", null);
//
		List<Header> headers = new ArrayList<Header>();
//		headers.add(new BasicHeader("Connection", "keep-alive"));
		// headers.add(new BasicHeader("Accept", "*/*"));
//		headers.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/;q=0.8"));
		// headers.add(new BasicHeader("Content-Type",
		// "application/x-www-form-urlencoded; charset=UTF-8"));
		headers.add(new BasicHeader("Cookie", "Authorization=Bearer c071d339e59d4448a77b52c6ff2c818c"));
		// headers.add(new BasicHeader("X-Requested-With",
		// "XMLHttpRequest"));
//		headers.add(new BasicHeader("Cache-Control", "max-age=0"));
//		headers.add(new BasicHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) "));

		final Header[] headerArray = new Header[headers.size()];
		headers.toArray(headerArray);
		ExecutorService executorService = ExecutorUtils.getCachedThreadPool("ddd");
		for(int i=0;i<2000;i++){
			final int requestSeq = i;
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					//183.136.185.227/oper-activity/djdactivity/golden_egg_count
//					System.out.println(HttpClientUtil.postData("http://183.136.185.227/oper-activity/djdactivity/golden_egg_count", headerArray,null));
					HttpClientUtil.postData("test_" + requestSeq % 10,"http://192.168.7.61/", headerArray,null);
				}
			});
		}

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
