package com.sinotopia.fundamental.codegenerator.io;

import com.sinotopia.fundamental.codegenerator.basis.global.Consts;
import com.sinotopia.fundamental.codegenerator.utils.CodeUtils;
import com.sinotopia.fundamental.codegenerator.utils.StrUtils;

public class PathBuilder {
	private String root;
	private String path;
	private String packageName;
	private String className;
	private String extension = Consts.FILE_EXTENSION;
	private String fileName;
	public static PathBuilder newInstance(String root) {
		return new PathBuilder(root);
	}
	public PathBuilder(String root) {
		this.root = root;
	}
	public PathBuilder setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}
	public PathBuilder setClassName(String className) {
		this.className = className;
		return this;
	}
	public PathBuilder setExtension(String extension) {
		this.extension = extension;
		return this;
	}
	public PathBuilder setPackageName(String packageName) {
		this.packageName = packageName;
		return this;
	}
//	public PathBuilder setClass(AbsClass cls) {
//		return setPackageName(CodeUtils.getPackageNameFromFullClassName(cls.fullClassName))
//				.setClassName(CodeUtils.getClassNameFromFullClassName(cls.fullClassName))
//				.setExtension(".java");
//	}
	public PathBuilder setOutputable(Outputable out) {
		return setPackageName(out.getPackageName())
				.setClassName(out.processOutputFileName())
				.setExtension(formatExtension(out.processOutputExtensionName()));
	}
	public PathBuilder setPath(String path) {
		this.path = path;
		return this;
	}
	
	public String build() {
		StringBuilder sb = new StringBuilder();
		sb.append(formatRoot(root));
		sb.append(processFileName());
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return build();
	}
	
	private String formatRoot(String root) {
		if (root != null && !root.endsWith("/") && !root.endsWith("\\")) {
			return root+"/";
		}
		return root;
	}
	
	private String processFileName() {
		StringBuilder sb = new StringBuilder();
		if (path != null) {
			sb.append(formatPath(path));
		}
		else {
			if (StrUtils.notEmpty(packageName)) {
				sb.append(CodeUtils.getPathFromPackageName(packageName));
			}
		}
		if (StrUtils.notEmpty(className)) {
			sb.append(className);
			sb.append(formatExtension(extension));
		}
		if (StrUtils.notEmpty(fileName)) {
			sb.append(fileName);
		}
		return sb.toString();
	}
	
	private String formatPath(String path) {
		if (path != null) {
			return path + "/";
		}
		return null;
	}
	
	private String formatExtension(String extension) {
		if (extension != null) {
			return extension.startsWith(".") ? extension : "."+extension;
		}
		return null;
	}
	
	public static String build(String root, String packageName) {
		return root+"/"+packageName.replace(".", "/")+"/";
	}
}
