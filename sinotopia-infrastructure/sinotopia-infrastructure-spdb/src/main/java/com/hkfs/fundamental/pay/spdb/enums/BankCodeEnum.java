package com.hkfs.fundamental.pay.spdb.enums;

public enum BankCodeEnum {
	ICBC("ICBC","102100099996","工商银行"),
	ABC("ABC","103100000026","农业银行"),
	CCB("CCB","105100000017","建设银行"),
	CMBCHINA("CMBCHINA","308584000013","招商银行"),
	CEB("CEB","303100000006","光大银行"),
	ECITIC("ECITIC","302100011000","中信银行"),
	POST("POST","403100000004","邮政储蓄银行"),
	BOC("BOC","104100000004","中国银行"),
	BOCO("BOCO","301290000007","交通银行"),
	CIB("CIB","309391000011","兴业银行"),
	PINGAN("PINGAN","307584007998","平安银行"),
	CMBC("CMBC","305100000013","民生银行"),
	GDB("GDB","306581000003","广发银行"),
	HXB("HXB","304100040000","华夏银行"),
	BCCB("BCCB","313100000013","北京银行"),
	SPDB("SPDB","310290000013","浦发银行"),
	GZCB("GZCB","313581003284","广州银行"),
	HZBANK("HZBANK","313331000014","杭州银行"),
	SHB("SHB","325290000012","上海银行"),
	BSB("BSB","313192000013","包商银行"),
	HRBCB("HRBCB","313261000018","哈尔滨银行"),
	NBCB("NBCB","313332082914","宁波银行"),
	CZ("CZ","316331000018","浙商银行"),
	NJCB("NJCB","313301008887","南京银行"),
	AHCB("AHCB","319361000013","徽商银行"),
	HBYH("HBYH","313121006888","河北银行"),
	DLB("DLB","313222080002","大连银行"),
	CBHB("CBHB","318110000014","渤海银行"),
	CQRCB("CQRCB","314653000011","重庆农村商业银行"),
	GNXS("GNXS","314581000011","广州农村商业银行"),
	LJB("LJB","313261099913","龙江银行"),
	JSBCHINA("JSBCHINA","313301099999","江苏银行"),
	CQCB("CQCB","313653000013","重庆银行");

	private String code;
	private String spdbBankCode;
	private String bundlekey;

	private BankCodeEnum(String code, String spdbBankCode, String bundleKey) {
		this.code = code;
		this.spdbBankCode = spdbBankCode;
		this.bundlekey = bundleKey;
	}

	public static BankCodeEnum getByCode(String code) {
		for (BankCodeEnum status : values()) {
			if (status.getCode().equals(code)) {
				return status;
			}
		}
		return null;
	}

	/**
	 * @return the value
	 */
	public String getSpdbBankCode() {
		return spdbBankCode;
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

	public static void main(String[] args) {
		System.out.println("test:"+BankCodeEnum.getByCode("ABC").getSpdbBankCode());
	}
}
