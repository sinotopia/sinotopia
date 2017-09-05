package com.sinotopia.demonstration.admin.service.impl;

import com.sinotopia.demonstration.admin.bean.AdminMenu;
import com.sinotopia.demonstration.admin.bean.AdminRoleMenu;
import com.sinotopia.demonstration.admin.dao.AdminMenuDao;
import com.sinotopia.demonstration.admin.dao.AdminRoleMenuDao;
import com.sinotopia.demonstration.admin.enums.AdminStatusEnum;
import com.sinotopia.demonstration.admin.enums.AdminTypeEnum;
import com.sinotopia.demonstration.admin.param.*;
import com.sinotopia.demonstration.admin.result.AdminMenuView;
import com.sinotopia.demonstration.admin.result.AdminRoleMenuView;
import com.sinotopia.demonstration.admin.service.api.AdminMenuService;
import com.sinotopia.fundamental.api.data.ListResultEx;
import com.sinotopia.fundamental.api.data.ObjectResultEx;
import com.sinotopia.fundamental.api.data.OrderBy;
import com.sinotopia.fundamental.api.data.ResultEx;
import com.sinotopia.fundamental.common.utils.StrUtils;
import com.sinotopia.fundamental.database.page.PageList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 菜单服务
 */
@Service("adminMenuService")
public class AdminMenuServiceImpl implements AdminMenuService {

    @Autowired
    private AdminMenuDao adminMenuDao;

    @Autowired
    private AdminRoleMenuDao adminRoleMenuDao;

    @Override
    public ResultEx addMenu(AddMenuParameter parameter) {
        ResultEx resultEx = new ResultEx();

        Integer status = parameter.getStatus() != null ?
                parameter.getStatus() : AdminStatusEnum.ENABLED.getValue();
        if (AdminStatusEnum.parse(status) == null) {
            return resultEx.makeParameterErrorResult();
        }

        Date now = new Date();
        AdminMenu adminMenu = new AdminMenu();
        adminMenu.setUrl(parameter.getUrl());
        adminMenu.setName(parameter.getName());
        adminMenu.setParentId(parameter.getParentId());
        adminMenu.setSequence(parameter.getSequence());
        adminMenu.setStatus(status);
        adminMenu.setCreatedTime(now);
        adminMenu.setUpdatedTime(now);
        adminMenuDao.add(adminMenu);

        return resultEx.makeSuccessResult();
    }

    @Override
    public ResultEx deleteMenu(DeleteMenuParameter parameter) {
        ResultEx resultEx = new ResultEx();

        AdminMenu adminMenu = new AdminMenu();
        adminMenu.setId(parameter.getId());
        adminMenu.setStatus(AdminStatusEnum.DELETED.getValue());
        adminMenu.setUpdatedTime(new Date());
        adminMenuDao.update(adminMenu);

        return resultEx.makeSuccessResult();
    }

    @Override
    public ObjectResultEx<AdminMenuView> getMenu(GetMenuParameter parameter) {
        ObjectResultEx<AdminMenuView> resultEx = new ObjectResultEx<AdminMenuView>();

        AdminMenu adminMenu = adminMenuDao.getById(parameter.getId());
        if (adminMenu != null) {
            AdminMenuView adminMenuView = new AdminMenuView();
            BeanUtils.copyProperties(adminMenu, adminMenuView);
            resultEx.setDataObject(adminMenuView);
        }

        return resultEx.makeSuccessResult();
    }

    @Override
    public ResultEx updateMenu(UpdateMenuParameter parameter) {
        ResultEx resultEx = new ResultEx();

        if (parameter.getStatus() != null && AdminStatusEnum.parse(parameter.getStatus()) == null) {
            return resultEx.makeParameterErrorResult();
        }

        AdminMenu adminMenu = new AdminMenu();
        adminMenu.setId(parameter.getId());
        adminMenu.setUpdatedTime(new Date());
        adminMenu.setStatus(parameter.getStatus());
        adminMenu.setName(parameter.getName());
        adminMenu.setUrl(parameter.getUrl());
        adminMenu.setParentId(parameter.getParentId());
        adminMenu.setSequence(parameter.getSequence());
        adminMenuDao.update(adminMenu);

        return resultEx.makeSuccessResult();
    }

