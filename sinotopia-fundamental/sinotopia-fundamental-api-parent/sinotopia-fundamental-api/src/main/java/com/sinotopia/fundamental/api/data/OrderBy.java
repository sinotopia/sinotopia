package com.sinotopia.fundamental.api.data;

/**
 * <p>定义排序的</p>
 *
 */
public class OrderBy extends DataObjectBase{

    public static final String ASC = "ASC";

    public static final String DESC = "DESC";

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

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    //查询条件
    private String condition;

    /**
     * 获取排序方向，如：ASC
     * @return
     */
    public String getDirection() {
        return asc ? ASC : DESC;
    }

    /**
     * 获取排序条件，如：id DESC
     * @return
     */
    public String getCondition() {
        if (condition == null) {
            if (field == null) {
                throw new IllegalArgumentException("field is required.");
            }
            condition = new StringBuilder(field)
                    .append(" ")
                    .append(getDirection())
                    .toString();
        }
        return condition;
    }

    /**
     * 获取完整的排序语句，如：ORDER BY id DESC
     * @return
     */
    public String getContent() {
        return new StringBuilder("ORDER BY ")
                .append(getCondition())
                .toString();
    }

    public OrderBy link(String field) {
        this.condition = new StringBuilder()
                .append(getCondition())
                .append(", ")
                .append(field)
                .toString();
        return this;
    }

    public OrderBy desc() {
        return resetCondition(false);
    }

    public OrderBy asc() {
        return resetCondition(true);
    }

    private OrderBy resetCondition(boolean asc) {
        this.asc = asc;
        String origin = getCondition();
        if (!origin.endsWith(DESC) && !origin.endsWith(ASC)) {
            this.condition = new StringBuilder()
                    .append(origin)
                    .append(" ")
                    .append(asc ? ASC : DESC)
                    .toString();
        }
        return this;
    }

    public static OrderBy by(String field) {
        return new OrderBy(field);
    }


//    public static void main(String[] args) {
//        //OrderBy orderBy = new OrderBy("id", false);
//        //OrderBy orderBy = OrderBy.by("id").desc();//单个order by即ORDER BY id DESC
//        OrderBy orderBy = OrderBy.by("id").desc().link("name").asc();//多个order by即ORDER BY id DESC, name ASC
//        System.out.println(orderBy.getDirection());//ASC
//        System.out.println(orderBy.getCondition());//id DESC, name ASC
//        System.out.println(orderBy.getContent());//ORDER BY id DESC, name ASC
//    }
}
