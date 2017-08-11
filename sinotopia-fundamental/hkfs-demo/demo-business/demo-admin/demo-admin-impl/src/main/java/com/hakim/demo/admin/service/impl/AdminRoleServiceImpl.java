package com.hakim.demo.admin.service.impl;

import com.hakim.demo.admin.bean.AdminRole;
import com.hakim.demo.admin.bean.AdminUserRole;
import com.hakim.demo.admin.dao.AdminRoleDao;
import com.hakim.demo.admin.dao.AdminUserRoleDao;
import com.hakim.demo.admin.enums.AdminResultEnum;
import com.hakim.demo.admin.enums.AdminStatusEnum;
import com.hakim.demo.admin.result.AdminRoleView;
import com.hakim.demo.admin.result.AdminUserRoleView;
import com.hakim.demo.admin.service.api.AdminRoleService;
import com.hakim.demo.admin.param.*;
import com.hkfs.fundamental.api.data.ListResultEx;
import com.hkfs.fundamental.api.data.ObjectResultEx;
import com.hkfs.fundamental.api.data.OrderBy;
import com.hkfs.fundamental.api.data.ResultEx;
import com.hkfs.fundamental.common.utils.StrUtils;
import com.hkfs.fundamental.database.page.PageList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 角色服务
 * Created by brucezee on 2017/3/1.
 */
@Service("adminRoleService")
public class AdminRoleServiceImpl implements AdminRoleService {
    @Autowired
    private AdminRoleDao adminRoleDao;
    @Autowired
    private AdminUserRoleDao adminUserRoleDao;

    @Override
    public ResultEx addRole(AddRoleParameter parameter) {
        ResultEx resultEx = new ResultEx();

        Integer status = parameter.getStatus() != null ?
                parameter.getStatus() : AdminStatusEnum.ENABLED.getValue();
        if (AdminStatusEnum.parse(status) == null) {
            return resultEx.makeParameterErrorResult();
        }

        if (isAdminRoleCodeExists(parameter.getCode())) {
            return resultEx.makeFailedResult(AdminResultEnum.ROLE_CODE_EXISTS.getComment());
        }

        Date now = new Date();
        AdminRole adminRole = new AdminRole();
        adminRole.setCode(parameter.getCode());
        adminRole.setName(parameter.getName());
        adminRole.setDescription(parameter.getDescription());
        adminRole.setStatus(status);
        adminRole.setCreatedTime(now);
        adminRole.setUpdatedTime(now);
        adminRoleDao.add(adminRole);

        return resultEx.makeSuccessResult();
    }

    private boolean isAdminRoleCodeExists(String roleCode) {
        return getAdminRoleByCode(roleCode) != null;
    }

