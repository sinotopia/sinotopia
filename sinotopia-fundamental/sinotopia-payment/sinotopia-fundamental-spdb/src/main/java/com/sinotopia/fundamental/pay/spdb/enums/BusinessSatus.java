package com.sinotopia.fundamental.pay.spdb.enums;

public enum BusinessSatus {
	BACKDATA_ZERO(0, "", "失败"),BACKDATA_ONE(1, "", "成功"), BACKDATA_TWO(2, "", "待认证"), BACKDATA_THREE(
			3, "", "在途");

	private int code;
	private String value;
	private String bundlekey;

	private BusinessSatus(int code, String value, String bundleKey) {
		this.code = code;
		this.value = value;
		this.bundlekey = bundleKey;
	}

	public static BusinessSatus getByCode(int value) {
		for (BusinessSatus status : values()) {
			if (status.getCode() == value) {
				return status;
			}
		}
		return null;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the bundlekey
	 */
	public String getBundlekey() {
		return bundlekey;
	}

	/**
	 * This function is help use to get multiple language support message from
	 * resource bundle.
	 */
	public String getBundleMessage() {
		// TODO move text from bundle key to resource bundle files and make this
		// work.
		// return ResourceBundleHelper.getMessage(bundlekey);
		return bundlekey;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
}
