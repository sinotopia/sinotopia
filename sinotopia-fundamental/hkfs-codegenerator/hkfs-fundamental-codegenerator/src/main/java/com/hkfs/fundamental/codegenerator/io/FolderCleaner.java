package com.hkfs.fundamental.codegenerator.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hkfs.fundamental.codegenerator.basis.global.Config;

public class FolderCleaner {
	private String[] folderPaths;
	private String fileExtension;
	private FolderCleaner(String...folderPaths) {
		this.folderPaths = folderPaths;
	}
	private FolderCleaner(PathBuilder...builders) {
		List<String> list = new ArrayList<String>();
		for (PathBuilder builder : builders) {
			list.add(builder.build());
		}
		this.folderPaths = list.toArray(new String[list.size()]);
	}
	
	public static FolderCleaner newInstance(String...folderPaths) {
		return new FolderCleaner(folderPaths);
	}
	public static FolderCleaner newInstance(PathBuilder...builders) {
		return new FolderCleaner(builders);
	}
	public FolderCleaner setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
		return this;
	}
	
	
	public void clear() {
		if (folderPaths != null) {
			for (String folderPath : folderPaths) {
				deleteFolderFiles(folderPath, fileExtension);
			}
		}
	}
	
	/**
	 * 删除一个文件夹下的所有文件（子一级）
	 * @param path 文件夹路径
	 * @return 删除文件的总大小
	 */
	public long deleteFolderFiles(String path, String fileExtension) {
		long deletedFileSize = 0;
		if (path != null) {
			File file = new File(path);
			if (file.exists() && file.isDirectory()) {
				File[] files = file.listFiles();
				int length = files.length;
				if (length <= 0) {
					return 0;
				}
				
				for (int i=0;i<length;i++) {
					file = files[i];
					if (!file.isFile()) {
						continue;
					}
					
					if (fileExtension == null || (file.getName().endsWith(fileExtension))) {
						deletedFileSize += file.length();
						file.delete();
						if (Config.isLogEnabled) {
							System.err.println("-DELETE- "+file.getAbsolutePath());
						}
					}
				}
			}
		}
		
		return deletedFileSize;
	}
}
