package com.sinotopia.fundamental.servlet.utils;

import com.sinotopia.fundamental.common.utils.IOUtils;
import com.sinotopia.fundamental.common.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;

public class ActionUtils {
	private static Logger logger = LoggerFactory.getLogger(ActionUtils.class);
	public static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * 从请求参数中获取字符串类型的参数
	 * @param request 请求
	 * @param parameterName 参数名称
	 * @return 成功返回字符串类型参数，失败返回null，空白字符串返回null。
	 */
	public static String getString(HttpServletRequest request, String parameterName) {
		return getString(request, parameterName, true);
	}

	/**
	 * 从请求参数中获取指定参数名称的参数值（字符串类型）
	 * @param request 请求
	 * @param parameterName 参数名
	 * @param ignoreEmptyText 是否忽略空白内容（如果获取到的内容为空串则返回null）
	 * @return 返回获取到的参数值（字符串类型）
	 */
	public static String getString(HttpServletRequest request, String parameterName, boolean ignoreEmptyText) {
		String parameter = request.getParameter(parameterName);
		if (ignoreEmptyText) {
			if (StrUtils.notEmpty(parameter)) {
				return parameter;
			}
			return null;
		}
		return parameter;
	}

	/**
	 * 从请求参数中获取Long类型的参数
	 * @param request 请求
	 * @param parameterName 参数名称
	 * @return 成功返回Long类型参数，失败返回null。
	 */
	public static Long getLong(HttpServletRequest request, String parameterName) {
		try {
			String parameter = getString(request, parameterName);
			if (StrUtils.notEmpty(parameter)) {
				return Long.parseLong(parameter);
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 从请求参数中获取Boolean类型的参数
	 * @param request 请求
	 * @param parameterName 参数名称
	 * @return 成功返回Boolean类型参数，失败返回null。
	 */
	public static Boolean getBoolean(HttpServletRequest request, String parameterName) {
		try {
			String parameter = getString(request, parameterName);
			if (StrUtils.notEmpty(parameter)) {
				return Boolean.parseBoolean(parameter);
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 从请求参数中获取Integer类型的参数
	 * @param request 请求
	 * @param parameterName 参数名称
	 * @return 成功返回Integer类型参数，失败返回null。
	 */
	public static Integer getInt(HttpServletRequest request, String parameterName) {
		try {
			String parameter = getString(request, parameterName);
			if (StrUtils.notEmpty(parameter)) {
				return Integer.parseInt(parameter);
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 从请求参数中获取Double类型的参数
	 * @param request 请求
	 * @param parameterName 参数名称
	 * @return 成功返回Double类型参数，失败返回null。
	 */
	public static Double getDouble(HttpServletRequest request, String parameterName) {
		try {
			String parameter = getString(request, parameterName);
			if (StrUtils.notEmpty(parameter)) {
				return Double.parseDouble(parameter);
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 从请求中获取字符串内容
	 * @param request 请求
	 * @param charset 字符集名称
	 * @return 成功返回请求的字符串，失败返回null。
	 */
	public static String getContentFromRequest(HttpServletRequest request, String charset) {
		String data = null;
		try {
			InputStream is = request.getInputStream();
			byte[] byteData = IOUtils.readBytes(is);
			if (byteData != null) {
				data = new String(byteData, charset);
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return data;
	}

	/**
	 * 从请求中获取字符串内容（UTF-8）
	 * @param request 请求
	 * @return 成功返回请求的字符串，失败返回null。
	 */
	public static String getContentFromRequest(HttpServletRequest request) {
		return getContentFromRequest(request, DEFAULT_CHARSET);
	}

	/**
	 * 输出字符串到远端（UTF-8）
	 * @param response 响应
	 * @param data 需要输出的内容
	 */
	public static void print(HttpServletResponse response, String data) {
		print(response, data, DEFAULT_CHARSET);
	}

	/**
	 * 输出字符串到远端
	 * @param response 响应
	 * @param data 需要输出的内容
	 * @param charset 字符集名称
	 */
	public static void print(HttpServletResponse response, String data, String charset) {
		print(response, data, charset, null);
	}

	/**
	 * 输出字符串到远端
	 * @param response 响应
	 * @param data 需要输出的内容
	 * @param charset 字符集名称
	 * @param contentType 数据类型
	 */
	public static void print(HttpServletResponse response, String data, String charset, String contentType) {
		response.setCharacterEncoding(charset);
		response.setBufferSize(8192);
		if (contentType != null && contentType.length() > 0) {
			response.setContentType(contentType);
		}
		try {
			PrintWriter writer = response.getWriter();
			writer.print(data);
			writer.flush();
			writer.close();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 从请求中获取所有的参数map
	 * @param request 请求
	 * @return 所有参数的map
	 */
	public static HashMap<String, String> getParams(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();
		Enumeration<?> paramNames = request.getParameterNames();
		if (paramNames != null) {
			while (paramNames.hasMoreElements()) {
				String paramName = (String) paramNames.nextElement();
				String[] paramValues = request.getParameterValues(paramName);
				if (paramValues != null && paramValues.length == 1) {
					map.put(paramName, paramValues[0]);
				}
			}
		}
		return map;
	}

	/**
	 * 获取本机ip
	 * @return
	 */
	public static String getLocalhostIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			throw new RuntimeException("get localhost IP failed", e);
		}
	}

	/**
	 * 获取当前网络ip
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request){
		String ipAddress = request.getHeader("x-forwarded-for");
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
				//根据网卡取本机配置的IP
				InetAddress inet=null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					logger.error(e.getMessage(), e);
				}
				ipAddress= inet.getHostAddress();
			}
		}
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
			if(ipAddress.indexOf(",")>0){
				ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

}
