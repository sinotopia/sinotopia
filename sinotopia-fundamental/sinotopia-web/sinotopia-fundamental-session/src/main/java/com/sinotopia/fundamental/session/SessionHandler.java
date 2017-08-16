package com.sinotopia.fundamental.session;

import com.alibaba.fastjson.JSON;
import com.sinotopia.fundamental.api.enums.BizFields;
import com.sinotopia.fundamental.api.params.SessionIdentity;
import com.sinotopia.fundamental.common.utils.StrUtils;
import com.sinotopia.fundamental.common.utils.TimeUtils;
import com.sinotopia.fundamental.redis.RedisConnector;
import com.sinotopia.fundamental.servlet.utils.CookieUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户处理用户登录信息
 * Created by zhoubing on 2016/4/12.
 */
public class SessionHandler<T> {
    public static final String SESSION_CACHE_KEY_PREFIX = "sinotopia.session.";
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
     * 根据请求获取用户会话
     * @param request
     * @return
     */
    public <T extends SessionIdentity> T getSessionIdentity(HttpServletRequest request, Class<?> cls) {
        String token = getAccessToken(request);
        if (StrUtils.notEmpty(token)) {
            return getSessionIdentity(token, cls);
        }
        return null;
    }

    /**
     * 根据token获取用户会话
     * @param token
     * @return
     */
    public <T extends SessionIdentity> T getSessionIdentity(String token, Class<?> cls) {
        String key = processSessionIdentityCacheKey(token);
        String data = RedisConnector.get(key);
        if (StrUtils.notEmpty(data)) {
            return (T) JSON.parseObject(data, cls);
        }
        return null;
    }

    /**
     * 缓存token对应的用户信息
     * @param token
     * @param SessionIdentity
     */
    public void setSessionIdentity(String token, T SessionIdentity) {
        setSessionIdentity(token, SessionIdentity, SESSION_EXPIRE_MILLIS);
    }

    /**
     * 缓存token对应的用户信息
     * @param token
     * @param SessionIdentity
     * @param expire 毫秒数
     */
    public void setSessionIdentity(String token, T SessionIdentity ,Long expire) {
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
    public String setSessionIdentity(T sessionIdentity) {
        return setSessionIdentity(sessionIdentity,SESSION_EXPIRE_MILLIS);
    }

    /**
     * 设置一个过期时间
     * @param sessionIdentity
     * @param expire 毫秒数
     * @return
     */
    public String setSessionIdentity(T sessionIdentity, Long expire) {
        if (sessionIdentity != null) {
            String token = StrUtils.getUUID();
            setSessionIdentity(token, sessionIdentity, expire);
            return token;
        }
        return null;
    }

    /**
     * 删除用户会话信息
     * @param accessToken
     * @return
     */
    public boolean removeSessionIdentity(String accessToken) {
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
    public String login(T sessionIdentity, HttpServletResponse response) {
        return login(sessionIdentity,response,SESSION_EXPIRE_MILLIS);
    }

    /**
     * 设置用户的accesToken的过期时间
     * @param sessionIdentity
     * @param response
     * @param timeout  毫秒数
     * @return
     */
    public String login(T sessionIdentity, HttpServletResponse response, Long timeout) {
        if (sessionIdentity != null) {
            String accessToken = setSessionIdentity(sessionIdentity, timeout);
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
    public void logout(HttpServletRequest request, HttpServletResponse response) {
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
    public void login(String accessToken, HttpServletResponse response, Long timeout) {
        String cookie = processAccessTokenCookie(accessToken);
        int expiry = ((Long) (timeout / 1000)).intValue();
        CookieUtils.setCookieHttpOnly(response, BizFields.AUTHORIZATION, cookie, null, "/", expiry);
    }
}
