package com.sinotopia.fundamental.common.utils;

import com.sinotopia.fundamental.common.assist.CodeMocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class PathUtils {
	private static Logger logger = LoggerFactory.getLogger(CodeMocker.class);
	public static void main(String[] args) {
		System.out.println(getOutputRoot(PathUtils.class));
		System.out.println(getProjectRoot(PathUtils.class, "build/classes"));
	}
	
	/** 
	 * 获取当前java类运行的根路径，一般为bin或classes目录
	 */
	public static String getOutputRoot(Class<?> cls) {
		String path = null;
		try {
			URL u = cls.getResource("");
			if (u == null) {
				return null;
			}
			
			String pkg = cls.getPackage().getName().replace(".", "/");
			path = u.getFile();
			if (path == null) {
				return null;
			}
			path = path
					.replace("file:/", "")
					.replace("!/"+pkg+"/", "")
					.replace("\\", "/")
					.replace("/"+pkg, "");
//					.replace("/bin/"+pkg, "")
//					.replace("/classes/"+pkg, "");
			int index = path.lastIndexOf("/");
			if (index > 0) {
				path = path.substring(0, index);
			}
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			if (!path.endsWith("/")) {
				path = path + "/";
			}
			path = path.replace("%20", " ");
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return path;
	}
	
	/**
	 * 获取指定类在运行时所在的项目路径根路径
	 * @param cls 类
	 * @param outputPath 项目输出目录，基于项目一般为bin或build/classes
	 * @return
	 */
	public static String getProjectRoot(Class<?> cls, String outputPath) {
		String outputRoot = getOutputRoot(cls);
		if (outputRoot != null) {
			return outputRoot.replace(outputPath.replace("\\", "/"), "").replace("//", "/");
		}
		return null;
	}
	
}
