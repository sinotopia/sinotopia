package com.sinotopia.fundamental.api.data;


import com.sinotopia.fundamental.api.enums.ResultCode;

public class ResultEx extends Result {

    private static final long serialVersionUID = 1L;

    public boolean isSuccess() {
        Integer retCode = getRetCode();
        return retCode != null && retCode == ResultCode.SUCCESS.getCode();
    }

    public boolean isFailed() {
        return !isSuccess();
    }

    public ResultEx makeResult(Integer retCode, String retMsg, Object data) {
        if (retCode != null) {
            setRetCode(retCode);
        }
        if (retMsg != null) {
            setRetMsg(retMsg);
        }
        if (data != null) {
            setData(data);
        }
        return this;
    }

    public ResultEx makeResult(Integer retCode, String retMsg) {
        return makeResult(retCode, retMsg, null);
    }

    public ResultEx makeResult(ResultCode retCode, String retMsg) {
        return makeResult(retCode.getCode(), retMsg, null);
    }

    public ResultEx makeResult(Integer retCode) {
        return makeResult(retCode, null, null);
    }

    public ResultEx makeResult(Result result) {
        return makeResult(result.getRetCode(), result.getRetMsg(), result.getData());
    }

    public ResultEx makeParameterErrorResult() {
        return makeResult(ResultCode.PARAMETER_ERROR);
    }

    public ResultEx makeParameterErrorResult(String retMsg) {
        return makeResult(ResultCode.PARAMETER_ERROR, retMsg);
    }

    public ResultEx makeAuthorizationErrorResult() {
        return makeResult(ResultCode.AUTHORIZATION_ERROR);
    }

    public ResultEx makeAuthorizationErrorResult(String retMsg) {
        return makeResult(ResultCode.AUTHORIZATION_ERROR, retMsg);
    }

    public ResultEx makeSuccessResult() {
        return makeResult(ResultCode.SUCCESS);
    }

    public ResultEx makeFailedResult() {
        return makeResult(ResultCode.FAILED);
    }

    public ResultEx makeFailedResult(String retMsg) {
        return makeResult(ResultCode.FAILED.getCode(), retMsg, null);
    }

    public ResultEx makeResult(ResultCode retCode) {
        return makeResult(retCode.getCode(), retCode.getDescription(), null);
    }
}
