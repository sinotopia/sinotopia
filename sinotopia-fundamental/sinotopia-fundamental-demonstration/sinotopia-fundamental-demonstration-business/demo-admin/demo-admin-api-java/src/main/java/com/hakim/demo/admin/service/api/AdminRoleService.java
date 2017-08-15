package com.hakim.demo.admin.service.api;

import com.hakim.demo.admin.param.*;
import com.hakim.demo.admin.result.AdminRoleView;
import com.hakim.demo.admin.result.AdminUserRoleView;
import com.sinotopia.fundamental.api.data.ListResultEx;
import com.sinotopia.fundamental.api.data.ObjectResultEx;
import com.sinotopia.fundamental.api.data.ResultEx;

/**
 * 角色服务
 * Created by brucezee on 2017/3/1.
 */
public interface AdminRoleService {
    /**
     * 新增角色
     * @param parameter
     * @return
     */
    public ResultEx addRole(AddRoleParameter parameter);

    /**
     * 删除角色
     * @param parameter
     * @return
     */
    public ResultEx deleteRole(DeleteRoleParameter parameter);

    /**
     * 获取角色
     * @param parameter
     * @return
     */
    public ObjectResultEx<AdminRoleView> getRole(GetRoleParameter parameter);

    /**
     * 更新角色
     * @param parameter
     * @return
     */
    public ResultEx updateRole(UpdateRoleParameter parameter);

    /**
     * 获取角色列表
     * @param parameter
     * @return
     */
    public ListResultEx<AdminRoleView> getRoleList(GetRoleListParameter parameter);


    /**
     * 新增用户对应角色
     * @param parameter
     * @return
     */
    public ResultEx addUserRole(AddUserRoleParameter parameter);

    /**
     * 删除用户对应角色
     * @param parameter
     * @return
     */
    public ResultEx deleteUserRole(DeleteUserRoleParameter parameter);

    /**
     * 获取用户对应角色
     * @param parameter
     * @return
     */
    public ObjectResultEx<AdminUserRoleView> getUserRole(GetUserRoleParameter parameter);

    /**
     * 更新用户对应角色
     * @param parameter
     * @return
     */
    public ResultEx updateUserRole(UpdateUserRoleParameter parameter);

    /**
     * 获取用户对应角色列表
     * @param parameter
     * @return
     */
    public ListResultEx<AdminUserRoleView> getUserRoleList(GetUserRoleListParameter parameter);
}
