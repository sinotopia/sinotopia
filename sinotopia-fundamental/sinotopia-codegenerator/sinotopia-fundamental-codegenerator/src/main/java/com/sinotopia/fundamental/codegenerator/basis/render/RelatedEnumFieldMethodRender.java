package com.sinotopia.fundamental.codegenerator.basis.render;

import com.sinotopia.fundamental.codegenerator.basis.data.Field;

public class RelatedEnumFieldMethodRender implements FieldMethodRender {
	@Override
	public String render(Field field) {
		return field.processRelatedEnumMethods();
	}
}
