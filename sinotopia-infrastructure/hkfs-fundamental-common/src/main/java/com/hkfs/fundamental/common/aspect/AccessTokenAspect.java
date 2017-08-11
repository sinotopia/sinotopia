package com.hkfs.fundamental.common.aspect;

import com.hkfs.fundamental.api.enums.BizRetCode;
import com.hkfs.fundamental.api.params.BaseParameter;
import com.hkfs.fundamental.api.params.SessionIdentity;
import com.hkfs.fundamental.api.params.SessionParameter;
import com.hkfs.fundamental.common.handler.SessionHandler;
import com.hkfs.fundamental.common.utils.NetworkUtil;
import com.hkfs.fundamental.common.utils.StrUtils;
import com.hkfs.fundamental.exception.HkfsBizException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class AccessTokenAspect {
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
						((BaseParameter) arg).setClientIp(NetworkUtil.getIpAddr(request));
					}
				}
			}
			if (arg instanceof SessionParameter) {
				sessionParameter = (SessionParameter) arg;//这里假设参数中只有一个会话参数
				break;
			}
		}

		if (sessionParameter != null) {
			if(sessionParameter.getSessionIdentity() == null){
				String accessToken = getAccessToken();
				if (StrUtils.isEmpty(accessToken)) {
					throw new HkfsBizException(BizRetCode.NOT_LOGIN_ERROR.getCode(), BizRetCode.NOT_LOGIN_ERROR.getDescription());
				}
				//获取会话
				SessionIdentity sessionIdentity = SessionHandler.getSessionIdentity(accessToken);
				if (sessionIdentity == null) {
					throw new HkfsBizException(BizRetCode.NOT_LOGIN_ERROR.getCode(), BizRetCode.NOT_LOGIN_ERROR.getDescription());
				}
				//设置会话对象
				sessionParameter.setSessionIdentity(sessionIdentity);
			}
		}

		return pjp.proceed();
	}

	private void processArgs(Object arg){

	}
	private String getAccessToken() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		return SessionHandler.getAccessToken(request);
	}
}
