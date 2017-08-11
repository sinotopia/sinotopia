package com.hkfs.fundamental.database.sqlanalysis;

import com.hkfs.fundamental.common.utils.ObjectUtils;

/**
 * SQL语句的Explain结果
 * Created by zhoubing on 2016/12/15.
 */
public class Explain {
    private Long id;
    private String selectType;
    private String table;
    private String possibleKeys;
    private String key;
    private Integer keyLen;
    private String ref;
    private Integer rows;
    private String extra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getPossibleKeys() {
        return possibleKeys;
    }

    public void setPossibleKeys(String possibleKeys) {
        this.possibleKeys = possibleKeys;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getKeyLen() {
        return keyLen;
    }

    public void setKeyLen(Integer keyLen) {
        this.keyLen = keyLen;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return ObjectUtils.objectToMap(this).toString();
    }
}