    @Override
    public ListResultEx<AdminMenuView> getMenuList(GetMenuListParameter parameter) {
        ListResultEx<AdminMenuView> resultEx = new ListResultEx<AdminMenuView>();

        AdminMenu adminMenu = new AdminMenu();
        adminMenu.setId(parameter.getId());
        adminMenu.setParentId(parameter.getParentId());
        adminMenu.setStatus(parameter.getStatus());
        adminMenu.setSequence(parameter.getSequence());

        adminMenu.putExtendedParameterValue("nameLike", parameter.getName());
        adminMenu.putExtendedParameterValue("excludedStatus", AdminStatusEnum.DELETED.getValue());

        adminMenu.orderBy(OrderBy.by("createdTime").desc());

        adminMenu.request(parameter.getRequestOffset(), parameter.getRequestCount());

        //直接返回不分组
//        PageList<AdminMenu> pageList = adminMenuDao.pageQuery(adminMenu);
//        resultEx.setTotalCount(pageList.getTotalCount());
//        resultEx.setDataList(pageList.toList(AdminMenuView.class));
//        return resultEx.makeSuccessResult();

        //分组
        PageList<AdminMenu> pageList = adminMenuDao.pageQuery(adminMenu);
        List<AdminMenuView> viewList = pageList.toList(AdminMenuView.class);
        Map<Long, List<AdminMenuView>> viewMap = new HashMap<>();
        for (AdminMenuView view : viewList) {
            List<AdminMenuView> list = viewMap.get(view.getParentId());
            if (list == null) {
                list = new ArrayList<>();
                viewMap.put(view.getParentId(), list);
            }
            list.add(view);
        }

        List<AdminMenuView> resultList = new ArrayList<>(viewList.size());
        Iterator<Map.Entry<Long, List<AdminMenuView>>> it = viewMap.entrySet().iterator();
        Map.Entry<Long, List<AdminMenuView>> entry = null;
        while (it.hasNext()) {
            resultList.addAll(it.next().getValue());
        }

        resultEx.setTotalCount(pageList.getTotalCount());
        resultEx.setDataList(resultList);

        return resultEx.makeSuccessResult();
    }

    @Override
    public ResultEx addRoleMenu(AddRoleMenuParameter parameter) {
        ResultEx resultEx = new ResultEx();

        Integer status = parameter.getStatus() != null ?
                parameter.getStatus() : AdminStatusEnum.ENABLED.getValue();
        if (AdminStatusEnum.parse(status) == null) {
            return resultEx.makeParameterErrorResult();
        }

        if (parameter.getMenuId() != null) {
            addRoleMenuByMenuId(parameter.getRoleId(), parameter.getMenuId());
        } else if (StrUtils.notEmpty(parameter.getMenuIds())) {
            addRoleMenuByMenuIds(parameter.getRoleId(), parameter.getMenuIds());
        } else {
            return resultEx.makeParameterErrorResult();
        }

        return resultEx.makeSuccessResult();
    }

    private void addRoleMenuByMenuIds(Long roleId, String menuIds) {
        Set<Long> menuIdSet = getMenuIdSet(menuIds);
        List<AdminRoleMenu> roleMenuList = getRoleMenuListByRoleId(roleId);

        for (AdminRoleMenu each : roleMenuList) {
            if (menuIdSet.contains(each.getMenuId())) {
                enableAdminRoleMenu(each);
                menuIdSet.remove(each.getMenuId());
            } else {
                adminRoleMenuDao.delete(each.getId());
            }
        }

        if (menuIdSet.size() > 0) {
            Iterator<Long> it = menuIdSet.iterator();
            while (it.hasNext()) {
                addRoleMenuByMenuId(roleId, it.next());
            }
        }
    }

    private void enableAdminRoleMenu(AdminRoleMenu menu) {
        if (menu.getStatus() == AdminStatusEnum.DISABLED.getValue()) {
            AdminRoleMenu adminRoleMenu = new AdminRoleMenu();
            adminRoleMenu.setId(menu.getId());
            adminRoleMenu.setStatus(AdminStatusEnum.ENABLED.getValue());
            adminRoleMenu.setUpdatedTime(new Date());
            adminRoleMenuDao.update(adminRoleMenu);
        }
    }

    private List<AdminRoleMenu> getRoleMenuListByRoleId(Long roleId) {
        AdminRoleMenu adminRoleMenu = new AdminRoleMenu();
        adminRoleMenu.setRoleId(roleId);
        return adminRoleMenuDao.query(adminRoleMenu);
    }

    private Set<Long> getMenuIdSet(String menuIds) {
        String[] array = menuIds.split(",");
        Set<Long> menuIdSet = new HashSet<>(array.length);
        for (String each : array) {
            menuIdSet.add(Long.parseLong(each));
        }
        return menuIdSet;
    }

    private void addRoleMenuByMenuId(Long roleId, Long menuId) {
        if (!isRoleMenuExists(roleId, menuId)) {
            Date now = new Date();
            AdminRoleMenu adminRoleMenu = new AdminRoleMenu();
            adminRoleMenu.setRoleId(roleId);
            adminRoleMenu.setMenuId(menuId);
            adminRoleMenu.setStatus(AdminStatusEnum.ENABLED.getValue());
            adminRoleMenu.setCreatedTime(now);
            adminRoleMenu.setUpdatedTime(now);
            adminRoleMenuDao.add(adminRoleMenu);
        }
    }

