package com.hkfs.fundamental.codegenerator.recovery;

import com.hkfs.fundamental.codegenerator.basis.data.Class;

/**
 * 注释处理器
 * Created by zhoubing on 2016/5/25.
 */
public interface CommentProcessor {
    /**
     * 处理注释
     * @param cls 类
     * @param filePath 类文件路径
     */
    public void process(Class cls, String filePath);
}
