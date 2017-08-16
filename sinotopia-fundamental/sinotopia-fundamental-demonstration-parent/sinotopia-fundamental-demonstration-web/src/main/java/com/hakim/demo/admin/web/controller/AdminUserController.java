package com.hakim.demo.admin.web.controller;

import com.hakim.demo.admin.param.*;
import com.hakim.demo.admin.result.AdminUserView;
import com.hakim.demo.admin.result.LoginUserView;
import com.hakim.demo.admin.service.api.AdminUserService;
import com.sinotopia.fundamental.api.data.ListResultEx;
import com.sinotopia.fundamental.api.data.ObjectResultEx;
import com.sinotopia.fundamental.api.data.ResultEx;
import com.sinotopia.fundamental.api.params.SessionIdentity;
import com.sinotopia.fundamental.common.utils.TimeUtils;
import com.sinotopia.fundamental.session.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户管理
 * Created by brucezee on 2017/3/1.
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;
    
    @RequestMapping(value = "/addUser", name = "新增用户")
    public ResultEx addUser(AddUserParameter parameter) {
        return adminUserService.addUser(parameter);
    }

    @RequestMapping(value = "/deleteUser", name = "删除用户")
    public ResultEx deleteUser(DeleteUserParameter parameter) {
        return adminUserService.deleteUser(parameter);
    }

    @RequestMapping(value = "/getUser", name = "获取用户")
    public ObjectResultEx<AdminUserView> getUser(GetUserParameter parameter) {
        return adminUserService.getUser(parameter);
    }

    @RequestMapping(value = "/updateUser", name = "更新用户")
    public ResultEx updateUser(UpdateUserParameter parameter) {
        return adminUserService.updateUser(parameter);
    }

    @RequestMapping(value = "/getUserList", name = "获取用户列表")
    public ListResultEx<AdminUserView> getUserList(GetUserListParameter parameter) {
        return adminUserService.getUserList(parameter);
    }

    @RequestMapping(value = "/loginUser", name = "登录用户")
    public ObjectResultEx<LoginUserView> loginUser(LoginUserParameter parameter, HttpServletResponse response) {
        ObjectResultEx<LoginUserView> resultEx = adminUserService.loginUser(parameter);//调用实际登录接口验证用户名密码
        if (resultEx.isFailed()) {
            return resultEx;//登录失败返回
        }

        LoginUserView loginUserView = resultEx.getDataObject();//获取登录后用户信息

        //根据获取的用户信息转换成SessionIdentity
        SessionIdentity sessionIdentity = new SessionIdentity();
        sessionIdentity.setUserId(loginUserView.getId());
        sessionIdentity.setUserType(String.valueOf(loginUserView.getType()));
        sessionIdentity.setRoles(loginUserView.getRoles());
        sessionIdentity.setName(loginUserView.getName());
        sessionIdentity.setPhone(loginUserView.getPhone());

        //通过SessionHandler实现SessionIdentity缓存到redis以及向客户端返回相应会话cookie
        SessionHandler sessionHandler = new SessionHandler();
        String accessToken = sessionHandler.login(sessionIdentity, response, TimeUtils.MILLIS_OF_HOUR*12);

        //如果当前登录接口需要返回用户信息则返回，否则重新实例化一个Result返回成功即可
        loginUserView.setAccessToken(accessToken);
        loginUserView.setId(null);

        return resultEx;
    }

    @RequestMapping(value = "/logoutUser", name = "退出用户")
    public ResultEx logoutUser(HttpServletRequest request, HttpServletResponse response) {
        SessionHandler sessionHandler = new SessionHandler();
        SessionIdentity sessionIdentity = sessionHandler.getSessionIdentity(
                request, SessionIdentity.class);//获取SessionIdentity

        //设置SessionIdentity到退出接口的参数类
        //这里LogoutUserParameter没有继承自SessionParameter是考虑到前端页面如果登录过期了也可以调用退出接口
        //故手动设置SessionIdentity而不是自动注入
        LogoutUserParameter parameter = new LogoutUserParameter();
        parameter.setSessionIdentity(sessionIdentity);
        ResultEx resultEx = adminUserService.logoutUser(parameter);
        if (resultEx.isFailed()) {
            return resultEx;
        }

        //删除token对应的用户登录信息，返回响应删除原有cookie
        sessionHandler.logout(request, response);
        return resultEx;
    }

    @RequestMapping(value = "/modifyPassword", name = "修改密码")
    public ResultEx modifyPassword(ModifyPasswordParameter parameter) {
        return adminUserService.modifyPassword(parameter);
    }
}
