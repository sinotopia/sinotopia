package com.sinotopia.fundamental.pay.spdb.enums;

public enum TransCode {
	WITHDRAW("8801", "", "单笔提现"),
	WITHDRAW_QUERY("8804", "", "单笔提现查询"), 
	WITHDRAW_DETAIL("8924", "", "帐户明细"),
	WITHDRAW_EG("EG01", "", "网银互联跨行提现"),
	WITHDRAW_EG_QUERY("EG30", "", "网银提现查询"),
	WITHDRAW_BANKNO_QUERY("EG48", "", "网银支付行名行号表查询"),
	WITHDRAW_BALANCE("4402", "", "账号余额查询");

	private String code;
	private String value;
	private String bundlekey;

	private TransCode(String code, String value, String bundleKey) {
		this.code = code;
		this.value = value;
		this.bundlekey = bundleKey;
	}

	public static TransCode getByCode(String value) {
		for (TransCode status : values()) {
			if (status.getCode().equals(value)) {
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
	public String getCode() {
		return code;
	}
}
