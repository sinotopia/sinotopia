package com.sinotopia.fundamental.codegenerator.io;

import com.sinotopia.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.sinotopia.fundamental.codegenerator.basis.global.Config;
import com.sinotopia.fundamental.codegenerator.utils.FileUtils;

public class CodeOutputer {
	public String filePath;
	public String content;
	
	public CodeOutputer(String filePath, String content) {
		this.filePath = filePath;
		this.content = content;
	}
	public CodeOutputer(String filePath, AbsFormat format) {
		this.filePath = filePath;
		this.content = format.toString();
	}
	public CodeOutputer(PathBuilder builder, AbsFormat format) {
		this.filePath = builder.build();
		this.content = format.toString();
	}
	public CodeOutputer(PathBuilder builder, String content) {
		this.filePath = builder.build();
		this.content = content;
	}
	
	
	public static CodeOutputer newInstance(String filePath, String content) {
		return new CodeOutputer(filePath, content);
	}
	public static CodeOutputer newInstance(String filePath, AbsFormat format) {
		return new CodeOutputer(filePath, format);
	}
	public static CodeOutputer newInstance(PathBuilder builder, AbsFormat format) {
		return new CodeOutputer(builder, format);
	}
	public static CodeOutputer newInstance(PathBuilder builder, String content) {
		return new CodeOutputer(builder, content);
	}
	
	public boolean output() {
		if (Config.isLogEnabled) {
			System.out.println("[OUTPUT] "+filePath);
		}
		return FileUtils.outputStringToFile(filePath, content);
	}
}