package com.hkfs.fundamental.parser.test;

import com.alibaba.fastjson.JSON;
import com.hkfs.fundamental.parser.Parser;
import com.hkfs.fundamental.parser.ParserWrap;
import com.hkfs.fundamental.parser.define.FieldDefine;
import com.hkfs.fundamental.parser.json.JsonParser;
import com.hkfs.fundamental.parser.processor.FieldProcessor;
import com.hkfs.fundamental.parser.processor.date.DateFormatFieldProcessor;

/**
 * Created by zhoubing on 2016/11/22.
 */
public class TestJson {
    public static void main(String[] args) {
        String data = "{\"retCode\":100,\"retMsg\":\"OK\",\"data\":{\"store\":{\"bookStore\":{\"manager\":\"John\",\"customer\":\"Justin\",\"name\":\"xinhua shudian\",\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95,\"time\":\"20150311\"},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\",\"price\":12.99,\"time\":\"20150924\"},{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99,\"time\":\"20151212\"},{\"category\":\"fiction\",\"author\":\"J. R. R. Tolkien\",\"title\":\"The Lord of the Rings\",\"isbn\":\"0-395-19395-8\",\"price\":22.99,\"time\":\"20161124\"}]},\"bicycleStore\":{\"manager\":\"Mike\",\"bicycle\":{\"color\":\"red\",\"price\":19.95},\"factory\":{\"factoryName\":\"Beijing Book Ltd.Coxxxxx\"}},\"address\":\"zhejiang hangzhou\"},\"product\":{\"sellCount\":100,\"expensive\":10}}}";
//        {"retCode":100,"retMsg":"OK","data":{"store":{"bookStore":{"manager":"John","customer":"Justin","name":"xinhua shudian","book":[{"category":"reference","author":"Nigel Rees","title":"Sayings of the Century","price":8.95,"time":"20150311"},{"category":"fiction","author":"Evelyn Waugh","title":"Sword of Honour","price":12.99,"time":"20150924"},{"category":"fiction","author":"Herman Melville","title":"Moby Dick","isbn":"0-553-21311-3","price":8.99,"time":"20151212"},{"category":"fiction","author":"J. R. R. Tolkien","title":"The Lord of the Rings","isbn":"0-395-19395-8","price":22.99,"time":"20161124"}]},"bicycleStore":{"manager":"Mike","bicycle":{"color":"red","price":19.95},"factory":{"factoryName":"Beijing Book Ltd.Coxxxxx"}},"address":"zhejiang hangzhou"},"product":{"sellCount":100,"expensive":10}}}

        String data2 = "{\"paymentList\":[{\"baseAmount\":77788,\"company\":\"abc company\",\"companyAmount\":55.55},{\"baseAmount\":446,\"company\":\"guangzhou co\",\"companyAmount\":55.55},{\"baseAmount\":11224,\"company\":\"nanjing co\",\"companyAmount\":55.55}],\"user\":{\"authItem\":\"security\",\"channelCode\":\"djd\",\"cityCode\":\"621000\",\"idcard\":\"431381198109106573\",\"name\":\"test\",\"phone\":\"15858295625\",\"provinceCode\":\"620000\",\"token\":\"HkFs000000000wfTnwkbYl3My2wpbTqd\",\"userId\":\"1\"}}";
//        {"paymentList":[{"baseAmount":77788,"company":"abc company","companyAmount":55.55},{"baseAmount":446,"company":"guangzhou co","companyAmount":55.55},{"baseAmount":11224,"company":"nanjing co","companyAmount":55.55}],"user":{"authItem":"security","channelCode":"djd","cityCode":"621000","idcard":"431381198109106573","name":"test","phone":"15858295625","provinceCode":"620000","token":"HkFs000000000wfTnwkbYl3My2wpbTqd","userId":"1"}}

        Parser parser = new JsonParser();
        TestObject object = parser.parse(
                new ParserWrap[]{
                    new ParserWrap(data, getFieldDefine()),
                    new ParserWrap(data2, getFieldDefine2()),
//                    new ParserWrap(data, DefineLoader.getFieldDefine(TestJson.class.getResourceAsStream("/parser.xml"), "bookParser-json-part1")),
//                    new ParserWrap(data2, DefineLoader.getFieldDefine(TestJson.class.getResourceAsStream("/parser.xml"), "bookParser-json-part2")),
                },
                TestObject.class);

        System.out.println(JSON.toJSONString(object));
    }

    private static FieldDefine getFieldDefine() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newShort("expensive", "$.data.product.expensive"),
                FieldDefine.newObject("store", "$.data.store.bookStore", new FieldDefine[]{
                        FieldDefine.newObject("bicycle", "$.data.store.bicycleStore.bicycle", new FieldDefine[]{
                                FieldDefine.newString("color", "$..color"),
                                FieldDefine.newDouble("price", "$..price"),
                                FieldDefine.newString("bicycleStoreManager", "$.data.store.bicycleStore.manager"),
                        }),
                        FieldDefine.newArray("book", "$.data.store.bookStore.book", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("category", "$..category"),
                                FieldDefine.newString("author", "$..author"),
                                FieldDefine.newString("title", "$..title"),
                                FieldDefine.newString("isbnNo", "$..isbn"),//名称不一致
                                FieldDefine.newDouble("price", "$..price"),
                                FieldDefine.newString("createTime", "$..time", new DateFormatFieldProcessor("yyyy-MM-dd")),
                        })),

                        FieldDefine.newString("manager", "$.data.store.bookStore.manager", new FieldProcessor<String>() {
                            @Override
                            public String process(String value) {
                                return value != null ? value + " Joden" : "";
                            }
                        }),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("address", "$.data.store.address"),
                        FieldDefine.newString("factoryName", "$.data.store.bicycleStore.factory.factoryName"),
                        FieldDefine.newString("storeName", "$.data.store.bookStore.name"),
                }),
        });

        return root;
    }
    private static FieldDefine getFieldDefine2() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newObject("store", new FieldDefine[]{
                        FieldDefine.newArray("book", "$.paymentList", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("cityCode", "$.user.cityCode"),
                                FieldDefine.newDouble("baseAmount", "$..baseAmount"),
                        })),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("channelCode", "$.user.channelCode"),
                }),
        });

        return root;
    }

}
