package com.sinotopia.fundamental.servlet.base;

import com.alibaba.fastjson.JSON;
import com.sinotopia.fundamental.common.utils.StrUtils;
import com.sinotopia.fundamental.servlet.utils.ActionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Controller基类
 * Created by zhoubing on 2016/6/7.
 */
public class BaseController implements Serializable {
    /**
     * 从请求流中获取json字符串并解析成对象
     * @param request
     * @param cls
     * @param <T>
     * @return
     */
    protected <T> T getRequestObject(HttpServletRequest request, Class<T> cls) {
        String data = StrUtils.trim(ActionUtils.getContentFromRequest(request, StrUtils.UTF_8));
        if (StrUtils.isEmpty(data)) {
            return null;
        }
        if (!data.startsWith("{") || !data.endsWith("}")) {
            return null;
        }
        return JSON.parseObject(data, cls);
    }

    /**
     * 跳转
     * @param url
     * @return
     */
    protected String redirect(String url) {
        return "redirect:" + StrUtils.valueOf(url);
    }
}
