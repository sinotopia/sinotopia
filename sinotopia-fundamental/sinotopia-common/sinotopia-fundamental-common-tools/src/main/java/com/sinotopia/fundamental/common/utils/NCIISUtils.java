package com.hkfs.fundamental.common.utils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>身份证校验</p>
 * @Author dzr
 * @Date 2016/06/01
 */
public class NCIISUtils {
    
    private static final Pattern CID_PATTERN = Pattern.compile("^(((\\d{6})(\\d{8})(\\d{2}(\\d)))(.?)).*");
    
    private static final String DATE_FORMAT = "yyyyMMdd";
    
    public static NCID parseNCID(String rawCID) {
        return parseNCID(rawCID, true);
    }
    
    /**
     * 解析身份证号码为对象
     * @param rawCID 输入的身份证字符串
     * @return 身份证号码对象
     * 
     *         身份证号码组成
     *         1、号码的结构
     *         公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
     *         八位数字出生日期码，三位数字顺序码和一位数字字母校验码。
     *         2、地址码(前六位数）
     *         表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
     *         3、出生日期码（第七位至十四位）
     *         表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
     *         4、顺序码（第十五位至十七位）
     *         表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，
     *         顺序码的奇数分配给男性，偶数分配给女性。
     *         5、校验码（第十八位数）
     *         （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, , 16 ，先对前17位数字的权求和
     *         Ai:表示第i位置上的身份证号码数字值
     *         Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     *         （加权因子计算方式：从右至左1-18，记为i，2^(i-1)%11即为每位的加权因子）
     *         （2）计算模 Y = mod(S, 11)
     *         （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2（计算方式：(12-S%11)%11，10换成X）
     */
    public static NCID parseNCID(String rawCID, boolean validate) {
        rawCID = StrUtils.trim(rawCID);
        if (StrUtils.isEmpty(rawCID)) {
            return null;
        }
        Matcher matcher = CID_PATTERN.matcher(rawCID);
        if (matcher.find()) {
            String areaCode = matcher.group(3);
            String birthDateStr = matcher.group(4);
            String inDateSN = matcher.group(5);
            //String genderMark = matcher.group(6);
            String checkCodeRaw = matcher.group(7);
            
            NCID ncid = new NCID();
            ncid.setAreaCode(areaCode);
            ncid.setBirthDateStr(birthDateStr);
            ncid.setInDateSN(inDateSN);
            ncid.setCheckCode(checkCodeRaw);
            
            if (validate) {
                if (!ncid.isValid()) {

                }
            }
            
            return ncid;
        }
        return null;
    }
    
    public static boolean validateNCID(NCID ncid) {
        if (null == ncid) {
            return false;
        }
        String checkCodeRaw = ncid.getCheckCode();
        if (null == checkCodeRaw) {
            return false;
        }
        String checkCode = calculateCheckCode(ncid);
        return checkCodeRaw.equals(checkCode);
    }
    
