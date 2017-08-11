package com.hkfs.fundamental.common.assist.file;

/**
 * 一行文本处理器
 * Created by zhoubing on 2016/12/14.
 */
public interface FileLineProcessor {
    /**
     * 处理行
     * @param line
     * @return 取消遍历返回false，继续遍历返回true
     */
    public boolean process(String line);

    /**
     * 读取异常
     * @param e
     */
    public void onException(Exception e);
}
