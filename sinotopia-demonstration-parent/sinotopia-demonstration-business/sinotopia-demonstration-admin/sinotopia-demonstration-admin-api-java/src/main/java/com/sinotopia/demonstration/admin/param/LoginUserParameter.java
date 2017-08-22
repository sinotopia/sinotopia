package com.sinotopia.demonstration.admin.param;

import com.sinotopia.fundamental.api.params.BaseParameter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 登录用户参数
 * Created by brucezee on 2017/3/1.
 */
public class LoginUserParameter extends BaseParameter {
    /**
     * 登录用户名
     */
    @NotBlank(message = "登录用户名不能为空")
    private String username;
    /**
     * 登录密码
     */
    @NotBlank(message = "登录密码不能为空")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
