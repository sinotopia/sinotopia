package com.sinotopia.fundamental.codegenerator.output;

import com.sinotopia.fundamental.codegenerator.basis.data.db.Mapper;
import com.sinotopia.fundamental.codegenerator.utils.StrUtils;
import com.sinotopia.fundamental.codegenerator.io.CodeInputer;
import com.sinotopia.fundamental.codegenerator.io.CodeOutputer;
import com.sinotopia.fundamental.codegenerator.io.FolderCleaner;
import com.sinotopia.fundamental.codegenerator.io.PathBuilder;

import java.util.List;

public class MapperCodeOutputer {
	private String root;
	private String packageName;
	private boolean clearFolderBeforeOutput;
	private String clearFileExtension;
	private boolean updateExistingMapper;
	private MapperCodeOutputer(String root, String packageName) {
		this.root = root;
		this.packageName = packageName;
	}
	
	public static MapperCodeOutputer newInstance(String root, String packageName) {
		return new MapperCodeOutputer(root, packageName);
	}
	public MapperCodeOutputer setClearFolderBeforeOutput(boolean clearFolderBeforeOutput) {
		this.clearFolderBeforeOutput = clearFolderBeforeOutput;
		return this;
	}
	public MapperCodeOutputer setClearFileExtension(String clearFileExtension) {
		this.clearFileExtension = clearFileExtension;
		return this;
	}
	public MapperCodeOutputer setUpdateExistingMapper(boolean updateExistingMapper) {
		this.updateExistingMapper = updateExistingMapper;
		return this;
	}
	
	private void clear() {
		if (clearFolderBeforeOutput) {
			FolderCleaner.newInstance(PathBuilder.build(root, packageName)).setFileExtension(clearFileExtension).clear();
		}
	}
	
	public void output(Mapper[] classes) {
		clear();
		for (Mapper cls : classes) {
			output(cls);
		}
	}
	public <T extends Mapper> void output(List<T> list) {
		clear();
		for (T t : list) {
			output(t);
		}
	}
	private void output(Mapper mapper) {
		PathBuilder path = PathBuilder.newInstance(root).setOutputable(mapper);
		if (updateExistingMapper) {
			String existingMapper = CodeInputer.newInstance(path).read();
			if (StrUtils.notEmpty(existingMapper)) {
				mapper.setExistingMapper(existingMapper);
			}
		}
		CodeOutputer.newInstance(path, mapper.toString()).output();
	}
}