    private AdminRole getAdminRoleByCode(String roleCode) {
        AdminRole adminRole = new AdminRole();
        adminRole.setCode(roleCode);
        List<AdminRole> list = adminRoleDao.query(adminRole);
        if (list != null && list.size() > 1) {
            throw new IllegalStateException(AdminResultEnum.ROLE_CODE_EXISTS.getComment());
        }
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public ResultEx deleteRole(DeleteRoleParameter parameter) {
        ResultEx resultEx = new ResultEx();

        AdminRole adminRole = new AdminRole();
        adminRole.setId(parameter.getId());
        adminRole.setStatus(AdminStatusEnum.DELETED.getValue());
        adminRole.setUpdatedTime(new Date());
        adminRoleDao.update(adminRole);

        return resultEx.makeSuccessResult();
    }

    @Override
    public ObjectResultEx<AdminRoleView> getRole(GetRoleParameter parameter) {
        ObjectResultEx<AdminRoleView> resultEx = new ObjectResultEx<AdminRoleView>();

        AdminRole adminRole = adminRoleDao.getById(parameter.getId());
        if (adminRole != null) {
            AdminRoleView adminRoleView = new AdminRoleView();
            BeanUtils.copyProperties(adminRole, adminRoleView);
            resultEx.setDataObject(adminRoleView);
        }

        return resultEx.makeSuccessResult();
    }

    @Override
    public ResultEx updateRole(UpdateRoleParameter parameter) {
        ResultEx resultEx = new ResultEx();

        if (parameter.getStatus() != null && AdminStatusEnum.parse(parameter.getStatus()) == null) {
            return resultEx.makeParameterErrorResult();
        }

        AdminRole existingAdminRole = adminRoleDao.getById(parameter.getId());
        if (existingAdminRole == null) {
            return resultEx.makeFailedResult(AdminResultEnum.NOT_FOUND.getComment());
        }
        if (StrUtils.notEmpty(parameter.getCode())) {
            AdminRole foundAdminRole = getAdminRoleByCode(parameter.getCode());
            if (foundAdminRole != null && !foundAdminRole.getId().equals(existingAdminRole.getId())) {
                return resultEx.makeFailedResult(AdminResultEnum.ROLE_CODE_EXISTS.getComment());
            }
        }

        AdminRole adminRole = new AdminRole();
        adminRole.setId(existingAdminRole.getId());
        adminRole.setUpdatedTime(new Date());
        adminRole.setStatus(parameter.getStatus());
        adminRole.setName(parameter.getName());
        adminRole.setCode(parameter.getCode());
        adminRole.setDescription(parameter.getDescription());
        adminRoleDao.update(adminRole);

        return resultEx.makeSuccessResult();
    }

    @Override
    public ListResultEx<AdminRoleView> getRoleList(GetRoleListParameter parameter) {
        ListResultEx<AdminRoleView> resultEx = new ListResultEx<AdminRoleView>();

        AdminRole adminRole = new AdminRole();
        adminRole.setId(parameter.getId());
        adminRole.setStatus(parameter.getStatus());
        adminRole.setCode(parameter.getCode());

        adminRole.putExtendedParameterValue("nameLike", parameter.getName());
        adminRole.putExtendedParameterValue("excludedStatus", AdminStatusEnum.DELETED.getValue());

        adminRole.orderBy(OrderBy.by("createdTime").desc());

        adminRole.request(parameter.getRequestOffset(), parameter.getRequestCount());

        PageList<AdminRole> pageList = adminRoleDao.pageQuery(adminRole);
        resultEx.setTotalCount(pageList.getTotalCount());
        resultEx.setDataList(pageList.toList(AdminRoleView.class));
        return resultEx.makeSuccessResult();
    }

    @Override
    public ResultEx addUserRole(AddUserRoleParameter parameter) {
        ResultEx resultEx = new ResultEx();

        Integer status = parameter.getStatus() != null ?
                parameter.getStatus() : AdminStatusEnum.ENABLED.getValue();
        if (AdminStatusEnum.parse(status) == null) {
            return resultEx.makeParameterErrorResult();
        }

        //判断用户是否已经存在该角色
        if (isAdminRoleExists(parameter.getUserId(), parameter.getRoleId())) {
            return resultEx.makeFailedResult(AdminResultEnum.USER_ROLE_EXISTS.getComment());
        }

        Date now = new Date();
        AdminUserRole adminUserRole = new AdminUserRole();
        adminUserRole.setUserId(parameter.getUserId());
        adminUserRole.setRoleId(parameter.getRoleId());
        adminUserRole.setStatus(status);
        adminUserRole.setCreatedTime(now);
        adminUserRole.setUpdatedTime(now);
        adminUserRoleDao.add(adminUserRole);

        return resultEx.makeSuccessResult();
    }

    private boolean isAdminRoleExists(Long userId, Long roleId) {
        AdminUserRole adminUserRole = new AdminUserRole();
        adminUserRole.setUserId(userId);
        adminUserRole.setRoleId(roleId);
        return adminUserRoleDao.count(adminUserRole) > 0;
    }

    @Override
    public ResultEx deleteUserRole(DeleteUserRoleParameter parameter) {
        ResultEx resultEx = new ResultEx();

        adminUserRoleDao.delete(parameter.getId());

        return resultEx.makeSuccessResult();
    }

    @Override
    public ObjectResultEx<AdminUserRoleView> getUserRole(GetUserRoleParameter parameter) {
        ObjectResultEx<AdminUserRoleView> resultEx = new ObjectResultEx<AdminUserRoleView>();

        AdminUserRole adminUserRole = new AdminUserRole();
        adminUserRole.setId(parameter.getId());
        List<AdminUserRoleView> resultList = adminUserRoleDao.queryUserRoleList(adminUserRole);
        if (resultList != null && resultList.size() > 0) {
            resultEx.setDataObject(resultList.get(0));
        }
        return resultEx.makeSuccessResult();
    }

    @Override
    public ResultEx updateUserRole(UpdateUserRoleParameter parameter) {
        ResultEx resultEx = new ResultEx();

        if (parameter.getStatus() != null && AdminStatusEnum.parse(parameter.getStatus()) == null) {
            return resultEx.makeParameterErrorResult();
        }

        AdminUserRole adminUserRole = new AdminUserRole();
        adminUserRole.setId(parameter.getId());
        adminUserRole.setUpdatedTime(new Date());
        adminUserRole.setStatus(parameter.getStatus());
        adminUserRoleDao.update(adminUserRole);

        return resultEx.makeSuccessResult();
    }

    @Override
    public ListResultEx<AdminUserRoleView> getUserRoleList(GetUserRoleListParameter parameter) {
        ListResultEx<AdminUserRoleView> resultEx = new ListResultEx<AdminUserRoleView>();

        AdminUserRole adminUserRole = new AdminUserRole();
        adminUserRole.setId(parameter.getId());
        adminUserRole.setStatus(parameter.getStatus());
        adminUserRole.setUserId(parameter.getUserId());
        adminUserRole.setRoleId(parameter.getRoleId());

        adminUserRole.putExtendedParameterValue("userIds", parameter.getUserIds());
        adminUserRole.putExtendedParameterValue("roleStatus", AdminStatusEnum.ENABLED.getValue());

        adminUserRole.orderBy(OrderBy.by("createdTime").desc());

        adminUserRole.request(parameter.getRequestOffset(), parameter.getRequestCount());

        PageList<AdminUserRoleView> pageList = adminUserRoleDao.queryUserRoleList(adminUserRole);
        resultEx.setTotalCount(pageList.getTotalCount());
        resultEx.setDataList(pageList);
        return resultEx.makeSuccessResult();
    }
}
