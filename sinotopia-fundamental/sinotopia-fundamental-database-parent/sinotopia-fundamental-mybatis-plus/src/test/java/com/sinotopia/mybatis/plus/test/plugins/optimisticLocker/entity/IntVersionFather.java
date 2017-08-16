package com.sinotopia.mybatis.plus.test.plugins.optimisticLocker.entity;

import com.sinotopia.mybatis.plus.annotations.Version;

public class IntVersionFather {
    @Version
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
