package com.sinotopia.fundamental.parser.test;

import com.alibaba.fastjson.JSON;
import com.sinotopia.fundamental.parser.DefineLoader;
import com.sinotopia.fundamental.parser.Parser;
import com.sinotopia.fundamental.parser.ParserWrap;
import com.sinotopia.fundamental.parser.define.FieldDefine;
import com.sinotopia.fundamental.parser.processor.FieldProcessor;
import com.sinotopia.fundamental.parser.processor.date.DateFormatFieldProcessor;
import com.sinotopia.fundamental.parser.xml.XmlParser;

/**
 * Created by zhoubing on 2016/11/22.
 */
public class TestXml {
    public static void main(String[] args) {
        String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root><retCode hello=\"OO\">100</retCode><retMsg>OK</retMsg><data><store><bookStore><manager>John</manager><customer>Justin</customer><name>xinhua shudian</name><book><category>reference</category><author>Nigel Rees</author><title>Sayings of the Century</title><price>8.95</price><time>20150311</time></book><book><category>fiction</category><author>Evelyn Waugh</author><title>Sword of Honour</title><price>12.99</price><time>20150924</time></book><book><category>fiction</category><author>Herman Melville</author><title>Moby Dick</title><isbn>0-553-21311-3</isbn><price>8.99</price><time>20151212</time></book><book><category>fiction</category><author>J. R. R. Tolkien</author><title>The Lord of the Rings</title><isbn>0-395-19395-8</isbn><price>22.99</price><time>20161124</time></book></bookStore><bicycleStore><manager>Mike</manager><bicycle><color>red</color><price>19.95</price></bicycle><factory><factoryName>Beijing Book Ltd.Coxxxxx</factoryName></factory></bicycleStore><address>zhejiang hangzhou</address></store><product><sellCount>100</sellCount><expensive>10</expensive></product></data></root>";
//        <?xml version="1.0" encoding="utf-8"?><root><retCode>100</retCode><retMsg>OK</retMsg><data><store><bookStore><manager>John</manager><customer>Justin</customer><name>xinhua shudian</name><book><category>reference</category><author>Nigel Rees</author><title>Sayings of the Century</title><price>8.95</price><time>20150311</time></book><book><category>fiction</category><author>Evelyn Waugh</author><title>Sword of Honour</title><price>12.99</price><time>20150924</time></book><book><category>fiction</category><author>Herman Melville</author><title>Moby Dick</title><isbn>0-553-21311-3</isbn><price>8.99</price><time>20151212</time></book><book><category>fiction</category><author>J. R. R. Tolkien</author><title>The Lord of the Rings</title><isbn>0-395-19395-8</isbn><price>22.99</price><time>20161124</time></book></bookStore><bicycleStore><manager>Mike</manager><bicycle><color>red</color><price>19.95</price></bicycle><factory><factoryName>Beijing Book Ltd.Coxxxxx</factoryName></factory></bicycleStore><address>zhejiang hangzhou</address></store><product><sellCount>100</sellCount><expensive>10</expensive></product></data></root>

        String data2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><paymentList><payment><value>77788</value><value>abc company</value><value>55.55</value></payment><payment><value>4124</value><value>nanjing co</value><value>19.14</value></payment><payment><value>2588</value><value>abc company</value><value>62.09</value></payment></paymentList><user channelCode=\"djd\"><authItem>security</authItem><cityCode>621000</cityCode><idcard>431381198109106573</idcard><name>test</name><phone>15858295625</phone><provinceCode>620000</provinceCode><token>sinotopia000000000wfTnwkbYl3My2wpbTqd</token><userId>1</userId></user></root>";
//        <?xml version="1.0" encoding="UTF-8" ?><root><paymentList><payment><value>77788</value><value>abc company</value><value>55.55</value></payment><payment><value>4124</value><value>nanjing co</value><value>19.14</value></payment><payment><value>2588</value><value>abc company</value><value>62.09</value></payment></paymentList><user channelCode="djd"><authItem>security</authItem><cityCode>621000</cityCode><idcard>431381198109106573</idcard><name>test</name><phone>15858295625</phone><provinceCode>620000</provinceCode><token>sinotopia000000000wfTnwkbYl3My2wpbTqd</token><userId>1</userId></user></root>

        Parser parser = new XmlParser();
        TestObject object = parser.parse(
                new ParserWrap[]{
//                        new ParserWrap(data, getFieldDefine()),
//                        new ParserWrap(data2, getFieldDefine2()),
                        new ParserWrap(data, DefineLoader.getFieldDefine(TestXml.class.getResourceAsStream("/parser.xml"), "bookParser-xml-part1")),
                        new ParserWrap(data2, DefineLoader.getFieldDefine(TestXml.class.getResourceAsStream("/parser.xml"), "bookParser-xml-part2")),
                },
                TestObject.class);

        System.out.println(JSON.toJSONString(object));


    }

    private static FieldDefine getFieldDefine() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newInteger("expensive", "/root/data/product/expensive"),
                FieldDefine.newObject("store", "/root/data/store/bookStore", new FieldDefine[]{
                        FieldDefine.newObject("bicycle", "/root/data/store/bicycleStore/bicycle", new FieldDefine[]{
                                FieldDefine.newString("color", "./color"),
                                FieldDefine.newDouble("price", "./price"),
                                FieldDefine.newString("bicycleStoreManager", "/root/data/store/bicycleStore/manager"),
                        }),
                        FieldDefine.newArray("book", "/root/data/store/bookStore/book", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("category", "./category"),
                                FieldDefine.newString("author", "./author"),
                                FieldDefine.newString("title", "./title"),
                                FieldDefine.newString("isbnNo", "./isbn"),//名称不一致
                                FieldDefine.newDouble("price", "./price"),
                                FieldDefine.newString("createTime", "./time", new DateFormatFieldProcessor("yyyy-MM-dd")),
                        })),

                        FieldDefine.newString("manager", "/root/data/store/bookStore/manager", new FieldProcessor<String>() {
                            @Override
                            public String process(String value) {
                                return value != null ? value + " Joden" : "";
                            }
                        }),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("address", "/root/data/store/address"),
                        FieldDefine.newString("factoryName", "/root/data/store/bicycleStore/factory/factoryName"),
                        FieldDefine.newString("storeName", "/root/data/store/bookStore/name"),
                }),
        });

        return root;
    }
    private static FieldDefine getFieldDefine2() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newObject("store", new FieldDefine[]{
                        FieldDefine.newArray("book", "/root/paymentList/payment", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("cityCode", "/root/user/cityCode"),
                                FieldDefine.newDouble("baseAmount", "./value[1]"),
                        })),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("channelCode", "/root/user/@channelCode"),//获取属性
                }),
        });

        return root;
    }

}
