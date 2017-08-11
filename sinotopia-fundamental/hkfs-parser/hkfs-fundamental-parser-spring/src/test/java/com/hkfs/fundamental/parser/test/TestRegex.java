package com.hkfs.fundamental.parser.test;

import com.alibaba.fastjson.JSON;
import com.hkfs.fundamental.common.utils.TimeUtils;
import com.hkfs.fundamental.parser.Parser;
import com.hkfs.fundamental.parser.ParserWrap;
import com.hkfs.fundamental.parser.define.FieldDefine;
import com.hkfs.fundamental.parser.processor.FieldProcessor;
import com.hkfs.fundamental.parser.regex.RegexParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by zhoubing on 2016/11/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:application*.xml"})
public class TestRegex {
    @Resource(name = "bookParser-regex-part1")
    private FieldDefine define1;
    @Resource(name = "bookParser-regex-part2")
    private FieldDefine define2;

    @Test
    public void test() {
        String data = "<html>\n" +
                "    <head>\n" +
                "        <title>Document</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div class=\"data\">\n" +
                "            <div class=\"store\">\n" +
                "                <div id=\"bookStore\">\n" +
                "                    <span class=\"manager\">John</span>\n" +
                "                    <span class=\"customer\">Justin</span>\n" +
                "                    <span class=\"name\">xinhua shudian</span>\n" +
                "                    <table class=\"tb\">\n" +
                "                        <tr>\n" +
                "                            <td>reference</td>\n" +
                "                            <td>Nigel Rees</td>\n" +
                "                            <td>Sayings of the Century</td>\n" +
                "                            <td>0-553-21311-3</td>\n" +
                "                            <td>8.95</td>\n" +
                "                            <td>20150311</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>fiction</td>\n" +
                "                            <td>Evelyn Waugh</td>\n" +
                "                            <td>Sword of Honour</td>\n" +
                "                            <td>0-553-21311-3</td>\n" +
                "                            <td>12.99</td>\n" +
                "                            <td>20150924</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>fiction</td>\n" +
                "                            <td>Herman Melville</td>\n" +
                "                            <td>Moby Dick</td>\n" +
                "                            <td>0-553-21311-3</td>\n" +
                "                            <td>8.99</td>\n" +
                "                            <td>20151212</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>fiction</td>\n" +
                "                            <td>J. R. R. Tolkien</td>\n" +
                "                            <td>The Lord of the Rings</td>\n" +
                "                            <td>0-395-19395-8</td>\n" +
                "                            <td>22.99</td>\n" +
                "                            <td>20161124</td>\n" +
                "                        </tr>\n" +
                "                    </table>\n" +
                "                </div>\n" +
                "                <div id=\"bicycleStore\">\n" +
                "                    <span class=\"manager\">Mike</span>\n" +
                "                    <div>\n" +
                "                        <span class=\"color\">red</span>\n" +
                "                        <span class=\"price\">19.95</span>\n" +
                "                    </div>\n" +
                "                    <div>\n" +
                "                        <span class=\"factoryName\">Beijing Book Ltd.Coxxxxx</span>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                <span>zhejiang hangzhou</span>\n" +
                "            </div>\n" +
                "            <span id=\"product\">\n" +
                "                <span class=\"sellCount\">100</span>\n" +
                "                <span class=\"expensive\">10</span>\n" +
                "            </span>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "</html>";
//        <html><head><title>Document</title></head><body><div class="data"><div class="store"><div id="bookStore"><span class="manager">John</span><span class="customer">Justin</span><span class="name">xinhua shudian</span><table class="tb"><tr><td>reference</td><td>Nigel Rees</td><td>Sayings of the Century</td><td>0-553-21311-3</td><td>8.95</td><td>20150311</td></tr><tr><td>fiction</td><td>Evelyn Waugh</td><td>Sword of Honour</td><td>0-553-21311-3</td><td>12.99</td><td>20150924</td></tr><tr><td>fiction</td><td>Herman Melville</td><td>Moby Dick</td><td>0-553-21311-3</td><td>8.99</td><td>20151212</td></tr><tr><td>fiction</td><td>J. R. R. Tolkien</td><td>The Lord of the Rings</td><td>0-395-19395-8</td><td>22.99</td><td>20161124</td></tr></table></div><div id="bicycleStore"><span class="manager">Mike</span><div><span class="color">red</span><span class="price">19.95</span></div><div><span class="factoryName">Beijing Book Ltd.Coxxxxx</span></div></div><span>zhejiang hangzhou</span></div><span id="product"><span class="sellCount">100</span><span class="expensive">10</span></span></div></body></html>

        String data2 = "<html>\n" +
                "    <head>\n" +
                "        <title>Document</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div>\n" +
                "            <table id=\"paymentList\">\n" +
                "                <tr>\n" +
                "                    <td>77788</td>\n" +
                "                    <td>abc company</td>\n" +
                "                    <td>55.55</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>446</td>\n" +
                "                    <td>guangzhou co</td>\n" +
                "                    <td>55.55</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>11224</td>\n" +
                "                    <td>nanjing co</td>\n" +
                "                    <td>55.55</td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "            <div class=\"user\" channelCode=\"djd\">\n" +
                "                <label class=\"authItem\">security</label>\n" +
                "                <span class=\"cityCode\">621000</span>\n" +
                "                <label class=\"idcard\">431381198109106573</label>\n" +
                "                <label class=\"name\">test</label>\n" +
                "                <label class=\"phone\">15858295625</label>\n" +
                "                <label class=\"provinceCode\">620000</label>\n" +
                "                <label class=\"token\">HkFs000000000wfTnwkbYl3My2wpbTqd</label>\n" +
                "                <label class=\"userId\">1</label>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "</html>";
//        <html><head><title>Document</title></head><body><div><table id="paymentList"><tr><td>77788</td><td>abc company</td><td>55.55</td></tr><tr><td>446</td><td>guangzhou co</td><td>55.55</td></tr><tr><td>11224</td><td>nanjing co</td><td>55.55</td></tr></table><div class="user" channelCode="djd"><label class="authItem">security</label><span class="cityCode">621000</span><label class="idcard">431381198109106573</label><label class="name">test</label><label class="phone">15858295625</label><label class="provinceCode">620000</label><label class="token">HkFs000000000wfTnwkbYl3My2wpbTqd</label><label class="userId">1</label></div></div></body></html>

            Parser parser = new RegexParser();
            TestObject object = parser.parse(
                    new ParserWrap[]{
//                            new ParserWrap(data, getFieldDefine()),
//                            new ParserWrap(data2, getFieldDefine2()),
                        new ParserWrap(data, define1),
                        new ParserWrap(data2, define2),
                    },
                    TestObject.class);

        System.out.println(JSON.toJSONString(object));
    }

