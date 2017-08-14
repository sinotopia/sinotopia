package com.hkfs.fundamental.codegenerator.basis.data;

import com.hkfs.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;
import com.hkfs.fundamental.codegenerator.utils.StrUtils;

import java.util.*;

/**
 * 注解
 * @author zhoub 2015年10月28日 下午11:25:23
 */
public class Annotation extends AbsFormat {
	public String name;//注解的名称（包含@）
	public String fullClassName;//注解的完整类名
	public Map<String, String> keyValues = new HashMap<String, String>();//@Anno(value="xxxx",p="yyyy")中的键值对

	public Annotation(String name) {
		this(name, name);
	}
	public Annotation(String name, String fullClassName) {
		this.name = name;
		this.fullClassName = formatFullClassName(fullClassName);
		processKeyValues(name);
	}

	protected String formatFullClassName(String fullClassName) {
		if (fullClassName.startsWith("@")) {
			if (fullClassName.contains("(")) {
				return StrUtils.getMiddleText(fullClassName, "@", "(");
			}
			else {
				return StrUtils.getMiddleText(fullClassName, "@", null, true, false);
			}
		}

		return CodeUtils.formatFullClassName(fullClassName);
	}

	protected void processKeyValues(String name) {
		String text = StrUtils.getMiddleText(name, "(", ")");
		if (StrUtils.notEmpty(text)) {
			//@Anno("xxxx")
			//@Anno("xxxx", "yyyy")
			//@Anno(value="xxxx",p="yyyy")

			text = StrUtils.trimQuotation(text);//去除引号
			String[] textArray = text.split(",");
			for (String each : textArray) {
				each = StrUtils.trim(each);//去除空格
				//处理 exp="p=1" 这样的字符串
				String key = StrUtils.getMiddleText(each, null, "=", true, true);
				String value = StrUtils.getMiddleText(each, "=", null, true, false);
				if (StrUtils.notEmpty(key) && value != null) {
					key = StrUtils.trimQuotation(StrUtils.trim(key));//去除空格和引号
					value = StrUtils.trimQuotation(StrUtils.trim(value));//去除空格和引号
					keyValues.put(key, value);
				}
				else {
					each = StrUtils.trimQuotation(StrUtils.trim(each));//去除空格和引号
					keyValues.put("value", each);
				}
			}
		}
	}



	@Override
	public String toString() {
		init();
		return render(name+line());
	}
}