    public static Date parseBirthDateString(String dateStr) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    public static String formatBirthDateString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }
    
    /**
     * 计算身份证号码校验位
     * @param ncid 身份证号码对象
     * @return 身份证号码校验位
     * 
     *         身份证号码组成
     *         1、号码的结构
     *         公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
     *         八位数字出生日期码，三位数字顺序码和一位数字字母校验码。
     *         2、地址码(前六位数）
     *         表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
     *         3、出生日期码（第七位至十四位）
     *         表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
     *         4、顺序码（第十五位至十七位）
     *         表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，
     *         顺序码的奇数分配给男性，偶数分配给女性。
     *         5、校验码（第十八位数）
     *         （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, , 16 ，先对前17位数字的权求和
     *         Ai:表示第i位置上的身份证号码数字值
     *         Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     *         （加权因子计算方式：从右至左1-18，记为i，2^(i-1)%11即为每位的加权因子）
     *         （2）计算模 Y = mod(S, 11)
     *         （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2（计算方式：(12-S%11)%11，10换成X）
     */
    public static String calculateCheckCode(NCID ncid) {
        if (null != ncid) {
            String areaCode = ncid.getAreaCode();
            String birthDateStr = ncid.getBirthDateStr();
            String inDateSN = ncid.getInDateSN();
            if (checkAreaCodeValid(areaCode) && checkBirthDateStrValid(birthDateStr)
                    && null != parseGenderMarkFromInDateSN(inDateSN)) {
                String infoCodeStr = areaCode + birthDateStr + inDateSN;
                int infoSum = 0;
                int infoCodeNum = 0;
                int posPower = 0;
                for (int i = 0; i < 17; i++) {
                    infoCodeNum = infoCodeStr.charAt(i) - '0';
                    posPower = (2 << (18 - i - 1 - 1)) % 11;
                    infoSum += infoCodeNum * posPower;
                }
                int checkCodeNum = (12 - infoSum % 11) % 11;
                String checkCode = 10 == checkCodeNum ? "X" : String.valueOf(checkCodeNum);
                return checkCode;
            }
        }
        return null;
    }

    private static boolean isNumeric(String str){
        if(str == null) {
            return false;
        } else {
            int sz = str.length();

            for(int i = 0; i < sz; ++i) {
                if(!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean checkAreaCodeValid(String areaCode) {
        return null != areaCode && 6 == areaCode.length() && isNumeric(areaCode);
    }
    
    public static boolean checkBirthDateStrValid(String birthDateStr) {
        if (null != birthDateStr && 8 == birthDateStr.length() && isNumeric(birthDateStr)) {
            return null != parseBirthDateString(birthDateStr);
        }
        return false;
    }
    
    public static String parseGenderMarkFromInDateSN(String inDateSN) {
        if (null != inDateSN && 3 == inDateSN.length() && isNumeric(inDateSN)) {
            return inDateSN.substring(2);
        }
        return null;
    }
    
    public static String toString(NCID ncid) {
        return new StringBuilder(18).append(ncid.getAreaCode()).append(ncid.getBirthDateStr())
                .append(ncid.getInDateSN()).append(ncid.getCheckCode()).toString();
    }
    
    public static String correctCID(String rawCID) {
        NCID ncid = parseNCID(rawCID, false);
        String checkCode = calculateCheckCode(ncid);
        ncid.setCheckCode(checkCode);
        return toString(ncid);
    }
    
    public static void main(String[] args) {

        NCID ncid = NCIISUtils.parseNCID("331021199602130612", false);
        System.out.println(ncid.isValid());
    }

    public static class NCID implements Serializable {

        /**
         * serialVersionUID
         */
        private static final long serialVersionUID = 1L;

        private String fullCID;

        private String areaCode;

        private String birthDateStr;

        private Date birthDate;

        private String inDateSN;

        private String genderMark;

        private String checkCode;

        private boolean validated;

        private boolean valid;

        public boolean isMale() {
            return isValid() ? 1 == (genderMark.charAt(0) - '0') % 2 : false;
        }

        public String getFullCID() {
            if (null == fullCID) {
                if (isValid()) {
                    fullCID = NCIISUtils.toString(this);
                }
            }
            return fullCID;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            fullCID = null;
            this.areaCode = areaCode;
        }

        public String getBirthDateStr() {
            return birthDateStr;
        }

        public void setBirthDateStr(String birthDateStr) {
            fullCID = null;
            birthDate = NCIISUtils.parseBirthDateString(birthDateStr);
            this.birthDateStr = birthDateStr;
        }

        public Date getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(Date birthDate) {
            fullCID = null;
            birthDateStr = NCIISUtils.formatBirthDateString(birthDate);
            this.birthDate = birthDate;
        }

        public String getInDateSN() {
            return inDateSN;
        }

        public void setInDateSN(String inDateSN) {
            fullCID = null;
            genderMark = NCIISUtils.parseGenderMarkFromInDateSN(inDateSN);
            this.inDateSN = inDateSN;
        }

        public String getGenderMark() {
            return genderMark;
        }

        public String getCheckCode() {
            return checkCode;
        }

        public void setCheckCode(String checkCode) {
            this.checkCode = checkCode;
        }

        public boolean isValid() {
            if (!validated) {
                valid = NCIISUtils.validateNCID(this);
            }
            return valid;
        }

        @Override
        public String toString() {
            return "NCID [fullCID=" + fullCID + ", areaCode=" + areaCode + ", birthDateStr=" + birthDateStr
                    + ", inDateSN=" + inDateSN + ", genderMark=" + genderMark + ", checkCode=" + checkCode + "]";
        }

    }
}
