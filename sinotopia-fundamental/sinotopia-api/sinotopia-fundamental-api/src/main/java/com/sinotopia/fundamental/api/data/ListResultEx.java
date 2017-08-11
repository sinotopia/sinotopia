package com.sinotopia.fundamental.api.data;

import com.sinotopia.fundamental.api.enums.BizRetCode;

import java.util.List;

public class ListResultEx<T> extends ResultEx {
	private static final long serialVersionUID = 1L;
	//总记录数
	private Integer totalCount;
	//页码
	private Integer pageNo;
	//每页数量
	private Integer pageSize;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@SuppressWarnings("unchecked")
	public List<T> getDataList() {
		Object data = getData();
		if (data != null && data instanceof List<?>) {
			return (List<T>)data;
		}
		return null;
	}

	public void setDataList(List<T> list) {
		super.setData(list);
	}

	@Override
	@Deprecated
	@SuppressWarnings("unchecked")
	public void setData(Object value) {
		try {
			List<T> t = (List<T>) value;
			super.setData(t);
		}
		catch (Throwable e) {
			throw new IllegalArgumentException("parameter type error!");
		}
	}

	public ListResultEx<T> makeResult(Integer error, String message, List<T> list) {
		super.makeResult(error, message, list);
		return this;
	}

	public ListResultEx<T> makeResult(Integer error, String message) {
		return makeResult(error, message, null);
	}

	public ListResultEx<T> makeResult(BizRetCode error, String message) {
		return makeResult(error.getCode(), message, null);
	}

	public ListResultEx<T> makeResult(Integer error) {
		return makeResult(error, null, null);
	}

	@SuppressWarnings("unchecked")
	public ListResultEx<T> makeResult(Result result) {
		return makeResult(result.getRetCode(), result.getRetMsg(), (List<T>) result.getData());
	}

	public ListResultEx<T> makeParameterErrorResult() {
		return makeResult(BizRetCode.PARAMETER_ERROR);
	}

	public ListResultEx<T> makeParameterErrorResult(String message) {
		return makeResult(BizRetCode.PARAMETER_ERROR, message);
	}

	public ListResultEx<T> makeAuthorizationErrorResult() {
		return makeResult(BizRetCode.AUTHORIZATION_ERROR);
	}

	public ListResultEx<T> makeAuthorizationErrorResult(String message) {
		return makeResult(BizRetCode.AUTHORIZATION_ERROR, message);
	}

	public ListResultEx<T> makeSuccessResult() {
		return makeResult(BizRetCode.SUCCESS);
	}

	public ListResultEx<T> makeFailedResult() {
		return makeResult(BizRetCode.FAILED);
	}

	public ListResultEx<T> makeFailedResult(String message) {
		return makeResult(BizRetCode.FAILED.getCode(), message, null);
	}

	public ListResultEx<T> makeResult(BizRetCode retCode) {
		return makeResult(retCode.getCode(), retCode.getDescription(), null);
	}
}
