package com.hkfs.fundamental.codegenerator.basis.render;

import com.hkfs.fundamental.codegenerator.basis.data.Field;

public class RelatedEnumFieldMethodRender implements FieldMethodRender {
	@Override
	public String render(Field field) {
		return field.processRelatedEnumMethods();
	}
}
