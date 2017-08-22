package com.sinotopia.demonstration.admin.param;

import com.sinotopia.fundamental.api.params.SessionParameter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 修改密码参数
 * Created by brucezee on 2017/3/3.
 */
public class ModifyPasswordParameter extends SessionParameter {
    /**
     * 原密码
     */
    @NotBlank(message = "原密码不能为空")
    private String originPassword;
    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
