package com.hkfs.fundamental.exception.aspect;

import com.alibaba.fastjson.JSON;
import com.sinotopia.fundamental.api.data.Result;
import com.sinotopia.fundamental.api.enums.BizRetCode;
import com.hkfs.fundamental.exception.HkfsBizException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ExceptionHandleAspect {
	private final Logger logger = LoggerFactory.getLogger(ExceptionHandleAspect.class);

	public ExceptionHandleAspect() {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + ExceptionHandleAspect.class.getName() + " loaded");
	}

	public Object handleException(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();
		Class<?> returnType = method.getReturnType();

		Object result = null;
		try {
			result = pjp.proceed();
		} catch (HkfsBizException e) {
			return handleMethodReturnType(returnType, e.getRetCode(), e.getRetMsg(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return handleMethodReturnType(returnType, BizRetCode.COMMON_ERROR.getCode(), BizRetCode.COMMON_ERROR.getDescription(), e);
		}
		return result;
	}


	/**
	 * 不同类型的返回结果不同的处理
	 * @param returnType
	 * @param retCode
	 * @param retMsg
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Object handleMethodReturnType(Class<?> returnType, Integer retCode, String retMsg, Exception e) {
		if (Result.class.isAssignableFrom(returnType)) {
			try {
				Result result = (Result) returnType.newInstance();
				result.setRetCode(retCode);
				result.setRetMsg(retMsg);
				return result;
			} catch (Exception ex) {
				logger.error(ex.getMessage(), e);
			}
		}
		else if(String.class == returnType){
			//如果有其他类型的返回结果这里处理
			Result result = new Result();
			result.setRetCode(retCode);
			result.setRetMsg(retMsg);
			return JSON.toJSONString(result);
		}

		logger.error("the returnType[" + returnType.getName() + "] is not support now!");
		if (e != null) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
