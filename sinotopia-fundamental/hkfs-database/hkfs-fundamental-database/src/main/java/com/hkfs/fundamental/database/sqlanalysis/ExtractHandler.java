package com.hkfs.fundamental.database.sqlanalysis;

/**
 * Created by zhoubing on 2016/12/15.
 */
public interface ExtractHandler {
    /**
     * 从文本中获取sql语句
     * @param text
     * @return
     */
    public String getPreparing(String text);

    /**
     * 获取参数文本
     * @param text
     * @return
     */
    public String getParameters(String text);
}
