package com.sinotopia.demonstration.admin.service.api;

import com.sinotopia.demonstration.admin.param.*;
import com.sinotopia.demonstration.admin.result.AdminUserView;
import com.sinotopia.demonstration.admin.result.LoginUserView;
import com.sinotopia.fundamental.api.data.ListResultEx;
import com.sinotopia.fundamental.api.data.ObjectResultEx;
import com.sinotopia.fundamental.api.data.ResultEx;

/**
 * 用户服务
 */
public interface AdminUserService {

    /**
     * 新增用户
     * @param parameter
     * @return
     */
     ResultEx addUser(AddUserParameter parameter);

    /**
     * 删除用户
     * @param parameter
     * @return
     */
     ResultEx deleteUser(DeleteUserParameter parameter);

    /**
     * 获取用户
     * @param parameter
     * @return
     */
     ObjectResultEx<AdminUserView> getUser(GetUserParameter parameter);

    /**
     * 更新用户
     * @param parameter
     * @return
     */
     ResultEx updateUser(UpdateUserParameter parameter);

    /**
     * 获取用户列表
     * @param parameter
     * @return
     */
     ListResultEx<AdminUserView> getUserList(GetUserListParameter parameter);

    /**
     * 登录用户
     * @param parameter
     * @return
     */
     ObjectResultEx<LoginUserView> loginUser(LoginUserParameter parameter);

    /**
     * 退出用户
     * @param parameter
     * @return
     */
     ResultEx logoutUser(LogoutUserParameter parameter);

    /**
     * 修改密码
     * @param parameter
     * @return
     */
     ResultEx modifyPassword(ModifyPasswordParameter parameter);
}
