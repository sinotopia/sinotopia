package com.hakim.demo.admin.service.api;

import com.hakim.demo.admin.result.AdminUserView;
import com.hakim.demo.admin.result.LoginUserView;
import com.hakim.demo.admin.param.*;
import com.sinotopia.fundamental.api.data.ListResultEx;
import com.sinotopia.fundamental.api.data.ObjectResultEx;
import com.sinotopia.fundamental.api.data.ResultEx;

/**
 * 用户服务
 * Created by brucezee on 2017/3/1.
 */
public interface AdminUserService {
    /**
     * 新增用户
     * @param parameter
     * @return
     */
    public ResultEx addUser(AddUserParameter parameter);

    /**
     * 删除用户
     * @param parameter
     * @return
     */
    public ResultEx deleteUser(DeleteUserParameter parameter);

    /**
     * 获取用户
     * @param parameter
     * @return
     */
    public ObjectResultEx<AdminUserView> getUser(GetUserParameter parameter);

    /**
     * 更新用户
     * @param parameter
     * @return
     */
    public ResultEx updateUser(UpdateUserParameter parameter);

    /**
     * 获取用户列表
     * @param parameter
     * @return
     */
    public ListResultEx<AdminUserView> getUserList(GetUserListParameter parameter);

    /**
     * 登录用户
     * @param parameter
     * @return
     */
    public ObjectResultEx<LoginUserView> loginUser(LoginUserParameter parameter);

    /**
     * 退出用户
     * @param parameter
     * @return
     */
    public ResultEx logoutUser(LogoutUserParameter parameter);

    /**
     * 修改密码
     * @param parameter
     * @return
     */
    public ResultEx modifyPassword(ModifyPasswordParameter parameter);
}
