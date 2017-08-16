package com.hakim.demo.admin.web.controller;

import com.hakim.demo.admin.param.*;
import com.hakim.demo.admin.result.AdminRoleView;
import com.hakim.demo.admin.result.AdminUserRoleView;
import com.hakim.demo.admin.service.api.AdminRoleService;
import com.sinotopia.fundamental.api.data.ListResultEx;
import com.sinotopia.fundamental.api.data.ObjectResultEx;
import com.sinotopia.fundamental.api.data.ResultEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理
 * Created by brucezee on 2017/3/1.
 */
@RestController
@RequestMapping("/admin/role")
public class AdminRoleController {
    @Autowired
    private AdminRoleService adminRoleService;

    @RequestMapping(value = "/addRole", name = "新增角色")
    public ResultEx addRole(AddRoleParameter parameter) {
        return adminRoleService.addRole(parameter);
    }

    @RequestMapping(value = "/deleteRole", name = "删除角色")
    public ResultEx deleteRole(DeleteRoleParameter parameter) {
        return adminRoleService.deleteRole(parameter);
    }

    @RequestMapping(value = "/getRole", name = "获取角色")
    public ObjectResultEx<AdminRoleView> getRole(GetRoleParameter parameter) {
        return adminRoleService.getRole(parameter);
    }

    @RequestMapping(value = "/updateRole", name = "更新角色")
    public ResultEx updateRole(UpdateRoleParameter parameter) {
        return adminRoleService.updateRole(parameter);
    }

    @RequestMapping(value = "/getRoleList", name = "获取角色列表")
    public ListResultEx<AdminRoleView> getRoleList(GetRoleListParameter parameter) {
        return adminRoleService.getRoleList(parameter);
    }

    @RequestMapping(value = "/addUserRole", name = "新增用户对应角色")
    public ResultEx addUserRole(AddUserRoleParameter parameter) {
        return adminRoleService.addUserRole(parameter);
    }

    @RequestMapping(value = "/deleteUserRole", name = "删除用户对应角色")
    public ResultEx deleteUserRole(DeleteUserRoleParameter parameter) {
        return adminRoleService.deleteUserRole(parameter);
    }

    @RequestMapping(value = "/getUserRole", name = "获取用户对应角色")
    public ObjectResultEx<AdminUserRoleView> getUserRole(GetUserRoleParameter parameter) {
        return adminRoleService.getUserRole(parameter);
    }

    @RequestMapping(value = "/updateUserRole", name = "更新用户对应角色")
    public ResultEx updateUserRole(UpdateUserRoleParameter parameter) {
        return adminRoleService.updateUserRole(parameter);
    }

    @RequestMapping(value = "/getUserRoleList", name = "获取用户对应角色列表")
    public ListResultEx<AdminUserRoleView> getUserRoleList(GetUserRoleListParameter parameter) {
        return adminRoleService.getUserRoleList(parameter);
    }
}
