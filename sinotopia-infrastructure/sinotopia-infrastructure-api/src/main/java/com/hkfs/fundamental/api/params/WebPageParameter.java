package com.hkfs.fundamental.api.params;

/**
 * 不需要登录的分页基类
 * Created by zhoubing on 2016/4/11.
 */
public class WebPageParameter extends BaseParameter {
    private static final long serialVersionUID = 1L;

    //页码
    private Integer pageNo = 1;
    //每页数量
    private Integer pageSize = 10;
    //请求偏移量
    public Integer requestOffset;
    //请求数量
    public Integer requestCount;

    public WebPageParameter(){
        super();
        calculateByPage(pageNo,pageSize);
    }
    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        if(pageNo != null) {
            this.pageNo = pageNo;
        }
        calculateByPage(pageNo, pageSize);
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if(pageSize != null){
            this.pageSize = pageSize;
        }
        calculateByPage(pageNo, pageSize);
    }

    public void calculateByPage(Integer pageNo, Integer pageSize) {
        if (pageNo != null && pageSize != null) {
            this.requestOffset = (pageNo - 1) * pageSize;
            this.requestCount = pageSize;
        }
    }

    public Integer getRequestOffset() {
        return requestOffset;
    }

    public void setRequestOffset(Integer requestOffset) {
        this.requestOffset = requestOffset;
        calculateByCount(requestOffset, requestCount);
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
        calculateByCount(requestOffset, requestCount);
    }

    public void calculateByCount(Integer requestOffset, Integer requestCount) {
        if (requestOffset != null && requestCount != null && requestCount != 0) {
            this.pageSize = requestCount;
            this.pageNo = requestOffset/requestCount + 1;
        }
    }

    @Override
    public String toString() {
        return "WebPageParameter{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", requestOffset=" + requestOffset +
                ", requestCount=" + requestCount +
                '}';
    }
}
