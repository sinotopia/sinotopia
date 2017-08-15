package com.sinotopia.fundamental.common.handler;

/**
 * Created by Administrator on 2015/12/21.
 */
public class PageHandler {

    private Integer requestOffset;

    private Integer requestCount;

    public void setPage(Integer pageNo, Integer pageSize) {
        if (pageNo != null) {
            if (pageNo <= 0) {
                throw new IllegalArgumentException("起始页码为1");
            }
            this.requestOffset = (pageNo - 1) * pageSize;
        }
        this.requestCount = pageSize;
    }

    public void setRequestOffset(Integer requestOffset) {
        this.requestOffset = requestOffset;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public Integer getRequestOffset() {
        return requestOffset;
    }

    public Integer getRequestCount() {
        return requestCount;
    }

}