    private boolean isRoleMenuExists(Long roleId, Long menuId) {
        AdminRoleMenu adminRoleMenu = new AdminRoleMenu();
        adminRoleMenu.setRoleId(roleId);
        adminRoleMenu.setMenuId(menuId);
        return adminRoleMenuDao.count(adminRoleMenu) > 0;
    }

    @Override
    public ResultEx deleteRoleMenu(DeleteRoleMenuParameter parameter) {
        ResultEx resultEx = new ResultEx();

        adminRoleMenuDao.delete(parameter.getId());

        return resultEx.makeSuccessResult();
    }

    @Override
    public ObjectResultEx<AdminRoleMenuView> getRoleMenu(GetRoleMenuParameter parameter) {
        ObjectResultEx<AdminRoleMenuView> resultEx = new ObjectResultEx<AdminRoleMenuView>();

        AdminRoleMenu adminRoleMenu = new AdminRoleMenu();
        adminRoleMenu.setId(parameter.getId());
        List<AdminRoleMenuView> resultList = adminRoleMenuDao.queryRoleMenuList(adminRoleMenu);
        if (resultList != null && resultList.size() > 0) {
            resultEx.setDataObject(resultList.get(0));
        }
        return resultEx.makeSuccessResult();
    }

    @Override
    public ResultEx updateRoleMenu(UpdateRoleMenuParameter parameter) {
        ResultEx resultEx = new ResultEx();

        if (parameter.getStatus() != null && AdminStatusEnum.parse(parameter.getStatus()) == null) {
            return resultEx.makeParameterErrorResult();
        }

        AdminRoleMenu adminRoleMenu = new AdminRoleMenu();
        adminRoleMenu.setId(parameter.getId());
        adminRoleMenu.setUpdatedTime(new Date());
        adminRoleMenu.setStatus(parameter.getStatus());
        adminRoleMenuDao.update(adminRoleMenu);

        return resultEx.makeSuccessResult();
    }

    @Override
    public ListResultEx<AdminRoleMenuView> getRoleMenuList(GetRoleMenuListParameter parameter) {
        ListResultEx<AdminRoleMenuView> resultEx = new ListResultEx<AdminRoleMenuView>();

        AdminRoleMenu adminRoleMenu = new AdminRoleMenu();
        adminRoleMenu.setId(parameter.getId());
        adminRoleMenu.setStatus(parameter.getStatus());
        adminRoleMenu.setMenuId(parameter.getMenuId());
        adminRoleMenu.setRoleId(parameter.getRoleId());

        adminRoleMenu.orderBy(OrderBy.by("createdTime").desc());

        adminRoleMenu.putExtendedParameterValue("menuStatus", AdminStatusEnum.ENABLED.getValue());

        adminRoleMenu.request(parameter.getRequestOffset(), parameter.getRequestCount());

        PageList<AdminRoleMenuView> pageList = adminRoleMenuDao.queryRoleMenuList(adminRoleMenu);
        resultEx.setTotalCount(pageList.getTotalCount());
        resultEx.setDataList(pageList);
        return resultEx.makeSuccessResult();
    }

    @Override
    public ListResultEx<AdminMenuView> getSessionMenuList(GetSessionMenuListParameter parameter) {
        ListResultEx<AdminMenuView> resultEx = new ListResultEx<AdminMenuView>();

        if (AdminTypeEnum.isAdministrator(parameter.getSessionIdentity().getUserType())) {
            GetMenuListParameter getMenuListParameter = new GetMenuListParameter();
            getMenuListParameter.setRequestOffset(0);
            getMenuListParameter.setRequestCount(100);
            return getMenuList(getMenuListParameter);
        }

        String[] roles = parameter.getSessionIdentity().getRoles();
        if (roles == null || roles.length == 0) {
            return resultEx.makeAuthorizationErrorResult();
        }

        AdminRoleMenu adminRoleMenu = new AdminRoleMenu();
        adminRoleMenu.putExtendedParameterValue("roleCodes", roles);
        PageList<AdminRoleMenuView> pageList = adminRoleMenuDao.queryRoleMenuList(adminRoleMenu);

        //转换对象
        List<AdminMenuView> resultList = new ArrayList<AdminMenuView>(pageList.size());
        for (AdminRoleMenuView each : pageList) {
            AdminMenuView adminMenuView = new AdminMenuView();
            adminMenuView.setId(each.getMenuId());
            adminMenuView.setName(each.getMenuName());
            adminMenuView.setUrl(each.getMenuUrl());
            adminMenuView.setSequence(each.getMenuSequence());
            adminMenuView.setCreatedTime(each.getCreatedTime());
            adminMenuView.setUpdatedTime(each.getUpdatedTime());
            adminMenuView.setStatus(each.getMenuStatus());
            adminMenuView.setParentId(each.getMenuParentId());
            resultList.add(adminMenuView);
        }

        resultEx.setDataList(resultList);
        return resultEx.makeSuccessResult();
    }
}
