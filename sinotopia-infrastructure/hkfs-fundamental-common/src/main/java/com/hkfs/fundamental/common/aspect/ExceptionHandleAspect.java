package com.hkfs.fundamental.common.aspect;

import com.alibaba.fastjson.JSON;
import com.hkfs.fundamental.api.data.ListResultEx;
import com.hkfs.fundamental.api.data.ObjectResultEx;
import com.hkfs.fundamental.api.data.Result;
import com.hkfs.fundamental.api.data.ResultEx;
import com.hkfs.fundamental.api.enums.BizRetCode;
import com.hkfs.fundamental.exception.HkfsBizException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ExceptionHandleAspect {
	private final Logger logger = LoggerFactory.getLogger(ExceptionHandleAspect.class);

	public Object handleException(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();
		Class<?> returnType = method.getReturnType();

		Object result = null;
		try {
			result = pjp.proceed();
		} catch (HkfsBizException e) {
			return handleMethodReturnType(returnType, e.getRetCode(), e.getRetMsg());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return handleMethodReturnType(returnType, BizRetCode.COMMON_ERROR.getCode(), BizRetCode.COMMON_ERROR.getDescription());
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
	private Object handleMethodReturnType(Class<?> returnType, Integer retCode, String retMsg) {
		if (Result.class == returnType) {
			Result result = new Result();
			result.setRetCode(retCode);
			result.setRetMsg(retMsg);
			return result;
		}
		else if (ResultEx.class == returnType) {
			ResultEx result = new ResultEx();
			result.setRetCode(retCode);
			result.setRetMsg(retMsg);
			return result;
		}
		else if (ObjectResultEx.class == returnType) {
			ObjectResultEx result = new ObjectResultEx();
			result.setRetCode(retCode);
			result.setRetMsg(retMsg);
			return result;
		}
		else if (ListResultEx.class == returnType) {
			ListResultEx result = new ListResultEx();
			result.setRetCode(retCode);
			result.setRetMsg(retMsg);
			return result;
		}
		else if(String.class == returnType){
			//如果有其他类型的返回结果这里处理
			Result result = new Result();
			result.setRetCode(retCode);
			result.setRetMsg(retMsg);
			return JSON.toJSONString(result);
		}else{
			logger.error("the returnType["+returnType.getName()+"] is not support now!");
			return null;
		}
	}
}
