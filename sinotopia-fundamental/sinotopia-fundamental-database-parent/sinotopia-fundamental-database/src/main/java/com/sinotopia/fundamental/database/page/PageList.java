package com.sinotopia.fundamental.database.page;

import com.sinotopia.fundamental.database.ArrayListEx;

/**
 * 分页列表结果对象
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
