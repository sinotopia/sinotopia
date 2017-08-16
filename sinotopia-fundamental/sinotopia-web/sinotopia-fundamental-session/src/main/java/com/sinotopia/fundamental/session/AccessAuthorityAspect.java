package com.sinotopia.fundamental.session;

import com.sinotopia.fundamental.api.params.SessionParameter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 接口访问权限切面
 * Created by brucezee on 2017/2/8.
 */
public class AccessAuthorityAspect {
    private Logger logger = LoggerFactory.getLogger(AccessAuthorityAspect.class);

    public AccessAuthorityAspect() {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + AccessAuthorityAspect.class.getName() + " loaded");
    }

    //用于处理用户会话，使用者可以自定义注入
    private UserAuthorityChecker userAuthorityChecker;
    
    public Object auth(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null){
            return pjp.proceed();
        }
        SessionParameter sessionParameter = null;
        for (Object arg : args) {
            if (arg instanceof SessionParameter) {
                sessionParameter = (SessionParameter) arg;//这里假设参数中只有一个会话参数
                break;
            }
        }

        if (sessionParameter != null) {
            getUserAuthorityChecker().check(pjp, sessionParameter);
        }

        return pjp.proceed();
    }

    public UserAuthorityChecker getUserAuthorityChecker() {
        if (userAuthorityChecker == null) {
            userAuthorityChecker = new BaseUserAuthorityChecker();
        }
        return userAuthorityChecker;
    }

    public void setUserAuthorityChecker(UserAuthorityChecker userAuthorityChecker) {
        this.userAuthorityChecker = userAuthorityChecker;
    }
}
