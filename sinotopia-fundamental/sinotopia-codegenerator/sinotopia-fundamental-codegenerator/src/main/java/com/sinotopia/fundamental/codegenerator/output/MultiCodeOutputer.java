package com.sinotopia.fundamental.codegenerator.output;

import com.sinotopia.fundamental.codegenerator.basis.data.Clazz;
import com.sinotopia.fundamental.codegenerator.basis.data.Field;
import com.sinotopia.fundamental.codegenerator.basis.data.base.AbsClass;
import com.sinotopia.fundamental.codegenerator.utils.FileUtils;
import com.sinotopia.fundamental.codegenerator.io.CodeOutputer;
import com.sinotopia.fundamental.codegenerator.io.FolderCleaner;
import com.sinotopia.fundamental.codegenerator.io.Outputable;
import com.sinotopia.fundamental.codegenerator.io.PathBuilder;

import java.util.List;

public class MultiCodeOutputer {
	private String root;
	private String packageName;
	private boolean clearFolderBeforeOutput;
	private boolean giveupOutputIfExists;
	private String clearFileExtension;
	private String path;
	
	private MultiCodeOutputer(String root, String packageName) {
		this.root = root;
		this.packageName = packageName;
	}
	
	public static MultiCodeOutputer newInstance(String root, String packageName) {
		return new MultiCodeOutputer(root, packageName);
	}
	public MultiCodeOutputer setClearFolderBeforeOutput(boolean clearFolderBeforeOutput) {
		this.clearFolderBeforeOutput = clearFolderBeforeOutput;
		return this;
	}
	public MultiCodeOutputer setClearFileExtension(String clearFileExtension) {
		this.clearFileExtension = clearFileExtension;
		return this;
	}
	public MultiCodeOutputer setPath(String path) {
		this.path = path;
		return this;
	}
	public MultiCodeOutputer setGiveupOutputIfExists(boolean giveupOutputIfExists) {
		this.giveupOutputIfExists = giveupOutputIfExists;
		return this;
	}
	
	/**
	 * 输出Class[]的relatedEnum
	 * @param classes Class[]
	 */
	public void outputEnum(AbsClass[] classes) {
		clear();
		for (AbsClass cls : classes) {
			outputEnum(cls);
		}
	}
	/**
	 * 输出List&lt;Class&gt;的relatedEnum
	 * @param list &lt;Class&gt;
	 */
	public <T extends AbsClass> void outputEnum(List<T> list) {
		clear();
		for (T t : list) {
			outputEnum(t);
		}
	}
	
	private void clear() {
		if (clearFolderBeforeOutput) {
			FolderCleaner.newInstance(PathBuilder.build(root, path != null ? path : packageName)).setFileExtension(clearFileExtension).clear();
		}
	}
	
	/**
	 * 输出Class的relatedEnum
	 * @param cls Class
	 */
	public void outputEnum(AbsClass cls) {
		if (cls instanceof Clazz) {
			Field[] fields = ((Clazz) cls).fields;
			if (fields != null && fields.length > 0) {
				for (Field field : fields) {
					if (field.relatedEnum != null) {
						PathBuilder path = PathBuilder.newInstance(root).setOutputable(field.relatedEnum);
						CodeOutputer.newInstance(path, field.relatedEnum).output();
					}
				}
			}
		}
	}
	
	public void output(Outputable[] classes) {
		clear();
		for (Outputable cls : classes) {
			output(cls);
		}
	}
	public <T extends Outputable> void output(List<T> list) {
		clear();
		for (T t : list) {
			output(t);
		}
	}
	public void output(Outputable outputable) {
		PathBuilder path = getOutputablePathBuilder(outputable);
		if (giveupOutputIfExists && FileUtils.isFileExists(path.build())) {
			return;
		}
		CodeOutputer.newInstance(path, outputable.toString()).output();
	}
	
	public PathBuilder getOutputablePathBuilder(Outputable outputable) {
		return PathBuilder.newInstance(root).setPath(path).setOutputable(outputable);
	}
}
