package com.hkfs.fundamental.session;

import com.sinotopia.fundamental.api.params.SessionIdentity;
import com.sinotopia.fundamental.api.params.SessionParameter;

/**
 * 会话处理器
 * Created by zhoubing on 2016/7/28.
 */
public interface SessionProcessor {
    /**
     * 根据token获取用户信息
     * @param sessionParameter
     * @param accessToken
     * @return
     */
    public SessionIdentity process(SessionParameter sessionParameter, String accessToken);
}
