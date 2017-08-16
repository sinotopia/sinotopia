package com.sinotopia.fundamental.session;

import com.sinotopia.fundamental.api.params.SessionParameter;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 用户权限检查器
 * Created by brucezee on 2017/2/8.
 */
public interface UserAuthorityChecker {
    /**
     * 检查用户类型和角色
     * @param pjp
     * @param sessionParameter
     */
    public void check(ProceedingJoinPoint pjp, SessionParameter sessionParameter);
}
