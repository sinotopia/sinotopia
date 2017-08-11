package com.hkfs.fundamental.codegenerator.io;

import com.hkfs.fundamental.codegenerator.basis.data.*;
import com.hkfs.fundamental.codegenerator.basis.data.Class;
import com.hkfs.fundamental.codegenerator.basis.global.Config;
import com.hkfs.fundamental.codegenerator.utils.FileUtils;

public class CodeInputer {
	public String filePath;
	public CodeInputer(String filePath) {
		this.filePath = filePath;
	}
	
	public static CodeInputer newInstance(String filePath) {
		return new CodeInputer(filePath);
	}
	
	public static CodeInputer newInstance(PathBuilder path) {
		return new CodeInputer(path.build());
	}
	
	public String read() {
		return FileUtils.getStringFromFile(filePath);
	}
	
	public static void main(String[] args) {
		Config.addFullClass("com.brucezee.codegenerator.basis.data.base.AbsFormat");
		
		Class cls = new Class("com.brucezee.codegenerator.io.CodeOutputer3");
		cls.addField(new Field("filePath", "String", "输出文件路径"));
		cls.addField(new Field("content", "String", "需要输出的内容"));
		cls.addConstructor(new Constructor(cls, cls.fields));
		cls.addMethod(new Method("output", "boolean", "输出指定内容到指定文件"));
		
		cls.addMethod(new Method("output", "boolean", new Param("com.brucezee.codegenerator.basis.data.base.AbsFormat", "format")));
		System.out.println(cls);
		
		String root = "F:/works/MyWorks/BruceZeeCodeGenerator2/src";
		PathBuilder path = PathBuilder.newInstance(root).setOutputable(cls);
		CodeOutputer.newInstance(path, cls).output();
		
		System.out.println(PathBuilder.newInstance(root).setFileName("Hello.java"));
	}
}
