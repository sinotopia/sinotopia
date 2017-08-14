package com.hkfs.fundamental.database;

import com.hkfs.fundamental.config.FundamentalConfigProvider;
import org.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * Created by zhoubing on 2016/4/25.
 */
public class FundamentalMapperScannerConfigurer extends MapperScannerConfigurer {
    /**
     * 由于MapperScannerConfigurer在注入basePackage时PropertyPlaceholderConfigurer还未初始化，无法将配置文件中的basePackage值注入
     * 这里需要手动调用FundamentalConfigProvider的方法获取basePackage的配置值
     * @param basePackage
     */
    @Override
    public void setBasePackage(String basePackage) {
        if (basePackage.startsWith("${") && basePackage.endsWith("}")) {
            String key = basePackage.substring(2, basePackage.length()-1).trim();
            super.setBasePackage(FundamentalConfigProvider.getString(key));
        }
        else {
            super.setBasePackage(basePackage);
        }
    }
}
