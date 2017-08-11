package com.sinotopia.fundamental.api.data;

import com.sinotopia.fundamental.api.enums.BizRetCode;

public class ObjectResultEx<T> extends ResultEx {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public T getDataObject() {
		Object data = getData();
		if (data != null) {
			return (T)data;
		}
		return null;
	}

	public void setDataObject(T object) {
		super.setData(object);
	}

	@Override
	@Deprecated
	@SuppressWarnings("unchecked")
	public void setData(Object value) {
		try {
			T t = (T) value;
			super.setData(t);
		}
		catch (Throwable e) {
			throw new IllegalArgumentException("parameter type error!");
		}
	}

	public ObjectResultEx<T> makeResult(Integer error, String message, Object data) {
		super.makeResult(error, message, data);
		return this;
	}

	public ObjectResultEx<T> makeResult(Integer error, String message) {
		return makeResult(error, message, null);
	}

	public ObjectResultEx<T> makeResult(BizRetCode retCode, String message) {
		return makeResult(retCode.getCode(), message, null);
	}

	public ObjectResultEx<T> makeResult(Integer error) {
		return makeResult(error, null, null);
	}

	@SuppressWarnings("unchecked")
	public ObjectResultEx<T> makeResult(Result result) {
		return makeResult(result.getRetCode(), result.getRetMsg(), (T) result.getData());
	}

	public ObjectResultEx<T> makeParameterErrorResult() {
		return makeResult(BizRetCode.PARAMETER_ERROR);
	}

	public ObjectResultEx<T> makeParameterErrorResult(String message) {
		return makeResult(BizRetCode.PARAMETER_ERROR, message);
	}

	public ObjectResultEx<T> makeAuthorizationErrorResult() {
		return makeResult(BizRetCode.AUTHORIZATION_ERROR);
	}

	public ObjectResultEx<T> makeAuthorizationErrorResult(String message) {
		return makeResult(BizRetCode.AUTHORIZATION_ERROR, message);
	}

	public ObjectResultEx<T> makeSuccessResult() {
		return makeResult(BizRetCode.SUCCESS);
	}

	public ObjectResultEx<T> makeFailedResult() {
		return makeResult(BizRetCode.FAILED);
	}

	public ObjectResultEx<T> makeFailedResult(String message) {
		return makeResult(BizRetCode.FAILED.getCode(), message, null);
	}

	public ObjectResultEx<T> makeResult(BizRetCode retCode) {
		return makeResult(retCode.getCode(), retCode.getDescription(), null);
	}
}
