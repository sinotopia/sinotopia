package com.hkfs.fundamental.logger.aspect;

import com.sinotopia.fundamental.api.data.Result;
import com.hkfs.fundamental.common.utils.StrUtils;
import com.hkfs.fundamental.config.FundamentalConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;


public class PrintUtils {
    private static Map<String, PrintType> printFilter = new HashMap<String, PrintType>();
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintUtils.class);
    public static final String HTTP_SERVLET_RESPONSE = "javax.servlet.http.HttpServletResponse";
    public static final String HTTP_SERVLET_REQUEST = "javax.servlet.http.HttpServletRequest";

    public static final String ID_NO_LIST = "log.mask.field.idno.list";
    public static final String PASSWORD_LIST = "log.mask.field.password.list";
    public static final String BIGDATA_LIST = "log.mask.field.bigdata.list";

    private static final String DEFAULT_ID_NO_LIST = "idno,idNo,idcard,idCard,cardno,cardNo,bankcardno,bankCardNo,identNumber,identNumber";
    private static final String DEFAULT_PASSWORD_LIST = "independentpassword,independentPassword,independentPassWord,password,password";
    private static final String DEFAULT_BIGDATA_LIST = "image,content,description,remark,message";

    static {

        String idNoList = FundamentalConfigProvider.getString(ID_NO_LIST, PrintUtils.DEFAULT_ID_NO_LIST);
        String passwordList = FundamentalConfigProvider.getString(PASSWORD_LIST, PrintUtils.DEFAULT_PASSWORD_LIST);
        String bigDataList = FundamentalConfigProvider.getString(BIGDATA_LIST, PrintUtils.DEFAULT_BIGDATA_LIST);
        if (!StrUtils.isEmpty(idNoList)) {
            String[] idNoArr = idNoList.split(",");
            for (String idNo : idNoArr) {
                printFilter.put(idNo, PrintType.IDNO);
            }
        }
        if (!StrUtils.isEmpty(passwordList)) {
            String[] passwordArr = passwordList.split(",");
            for (String password : passwordArr) {
                printFilter.put(password, PrintType.PASSWORD);
            }
        }
        if (!StrUtils.isEmpty(bigDataList)) {
            String[] bigDatArr = bigDataList.split(",");
            for (String bigData : bigDatArr) {
                printFilter.put(bigData, PrintType.BIGDATA);
            }
        }
    }

    public static void putPrintFilter(String fildName, PrintType type) {
        printFilter.put(fildName, type);
    }


    public static String printSensitiveRealLength(String info) {
        if (StrUtils.isEmpty(info)) {
            return "";
        } else {
            return info.replaceAll(".", "*");
        }
    }

    public static String printSensitiveFixLength(String info) {
        if (StrUtils.isEmpty(info)) {
            return "";
        } else {
            return "******";
        }
    }

    public static String printSensitiveKeepTerminal(String info) {
        return printSensitiveKeepTerminal(info, 50);
    }

    public static String printSensitiveKeepTerminal(String info, int maskPercentage) {
        if (StrUtils.isEmpty(info)) {
            return "";
        } else {
            if (maskPercentage > 100) {
                maskPercentage = 100;
            } else if (maskPercentage < 0) {
                maskPercentage = 0;
            }
            int fullLength = info.length();
            int maskLength = fullLength * maskPercentage / 100;
            if (0 == maskLength && maskPercentage > 0) {
                maskLength = 1;
            }
            int plainLength = fullLength - maskLength;
            int plainHalfLength = plainLength / 2;
            if (0 == plainHalfLength && maskPercentage < 100) {
                plainHalfLength = 1;
            }
            int maskStart = plainHalfLength;
            int maskEnd = maskStart + maskLength;
            if (maskEnd > fullLength) {
                maskEnd = fullLength;
            }
            StringBuilder sBuilder = new StringBuilder(info);
            for (int i = maskStart; i <= maskEnd; i++) {
                sBuilder.setCharAt(i, '*');
            }
            return sBuilder.toString();
        }
    }

    public static String printBigDataLength(String data) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("(length=");
        if (null == data) {
            sBuilder.append(0);
        } else {
            sBuilder.append(data.length());
        }
        sBuilder.append(')');
        return sBuilder.toString();
    }

    public static String printPwd(String pwd) {
        return printSensitiveFixLength(pwd);
    }

    public static String printIdNo(String idNo) {
        return printSensitiveKeepTerminal(idNo);
    }

    public static String printBankCardNo(String bankCardNo) {
        return printSensitiveKeepTerminal(bankCardNo);
    }

    @SuppressWarnings("unchecked")
	public static String printObjectData(Object data) {
    	return printObjectData(data, null);
    }

    public static String printObjectData(String name, Object data) {
    	return printObjectData(data, printFilter.get(name));
    }

    private static String printObjectData(Object data, PrintType type) {
        if (data == null) {
            return "";
        }

    	StringBuilder sb = new StringBuilder();
    	if (data instanceof String){
    		if (type == null) {
    			sb.append(data);
    		}else{
    			switch (type){
    				case IDNO :
    					 sb.append(printIdNo(String.valueOf(data)));
    					 break;
    				case PASSWORD:
    					sb.append(printPwd(String.valueOf(data)));
    					break;
    				case BIGDATA:
    					sb.append(printBigDataLength(String.valueOf(data)));
    					break;
    				default:
    					sb.append(String.valueOf(data));
    			}
    		}
    	}
    	else if (data instanceof Map) {
    		sb.append(printMapData((Map<Object, Object>)data));
    	}
    	else if (data instanceof Collection){
    		sb.append(printListData((Collection<Object>)data));
    	}
    	else if (data instanceof Object[]){
    		sb.append(printArrayData((Object[])data));
        }
        else if (isIgnoreParameter(data)) {
            sb.append(data);
        }
        else if (data instanceof Result) {
            sb.append(printResult((Result)data));
        }
        else {
            sb.append(printObjectString(data));
        }
        return sb.toString();
    }

    private static String printResult(Result result) {
        HashSet<String> ignoreMethods = new HashSet<String>(4);
        ignoreMethods.add("isSuccess");
        ignoreMethods.add("isFailed");
        ignoreMethods.add("getDataObject");
        ignoreMethods.add("getDataList");
        return printObjectString(result, ignoreMethods);
    }

    /**
     * 判断是否是不需要再次遍历成员变量的打印对象
     * @param data
     * @return
     */
    private static boolean isIgnoreParameter(Object data) {
        if (data == null) {
            return true;
        }
        if (data instanceof Number
                || data instanceof Boolean
                || data instanceof Date
                || data instanceof Enum) {
            return true;
        }

        Class cls = data.getClass();
        if (cls.getName().equals(HTTP_SERVLET_REQUEST) || cls.getName().equals(HTTP_SERVLET_RESPONSE)) {
            return true;
        }
        //ClassUtils.getAllInterfaces这个方法遇到某些类会OutOfMemory
//        Class<?>[] interfaces = ClassUtils.getAllInterfaces(data);
//        if (interfaces != null && interfaces.length > 0) {
//            for (Class cls : interfaces) {
//                if (cls.getName().equals(HTTP_SERVLET_REQUEST) || cls.getName().equals(HTTP_SERVLET_RESPONSE)) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    private static String printMapData(Map<Object, Object> map) {
		Iterator<Entry<Object, Object>> i = map.entrySet().iterator();
		if (!i.hasNext())
			return "{}";
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (;;) {
			Entry<Object, Object> e = i.next();
			Object key = e.getKey();
			Object value = e.getValue();
			sb.append(printObjectData(key));
			sb.append('=');
			if (key instanceof String) {
				sb.append(printObjectData(value, printFilter.get(key)));
			}
			else{
				sb.append(printObjectData(value));
			}
			if (!i.hasNext())
				return sb.append('}').toString();
			sb.append(',').append(' ');
		}
    }

    private static String printListData(Collection<Object> data) {
		Iterator<Object> it = data.iterator();
		if (!it.hasNext())
			return "[]";

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (;;) {
			Object e = it.next();
			sb.append(printObjectData(e));
			if (!it.hasNext())
				return sb.append(']').toString();
			sb.append(',').append(' ');
		}
    }

    public static String printArrayData(Object[] data) {
    	StringBuilder sb = new StringBuilder();
    	if (data != null) {
    		sb.append("[");
    		for(int i=0; i<data.length; i++) {
    			sb.append(printObjectData(data[i]));
    		}
    		sb.append("]");
    	}else{
    		sb.append("[]");
    	}
    	return sb.toString();
    }

    private static String printObjectString(Object data) {
        return printObjectString(data, null);
    }
    private static String printObjectString(Object data, HashSet<String> ignoreMethods) {
        if (data == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        BeanInfo beanInfo;
        try {
            sb.append(data.getClass().getSimpleName()).append(" [");
            beanInfo = Introspector.getBeanInfo(data.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            if (pds != null) {
                for (PropertyDescriptor pd : pds) {
                    Method method = pd.getReadMethod();
                    if(method == null){
                        continue;
                    }
                    if (pd.getWriteMethod() == null) {
                        continue;
                    }
                    if ("getClass".equals(method.getName())) {
                        continue;
                    }
                    if (ignoreMethods != null && ignoreMethods.contains(method.getName())) {
                        continue;
                    }
                    String name = pd.getName();
                    Object value = method.invoke(data);
                    sb.append(name).append("=")
                            .append(PrintUtils.printObjectData(name, value))
                            .append(",");
                }
            }
            sb.append("]");
        } catch (Exception e) {
            return String.valueOf(data);
        }
        return sb.toString();
    }

	public static void printComponentLoaded(Logger logger, String componentName) {
		if (null == logger) {
			logger = LOGGER;
		}
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + componentName + " loaded");
	}

    public enum PrintType {
    	 PASSWORD,
    	 BIGDATA,
    	 IDNO,
    }


}
