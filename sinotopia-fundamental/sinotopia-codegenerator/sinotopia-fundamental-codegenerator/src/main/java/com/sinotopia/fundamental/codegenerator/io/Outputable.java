package com.sinotopia.fundamental.codegenerator.io;

public interface Outputable {
	/**
	 * 获取文件名
	 * @return 文件名（不包含扩展名）
	 */
	public String processOutputFileName();
	/**
	 * 获取文件扩展名
	 * @return 扩展名
	 */
	public String processOutputExtensionName();
	/**
	 * 获取文件包名
	 */
	public String getPackageName();
}
