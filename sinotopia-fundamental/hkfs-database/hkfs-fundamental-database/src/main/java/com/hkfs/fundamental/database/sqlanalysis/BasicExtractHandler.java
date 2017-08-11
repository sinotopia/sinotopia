package com.hkfs.fundamental.database.sqlanalysis;

import com.hkfs.fundamental.common.utils.StrUtils;

/**
 * 默认的sql及参数获取处理
 * Created by zhoubing on 2016/12/15.
 */
public class BasicExtractHandler implements ExtractHandler {
    @Override
    public String getPreparing(String text) {
        return StrUtils.getMiddleText(text, "Preparing: ", null);
    }

    @Override
    public String getParameters(String text) {
        return StrUtils.getMiddleText(text, "Parameters: ", null);
    }
}
