package com.sinotopia.fundamental.parser.test;

import com.alibaba.fastjson.JSON;
import com.sinotopia.fundamental.parser.DefineLoader;
import com.sinotopia.fundamental.parser.Parser;
import com.sinotopia.fundamental.parser.ParserWrap;
import com.sinotopia.fundamental.parser.define.FieldDefine;
import com.sinotopia.fundamental.parser.jsoup.JsoupParser;
import com.sinotopia.fundamental.parser.processor.FieldProcessor;
import com.sinotopia.fundamental.parser.processor.date.DateFormatFieldProcessor;
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
public class TestJsoup {

    @Resource(name = "bookParser-html-part1")
    private FieldDefine define1;
    @Resource(name = "bookParser-html-part2")
    private FieldDefine define2;


    @Test
    public void test() {
        String data = "<html><head><title>Document</title></head><body><div class=\"data\"><div class=\"store\"><div id=\"bookStore\"><span class=\"manager\">John</span><span class=\"customer\">Justin</span><span class=\"name\">xinhua shudian</span><table class=\"tb\"><tr><td>reference</td><td>Nigel Rees</td><td>Sayings of the Century</td><td>0-553-21311-3</td><td>8.95</td><td>20150311</td></tr><tr><td>fiction</td><td>Evelyn Waugh</td><td>Sword of Honour</td><td>0-553-21311-3</td><td>12.99</td><td>20150924</td></tr><tr><td>fiction</td><td>Herman Melville</td><td>Moby Dick</td><td>0-553-21311-3</td><td>8.99</td><td>20151212</td></tr><tr><td>fiction</td><td>J. R. R. Tolkien</td><td>The Lord of the Rings</td><td>0-395-19395-8</td><td>22.99</td><td>20161124</td></tr></table></div><div id=\"bicycleStore\"><span class=\"manager\">Mike</span><div><span class=\"color\">red</span><span class=\"price\">19.95</span></div><div><span class=\"factoryName\">Beijing Book Ltd.Coxxxxx</span></div></div><span>zhejiang hangzhou</span></div><span id=\"product\"><span class=\"sellCount\">100</span><span class=\"expensive\">10</span></span></div></body></html>";
//        <html><head><title>Document</title></head><body><div class="data"><div class="store"><div id="bookStore"><span class="manager">John</span><span class="customer">Justin</span><span class="name">xinhua shudian</span><table class="tb"><tr><td>reference</td><td>Nigel Rees</td><td>Sayings of the Century</td><td>0-553-21311-3</td><td>8.95</td><td>20150311</td></tr><tr><td>fiction</td><td>Evelyn Waugh</td><td>Sword of Honour</td><td>0-553-21311-3</td><td>12.99</td><td>20150924</td></tr><tr><td>fiction</td><td>Herman Melville</td><td>Moby Dick</td><td>0-553-21311-3</td><td>8.99</td><td>20151212</td></tr><tr><td>fiction</td><td>J. R. R. Tolkien</td><td>The Lord of the Rings</td><td>0-395-19395-8</td><td>22.99</td><td>20161124</td></tr></table></div><div id="bicycleStore"><span class="manager">Mike</span><div><span class="color">red</span><span class="price">19.95</span></div><div><span class="factoryName">Beijing Book Ltd.Coxxxxx</span></div></div><span>zhejiang hangzhou</span></div><span id="product"><span class="sellCount">100</span><span class="expensive">10</span></span></div></body></html>

        String data2 = "<html><head><title>Document</title></head><body><div><table id=\"paymentList\"><tr><td>77788</td><td>abc company</td><td>55.55</td></tr><tr><td>446</td><td>guangzhou co</td><td>55.55</td></tr><tr><td>11224</td><td>nanjing co</td><td>55.55</td></tr></table><div class=\"user\" channelCode=\"djd\"><label class=\"authItem\">security</label><span class=\"cityCode\">621000</span><label class=\"idcard\">431381198109106573</label><label class=\"name\">test</label><label class=\"phone\">15858295625</label><label class=\"provinceCode\">620000</label><label class=\"token\">sinotopia000000000wfTnwkbYl3My2wpbTqd</label><label class=\"userId\">1</label></div></div></body></html>";
//        <html><head><title>Document</title></head><body><div><table id="paymentList"><tr><td>77788</td><td>abc company</td><td>55.55</td></tr><tr><td>446</td><td>guangzhou co</td><td>55.55</td></tr><tr><td>11224</td><td>nanjing co</td><td>55.55</td></tr></table><div class="user" channelCode="djd"><label class="authItem">security</label><span class="cityCode">621000</span><label class="idcard">431381198109106573</label><label class="name">test</label><label class="phone">15858295625</label><label class="provinceCode">620000</label><label class="token">sinotopia000000000wfTnwkbYl3My2wpbTqd</label><label class="userId">1</label></div></div></body></html>

        Parser parser = new JsoupParser();
        TestObject object = parser.parse(
                new ParserWrap[] {
//                        new ParserWrap(data, getFieldDefine()),
//                        new ParserWrap(data2, getFieldDefine2()),
                        new ParserWrap(data, define1),
                        new ParserWrap(data2, define2),
                },
                TestObject.class);

        System.out.println(JSON.toJSONString(object));
    }

    private static FieldDefine getFieldDefine() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newInteger("expensive", "regex:<body([\\w\\W]*)</body> regex[1]:class=\"expensive\">(.*?)</span>"), // "span#product span.expensive"),
                FieldDefine.newObject("store", "div.store", new FieldDefine[]{
                        FieldDefine.newObject("bicycle", " #bicycleStore", new FieldDefine[]{
                                FieldDefine.newString("color", " span.color"),
                                FieldDefine.newDouble("price", " span.price"),
                                FieldDefine.newString("bicycleStoreManager", " span.manager"),
                        }),
                        FieldDefine.newArray("book", "div#bookStore table tr", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("category", " td:eq(0)"),
                                FieldDefine.newString("author", " td:eq(1)"),
                                FieldDefine.newString("title", " td:eq(2)"),
                                FieldDefine.newString("isbnNo", " td:eq(3)"),//名称不一致
                                FieldDefine.newDouble("price", " td:eq(4)"),
                                FieldDefine.newString("createTime", " td:eq(5)", new DateFormatFieldProcessor("yyyy-MM-dd")),
                        })),

                        FieldDefine.newString("manager", "#bookStore span.manager", new FieldProcessor<String>() {
                            @Override
                            public String process(String value) {
                                return value != null ? value + " Joden" : "";
                            }
                        }),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("address", "div.store > span.html()"),
                        FieldDefine.newString("factoryName", "div#bicycleStore div span.factoryName"),
                        FieldDefine.newString("storeName", "segment:id=\"bookStore\">($)class=\"tb\"> segment:class=\"name\">($TT)</span>"),
                }),
        });

        return root;
    }
    private static FieldDefine getFieldDefine2() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newObject("store", new FieldDefine[]{
                        FieldDefine.newArray("book", "table#paymentList tr", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("cityCode", "div.user span.cityCode"),
                                FieldDefine.newDouble("baseAmount", " td:eq(0)"),
                        })),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("channelCode", "div.user.attr(channelCode)"),//获取属性
                }),
        });

        return root;
    }

}
