package com.sinotopia.fundamental.httpclient;

import com.sinotopia.fundamental.common.utils.StrUtils;
import com.sinotopia.fundamental.httpclient.http.*;
import com.sinotopia.fundamental.httpclient.utils.ClientUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 基于HttpClient扩展的用于请求响应处理的工具类
 * Created by zhoubing on 2016/12/2.
 */
public class ClientEx {
    private static Logger logger = LoggerFactory.getLogger(ClientEx.class);
    private RedirectStrategy redirectStrategy = new CoreRedirectStrategy();
    private HttpRequestRetryHandler retryHandler = new CoreRetryHandler();

    private List<Header> headerList = new LinkedList<Header>();
    private List<NameValuePair> pairList = new LinkedList<NameValuePair>();
    private BasicHttpParams params = new BasicHttpParams();

    private String charset = StrUtils.UTF_8;
    private HttpClient httpClient = null;

    private HttpResponse response = null;
    private HttpEntity httpEntity = null;
    private Result result = null;
    private String requestUrl = null;//最后一次请求的地址
    private boolean consumed = false;
    private boolean inited = false;

    public ClientEx() {
        initParams();
    }
    public static ClientEx newInstance() {
        return new ClientEx();
    }
    /**
     * 复用ClientEx对象，保留HttpParams、Headers，重新使用新的HttpClient
     */
    public ClientEx reset() {
        ClientUtils.shutdown(httpClient);

        this.pairList.clear();
        this.headerList.clear();
        this.httpEntity = null;
        this.httpClient = null;
        this.response = null;
        this.result = null;
        this.consumed = false;
        this.requestUrl = null;
        this.inited = false;

        return this;
    }

    public ClientEx enableSSL() {
        return enableSSL(false);
    }
    public ClientEx enableSSL(boolean forceReset) {
        if (inited && !forceReset) {
            return this;
        }
        try {
            RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new CoreTrustStrategy()).build();

            HttpClientBuilder builder = HttpClients.custom()
                    .setSSLSocketFactory(new CoreSSLConnectionSocketFactory(sslContext))
                    .setRedirectStrategy(redirectStrategy)
                    .setRetryHandler(retryHandler)
                    ;

            //cookie策略
            String cookiePolicy = (String) params.getParameter(ClientPNames.COOKIE_POLICY);
            if (cookiePolicy != null && cookiePolicy.equals(org.apache.http.client.config.CookieSpecs.BROWSER_COMPATIBILITY)) {
                CookieSpecProvider myCookieSpecProvider = new CoreCookieSpecProvider();
                String specProviderName = "myCookieSpecProvider";
                Registry<CookieSpecProvider> cookieSpecRegistry = RegistryBuilder.<CookieSpecProvider>create()
                        .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
                        .register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory())
                        .register(specProviderName, myCookieSpecProvider)
                        .build();
                requestConfigBuilder.setCookieSpec(specProviderName);
                builder.setDefaultCookieSpecRegistry(cookieSpecRegistry);
            }

