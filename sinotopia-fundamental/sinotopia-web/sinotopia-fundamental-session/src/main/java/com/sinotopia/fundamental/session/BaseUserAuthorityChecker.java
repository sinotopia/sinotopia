package com.hkfs.fundamental.session;

import com.sinotopia.fundamental.api.enums.ResultCode;
import com.sinotopia.fundamental.api.params.SessionIdentity;
import com.sinotopia.fundamental.api.params.SessionParameter;
import com.hkfs.fundamental.common.utils.StrUtils;
import com.hkfs.fundamental.session.annotation.UserRole;
import com.hkfs.fundamental.session.annotation.UserType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 默认权限检查器
 * Created by brucezee on 2017/2/8.
 */
public class BaseUserAuthorityChecker implements UserAuthorityChecker {
    @Override
    public void check(ProceedingJoinPoint pjp, SessionParameter sessionParameter) {
        //获取会话
        SessionIdentity sessionIdentity = sessionParameter.getSessionIdentity();
        if (sessionIdentity == null) {
            throw new com.hkfs.fundamental.exception.ApplicationBizException(ResultCode.NOT_LOGIN_ERROR.getCode(), ResultCode.NOT_LOGIN_ERROR.getDescription());
        }

        Class<?> targetClass = pjp.getTarget().getClass();
        checkUserType(sessionIdentity, targetClass.getAnnotation(UserType.class));
        checkUserRole(sessionIdentity, targetClass.getAnnotation(UserRole.class));

        Method targetMethod = getTargetMethod(pjp);
        checkUserType(sessionIdentity, targetMethod.getAnnotation(UserType.class));
        checkUserRole(sessionIdentity, targetMethod.getAnnotation(UserRole.class));
    }

    /**
     * 检查用户类型
     * @param sessionIdentity
     * @param userType
     */
    private void checkUserType(SessionIdentity sessionIdentity, UserType userType) {
        if (userType == null || isEmpty(userType.value())) {
            return;
        }
        String type = sessionIdentity.getUserType();
        if (StrUtils.notEmpty(type)) {
            Set<String> values = getSetFromArray(userType.value());
            if (values.contains(type)) {
                return;
            }
        }

        throw new com.hkfs.fundamental.exception.ApplicationBizException(ResultCode.AUTHORIZATION_ERROR.getCode(), ResultCode.AUTHORIZATION_ERROR.getDescription());
    }

    /**
     * 检查用户角色
     * @param sessionIdentity
     * @param userRole
     */
    private void checkUserRole(SessionIdentity sessionIdentity, UserRole userRole) {
        if (userRole == null || isEmpty(userRole.value())) {
            return;
        }
        String[] roles = sessionIdentity.getRoles();
        if (!isEmpty(roles)) {
            Set<String> values = getSetFromArray(userRole.value());
            for (String role : roles) {
                if (values.contains(role)) {
                    //OK
                    return;
                }
            }
        }

        throw new com.hkfs.fundamental.exception.ApplicationBizException(ResultCode.AUTHORIZATION_ERROR.getCode(), ResultCode.AUTHORIZATION_ERROR.getDescription());
    }

    /**
     * 数组转换成Set
     * @param array
     * @return
     */
    private Set<String> getSetFromArray(String[] array) {
        Set<String> set = new HashSet<String>();
        if (array != null) {
            for (String str : array) {
                set.add(str);
            }
            return set;
        }
        return set;
    }

    /**
     * 判断数组对象是否为空（null或空数组）
     * @param array
     * @return
     */
    private boolean isEmpty(String[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 获取切面方法
     * @param pjp
     * @return
     */
    private Method getTargetMethod(ProceedingJoinPoint pjp) {
        try {
            return pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(),
                    ((MethodSignature) pjp.getSignature()).getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
