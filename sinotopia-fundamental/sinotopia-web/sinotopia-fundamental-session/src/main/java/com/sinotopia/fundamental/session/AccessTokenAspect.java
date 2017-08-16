package com.sinotopia.fundamental.session;

import com.sinotopia.fundamental.api.enums.ResultCode;
import com.sinotopia.fundamental.api.params.BaseParameter;
import com.sinotopia.fundamental.api.params.SessionIdentity;
import com.sinotopia.fundamental.api.params.SessionParameter;
import com.sinotopia.fundamental.common.utils.StrUtils;
import com.sinotopia.fundamental.servlet.utils.ActionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 拦截客户端传递的token，并将token对应的用户信息赋值给基类相应字段
 */
public class AccessTokenAspect {
	private Logger logger = LoggerFactory.getLogger(AccessTokenAspect.class);

	public AccessTokenAspect() {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + AccessTokenAspect.class.getName() + " loaded");
	}

	//用于处理用户会话，使用者可以自定义注入
	private SessionProcessor sessionProcessor;

	public Object token(ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(requestAttributes == null){
			return pjp.proceed();
		}
		HttpServletRequest request = requestAttributes.getRequest();
		SessionParameter sessionParameter = null;
		for (Object arg : args) {
			if(arg instanceof BaseParameter){
				BaseParameter baseParameter = (BaseParameter) arg;
				if(StrUtils.isEmpty(baseParameter.getClientIp())){
					if(request != null) {
						((BaseParameter) arg).setClientIp(ActionUtils.getIpAddr(request));
					}
				}
			}
			if (arg instanceof SessionParameter) {
				sessionParameter = (SessionParameter) arg;//这里假设参数中只有一个会话参数
				break;
			}
		}

		if (sessionParameter != null) {
			//防止前端注入，强制覆盖
			String accessToken = SessionHandler.getAccessToken(request);
			if (StrUtils.isEmpty(accessToken)) {
				throw new com.sinotopia.fundamental.exception.ApplicationBizException(ResultCode.NOT_LOGIN_ERROR.getCode(), ResultCode.NOT_LOGIN_ERROR.getDescription());
			}

			//获取会话
			SessionIdentity sessionIdentity = getSessionProcessor().process(sessionParameter, accessToken);
			if (sessionIdentity == null) {
				throw new com.sinotopia.fundamental.exception.ApplicationBizException(ResultCode.NOT_LOGIN_ERROR.getCode(), ResultCode.NOT_LOGIN_ERROR.getDescription());
			}

			//设置会话对象
			sessionParameter.setSessionIdentity(sessionIdentity);
		}

		return pjp.proceed();
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
