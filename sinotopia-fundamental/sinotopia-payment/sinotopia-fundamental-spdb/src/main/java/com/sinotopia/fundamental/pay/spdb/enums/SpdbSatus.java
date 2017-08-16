package com.sinotopia.fundamental.pay.spdb.enums;

public enum SpdbSatus {
	BACKDATA_ZERO(0, "", "待补录"),BACKDATA_ONE(1, "", "待记帐"), BACKDATA_TWO(2, "", "待复核"), BACKDATA_THREE(
			3, "", "待授权"), BACKDATA_FOUR(4, "", "完成"), BACKDATA_EIGHT(
			8, "", "拒绝"), BACKDATA_NINE(9, "", "撤销");

	private int code;
	private String value;
	private String bundlekey;

	private SpdbSatus(int code, String value, String bundleKey) {
		this.code = code;
		this.value = value;
		this.bundlekey = bundleKey;
	}

	public static SpdbSatus getByCode(int value) {
		for (SpdbSatus status : values()) {
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
