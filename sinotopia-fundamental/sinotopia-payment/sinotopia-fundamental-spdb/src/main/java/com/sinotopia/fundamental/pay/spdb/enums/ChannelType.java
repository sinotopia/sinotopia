package com.hkfs.fundamental.pay.spdb.enums;

public enum ChannelType {
	LL(1001, "", "连连"), 
	YEEPAY(1002, "", "易宝"), 
	SPDB(1003, "", "浦发"),
	;

	private int code;
	private String value;
	private String bundlekey;

	private ChannelType(int code, String value, String bundleKey) {
		this.code = code;
		this.value = value;
		this.bundlekey = bundleKey;
	}

	public static ChannelType getByCode(int value) {
		for (ChannelType status : values()) {
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
