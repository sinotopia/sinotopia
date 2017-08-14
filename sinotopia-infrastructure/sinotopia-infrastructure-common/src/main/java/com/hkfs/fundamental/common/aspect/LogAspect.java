package com.hkfs.fundamental.common.aspect;

import com.alibaba.fastjson.JSON;
import com.hkfs.fundamental.common.utils.ActionUtils;
import com.hkfs.fundamental.common.utils.PrintUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dingjidai on 2015/12/23.
 */
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    public static final String FORMAT_KV = "kv";
    public static final String FORMAT_JSON = "json";


    private String from = "client";

    private String local = "api";

    //对象日志输出格式
    private String format = FORMAT_KV;

    private String inMark;

    private String outMark;

    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        MethodSignature methodSignature= (MethodSignature) pjp.getSignature();
        String[] paramNames = methodSignature.getParameterNames();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if(paramNames != null && paramNames.length > 0) {
            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                if (args[i] instanceof HttpServletResponse) {
                    continue;
                }
                if (args[i] instanceof HttpServletRequest) {
                    paramMap.putAll(ActionUtils.getParams((HttpServletRequest) args[i]));
                    continue;
                }
                paramMap.put(paramName, args[i]);
            }
        }
        String methodName = pjp.getTarget().getClass().getSimpleName()+"."+methodSignature.getMethod().getName();
        log(getInMark(), methodName, paramMap);
        Object result = null;
        try {
            result = pjp.proceed();
        } finally {
            log(getOutMark(), methodName, result);
        }
        return result;
    }

    private void log(String mark, String methodName, Object data) {
        if (FORMAT_JSON.equals(format)) {
            logger.info(mark +" "+methodName+" "+ JSON.toJSONString(data));
        }
        else if (FORMAT_KV.equals(format)) {
            logger.info(mark +" "+methodName+" "+ PrintUtils.printObjectData(data));
        }
        else {
            logger.info(mark +" "+methodName+" "+ PrintUtils.printObjectData(data));
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