            //是否支持循环跳转
            Boolean circularRedirectsAllowed = (Boolean) params.getParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS);
            if (circularRedirectsAllowed != null) {
                requestConfigBuilder.setCircularRedirectsAllowed(circularRedirectsAllowed);
            }
            //是否支持跳转
            Boolean redirectsEnabled = (Boolean) params.getParameter(ClientPNames.HANDLE_REDIRECTS);
            if (redirectsEnabled != null) {
                requestConfigBuilder.setRedirectsEnabled(redirectsEnabled);
            }
            //最大跳转次数
            Integer maxRedirects = (Integer) params.getParameter(ClientPNames.MAX_REDIRECTS);
            if (maxRedirects != null) {
                requestConfigBuilder.setMaxRedirects(maxRedirects);
            }
            //连接超时
            Integer connectTimeout = (Integer) params.getParameter(CoreConnectionPNames.CONNECTION_TIMEOUT);
            if (connectTimeout != null) {
                requestConfigBuilder.setConnectTimeout(connectTimeout);
                requestConfigBuilder.setConnectionRequestTimeout(connectTimeout);
            }
            //读取超时
            Integer socketTimeout = (Integer) params.getParameter(CoreConnectionPNames.SO_TIMEOUT);
            if (socketTimeout != null) {
                requestConfigBuilder.setSocketTimeout(socketTimeout);
            }
            //代理
            HttpHost httpHost = (HttpHost) params.getParameter(ConnRoutePNames.DEFAULT_PROXY);
            if (httpHost != null) {
                requestConfigBuilder.setProxy(httpHost);

                String username = (String) params.getParameter(PROXY_USERNAME);
                if (StrUtils.notEmpty(username)) {
                    String password = (String) params.getParameter(PROXY_PASSWORD);
                    if (StrUtils.notEmpty(password)) {
                        builder.setDefaultCredentialsProvider(buildCredentialsProvider(
                                username, password, httpHost.getHostName(), httpHost.getPort()));
                    }
                }
            }

            builder.setDefaultRequestConfig(requestConfigBuilder.build());
            this.httpClient = builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.inited = true;
        }
        return this;
    }

    /**
     * 使用自定义的HttpClient
     */
    public ClientEx httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    /**
     * 清除HttpParams
     */
    public void clearParams() {
        this.params.clear();
    }
    /**
     * 清除请求的Headers
     */
    public void clearHeaders() {
        this.headerList.clear();
    }
    /**
     * 使用默认的参数初始化HttpParams
     */
    public void initParams() {
        //默认配置
        HttpConnectionParams.setSocketBufferSize(params, DEFAULT_BUFFER_SIZE);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        redirecting(true);
        allowCircularRedirects(true);
        maxRedirects(DEFAULT_MAX_REDIRECTS);
        connectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        socketTimeout(DEFAULT_SOCKET_TIMEOUT);
        cookiePolicy(org.apache.http.client.config.CookieSpecs.BROWSER_COMPATIBILITY);

        setPCMode();
    }

    public static int DEFAULT_BUFFER_SIZE = 8192;//默认buffer大小
    public static int DEFAULT_MAX_REDIRECTS = 10;//默认最大跳转
    public static int DEFAULT_CONNECTION_TIMEOUT = 15000;//默认连接超时
    public static int DEFAULT_SOCKET_TIMEOUT = 45000;//默认读取超时

    /**
     * 设置连接超时时间
     */
    public ClientEx connectionTimeout(int timeout) {
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        return this;
    }
    /**
     * 设置读取超时时间
     */
    public ClientEx socketTimeout(int timeout) {
        HttpConnectionParams.setSoTimeout(params, timeout);
        return this;
    }
    /**
     * 设置是否抛请求过程异常
     */
    public ClientEx throwException(boolean value) {
        params.setParameter(THROW_EXCEPTION, value);
        return this;
    }
    /**
     * 设置代理
     */
    public ClientEx proxy(String host, int port) {
        if (StrUtils.notEmpty(host)) {
            params.setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(host, port));
        }
        else {
            params.removeParameter(ConnRoutePNames.DEFAULT_PROXY);
        }
        return this;
    }

    private static final String THROW_EXCEPTION = "throw-exception";
    private static final String PROXY_USERNAME = "proxy-username";
    private static final String PROXY_PASSWORD = "proxy-password";

    /**
     * 设置代理用户名密码
     */
    public ClientEx proxy(String username, String password) {
        if (username != null) {
            params.setParameter(PROXY_USERNAME, username);
        }
        else {
            params.removeParameter(PROXY_USERNAME);
        }
        if (password != null) {
            params.setParameter(PROXY_PASSWORD, password);
        }
        else {
            params.removeParameter(PROXY_PASSWORD);
        }
        return this;
    }
    /**
     * 设置代理ip，端口号，用户名，密码
     */
    public ClientEx proxy(String username, String password, String host, int port) {
        proxy(host, port);
        return proxy(username, password);
    }
    /**
     * 设置是否支持自动跳转
     */
    public ClientEx redirecting(boolean value) {
        HttpClientParams.setRedirecting(params, value);
        return this;
    }
    /**
     * 设置最大支持跳转的数量
     */
    public ClientEx maxRedirects(int max) {
        params.setParameter(ClientPNames.MAX_REDIRECTS, max);
        return this;
    }
    /**
     * 设置是否支持循环跳转
     */
    public ClientEx allowCircularRedirects(boolean value) {
        params.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, value);
        return this;
    }
    /**
     * 设置cookie的策略
     */
    public ClientEx cookiePolicy(String policy) {
        params.setParameter(ClientPNames.COOKIE_POLICY, policy);
        return this;
    }
    /**
     * 设置内容的字符集
     */
    public ClientEx charset(String charset) {
        HttpProtocolParams.setContentCharset(params, charset);
        this.charset = charset;
        return this;
    }
    /**
     * 添加请求的Header
     */
    public ClientEx header(Header header) {
        return header(header, false);
    }
    /**
     * 添加请求的Header列表
     */
    public ClientEx header(List<Header> headers) {
        headerList.addAll(headers);
        return this;
    }
    /**
     * 添加请求的Header数组
     */
    public ClientEx header(Header[] headers) {
        for (Header header : headers) {
            headerList.add(header);
        }
        return this;
    }
    /**
     * 添加请求的Header
     */
    public ClientEx header(String name, String value) {
        return header(name, value, false);
    }
    /**
     * 添加请求的Header
     */
    public ClientEx header(String name, String value, boolean override) {
        return header(new BasicHeader(name, value), override);
    }
    /**
     * 添加请求的Header
     */
    public ClientEx header(Header header, boolean override) {
        if (override) {
            for (int i=headerList.size()-1;i>=0;i--) {
                if (headerList.get(i).getName().equalsIgnoreCase(header.getName())) {
                    headerList.remove(i);
                    break;
                }
            }
        }

        headerList.add(header);
        return this;
    }
    /**
     * 添加请求的表单项
     */
    public ClientEx form(NameValuePair pair) {
        pairList.add(pair);
        return this;
    }

    /**
     * 不支持GZIP
     * @return
     */
    public ClientEx disableGzip() {
        return header("Accept-Encoding", "deflate, sdch", true);
    }

    /**
     * 添加请求的表单项
     * @param params
     * @return
     */
    public ClientEx form(Map<String, String> params) {
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            form(entry.getKey(), entry.getValue() != null ? entry.getValue().toString() : "");
        }
        return this;
    }
    /**
     * 添加请求的表单项
     */
    public ClientEx form(String name, String value) {
        pairList.add(new BasicNameValuePair(name, value));
        return this;
    }
    /**
     * 添加请求的表单项列表
     */
    public ClientEx form(List<NameValuePair> pairs) {
        pairList.addAll(pairs);
        return this;
    }
    /**
     * 添加请求的实体
     */
    public ClientEx form(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
        return this;
    }
    /**
     * 添加请求的表单项数组
     */
    public ClientEx form(NameValuePair[] pairs) {
        for (NameValuePair pair : pairs) {
            pairList.add(pair);
        }
        return this;
    }

    public static String USER_AGENT_PC = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    public static String USER_AGENT_MOBILE = "Mozilla/5.0 (iPhone; CPU iPhone OS 8_0 like Mac OS X) AppleWebKit/600.1.3 (KHTML, like Gecko) Version/8.0 Mobile/12A4345d Safari/600.1.4";

    /**
     * 设置User-Agent
     */
    public ClientEx userAgent(String userAgent) {
        return header("User-Agent", userAgent, true);
    }
    /**
     * 使用移动端模式
     */
    public ClientEx setMobileMode() {
        return userAgent(USER_AGENT_MOBILE);
    }
    /**
     * 使用移动端模式
     */
    public ClientEx setPCMode() {
        return userAgent(USER_AGENT_PC);
    }
    /**
     * 设置Referer
     */
    public ClientEx referer(String referer) {
        return header("Referer", referer, true);
    }
    /**
     * 设置Content-Type
     */
    public ClientEx contentType(String contentType) {
        return header("Content-Type", contentType, true);
    }
    /**
     * 设置Cookie
     */
    public ClientEx cookie(String cookie) {
        return header("Cookie", cookie, true);
    }
    /**
     * 设置跳转策略
     */
    public ClientEx redirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
        return this;
    }
    /**
     * 设置重试策略
     */
    public ClientEx retryHandler(HttpRequestRetryHandler retryHandler) {
        this.retryHandler = retryHandler;
        return this;
    }
    /**
     * 根据已经设置的参数构造请求
     */
    private HttpUriRequest buildRequest(HttpUriRequest request) {
        if (headerList.size() > 0) {
            for (Header header : headerList) {
                request.addHeader(header);
            }
        }
        if (request instanceof HttpEntityEnclosingRequestBase) {
            if (pairList.size() > 0) {
                try {
                    ((HttpEntityEnclosingRequestBase)request).setEntity(new UrlEncodedFormEntity(pairList, charset));
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if (httpEntity != null) {
                ((HttpEntityEnclosingRequestBase)request).setEntity(httpEntity);
            }
        }
        return request;
    }

    /**
     * GET请求
     */
    public ClientEx get(String url) {
        return get(url, null);
    }
    /**
     * GET请求
     */
    public ClientEx get(String url, HttpContext httpContext) {
        return request(url, httpContext, new HttpGet(url));
    }
    /**
     * POST请求
     */
    public ClientEx post(String url) {
        return post(url, null);
    }
    /**
     * POST请求
     */
    public ClientEx post(String url, HttpContext httpContext) {
        return request(url, httpContext, new HttpPost(url));
    }
    /**
     * 请求
     */
    public ClientEx request(String url, HttpContext httpContext, HttpUriRequest request) {
        this.requestUrl = url;
        this.result = new Result();

        this.enableSSL();

        this.buildRequest(request);

        if (httpClient instanceof DefaultHttpClient) {
            buildRedirectStrategy((DefaultHttpClient) httpClient);
            buildProxy((DefaultHttpClient) httpClient);
        }

        try {
            response = null;
            response = httpClient.execute(request, httpContext);
            result.statusLine = response.getStatusLine();
            result.headers = response.getAllHeaders();
        }
        catch (Exception e) {
            result.exception = e;
            if (isThrowException()) {
                throw new RuntimeException(e);
            }
        }

        return this;
    }
    /**
     * 是否抛请求时的异常
     */
    private boolean isThrowException() {
        //默认不抛
        Boolean value = (Boolean) params.getParameter(THROW_EXCEPTION);
        return value != null && value == true;
    }
    /**
     * 设置跳转策略
     */
    private void buildRedirectStrategy(DefaultHttpClient client) {
        client.setRedirectStrategy(redirectStrategy);
    }
    /**
     * 添加代理验证信息，用户名密码
     */
    private void buildProxy(DefaultHttpClient client) {
        String username = (String) params.getParameter(PROXY_USERNAME);
        if (StrUtils.notEmpty(username)) {
            String password = (String) params.getParameter(PROXY_PASSWORD);
            if (StrUtils.notEmpty(password)) {
                HttpHost httpHost = (HttpHost) params.getParameter(ConnRoutePNames.DEFAULT_PROXY);
                if (httpHost != null) {
                    client.setCredentialsProvider(buildCredentialsProvider(
                            username, password, httpHost.getHostName(), httpHost.getPort()));
                }
            }
        }
    }
    /**
     * 构造代理验证信息
     */
    private CredentialsProvider buildCredentialsProvider(String username, String password, String host, int port) {
        CredentialsProvider provider = null;
        if (StrUtils.notEmpty(username) && StrUtils.notEmpty(password) && StrUtils.notEmpty(host)) {
            provider = new BasicCredentialsProvider();

            AuthScope authScope = new AuthScope(host, port);
            UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(
                    username, password);
            provider.setCredentials(authScope, usernamePasswordCredentials);
        }
        return provider;
    }
    /**
     * 获取请求响应数据
     */
    public String getData() {
        if (result == null || response == null) {
            return null;
        }
        if (result.data != null || consumed) {
            return result.data;
        }
        try {
            result.data = ClientUtils.getContentFromResponseEx(response, charset);
        } catch (Exception e) {
            result.exception = e;
            if (isThrowException()) {
                if (isThrowException()) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            ClientUtils.shutdown(httpClient);
            consumed = true;
        }
        return result.data;
    }
    /**
     * 获取二进制数据
     */
    public byte[] getBytes() {
        if (result == null || response == null) {
            return null;
        }
        if (result.bytes != null || consumed) {
            return result.bytes;
        }
        try {
            result.bytes = EntityUtils.toByteArray(response.getEntity());
        } catch (IOException e) {
            result.exception = e;
            if (isThrowException()) {
                throw new RuntimeException(e);
            }
        } finally {
            ClientUtils.shutdown(httpClient);
            consumed = true;
        }
        return result.bytes;
    }
    /**
     * 获取图片二进制数据
     */
    public byte[] getImageBytes() {
        Header header = getHeader("Content-Type");
        if (header != null && header.getValue().contains("text")) {
            return null;
        }
        return getBytes();
    }
    /**
     * 关闭HttpClient
     */
    public ClientEx shutdown() {
        ClientUtils.shutdown(httpClient);
        return this;
    }
    /**
     * 获取result对象
     */
    public Result getResult() {
        return this.result;
    }
    /**
     * 获取请求的表单列表
     */
    public List<NameValuePair> getPairList() {
        return this.pairList;
    }
    /**
     * 获取请求的头列表
     */
    public List<Header> getHeaderList() {
        return this.headerList;
    }
    /**
     * 获取字符集
     */
    public String getCharset() {
        return this.charset;
    }
    /**
     * 获取最后一次请求地址
     */
    public String getRequestUrl() {
        return this.requestUrl;
    }
    /**
     * 获取请求异常
     */
    public Exception getException() {
        return result == null ? null : result.exception;
    }
    /**
     * 获取请求响应的Header数组
     */
    public Header[] getHeaders() {
        return result == null ? null : result.headers;
    }
    /**
     * 获取请求响应的Header数组
     */
    public int getStatusCode() {
        StatusLine statusLine = getStatusLine();
        if (statusLine != null) {
            return statusLine.getStatusCode();
        }
        return -1;
    }
    /**
     * 获取请求响应的HeaderLine
     */
    public StatusLine getStatusLine() {
        return result == null ? null : result.statusLine;
    }
    /**
     * 从响应头中获取cookie字符串
     */
    public String getCookie() {
        Header[] headers = getHeaders();
        if (headers != null) {
            return ClientUtils.getCookie(headers);
        }
        return null;
    }
    /**
     * 获取指定名称的请求响应的Header
     */
    public Header getHeader(String name) {
        Header[] headers = getHeaders();
        if (headers != null && headers.length > 0) {
            for (Header header : headers) {
                if (name.equalsIgnoreCase(header.getName())) {
                    return header;
                }
            }
        }
        return null;
    }
    /**
     * 获取指定名称的请求响应的最后一个Header
     */
    public Header getLastHeader(String name) {
        Header[] headers = getHeaders();
        if (headers != null && headers.length > 0) {
            int length = headers.length;
            for (int i=length-1;i>=0;i--) {
                if (name.equalsIgnoreCase(headers[i].getName())) {
                    return headers[i];
                }
            }
        }
        return null;
    }
    /**
     * 获取请求响应
     */
    public HttpResponse getResponse() {
        return response;
    }

    public void printResponse() {
        logger.info(getResponseText());
    }
    public void printStatusLine() {
        StatusLine statusLine = getStatusLine();
        if (statusLine != null) {
            logger.info(statusLine.toString());
        }
    }
    public void printData() {
        logger.info(getData());
    }
    public void printHeaders() {
        logger.info(getHeaderText());
    }

    public String getHeaderText() {
        StringBuilder sb = new StringBuilder();
        Header[] headers = getHeaders();
        if (headers != null && headers.length > 0) {
            for (Header header : headers) {
                sb.append(header).append("\n");
            }
            sb.substring(0, sb.length()-1);
        }
        return sb.toString();
    }
    public String getResponseText() {
        StringBuilder sb = new StringBuilder();
        sb.append(getStatusLine());
        sb.append(getHeaderText());
        sb.append(getData());
        return sb.toString();
    }

    public static class Result {
        private String data;//文本数据
        private byte[] bytes;//字节数组
        private Header[] headers;//相应头
        private StatusLine statusLine;//请求状态
        private Exception exception;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

        public Header[] getHeaders() {
            return headers;
        }

        public void setHeaders(Header[] headers) {
            this.headers = headers;
        }

        public StatusLine getStatusLine() {
            return statusLine;
        }

        public void setStatusLine(StatusLine statusLine) {
            this.statusLine = statusLine;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }
    }

    public static void main(String[] args) {
        ClientEx client = new ClientEx();

        client.charset("UTF-8");
        client.header("Connection", "keep-alive");
        client.cookie("Hm_lvt_79fae2027f43ca31186e567c6c8fe33e=1477638329; i_vnum=3");

        client.form("username", "15858585858");
        client.form("password", "123456");
        client.form("wd", "关键字");

        client.post("http://www.baidu.com/wd=sss");

        String data = client.getData();

        System.out.println(data);
    }
}
