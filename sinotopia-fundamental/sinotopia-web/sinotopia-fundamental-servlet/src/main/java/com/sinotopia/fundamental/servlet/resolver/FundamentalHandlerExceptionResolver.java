package com.hkfs.fundamental.servlet.resolver;

import com.sinotopia.fundamental.api.enums.ResultCode;
import com.hkfs.fundamental.common.utils.StrUtils;
import com.hkfs.fundamental.servlet.utils.ActionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理
 * Created by zhoubing on 2016/6/16.
 */
public class FundamentalHandlerExceptionResolver implements HandlerExceptionResolver {
    private Logger logger = LoggerFactory.getLogger(FundamentalHandlerExceptionResolver.class);

    //异常后跳转地址
    private String redirectUrl;
    //异常后的错误码
    private String retCode;
    //异常后的错误消息
    private String retMsg;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.error(ex.getMessage(), ex);

        if (StrUtils.notEmpty(redirectUrl)) {
            return new ModelAndView(redirectUrl);
        }

        ActionUtils.print(response, processResultJson(ResultCode.COMMON_ERROR), StrUtils.UTF_8);
        return null;
    }

    private String processResultJson(ResultCode bizRetCode) {
        return processResultJson(""+bizRetCode.getCode(), bizRetCode.getDescription());
    }

    private String processResultJson(String retCode, String retMsg) {
        return "{\"retCode\":"+StrUtils.valueOf(retCode)+",\"retMsg\":\""+StrUtils.valueOf(retMsg)+"\"}";
    }
}
