package com.sinotopia.fundamental.api.data;

/**
 * Created by Administrator on 2015/12/17.
 */
public class PageDataObjectBase extends DataObjectBase {
    private static final long serialVersionUID = 1L;
    public Integer requestOffset;
    public Integer requestCount;
    public Integer getRequestOffset() {
        return requestOffset;
    }
    public void setRequestOffset(Integer requestOffset) {
        this.requestOffset = requestOffset;
    }
    public Integer getRequestCount() {
        return requestCount;
    }
    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }
    /**
     * 请求从偏移量以后多少条记录
     * @param requestOffset 偏移量
     * @param requestCount 请求记录数
     */
    public void request(Integer requestOffset, Integer requestCount) {
        this.requestOffset = requestOffset;
        this.requestCount = requestCount;
    }
    /**
     * 只请求一条记录
     */
    public void requestOne() {
        this.requestOffset = 0;
        this.requestCount = 1;
    }
    /**
     * 请求指定记录条数的记录
     * @param requestCount 请求返回记录条数
     */
    public void request(Integer requestCount) {
        this.requestOffset = 0;
        this.requestCount = requestCount;
    }
}
