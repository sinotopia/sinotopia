package com.hakim.demo.admin.web.controller;

import com.hakim.demo.admin.param.*;
import com.hakim.demo.admin.result.AdminMenuView;
import com.hakim.demo.admin.result.AdminRoleMenuView;
import com.hakim.demo.admin.service.api.AdminMenuService;
import com.hkfs.fundamental.api.data.ListResultEx;
import com.hkfs.fundamental.api.data.ObjectResultEx;
import com.hkfs.fundamental.api.data.ResultEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单管理
 * Created by brucezee on 2017/3/1.
 */
@RestController
@RequestMapping("/admin/menu")
public class AdminMenuController {
    @Autowired
    private AdminMenuService adminMenuService;

    @RequestMapping(value = "/addMenu", name = "新增菜单")
    public ResultEx addMenu(AddMenuParameter parameter) {
        return adminMenuService.addMenu(parameter);
    }

    @RequestMapping(value = "/deleteMenu", name = "删除菜单")
    public ResultEx deleteMenu(DeleteMenuParameter parameter) {
        return adminMenuService.deleteMenu(parameter);
    }

    @RequestMapping(value = "/getMenu", name = "获取菜单")
    public ObjectResultEx<AdminMenuView> getMenu(GetMenuParameter parameter) {
        return adminMenuService.getMenu(parameter);
    }

    @RequestMapping(value = "/updateMenu", name = "更新菜单")
    public ResultEx updateMenu(UpdateMenuParameter parameter) {
        return adminMenuService.updateMenu(parameter);
    }

    @RequestMapping(value = "/getMenuList", name = "获取菜单列表")
    public ListResultEx<AdminMenuView> getMenuList(GetMenuListParameter parameter) {
        return adminMenuService.getMenuList(parameter);
    }

    @RequestMapping(value = "/addRoleMenu", name = "新增角色对应菜单")
    public ResultEx addRoleMenu(AddRoleMenuParameter parameter) {
        return adminMenuService.addRoleMenu(parameter);
    }

    @RequestMapping(value = "/deleteRoleMenu", name = "删除角色对应菜单")
    public ResultEx deleteRoleMenu(DeleteRoleMenuParameter parameter) {
        return adminMenuService.deleteRoleMenu(parameter);
    }

    @RequestMapping(value = "/getRoleMenu", name = "获取角色对应菜单")
    public ObjectResultEx<AdminRoleMenuView> getRoleMenu(GetRoleMenuParameter parameter) {
        return adminMenuService.getRoleMenu(parameter);
    }

    @RequestMapping(value = "/updateRoleMenu", name = "更新角色对应菜单")
    public ResultEx updateRoleMenu(UpdateRoleMenuParameter parameter) {
        return adminMenuService.updateRoleMenu(parameter);
    }

    @RequestMapping(value = "/getRoleMenuList", name = "获取角色对应菜单列表")
    public ListResultEx<AdminRoleMenuView> getRoleMenuList(GetRoleMenuListParameter parameter) {
        return adminMenuService.getRoleMenuList(parameter);
    }

    @RequestMapping(value = "/getSessionMenuList", name = "获取当前用户菜单列表")
    public ListResultEx<AdminMenuView> getSessionMenuList(GetSessionMenuListParameter parameter) {
        return adminMenuService.getSessionMenuList(parameter);
    }
}
