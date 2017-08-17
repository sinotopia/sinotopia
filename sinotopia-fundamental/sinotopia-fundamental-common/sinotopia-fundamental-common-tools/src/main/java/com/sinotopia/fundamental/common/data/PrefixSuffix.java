package com.sinotopia.fundamental.common.data;

/**
 * 前缀和后缀的配置
 */
public class PrefixSuffix {
    /**
     * 前缀
     */
    public String prefix;
    /**
     * 后缀
     */
    public String suffix;
    /**
     * 是否使用第一次找到的前缀作为截取前缀
     */
    public boolean lazyPrefix = true;
    /**
     * 是否使用第一次找到的后缀作为截取后缀
     */
    public boolean lazySuffix = true;

    public PrefixSuffix(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public PrefixSuffix(String prefix, String suffix, boolean lazyPrefix, boolean lazySuffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.lazyPrefix = lazyPrefix;
        this.lazySuffix = lazySuffix;
    }
}
