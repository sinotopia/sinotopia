package com.sinotopia.fundamental.codegenerator.basis.validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class UniqueValidator {
	/**
	 * 判断数组元素是否唯一，如果不唯一抛出异常
	 */
	public void validate(Unique[] array) {
		validate(Arrays.asList(array));
	}
	
	/**
	 * 判断列表元素是否唯一，如果不唯一抛出异常
	 */
	public void validate(List<Unique> list) {
		if (list != null && list.size() > 0) {
			int size = list.size();
			Object uniqueKey = list.get(0).getUniqueKey();
			if (uniqueKey instanceof String) {
				if (((String) uniqueKey).contains(Unique.DIVIDER)) {
					int totalKey = ((String) uniqueKey).split(Unique.DIVIDER).length;
					List<String[]> listArray = new ArrayList<String[]>(totalKey);
					for (int i=0;i<totalKey;i++) {
						listArray.add(new String[size]);
					}
					for (int i=0;i<size;i++) {
						String[] keyArray = ((String) list.get(i).getUniqueKey()).split(Unique.DIVIDER);
						for (int j=0;j<totalKey;j++) {
							listArray.get(j)[i] = keyArray[j];
						}
					}
					for (String[] array : listArray) {
						validate(array);
					}
					return;
				}
			}
			
			HashSet<Object> set = new HashSet<Object>(list.size());
			for (int i=0;i<size;i++) {
				Object key = list.get(i).getUniqueKey();
				if (!set.add(key)) {
					throw new IllegalArgumentException("element at index="+(i+1)+" with key="+key+" already exists!");
				}
			}
		}
	}
	
	public void validate(String[] array) {
		HashSet<Object> set = new HashSet<Object>(array.length);
		for (int i=0;i<array.length;i++) {
			validate(set, i, array[i]);
		}
	}
	
	private void validate(HashSet<Object> set, int i, String key) {
		if (!set.add(key)) {
			throw new IllegalArgumentException("element at index="+(i+1)+" with key="+key+" already exists!");
		}
	}
}
