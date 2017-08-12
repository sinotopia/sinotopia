package com.hkfs.fundamental.pay.spdb.enums;

public enum RemitLocation {
	ZERO(0, "", "同城"),ONE(1, "", "异地");

	private int code;
	private String value;
	private String bundlekey;

	private RemitLocation(int code, String value, String bundleKey) {
		this.code = code;
		this.value = value;
		this.bundlekey = bundleKey;
	}

	public static RemitLocation getByCode(int value) {
		for (RemitLocation status : values()) {
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
