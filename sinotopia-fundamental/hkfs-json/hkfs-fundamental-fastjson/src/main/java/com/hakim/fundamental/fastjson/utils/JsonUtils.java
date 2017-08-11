package com.hakim.fundamental.fastjson.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSON工具方法
 * Created by zhoubing on 2016/8/19.
 */
public class JsonUtils {
    /**
     * 判断字符串是否是正确的json
     * @param json
     * @return
     */
    public static boolean isValidJson(String json) {
        return isValidJsonObject(json) || isValidJsonArray(json);
    }

    /**
     * 判断字符串是否是正确的json object
     * @param json
     * @return
     */
    public static boolean isValidJsonObject(String json) {
        return isValidJsonText(json, '{', '}');
    }

    /**
     * 判断字符串是否是正确的json array
     * @param json
     * @return
     */
    public static boolean isValidJsonArray(String json) {
        return isValidJsonText(json, '[', ']');
    }

    private static boolean isValidJsonText(String json, char char1, char char2) {
        if (json == null || json.isEmpty()) {
            return false;
        }

        int length = json.length();
        for (int i=0;i<length;i++) {
            char ch = json.charAt(i);
            if (isBlankChar(ch)) {
                continue;
            }
            else if (ch == char1) {
                for (int j=length-1;j>=0;j--) {
                    ch = json.charAt(j);
                    if (isBlankChar(ch)) {
                        continue;
                    }
                    else if (ch == char2) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                return false;
            }
            else {
                return false;
            }
        }
        return false;
    }

    public static boolean isBlankChar(char ch) {
        return ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b';
    }

    /**
     * 判断json字符串是否格式错误
     * @param json
     * @return
     */
    public static boolean notValidJson(String json) {
        return !isValidJson(json);
    }

    /**
     * 获取一连串的jsonObject
     * @param jsonText
     * @param keys
     * @return
     */
    public static JSONObject getJSONObject(String jsonText, String...keys) {
        return getJSONObject(JSON.parseObject(jsonText), keys);
    }

    /**
     * 获取一连串的jsonObject
     * @param jsonObject
     * @param keys
     * @return
     */
    public static JSONObject getJSONObject(JSONObject jsonObject, String...keys) {
        if (keys.length == 1) {
            if (keys[0].contains(".")) {
                keys = keys[0].split("\\.");
            }
        }
        Matcher matcher = Pattern.compile("(.*)\\[(\\w+)\\]").matcher("");
        for (String key : keys) {
            if (matcher.reset(key).find()) {
                int index = Integer.parseInt(matcher.group(2));
                JSONArray jsonArray = jsonObject.getJSONArray(matcher.group(1));
                if (jsonArray == null || index >= jsonArray.size()) {
                    return null;
                }

                jsonObject = jsonArray.getJSONObject(index);
            }
            else {
                jsonObject = jsonObject.getJSONObject(key);
            }

            if (jsonObject == null) {
                return null;
            }
        }
        return jsonObject;
    }

    /**
     * 获取一连串的jsonArray
     * @param jsonText
     * @param keys
     * @return
     */
    public static JSONArray getJSONArray(String jsonText, String...keys) {
        return getJSONArray(JSON.parseObject(jsonText), keys);
    }

    /**
     * 获取一连串的jsonArray
     * @param jsonObject
     * @param keys
     * @return
     */
    public static JSONArray getJSONArray(JSONObject jsonObject, String...keys) {
        if (keys.length == 1) {
            if (keys[0].contains(".")) {
                keys = keys[0].split("\\.");
            }
        }
        Matcher matcher = Pattern.compile("(.*)\\[(\\w+)\\]").matcher("");
        for (int i=0;i<keys.length-1;i++) {
            if (matcher.reset(keys[i]).find()) {
                int index = Integer.parseInt(matcher.group(2));
                JSONArray jsonArray = jsonObject.getJSONArray(matcher.group(1));
                if (jsonArray == null || index >= jsonArray.size()) {
                    return null;
                }

                jsonObject = jsonArray.getJSONObject(index);
            }
            else {
                jsonObject = jsonObject.getJSONObject(keys[i]);
            }

            if (jsonObject == null) {
                return null;
            }
        }

        return jsonObject != null ? jsonObject.getJSONArray(keys[keys.length-1]) : null;
    }

    public static void main(String[] args) {
        String x = "{\n" +
                "  success: true,\n" +
                "  \"s0010ResList\": [\n" +
                "    {\n" +
                "      success: true,\n" +
                "      \"csrq\": \"1996年01月27日\",\n" +
                "      \"dwdm\": \"80030000\",\n" +
                "      \"dwmc\": \"绍兴市镜湖新区新日文化信息服务中心\",\n" +
                "      \"grbh\": \"0030676221\",\n" +
                "      \"kh\": \"A40350702\",\n" +
                "      \"xb\": \"女\",\n" +
                "      \"xm\": \"杨丽娟\",\n" +
                "      \"zjhm\": \"511527199601274023\",\n" +
                "      \"zjlx\": \"身份证\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"s0010Resdatas\": {\n" +
                "    success: true,\n" +
                "    \"currPage\": 1,\n" +
                "    \"pageCount\": 1,\n" +
                "    \"pageLines\": 10,\n" +
                "    \"rowCount\": 6,\n" +
                "    \"rowList\": [\n" +
                "      {\n" +
                "        success: true,\n" +
                "        \"csrq\": \"1996年01月27日\",\n" +
                "        \"dwdm\": \"80030000\",\n" +
                "        \"dwmc\": \"绍兴市镜湖新区新日文化信息服务中心\",\n" +
                "        \"grbh\": \"0030676221\",\n" +
                "        \"kh\": \"A40350702\",\n" +
                "        \"xb\": \"女\",\n" +
                "        \"xm\": \"杨丽娟\",\n" +
                "        \"zjhm\": \"511527199601274023\",\n" +
                "        \"zjlx\": \"身份证\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"s0011Resdatas\": null,\n" +
                "  \"s0012Resdatas\": null,\n" +
                "  \"s0013Resdatas\": null,\n" +
                "  \"s0014Resdatas\": null,\n" +
                "  \"s0015Resdatas\": null,\n" +
                "  \"s0016Resdatas\": null,\n" +
                "  \"s0024Resdatas\": null,\n" +
                "  \"s0025Resdatas\": null,\n" +
                "  \"s0030ResList\": null,\n" +
                "  \"s0031Resdatas\": null,\n" +
                "  \"s0032Resdatas\": null,\n" +
                "  \"s0033Resdatas\": null,\n" +
                "  \"s0034Resdatas\": null\n" +
                "}";

        JSONObject jsonObject = getJSONObject(x, "s0010ResList[0]");
        System.out.println(jsonObject);
    }
}
