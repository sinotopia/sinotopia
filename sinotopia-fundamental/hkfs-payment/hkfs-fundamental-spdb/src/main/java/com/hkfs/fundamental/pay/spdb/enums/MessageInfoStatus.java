package com.hkfs.fundamental.pay.spdb.enums;

public enum MessageInfoStatus {
	SUCCESS(1, "", "提现成功"), FAILURE(0, "", "提现失败");

	private int code;
	private String value;
	private String bundlekey;

	private MessageInfoStatus(int code, String value, String bundleKey) {
		this.code = code;
		this.value = value;
		this.bundlekey = bundleKey;
	}

	public static MessageInfoStatus getByCode(int value) {
		for (MessageInfoStatus status : values()) {
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
