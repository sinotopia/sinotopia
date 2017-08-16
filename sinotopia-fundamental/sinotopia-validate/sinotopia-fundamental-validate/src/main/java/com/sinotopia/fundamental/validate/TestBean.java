package com.sinotopia.fundamental.validate;


import com.sinotopia.fundamental.validate.utils.ValidateUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by pc on 2016/4/20.
 */
public class TestBean {

    @NotNull(message = "提现金额不能为空")
    private Double amount;
    @NotEmpty(message = "名字不能为空")
    private String name;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        TestBean bean = new TestBean();
        ValidateUtils.validOrThrowException(bean);
    }
}
