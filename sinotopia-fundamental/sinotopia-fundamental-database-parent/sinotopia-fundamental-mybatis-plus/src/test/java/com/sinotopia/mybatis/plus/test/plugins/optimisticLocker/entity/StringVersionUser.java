package com.sinotopia.mybatis.plus.test.plugins.optimisticLocker.entity;

import java.io.Serializable;

import com.sinotopia.mybatis.plus.annotations.TableField;
import com.sinotopia.mybatis.plus.annotations.TableName;
import com.sinotopia.mybatis.plus.annotations.Version;

@TableName("version_user")
public class StringVersionUser implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    @Version
    @TableField("version")
    private String tt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

}
