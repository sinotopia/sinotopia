package com.hkfs.fundamental.parser.test;

import com.alibaba.fastjson.JSON;
import com.hkfs.fundamental.common.utils.FileUtils;
import com.hkfs.fundamental.parser.Parser;
import com.hkfs.fundamental.parser.define.FieldDefine;
import com.hkfs.fundamental.parser.jsoup.JsoupParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by zhoubing on 2016/11/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:application*.xml"})
public class TestAll {

    @Autowired
    private TestJson testJson;
    @Autowired
    private TestXml testXml;
    @Autowired
    private TestJsoup testJsoup;
    @Autowired
    private TestRegex testRegex;


    @Test
    public void test() {
        testJson.test();
        testXml.test();
        testJsoup.test();
        testRegex.test();

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