    private static FieldDefine getFieldDefine() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newInteger("expensive", "regex[1]:class=\"expensive\">(.*?)</span>"),
                FieldDefine.newObject("store", new FieldDefine[]{
                        FieldDefine.newObject("bicycle", new FieldDefine[]{
                                FieldDefine.newString("color", "regex[1]:class=\"color\">(.*?)</span>"),
                                FieldDefine.newDouble("price", "regex[1]:class=\"price\">(.*?)</span>"),
                                FieldDefine.newString("bicycleStoreManager", "regex:id=\"bicycleStore\">([\\w\\W]*)</span> regex[1]:class=\"manager\">(.*?)</span>"),
                        }),
                        FieldDefine.newArray("book", "regex:<table([\\w\\W]*)</table> regex:<tr([\\w\\W]*?)</tr>", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("category", " regex[1]:<td>(.*?)</td>"),
                                FieldDefine.newString("author", " regex[3]:<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>"),
                                FieldDefine.newString("title", " regex[5]:<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>"),
                                FieldDefine.newString("isbnNo", " regex[7]:<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>"),//名称不一致
                                FieldDefine.newDouble("price", " regex[9]:<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>"),
                                FieldDefine.newString("createTime", " regex[11]:<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>", new FieldProcessor<String>() {
                                    @Override
                                    public String process(String value) {
                                        return TimeUtils.parseDateString(value, "yyyy-MM-dd");
                                    }
                                }),
                        })),

                        FieldDefine.newString("manager", "regex:id=\"bookStore\">([\\w\\W]*)table regex[1]:class=\"manager\">(.*?)</span>", new FieldProcessor<String>() {
                            @Override
                            public String process(String value) {
                                return value != null ? value + " Joden" : "";
                            }
                        }),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("address", "regex:id=\"bicycleStore\">([\\w\\W]*)id=\"product\"> regex[1]:<span>(.*)</span>"),
                        FieldDefine.newString("factoryName", "regex:id=\"bicycleStore\">([\\w\\W]*)id=\"product\"> regex[1]:class=\"factoryName\">(.*?)</span>"),
                        FieldDefine.newString("storeName", "regex:id=\"bookStore\">([\\w\\W]*)class=\"tb\"> regex[1]:class=\"name\">(.*?)</span>"),
                }),
        });

        return root;
    }
    private static FieldDefine getFieldDefine2() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newObject("store", new FieldDefine[]{
                        FieldDefine.newArray("book", "<table([\\w\\W]*?)</table> regex:<tr([\\w\\W]*?)</tr>", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("cityCode", "regex[1]:class=\"cityCode\">(.*?)</span>"),
                                FieldDefine.newDouble("baseAmount", " regex[1]:<td>(.*?)</td>([\\w\\W]*)<td>(.*?)</td>([\\w\\W]*)<td>(.*?)</td>"),
                        })),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("channelCode", "regex:class=\"user\"([\\w\\W]*?)</div> regex[1]:channelCode=\"(.*?)\""),//获取属性
                }),
        });

        return root;
    }

}
