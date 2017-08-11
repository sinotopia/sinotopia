package com.hkfs.fundamental.database.page;

import com.hkfs.fundamental.database.ArrayListEx;

/**
 * 分页列表结果对象
 * Created by brucezee on 2017/2/9.
 */
public class PageList<T> extends ArrayListEx<T> {
    //总记录数
    private Integer totalCount;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
