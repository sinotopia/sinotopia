package com.hkfs.fundamental.api.data;

/**
 * <p>定义排序的</p>
 *
 * @Author dzr
 * @Date 2016/6/20
 */
public class OrderBy extends DataObjectBase{

    public static final String ASC = "asc";

    public static final String DESC = "desc";

    private String field;

    private boolean asc = true;

    public OrderBy() {
    }

    public OrderBy(String field) {
        this.field = field;
    }

    public OrderBy(String field, boolean asc) {
        this.field = field;
        this.asc = asc;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDirection() {
        return asc ? ASC : DESC;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

}
