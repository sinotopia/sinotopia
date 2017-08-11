package com.hkfs.fundamental.pay.spdb.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * json辅助类
 * 
 * @author liliang
 *
 */
public class JsonUtil {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	// 获取JSON中数据
	public static String getValueByJson(String json, String key) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		if (jsonObject.get(key) == null) {
			return null;
		}
		return jsonObject.get(key).toString();
	}

	// xml转json
	public static String xml2JSON(String xml) {
		System.out.println(xml);
		if(xml!=null && xml!=""){
			return new XMLSerializer().read(xml).toString();
		}
		return "";
	}

	// json转xml
	public static String json2XML(String json) {
		JSONObject jobj = JSONObject.fromObject(json);
		String xml = new XMLSerializer().write(jobj);
		return xml;
	}

	// object转json
	public static String object2JSON(Object object) {
		JSONObject jsonObject = JSONObject.fromObject(object);
		return jsonObject.toString();
	}

	// json转map
	public static HashMap<String, String> json2Map(String json) {
		HashMap<String, String> data = new HashMap<String, String>();
		// 将json字符串转换成jsonObject
		JSONObject jsonObject = JSONObject.fromObject(json);
		Iterator it = jsonObject.keys();
		// 遍历jsonObject数据，添加到Map对象
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			String value = (String) jsonObject.get(key);
			data.put(key, value);
		}
		return data;
	}
	
	// json转List
    public static <T> List<T> jsonToList(String json,Class<T> clazz){  
        JSONArray jsonarray = JSONArray.fromObject(json);  
        List list = (List)JSONArray.toList(jsonarray, clazz);  
       return list;
          
    }  
 
}
