package com.sinotopia.fundamental.parser.test;

import com.alibaba.fastjson.JSON;
import com.sinotopia.fundamental.common.utils.FileUtils;
import com.sinotopia.fundamental.parser.Parser;
import com.sinotopia.fundamental.parser.define.FieldDefine;
import com.sinotopia.fundamental.parser.jsoup.JsoupParser;

import java.util.Map;

/**
 * Created by zhoubing on 2016/11/30.
 */
public class Test {
    public static void main(String[] args) {
        TestJson.main(null);
        TestXml.main(null);
        TestJsoup.main(null);
        TestRegex.main(null);

        String text = FileUtils.getStringFromFile("C:\\Users\\john\\Desktop\\test.html");
        Parser parser = new JsoupParser();
        Map result = parser.parse(text, FieldDefine.newObject(new FieldDefine[]{
                FieldDefine.newString("title", "div.loginHeaderTitle"),
                FieldDefine.newString("hello", "div.QRCodeTitle"),
                FieldDefine.newString("feedback", ".experienceFeedbackA"),
                FieldDefine.newString("html", "#imgCaptcha.outerHtml()"),
                FieldDefine.newInteger("code", "#loginForm.attr(\"data-resultcode\")"),
                FieldDefine.newString("browse", "segment:content=\"webkit\">($)</head> segment:[if lt ($)]>"),
                FieldDefine.newString("token", "regex[1]:/bundles/js/login\\?v=(.*?)\">"),
        }));
        System.out.println(JSON.toJSONString(result));

    }
}
