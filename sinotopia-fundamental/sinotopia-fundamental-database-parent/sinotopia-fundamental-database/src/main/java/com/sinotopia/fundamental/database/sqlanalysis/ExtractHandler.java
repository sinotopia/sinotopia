package com.sinotopia.fundamental.database.sqlanalysis;

/**
 */
public interface ExtractHandler {
    /**
     * 从文本中获取sql语句
     *
     * @param text
     * @return
     */
    String getPreparing(String text);

    /**
     * 获取参数文本
     *
     * @param text
     * @return
     */
    String getParameters(String text);
}
