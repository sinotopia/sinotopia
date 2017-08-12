package com.hkfs.fundamental.pay.spdb.util;


import com.hkfs.fundamental.pay.spdb.enums.ChannelType;
import com.hkfs.fundamental.pay.spdb.bean.ResponseWithDrawalsInfo;
import com.hkfs.fundamental.pay.spdb.bean.WithDrawalsInfo;
import com.hkfs.fundamental.pay.spdb.exception.WithDrawConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 浦发辅助类
 * 
 * @author liliang
 *
 */
public class HelperUtil {
	protected static final Logger logger = LoggerFactory.getLogger(HelperUtil.class);

	/**
	 * 产生随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int nextInt(final int min, final int max) {
		Random rand = new Random();
		int tmp = Math.abs(rand.nextInt());
		return tmp % (max - min + 1) + min;
	}

	/**
	 * 补位
	 * 
	 * @param number
	 *            最低数字
	 * @param num
	 *            最高数字
	 * @return
	 */
	public static String fillSeats(int number, int num) {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMinimumIntegerDigits(num);
		formatter.setGroupingUsed(false);
		String str = formatter.format(number);
		return str;
	}

	/**
	 * jaxb解析xml
	 * 
	 * @XmlAccessorType(XmlAccessType.FIELD) @XmlRootElement(name = "") 根节点
	 * @XmlElement(name = "") 子节点
	 * @XmlAttribute(name = "") 属性
	 * 
	 * @param clazz
	 * @param xml
	 * @return
	 */
	public static <T> T readXml(Class<T> clazz, String xml) {
		JAXBContext jaxbContext;
		try {
			// 加载映射bean类
			jaxbContext = JAXBContext.newInstance(clazz);
			// 创建解析
			Unmarshaller um = jaxbContext.createUnmarshaller();
			StringReader strReader = new StringReader(xml);
			StreamSource streamSource = new StreamSource(strReader);
			return (T) um.unmarshal(streamSource);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("jaxb解析xml:", e);
			// throw new Exception(e.getMessage());
		}
		return null;
	}

	/**
	 * date转String
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * String转Date
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date formatDate(String time, String pattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(time);
	}

	public static String map2Xml(String rootName, Map<String, String> map) {
		Iterator<String> lit = map.keySet().iterator();
		StringBuffer sb = new StringBuffer();
		String[] nodes = rootName.split("/");
		for (String node : nodes) {
			if (StringUtils.isNotBlank(node)) {
				sb.append(getXmlTag(node, false));
			}
		}

		while (lit.hasNext()) {
			String keyName = lit.next();
			String value = map.get(keyName);
			sb.append(getXmlTag(keyName, false));
			sb.append(value);
			sb.append(getXmlTag(keyName, true));
		}
		for (int i = nodes.length; i > 0; i--) {
			String node = nodes[i - 1];
			if (StringUtils.isNotBlank(node)) {
				sb.append(getXmlTag(node, true));
			}
		}
		return sb.toString();
	}

	public static String getXmlTag(String tagName, boolean isEnd) {
		StringBuffer sb = new StringBuffer();
		if (isEnd) {
			sb.append("</").append(tagName.toUpperCase()).append(">");
		} else {
			sb.append("<").append(tagName.toUpperCase()).append(">");
		}
		return sb.toString();
	}

	/**
	 * 计算时间相差天数
	 * 
	 * @param beforeDate
	 * @param afterDate
	 * @return
	 */
	public static int timeDifference(Date beforeDate, Date afterDate) {
		int day = 0;
		try {

			long diff = afterDate.getTime() - beforeDate.getTime();

			long days = diff / (1000 * 60 * 60 * 24);// 天
			// long hour=(diff/(60*60*1000)-days*24);//时
			// long min=((diff/(60*1000))-days*24*60-hour*60);//分
			// long s=(diff/1000-days*24*60*60-hour*60*60-min*60);//秒

			day = new Long(days).intValue();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("计算时间相差天数:", e);
		}
		return day;
	}

	/**
	 * 比较时间大小
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDate(Date date1, Date date2) {
		int num = 0;
		java.util.Calendar c1 = java.util.Calendar.getInstance();
		java.util.Calendar c2 = java.util.Calendar.getInstance();
		try {
			c1.setTime(date1);
			c2.setTime(date2);

			int result = c1.compareTo(c2);

			if (result == 0) {
				num = 0;// c1相等c2
			} else if (result < 0) {
				num = -1;// c1小于c2
			} else {
				num = 1;// c1大于c2
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("比较时间大小:", e);
		}
		return num;
	}

	// 根据网卡取本机配置的IP
	/**
	 * 获得主机IP
	 *
	 * @return String
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

	/**
	 * 获取本机ip地址，并自动区分Windows还是linux操作系统
	 * 
	 * @return String
	 */
	public static String getLocalIp() {
		String sIP = "";
		InetAddress ip = null;
		try {
			// 如果是Windows操作系统
			if (isWindowsOS()) {
				ip = InetAddress.getLocalHost();
			}
			// 如果是Linux操作系统
			else {
				boolean bFindIP = false;
				Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
						.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					if (bFindIP) {
						break;
					}
					NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
					// ----------特定情况，可以考虑用ni.getName判断
					// 遍历所有ip
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = (InetAddress) ips.nextElement();
						if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
								&& ip.getHostAddress().indexOf(":") == -1) {
							bFindIP = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取本机IP异常:"+e);
		}
		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		return sIP;
	}
	
	/**
	 * 获取发起请求的客户端IP信息
	 * 
	 * @param request
	 * @return 客户端IP信息
	 */
	public static String getRealIp(HttpServletRequest request){
		
		String ip = request.getHeader("X-Forwarded-For");
		
		if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		logger.info("Proxy-Client-IP:"+ip);
		
		if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		logger.info("WL-Proxy-Client-IP:"+ip);
		
		if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		logger.info("ip:"+ip);
		
		return ip;
	}
	
	/**
	 * 封装数据
	 * 
	 * @param transCode
	 * @param withDrawalsInfo
	 * @return
	 */
	public static  Map<String, Object> getMap(String transCode, WithDrawalsInfo withDrawalsInfo) {

		Map<String, Object> map = new HashMap<String, Object>();

		if (withDrawalsInfo != null) {

			map.put("transCode", transCode);
			map.put("timestamp", HelperUtil.formatDate(withDrawalsInfo.getTimestamp(), WithDrawConstants.SDF_LONG));// 交易时间
			map.put("packetId", withDrawalsInfo.getPacketId());
			map.put("withDrawalsInfo", withDrawalsInfo);

		} else {

			Date now = new Date();
			String packetId = HelperUtil.formatDate(now, WithDrawConstants.SDF_LONG_VARCHAR)
					+ String.valueOf(HelperUtil.nextInt(10000, 99999));// 报文号，当前时间+随机数

			if(StringUtils.isNotBlank(transCode)){
				map.put("transCode", transCode);
			}
			
			map.put("timestamp", HelperUtil.formatDate(now, WithDrawConstants.SDF_LONG));// 交易时间
			map.put("packetId", packetId);

		}
		
		map.put("masterId", WithDrawConstants.MASTERID);
		map.put("acctNo", WithDrawConstants.ACCTNO);
		map.put("acctName", WithDrawConstants.ACCTNAME);
		
		return map;
	}

}
