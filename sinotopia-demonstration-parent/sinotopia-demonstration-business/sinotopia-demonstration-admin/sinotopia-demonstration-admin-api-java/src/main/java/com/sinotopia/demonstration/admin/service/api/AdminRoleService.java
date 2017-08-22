package com.sinotopia.demonstration.admin.service.api;

import com.sinotopia.demonstration.admin.param.*;
import com.sinotopia.demonstration.admin.result.AdminRoleView;
import com.sinotopia.demonstration.admin.result.AdminUserRoleView;
import com.sinotopia.demonstration.admin.param.*;
import com.sinotopia.fundamental.api.data.ListResultEx;
import com.sinotopia.fundamental.api.data.ObjectResultEx;
import com.sinotopia.fundamental.api.data.ResultEx;

/**
 * 角色服务
 */
public interface AdminRoleService {

    /**
     * 新增角色
     * @param parameter
     * @return
     */
     ResultEx addRole(AddRoleParameter parameter);

    /**
     * 删除角色
     * @param parameter
     * @return
     */
     ResultEx deleteRole(DeleteRoleParameter parameter);

    /**
     * 获取角色
     * @param parameter
     * @return
     */
     ObjectResultEx<AdminRoleView> getRole(GetRoleParameter parameter);

    /**
     * 更新角色
     * @param parameter
     * @return
     */
     ResultEx updateRole(UpdateRoleParameter parameter);

    /**
     * 获取角色列表
     * @param parameter
     * @return
     */
     ListResultEx<AdminRoleView> getRoleList(GetRoleListParameter parameter);


    /**
     * 新增用户对应角色
     * @param parameter
     * @return
     */
     ResultEx addUserRole(AddUserRoleParameter parameter);

    /**
     * 删除用户对应角色
     * @param parameter
     * @return
     */
     ResultEx deleteUserRole(DeleteUserRoleParameter parameter);

    /**
     * 获取用户对应角色
     * @param parameter
     * @return
     */
     ObjectResultEx<AdminUserRoleView> getUserRole(GetUserRoleParameter parameter);

    /**
     * 更新用户对应角色
     * @param parameter
     * @return
     */
     ResultEx updateUserRole(UpdateUserRoleParameter parameter);

    /**
     * 获取用户对应角色列表
     * @param parameter
     * @return
     */
     ListResultEx<AdminUserRoleView> getUserRoleList(GetUserRoleListParameter parameter);
}
