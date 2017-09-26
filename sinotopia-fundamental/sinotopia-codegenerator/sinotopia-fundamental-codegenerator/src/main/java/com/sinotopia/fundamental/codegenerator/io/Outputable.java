package com.sinotopia.fundamental.codegenerator.io;

public interface Outputable {
    /**
     * 获取文件名
     *
     * @return 文件名（不包含扩展名）
     */
    String processOutputFileName();

    /**
     * 获取文件扩展名
     *
     * @return 扩展名
     */
    String processOutputExtensionName();

    /**
     * 获取文s件包名
     */
    String getPackageName();
}
