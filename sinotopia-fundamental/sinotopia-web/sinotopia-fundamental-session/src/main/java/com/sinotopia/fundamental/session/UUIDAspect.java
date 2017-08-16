package com.sinotopia.fundamental.session;

import com.sinotopia.fundamental.api.params.BaseParameter;
import com.sinotopia.fundamental.api.params.SessionIdentity;
import com.sinotopia.fundamental.api.params.SessionParameter;
import com.sinotopia.fundamental.common.utils.StrUtils;
import com.sinotopia.fundamental.servlet.utils.ActionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 获取sessionIdentity里面uuid放入MDC
 */
public class UUIDAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(UUIDAspect.class);

    private final static String MDC_UUID = "UUID";

    //用于处理用户会话，使用者可以自定义注入
    private SessionProcessor sessionProcessor;
    /**
     * 获取用户的uuid
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    public Object checkUuid(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return pjp.proceed();
        }
        Object returnValue = null;
        try {
            HttpServletRequest request = requestAttributes.getRequest();
            SessionParameter sessionParameter = null;
            for (Object arg : args) {
                if (arg instanceof BaseParameter) {
                    if (request != null) {
                        ((BaseParameter) arg).setClientIp(ActionUtils.getIpAddr(request));
                    }
                }
                if (arg instanceof SessionParameter) {
                    sessionParameter = (SessionParameter) arg;//这里假设参数中只有一个会话参数
                    break;
                }
            }

            if (sessionParameter != null) {
                String accessToken = getAccessToken();
                if (!StrUtils.isEmpty(accessToken)) {
                    //获取会话
                    SessionIdentity sessionIdentity = getSessionProcessor().process(sessionParameter, accessToken);
                    if (sessionIdentity != null) {
                        MDC.put(MDC_UUID, sessionIdentity.getUuid());
                        //设置会话对象
                        sessionParameter.setSessionIdentity(sessionIdentity);
                    }
                }
            }
            returnValue = pjp.proceed();
        } catch (Exception e) {
            LOGGER.error("Check uuid from request failed :", e);
        } finally {
            if (MDC.get(MDC_UUID) != null) {
                MDC.remove(MDC_UUID);
            }
            Map contextMap = MDC.getMDCAdapter().getCopyOfContextMap();
            if(contextMap != null && contextMap.isEmpty()){
                MDC.clear();
            }
        }
        return returnValue;
    }

    private String getAccessToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return SessionHandler.getAccessToken(request);
    }

    public SessionProcessor getSessionProcessor() {
        if (sessionProcessor == null) {
            sessionProcessor = new UniversalSessionProcessor();
        }
        return sessionProcessor;
    }

    public void setSessionProcessor(SessionProcessor sessionProcessor) {
        this.sessionProcessor = sessionProcessor;
    }
}
