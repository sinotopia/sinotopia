package com.sinotopia.fundamental.codegenerator.recovery;

import com.sinotopia.fundamental.codegenerator.basis.data.Clazz;

/**
 * 注释处理器
 */
public interface CommentProcessor {
    /**
     * 处理注释
     *
     * @param cls      类
     * @param filePath 类文件路径
     */
    void process(Clazz cls, String filePath);
}
