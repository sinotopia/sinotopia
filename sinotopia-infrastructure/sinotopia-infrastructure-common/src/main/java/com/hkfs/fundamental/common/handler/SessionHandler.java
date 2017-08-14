package com.hkfs.fundamental.common.handler;

import com.alibaba.fastjson.JSON;
import com.hkfs.fundamental.api.enums.BizFields;
import com.hkfs.fundamental.api.params.SessionIdentity;
import com.hkfs.fundamental.common.utils.CookieUtils;
import com.hkfs.fundamental.common.utils.StrUtils;
import com.hkfs.fundamental.common.utils.TimeUtils;
import com.hkfs.fundamental.redis.RedisConnector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhoubing on 2016/4/12.
 */
public class SessionHandler {
    public static final String SESSION_CACHE_KEY_PREFIX = "hkfs.session.";
    public static final long SESSION_EXPIRE_MILLIS = TimeUtils.MILLIS_OF_HOUR/2;//会话缓存时间毫秒数
    public static final String AUTHORIZATION_COOKIE_VALUE_PREFIX = "Bearer ";//授权cookie前缀
    public static final String DELETE_AUTHORIZATION_COOKIE_VALUE = "Bearer deleteMe";//删除授权cookie

    /**
     * 从请求中获取accessToken
     * @param request
     * @return
     */
    public static String getAccessToken(HttpServletRequest request) {
        String authorization = CookieUtils.getCookie(request, BizFields.AUTHORIZATION);
        if (StrUtils.notEmpty(authorization)) {
            return authorization.substring(AUTHORIZATION_COOKIE_VALUE_PREFIX.length());
        }
        authorization = request.getHeader(BizFields.AUTHORIZATION);
        if (StrUtils.notEmpty(authorization)) {
            return authorization.substring(AUTHORIZATION_COOKIE_VALUE_PREFIX.length());
        }
        return request.getParameter(BizFields.ACCESS_TOKEN);
    }

    /**
     * 组装accessToken的cookie值
     * @param accessToken
     * @return
     */
    public static String processAccessTokenCookie(String accessToken) {
        return new StringBuilder(AUTHORIZATION_COOKIE_VALUE_PREFIX).append(accessToken).toString();
    }

    /**
     * 根据token获取用户会话
     * @param token
     * @return
     */
    public static SessionIdentity getSessionIdentity(String token) {
        String key = processSessionIdentityCacheKey(token);
        String data = RedisConnector.get(key);
        if (StrUtils.notEmpty(data)) {
            return JSON.parseObject(data, SessionIdentity.class);
        }
        return null;
    }

    /**
     * 缓存token对应的用户信息
     * @param token
     * @param SessionIdentity
     */
    public static void setSessionIdentity(String token, SessionIdentity SessionIdentity) {
        SessionHandler.setSessionIdentity(token, SessionIdentity, SESSION_EXPIRE_MILLIS);
    }

    /**
     * 缓存token对应的用户信息
     * @param token
     * @param SessionIdentity
     * @param expire 毫秒数
     */
    public static void setSessionIdentity(String token, SessionIdentity SessionIdentity ,Long expire) {
        String key = processSessionIdentityCacheKey(token);
        String data = JSON.toJSONString(SessionIdentity);
        RedisConnector.set(key, expire, data);
    }

    public static String processSessionIdentityCacheKey(String accessToken) {
        return new StringBuilder(SESSION_CACHE_KEY_PREFIX).append(accessToken).toString();
    }


    /**
     * 设置用户会话信息
     * @param sessionIdentity
     * @return 返回token
     */
    public static String setSessionIdentity(SessionIdentity sessionIdentity) {
        return setSessionIdentity(sessionIdentity,SESSION_EXPIRE_MILLIS);
    }

    /**
     * 设置一个过期时间
     * @param sessionIdentity
     * @param expire 毫秒数
     * @return
     */
    public static String setSessionIdentity(SessionIdentity sessionIdentity,Long expire) {
        if (sessionIdentity != null) {
            String token = StrUtils.getUUID();
            setSessionIdentity(token,sessionIdentity,expire);
            return token;
        }
        return null;
    }

    /**
     * 删除用户会话信息
     * @param accessToken
     * @return
     */
    public static boolean removeSessionIdentity(String accessToken) {
        if (StrUtils.notEmpty(accessToken)) {
            String key = processSessionIdentityCacheKey(accessToken);
            RedisConnector.del(key);
            return true;
        }
        return false;
    }

    /**
     * 登录
     * @param sessionIdentity
     * @param response
     */
    public static String login(SessionIdentity sessionIdentity, HttpServletResponse response) {
        return SessionHandler.login(sessionIdentity,response,SESSION_EXPIRE_MILLIS);
    }

    /**
     * 设置用户的accesToken的过期时间
     * @param sessionIdentity
     * @param response
     * @param timeout  毫秒数
     * @return
     */
    public static String login(SessionIdentity sessionIdentity, HttpServletResponse response,Long timeout) {
        if (sessionIdentity != null) {
            String accessToken = setSessionIdentity(sessionIdentity,timeout);
            String cookie = processAccessTokenCookie(accessToken);
            int expiry = ((Long) (timeout / 1000)).intValue();
            CookieUtils.setCookieHttpOnly(response, BizFields.AUTHORIZATION, cookie, null, "/", expiry);
            return accessToken;
        }
        return "";
    }

    /**
     * 登出
     * @param response
     */
    public static void logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = getAccessToken(request);
        if (StrUtils.notEmpty(accessToken)) {
            removeSessionIdentity(accessToken);
            CookieUtils.setCookie(response, BizFields.AUTHORIZATION, DELETE_AUTHORIZATION_COOKIE_VALUE, null, "/", 0);
        }
    }


    /**
     * 设置用户的accesToken的过期时间
     * @param accessToken
     * @param response
     * @param timeout  毫秒数
     * @return
     */
    public static void login(String accessToken, HttpServletResponse response,Long timeout) {
        String cookie = processAccessTokenCookie(accessToken);
        int expiry = ((Long) (timeout / 1000)).intValue();
        CookieUtils.setCookieHttpOnly(response, BizFields.AUTHORIZATION, cookie, null, "/", expiry);
    }
}
