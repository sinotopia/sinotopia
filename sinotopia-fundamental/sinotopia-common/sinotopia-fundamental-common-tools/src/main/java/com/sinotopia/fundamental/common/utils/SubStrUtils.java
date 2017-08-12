package com.hkfs.fundamental.common.utils;

import com.hkfs.fundamental.common.data.PrefixSuffix;

/**
 * 截取字符串相关方法
 * Created by zhoubing on 2016/5/17.
 */
public class SubStrUtils {
    /**
     * 从文本中根据一连串的前缀和后缀截取字符串
     * @param content
     * @param prefixSuffixes
     * @return
     */
    public static String getMiddleText(String content, PrefixSuffix...prefixSuffixes) {
        if (prefixSuffixes == null || prefixSuffixes.length == 0) {
            return content;
        }
        if (prefixSuffixes.length > 1) {
            for (int i = 0; i < prefixSuffixes.length - 1; i++) {
                PrefixSuffix prefixSuffix = prefixSuffixes[i];
                content = StrUtils.getWholeText(content,
                        prefixSuffix.prefix,
                        prefixSuffix.suffix,
                        prefixSuffix.lazyPrefix,
                        prefixSuffix.lazySuffix);
            }
        }
        PrefixSuffix prefixSuffix = prefixSuffixes[prefixSuffixes.length-1];
        String text = StrUtils.getMiddleText(content,
                prefixSuffix.prefix,
                prefixSuffix.suffix,
                prefixSuffix.lazyPrefix,
                prefixSuffix.lazySuffix);
        return text != null ? StrUtils.trim(text) : null;
    }

    public static String getWholeText(String content, PrefixSuffix...prefixSuffixes) {
        if (prefixSuffixes == null || prefixSuffixes.length == 0) {
            return content;
        }
        for (int i = 0; i < prefixSuffixes.length; i++) {
            PrefixSuffix prefixSuffix = prefixSuffixes[i];
            content = StrUtils.getWholeText(content,
                    prefixSuffix.prefix,
                    prefixSuffix.suffix,
                    prefixSuffix.lazyPrefix,
                    prefixSuffix.lazySuffix);
        }
        return content != null ? StrUtils.trim(content) : null;
    }

    /**
     * 从一段文字中截取部分内容之后找出数字金额
     * @param content
     * @param prefix
     * @param suffix
     * @return
     */
    public static Double getAmountFromText(String content, String prefix, String suffix) {
        String text = StrUtils.getMiddleText(content, prefix, suffix);
        if (text != null) {
            String numbers = StrUtils.getFirstNumberFromText(text, false);
            if (numbers != null) {
                return NumberUtils.parseDouble(numbers);
            }
        }
        return null;
    }

    /**
     * 从一段文字中截取部分内容之后找出数字金额
     * @param content
     * @param prefixSuffixes
     * @return
     */
    public static Double getAmountFromText(String content, PrefixSuffix...prefixSuffixes) {
        String text = SubStrUtils.getMiddleText(content, prefixSuffixes);
        if (text != null) {
            String numbers = StrUtils.getFirstNumberFromText(text, false);
            if (numbers != null) {
                return NumberUtils.parseDouble(numbers);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String content = "<tr>\n" +
                "    <th>姓名：</th>\n" +
                "    <td colspan=\"3\">颜巾斌</td>\n" +
                "    <td rowspan=\"7\" valign=\"top\" class=\"alignCenter\"><img src=\"/archive/viewphoto.action?trnd=52995043242114317738307332427953&type=xl&dtid=30rt04yd802yo6vl\" width=\"110\" /></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <th>性别：</th>\n" +
                "    <td>男</td>\n" +
                "    <th>出生日期：</th>\n" +
                "    <td>1990年11月16日</td>\n" +
                "    </tr>\n" +
                "  <tr>\n" +
                "    <th>入学时间：</th>\n" +
                "    <td>2008年9月1日</td>\n" +
                "    <th>毕业时间：</th>\n" +
                "    <td>2012年6月20日</td>\n" +
                "    </tr>\n" +
                "  <tr>\n" +
                "    <th>学历类别：</th>\n" +
                "    <td class=\"data-s2 dks2-30rt04yd802yo6vl-s3\"></td>\n" +
                "    <th>学历层次：</th>\n" +
                "    <td class=\"data-s2 dks2-30rt04yd802yo6vl-s4\"></td>\n" +
                "    </tr>\n" +
                "  \n" +
                "  <tr>\n" +
                "    <th>毕业院校：</th>\n" +
                "    <td class=\"data-s2 dks2-30rt04yd802yo6vl-s1\"></td>\n" +
                "    <th>院校所在地：</th>\n" +
                "    <td>浙江省</td>\n" +
                "    </tr>\n" +
                "      <tr>\n" +
                "        <th>专业名称：</th>\n" +
                "        <td class=\"data-s2 dks2-30rt04yd802yo6vl-s2\"></td>\n" +
                "        <th>学习形式：</th>\n" +
                "        <td class=\"data-s2 dks2-30rt04yd802yo6vl-s6\"></td>\n" +
                "        </tr>\n" +
                "  <tr>\n" +
                "    <th>证书编号：</th>\n" +
                "    <td class=\"data-s2 dks2-30rt04yd802yo6vl-s5\"></td>\n" +
                "    <th>毕结业结论：</th>\n" +
                "    <td>毕业</td>\n" +
                "    </tr>\n";

        String name = SubStrUtils.getMiddleText(content,
                new PrefixSuffix("姓名：", "</tr>"),
                new PrefixSuffix("<td colspan=\"3\">", "</td>"));
        String imageUrl = SubStrUtils.getMiddleText(content,
                new PrefixSuffix("姓名：", "</tr>"),
                new PrefixSuffix("<img src=\"", "\""));
        String gender = SubStrUtils.getMiddleText(content,
                new PrefixSuffix("性别：", "</tr>"),
                new PrefixSuffix("<td>", "</td>")
                );
        String birthday = SubStrUtils.getMiddleText(content,
                new PrefixSuffix("性别：", "</tr>"),
                new PrefixSuffix("<td>", "</td>", false, false)
                );
        String enrolDate = SubStrUtils.getMiddleText(content,
                new PrefixSuffix("入学时间：", "</tr>"),
                new PrefixSuffix("<td>", "</td>")
                );
        String graduateDate = SubStrUtils.getMiddleText(content,
                new PrefixSuffix("入学时间：", "</tr>"),
                new PrefixSuffix("<td>", "</td>", false, false)
                );
        String universityRegion = SubStrUtils.getMiddleText(content,
                new PrefixSuffix("院校所在地：", "</td>"),
                new PrefixSuffix("<td>", "</td>")
                );
        String result = SubStrUtils.getMiddleText(content,
                new PrefixSuffix("毕结业结论：", "</td>"),
                new PrefixSuffix("<td>", "</td>")
                );


        System.out.println(name);
        System.out.println(imageUrl);
        System.out.println(gender);
        System.out.println(birthday);
        System.out.println(enrolDate);
        System.out.println(graduateDate);
        System.out.println(universityRegion);
        System.out.println(result);
    }
}
