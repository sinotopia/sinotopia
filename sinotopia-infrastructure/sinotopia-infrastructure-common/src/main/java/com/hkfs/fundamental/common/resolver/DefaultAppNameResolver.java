package com.hkfs.fundamental.common.resolver;

import com.hkfs.fundamental.common.utils.StrUtils;
import com.hkfs.fundamental.config.FundamentalConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>输入注释</p>
 *
 * @Author dzr
 * @Date 2016/6/6
 */
public class DefaultAppNameResolver implements AppNameResolver{

    public static  final Logger LOGGER = LoggerFactory.getLogger(DefaultAppNameResolver.class);
    private static final String APP_DUBBO_REG_KEY = "app.dubbo.regex.expr";
    private String regexDubboXml =  "dubbo-([\\w-]+)\\.xml";
    {
        String dubboXmlRegex = FundamentalConfigProvider.getString(APP_DUBBO_REG_KEY);
        if(!StrUtils.isEmpty(dubboXmlRegex)){
            regexDubboXml = dubboXmlRegex;
        }
        LOGGER.info("current dubbo xml regex expression:{}", regexDubboXml);
    }
    @Override
    public String resolver(String resourceName) {

        Pattern regexp = Pattern.compile(regexDubboXml);
        Matcher matcher =regexp.matcher(resourceName);
        while (matcher.find()){
            if(matcher.groupCount() == 1) {
                return matcher.group(1);
            }
        }
        return null;
    }
}
