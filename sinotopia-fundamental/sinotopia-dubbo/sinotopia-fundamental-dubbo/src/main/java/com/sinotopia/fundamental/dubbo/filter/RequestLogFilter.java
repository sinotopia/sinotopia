package com.sinotopia.fundamental.dubbo.filter;

import com.alibaba.dubbo.rpc.*;
import com.sinotopia.fundamental.api.params.SessionIdentity;
import com.sinotopia.fundamental.api.params.SessionParameter;
import com.sinotopia.fundamental.config.FundamentalConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>跟踪请求</p>
 *
 * @Author dzr
 * @Date 2016/6/18
 */
public class RequestLogFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(RequestLogFilter.class);
    public static final String HTTP_SERVLET_RESPONSE = "javax.servlet.http.HttpServletResponse";
    public static final String HTTP_SERVLET_REQUEST = "javax.servlet.http.HttpServletRequest";
    private final static String REQUEST_SEQ = "REQUEST_SEQ";
    private final static String MDC_UUID = "UUID";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        boolean isNew = false;
        boolean isClear = false;
        StringBuilder infoMark = new StringBuilder();
        String requestSEQ = null;
        String args = "none";
        try {
            args = PrintUtils.printArrayData(invocation.getArguments());
        } catch (Exception e) {
            logger.error("解析参数出现异常：", e);
        }
        this.checkUUID(invocation.getArguments());
        if (RpcContext.getContext().isConsumerSide()) {
            infoMark.append("CONSUME-").append(RpcContext.getContext().getLocalHost()).append(":").append(RpcContext.getContext().getLocalPort()).append(" -> ").append(RpcContext.getContext().getRemoteHost()).append(":").append(RpcContext.getContext().getRemotePort());
//            invoker.getUrl().getHost(), invoker.getUrl().getPort()
            //先从本地上下文取
            requestSEQ = LogContext.getContext().getRequestSeq();
            if (StringUtils.isEmpty(requestSEQ)) {
                requestSEQ = UUID.randomUUID().toString().replaceAll("-", "");
                LogContext.getContext().setRequestSeq(requestSEQ);
                MDC.put(REQUEST_SEQ, requestSEQ);
                logger.info("[" + infoMark + "] " + invoker.getInterface().getName() + "." + invocation.getMethodName() + ",Args:" + args + " ,New RequestSeq:" + requestSEQ);
                isNew = true;
                isClear = true;
            } else {
                logger.info("[" + infoMark + "] " + invoker.getInterface().getName() + "." + invocation.getMethodName() + ",Args:" + args + " ,Get RequestSeq:" + requestSEQ);
            }
            RpcContext.getContext().setAttachment(REQUEST_SEQ, requestSEQ);
        } else {
            infoMark.append("PROVIDER-").append(RpcContext.getContext().getRemoteHost()).append(":").append(RpcContext.getContext().getRemotePort()).append(" -> ").append(RpcContext.getContext().getLocalHost()).append(":").append(RpcContext.getContext().getLocalPort());
            isClear = true;
            requestSEQ = RpcContext.getContext().getAttachment(REQUEST_SEQ);
            if (StringUtils.isEmpty(requestSEQ)) {
                logger.info("[" + infoMark + "] " + invoker.getInterface().getName() + "." + invocation.getMethodName() + ",Args:" + args + " ,RequestSeq is empty!");
            } else {
                LogContext.getContext().setRequestSeq(requestSEQ);
                MDC.put(REQUEST_SEQ, requestSEQ);
                logger.info("[" + infoMark + "] " + invoker.getInterface().getName() + "." + invocation.getMethodName() + ",Args:" + args + " ,Get requestSeq :{}!", requestSEQ);
            }
        }
        if (invocation instanceof RpcInvocation) {
            ((RpcInvocation) invocation).setInvoker(invoker);
        }
        try {
            return invoker.invoke(invocation);
        } catch (Exception e) {
            logger.error("[" + infoMark + "] " + invoker.getInterface().getName() + "." + invocation.getMethodName() + ",Args:" + args + " ,Invoke Catch an Exception:", e);
            throw e;
        } finally {
            if (isClear) {
                MDC.remove(REQUEST_SEQ);
            }
            if (isNew) {
                MDC.remove(REQUEST_SEQ);
                LogContext.getContext().clear();
            }
            this.clearUUID();
            Map contextMap = MDC.getMDCAdapter().getCopyOfContextMap();
            if (contextMap != null && contextMap.isEmpty()) {
                MDC.clear();
            }
        }
    }

    private void checkUUID(Object[] arguments) {
        if (RpcContext.getContext().isProviderSide()) {
            if (arguments != null) {
                SessionParameter sessionParameter = null;
                for (Object arg : arguments) {
                    if (arg instanceof SessionParameter) {
                        sessionParameter = (SessionParameter) arg;//这里假设参数中只有一个会话参数
                        break;
                    }
                }
                if (sessionParameter != null) {
                    //获取会话
                    SessionIdentity sessionIdentity = sessionParameter.getSessionIdentity();
                    if (sessionIdentity != null) {
                        MDC.put(MDC_UUID, sessionIdentity.getUuid());
                    }
                }
            }
        }
    }

    private void clearUUID() {
        if (RpcContext.getContext().isProviderSide()) {
            if (MDC.get(MDC_UUID) != null) {
                MDC.remove(MDC_UUID);
            }
        }
    }

    /**
     * 日志上下文对象
     */
    public static class LogContext {

        private final static ThreadLocal<LogContext> threadLocal = new ThreadLocal<LogContext>() {
            protected LogContext initialValue() {
                return new LogContext();
            }
        };
        private String requestSeq;

        public String getRequestSeq() {
            return requestSeq;
        }

        public void setRequestSeq(String requestSeq) {
            this.requestSeq = requestSeq;
        }

        public static LogContext getContext() {
            return threadLocal.get();
        }

        public static void clear() {
            threadLocal.remove();
        }
    }


    public static class PrintUtils {
        private static Map<String, PrintType> printFilter = new HashMap<String, PrintType>();
        private static final Logger LOGGER = LoggerFactory.getLogger(PrintUtils.class);

        private static final String DEFAULT_ID_NO_LIST = "idno,idNo,idcard,idCard,cardno,cardNo,bankcardno,bankCardNo,identNumber,identNumber";
        private static final String DEFAULT_PASSWORD_LIST = "independentpassword,independentPassword,independentPassWord,password,password";
        private static final String DEFAULT_BIGDATA_LIST = "image,content,description,remark,message";

        static {

            String idNoList = FundamentalConfigProvider.getString(PropertiesKey.ID_NO_LIST, PrintUtils.DEFAULT_ID_NO_LIST);
            String passwordList = FundamentalConfigProvider.getString(PropertiesKey.PASSWORD_LIST, PrintUtils.DEFAULT_PASSWORD_LIST);
            String bigDataList = FundamentalConfigProvider.getString(PropertiesKey.BIGDATA_LIST, PrintUtils.DEFAULT_BIGDATA_LIST);
            if (!StringUtils.isEmpty(idNoList)) {
                String[] idNoArr = idNoList.split(",");
                for (String idNo : idNoArr) {
                    printFilter.put(idNo, PrintType.IDNO);
                }
            }
            if (!StringUtils.isEmpty(passwordList)) {
                String[] passwordArr = passwordList.split(",");
                for (String password : passwordArr) {
                    printFilter.put(password, PrintType.PASSWORD);
                }
            }
            if (!StringUtils.isEmpty(bigDataList)) {
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
            if (StringUtils.isEmpty(info)) {
                return "";
            } else {
                return info.replaceAll(".", "*");
            }
        }

        public static String printSensitiveFixLength(String info) {
            if (StringUtils.isEmpty(info)) {
                return "";
            } else {
                return "******";
            }
        }

        public static String printSensitiveKeepTerminal(String info) {
            return printSensitiveKeepTerminal(info, 50);
        }

        public static String printSensitiveKeepTerminal(String info, int maskPercentage) {
            if (StringUtils.isEmpty(info)) {
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
            StringBuilder sb = new StringBuilder();
            if (data instanceof String) {
                if (type == null) {
                    sb.append(data);
                } else {
                    switch (type) {
                        case IDNO:
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
            } else if (data instanceof Map) {
                sb.append(printMapData((Map<Object, Object>) data));
            } else if (data instanceof Collection) {
                sb.append(printListData((Collection<Object>) data));
            } else if (data instanceof Object[]) {
                sb.append(printArrayData((Object[]) data));
            } else if (isIgnoreParameter(data)) {
                sb.append(data);
            } else {
                sb.append(printObjectString(data));
            }
            return sb.toString();
        }

        /**
         * 判断是否是不需要再次遍历成员变量的打印对象
         *
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
            Class<?>[] interfaces = ClassUtils.getAllInterfaces(data);
            if (interfaces != null && interfaces.length > 0) {
                for (Class cls : interfaces) {
                    if (cls.getName().equals(HTTP_SERVLET_REQUEST) || cls.getName().equals(HTTP_SERVLET_RESPONSE)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private static String printMapData(Map<Object, Object> map) {
            Iterator<Map.Entry<Object, Object>> i = map.entrySet().iterator();
            if (!i.hasNext())
                return "{}";
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            for (; ; ) {
                Map.Entry<Object, Object> e = i.next();
                Object key = e.getKey();
                Object value = e.getValue();
                sb.append(printObjectData(key));
                sb.append('=');
                if (key instanceof String) {
                    sb.append(printObjectData(value, printFilter.get(key)));
                } else {
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
            for (; ; ) {
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
                for (int i = 0; i < data.length; i++) {
                    sb.append(printObjectData(data[i]));
                }
                sb.append("]");
            } else {
                sb.append("[]");
            }
            return sb.toString();
        }

        private static String printObjectString(Object data) {
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
                        if (method == null) {
                            continue;
                        }
                        if ("getClass".equals(method.getName())) {
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

    /**
     * <p>配置文件属性</p>
     *
     * @Author dzr
     * @Date 2016/4/22.
     */
    public interface PropertiesKey {

        public static final String ID_NO_LIST = "log.mask.field.idno.list";
        public static final String PASSWORD_LIST = "log.mask.field.password.list";
        public static final String BIGDATA_LIST = "log.mask.field.bigdata.list";
    }
}
