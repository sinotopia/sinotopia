package com.hkfs.fundamental.api.data;
/**
 * 
 * @author brucezee Jan 25, 2013 9:47:37 PM
 */
public class PojoDataObjectBase extends PageDataObjectBase {
	private static final long serialVersionUID = 1L;

	public Object extendedParameter;
	public Object getExtendedParameter() {
		return extendedParameter;
	}
	public void setExtendedParameter(Object extendedParameter) {
		this.extendedParameter = extendedParameter;
	}
}
