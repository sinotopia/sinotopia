package com.sinotopia.demonstration.admin.service.impl;

import com.sinotopia.demonstration.admin.bean.AdminUser;
import com.sinotopia.demonstration.admin.bean.AdminUserRole;
import com.sinotopia.demonstration.admin.dao.AdminUserDao;
import com.sinotopia.demonstration.admin.dao.AdminUserRoleDao;
import com.sinotopia.demonstration.admin.enums.AdminResultEnum;
import com.sinotopia.demonstration.admin.enums.AdminStatusEnum;
import com.sinotopia.demonstration.admin.enums.AdminTypeEnum;
import com.sinotopia.demonstration.admin.param.*;
import com.sinotopia.demonstration.admin.result.AdminRoleView;
import com.sinotopia.demonstration.admin.result.AdminUserRoleView;
import com.sinotopia.demonstration.admin.result.AdminUserView;
import com.sinotopia.demonstration.admin.result.LoginUserView;
import com.sinotopia.demonstration.admin.service.api.AdminRoleService;
import com.sinotopia.demonstration.admin.service.api.AdminUserService;
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
 * 用户服务
 */
@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserDao adminUserDao;

    @Autowired
    private AdminUserRoleDao adminUserRoleDao;

    @Autowired
    private AdminRoleService adminRoleService;


    @Override
    public ResultEx addUser(AddUserParameter parameter) {
        ResultEx resultEx = new ResultEx();

        if (!AdminTypeEnum.isAdministrator(parameter.getSessionIdentity().getUserType())) {
            return resultEx.makeAuthorizationErrorResult();
        }
        Integer status = parameter.getStatus() != null ?
                parameter.getStatus() : AdminStatusEnum.ENABLED.getValue();
        if (AdminStatusEnum.parse(status) == null) {
            return resultEx.makeParameterErrorResult();
        }
        if (isUsernameExists(parameter.getUsername())) {
            return resultEx.makeFailedResult(AdminResultEnum.USERNAME_EXISTS.getComment());
        }

        Date now = new Date();
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(parameter.getUsername());
        adminUser.setName(parameter.getName());
        adminUser.setPassword(StrUtils.getMd5(parameter.getPassword()));
        adminUser.setPhone(parameter.getPhone());
        adminUser.setEmail(parameter.getEmail());
        adminUser.setStatus(status);
        adminUser.setCreatedTime(now);
        adminUser.setUpdatedTime(now);
        adminUser.setType(AdminTypeEnum.MANAGER.getValue());//只能添加普通管理员
        adminUserDao.add(adminUser);

        return resultEx.makeSuccessResult();
    }

    private boolean isUsernameExists(String username) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(username);

        adminUser.putExtendedParameterValue("excludedStatus", AdminStatusEnum.DELETED.getValue());
        return adminUserDao.count(adminUser) > 0;
    }

    @Override
    public ResultEx deleteUser(DeleteUserParameter parameter) {
        ResultEx resultEx = new ResultEx();

        if (!AdminTypeEnum.isAdministrator(parameter.getSessionIdentity().getUserType())) {
            return resultEx.makeAuthorizationErrorResult();
        }

        AdminUser adminUser = new AdminUser();
        adminUser.setId(parameter.getId());
        adminUser.setStatus(AdminStatusEnum.DELETED.getValue());
        adminUser.setUpdatedTime(new Date());
        adminUserDao.update(adminUser);

        return resultEx.makeSuccessResult();
    }

    @Override
    public ObjectResultEx<AdminUserView> getUser(GetUserParameter parameter) {
        ObjectResultEx<AdminUserView> resultEx = new ObjectResultEx<AdminUserView>();

        AdminUser adminUser = adminUserDao.getById(parameter.getId());
        if (adminUser != null) {
            AdminUserView adminUserView = new AdminUserView();
            BeanUtils.copyProperties(adminUser, adminUserView);
            resultEx.setDataObject(adminUserView);
        }

        return resultEx.makeSuccessResult();
    }

    @Override
    public ResultEx updateUser(UpdateUserParameter parameter) {
        ResultEx resultEx = new ResultEx();

        if (!AdminTypeEnum.isAdministrator(parameter.getSessionIdentity().getUserType())) {
            return resultEx.makeAuthorizationErrorResult();
        }

        if (parameter.getStatus() != null && AdminStatusEnum.parse(parameter.getStatus()) == null) {
            return resultEx.makeParameterErrorResult();
        }

        AdminUser existingAdminUser = adminUserDao.getById(parameter.getId());
        if (existingAdminUser == null) {
            return resultEx.makeFailedResult(AdminResultEnum.NOT_FOUND.getComment());
        }
        if (existingAdminUser.getStatus() == AdminStatusEnum.DELETED.getValue()) {
            return resultEx.makeFailedResult(AdminResultEnum.RECORD_DELETED.getComment());
        }

        AdminUser adminUser = new AdminUser();
        adminUser.setId(existingAdminUser.getId());
        adminUser.setUpdatedTime(new Date());
        adminUser.setStatus(parameter.getStatus());
        adminUser.setEmail(parameter.getEmail());
        adminUser.setName(parameter.getName());
        adminUser.setPhone(parameter.getPhone());
        if (StrUtils.notEmpty(parameter.getPassword())) {
            adminUser.setPassword(StrUtils.getMd5(parameter.getPassword()));
        }
        adminUserDao.update(adminUser);

        return resultEx.makeSuccessResult();
    }

    @Override
    public ListResultEx<AdminUserView> getUserList(GetUserListParameter parameter) {
        ListResultEx<AdminUserView> resultEx = new ListResultEx<AdminUserView>();

        AdminUser adminUser = new AdminUser();
        adminUser.setId(parameter.getId());
        adminUser.setUsername(parameter.getUsername());
        adminUser.setEmail(parameter.getEmail());
        adminUser.setPhone(parameter.getPhone());
        adminUser.setStatus(parameter.getStatus());
        adminUser.setType(AdminTypeEnum.MANAGER.getValue());

        adminUser.putExtendedParameterValue("nameLike", parameter.getName());
        adminUser.putExtendedParameterValue("excludedStatus", AdminStatusEnum.DELETED.getValue());

        adminUser.orderBy(OrderBy.by("createdTime").desc());

        adminUser.request(parameter.getRequestOffset(), parameter.getRequestCount());

        PageList<AdminUser> pageList = adminUserDao.pageQuery(adminUser);
        List<AdminUserView> resultList = pageList.toList(AdminUserView.class);
        if (resultList != null && resultList.size() > 0) {
            List<Long> userIds = getUserIdsFromList(resultList);
            Map<Long, List<AdminRoleView>> roleMap = getUserRoleMap(userIds);
            for (AdminUserView each : resultList) {
                each.setRoles(roleMap.get(each.getId()));
            }
        }

        resultEx.setTotalCount(pageList.getTotalCount());
        resultEx.setDataList(resultList);
        return resultEx.makeSuccessResult();
    }

    private List<Long> getUserIdsFromList(List<AdminUserView> resultList) {
        List<Long> list = new ArrayList<>(resultList.size());
        for (AdminUserView each : resultList) {
            list.add(each.getId());
        }
        return list;
    }

    private Map<Long, List<AdminRoleView>> getUserRoleMap(List<Long> userIds) {
        Map<Long, List<AdminRoleView>> roleMap = new HashMap<>();

        GetUserRoleListParameter parameter = new GetUserRoleListParameter();
        parameter.setUserIds(userIds);
        ListResultEx<AdminUserRoleView> resultEx = adminRoleService.getUserRoleList(parameter);
        List<AdminUserRoleView> resultList = resultEx.getDataList();
        if (resultList != null) {
            for (AdminUserRoleView each : resultList) {
                AdminRoleView view = new AdminRoleView();
                view.setId(each.getRoleId());
                view.setCode(each.getRoleCode());
                view.setName(each.getRoleName());

                List<AdminRoleView> list = roleMap.get(each.getUserId());
                if (list == null) {
                    list = new ArrayList<>();
                    roleMap.put(each.getUserId(), list);
                }
                list.add(view);
            }
        }
        return roleMap;
    }

    @Override
    public ObjectResultEx<LoginUserView> loginUser(LoginUserParameter parameter) {
        ObjectResultEx<LoginUserView> resultEx = new ObjectResultEx<LoginUserView>();

        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(parameter.getUsername());
        adminUser.setPassword(StrUtils.getMd5(parameter.getPassword()));

        adminUser.putExtendedParameterValue("excludedStatus", AdminStatusEnum.DELETED.getValue());

        AdminUser existingAdminUser = adminUserDao.get(adminUser);
        if (existingAdminUser == null) {
            return resultEx.makeResult(AdminResultEnum.USERNAME_OR_PASSWORD_ERROR.getValue(),
                    AdminResultEnum.USERNAME_OR_PASSWORD_ERROR.getComment());
        }
        if (existingAdminUser.getStatus() != AdminStatusEnum.ENABLED.getValue()) {
            return resultEx.makeFailedResult(AdminResultEnum.USER_DISABLED.getComment());
        }

        Date now = new Date();
        AdminUser updateAdminUser = new AdminUser();
        updateAdminUser.setId(existingAdminUser.getId());
        updateAdminUser.setLastLoginTime(now);
        updateAdminUser.setUpdatedTime(now);
        adminUserDao.update(updateAdminUser);

        String[] roles = getUserRoles(existingAdminUser.getId());

        LoginUserView loginUserView = new LoginUserView();
        BeanUtils.copyProperties(existingAdminUser, loginUserView);
        loginUserView.setRoles(roles);

        resultEx.setDataObject(loginUserView);
        return resultEx.makeSuccessResult();
    }

    private String[] getUserRoles(Long userId) {
        AdminUserRole adminUserRole = new AdminUserRole();
        adminUserRole.setUserId(userId);
        adminUserRole.setStatus(AdminStatusEnum.ENABLED.getValue());
        List<AdminUserRoleView> roleList = adminUserRoleDao.queryUserRoleList(adminUserRole);
        List<String> userRoleList = new ArrayList<String>(roleList.size());
        for (AdminUserRoleView view : roleList) {
            userRoleList.add(view.getRoleCode());
        }
        return userRoleList.size() > 0 ? userRoleList.toArray(new String[userRoleList.size()]) : null;
    }

    @Override
    public ResultEx logoutUser(LogoutUserParameter parameter) {
        ResultEx resultEx = new ResultEx();

        //如果有需要这里可以自行实现

        return resultEx.makeSuccessResult();
    }

    @Override
    public ResultEx modifyPassword(ModifyPasswordParameter parameter) {
        ResultEx resultEx = new ResultEx();

        Long userId = parameter.getSessionIdentity().getUserId();

        AdminUser adminUser = new AdminUser();
        adminUser.setId(userId);
        adminUser.setPassword(StrUtils.getMd5(parameter.getOriginPassword()));
        int result = adminUserDao.count(adminUser);
        if (result == 0) {
            return resultEx.makeResult(AdminResultEnum.ORIGIN_PASSWORD_ERROR.getValue(),
                    AdminResultEnum.ORIGIN_PASSWORD_ERROR.getComment());
        }

        AdminUser updateAdminUser = new AdminUser();
        updateAdminUser.setId(userId);
        updateAdminUser.setPassword(StrUtils.getMd5(parameter.getNewPassword()));
        updateAdminUser.setUpdatedTime(new Date());
        adminUserDao.update(updateAdminUser);

        return resultEx.makeSuccessResult();
    }
}
