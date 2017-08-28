package com.sinotopia.fundamental.logger.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private static final String HTTP_SERVLET_RESPONSE = "javax.servlet.http.HttpServletResponse";
    private static final String HTTP_SERVLET_REQUEST = "javax.servlet.http.HttpServletRequest";
    public static final String FORMAT_KV = "kv";
    public static final String FORMAT_JSON = "json";

    private String from = "client";

    private String local = "api";

    //对象日志输出格式
    private String format = FORMAT_KV;

    private String inMark;

    private String outMark;

    public static void main(String[] args) {
        System.out.println(String.class.getName());
    }

    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        //切面如果出现异常不能影响正常的业务逻辑
        logBeforeSafe(pjp);

        Object result = null;
        try {
            result = pjp.proceed();
        } finally {
            logAfterSafe(pjp, result);
        }
        return result;
    }

    private void logAfterSafe(ProceedingJoinPoint pjp, Object result) {
        try {
            logAfter(pjp, result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void logAfter(ProceedingJoinPoint pjp, Object result) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String methodName = pjp.getTarget().getClass().getSimpleName() + "." + methodSignature.getMethod().getName();
        log(getOutMark(), methodName, result);
    }

    private void logBeforeSafe(ProceedingJoinPoint pjp) {
        try {
            logBefore(pjp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void logBefore(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String[] paramNames = methodSignature.getParameterNames();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (paramNames != null && paramNames.length > 0) {
            for (int i = 0; i < paramNames.length; i++) {
                if (i < args.length && paramNames[i] != null && args[i] != null) {
                    String paramName = paramNames[i];
                    if (args[i].getClass().getName().equals(HTTP_SERVLET_RESPONSE)) {
                        continue;
                    }
                    if (args[i].getClass().getName().equals(HTTP_SERVLET_REQUEST)) {
                        continue;
                    }
                    paramMap.put(paramName, args[i]);
                }
            }
        }
        String methodName = pjp.getTarget().getClass().getSimpleName() + "." + methodSignature.getMethod().getName();
        log(getInMark(), methodName, paramMap);
    }

    private void log(String mark, String methodName, Object data) {
        if (FORMAT_JSON.equals(format)) {
            logger.info(mark + " " + methodName + " " + JSON.toJSONString(data));
        } else if (FORMAT_KV.equals(format)) {
            logger.info(mark + " " + methodName + " " + PrintUtils.printObjectData(data));
        } else {
            logger.info(mark + " " + methodName + " " + PrintUtils.printObjectData(data));
        }
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        if (null == from) {
            from = "";
        }
        this.from = from;
        inMark = null;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        if (null == local) {
            local = "";
        }
        this.local = local;
        inMark = null;
    }

    public String getInMark() {
        if (null == inMark) {
            inMark = '[' + from + "->" + local + ']';
        }
        return inMark;
    }

    public void setInMark(String inMark) {
        this.inMark = inMark;
    }

    public String getOutMark() {
        if (null == outMark) {
            outMark = '[' + from + "<-" + local + ']';
        }
        return outMark;
    }

    public void setOutMark(String outMark) {
        this.outMark = outMark;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LoggerAspect [from=").append(from).append(", local=").append(local).append(", inMark=")
                .append(inMark).append(", outMark=").append(outMark).append("]");
        return builder.toString();
    }
}
