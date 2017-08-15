package com.sinotopia.fundamental.common.assist.file;

import com.sinotopia.fundamental.common.assist.CodeMocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基本行文本处理器
 * Created by zhoubing on 2016/12/14.
 */
public abstract class BasicFileLineProcessor implements FileLineProcessor {
    private static Logger logger = LoggerFactory.getLogger(CodeMocker.class);

    /**
     * 读取异常
     * @param e
     */
    public void onException(Exception e) {
        logger.error(e.getMessage(), e);
    }
}
