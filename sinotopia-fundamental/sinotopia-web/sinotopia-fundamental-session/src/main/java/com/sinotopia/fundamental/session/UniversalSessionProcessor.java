package com.sinotopia.fundamental.session;

import com.sinotopia.fundamental.api.params.SessionIdentity;
import com.sinotopia.fundamental.api.params.SessionParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 使用redis作为会话缓存的用户会话处理
 * Created by zhoubing on 2016/7/28.
 */
public class UniversalSessionProcessor implements SessionProcessor {
    private Logger logger = LoggerFactory.getLogger(UniversalSessionProcessor.class);

    @Override
    public SessionIdentity process(SessionParameter sessionParameter, String accessToken) {
        //获取SessionParameter中的sessionIdentity字段的具体类型
        Class<?> sessionIdentityCls = getSessionIdentityClass(sessionParameter);
        if (sessionIdentityCls == null) {
            return null;
        }

        //获取会话
        SessionHandler sessionHandler = new SessionHandler();
        return sessionHandler.getSessionIdentity(accessToken, sessionIdentityCls);
    }

    private Class<?> getSessionIdentityClass(SessionParameter sessionParameter) {
        Class<?> sessionIdentityCls = null;
        Class<?> cls = sessionParameter.getClass();
        if (cls != SessionParameter.class) {
            //找出父类中的泛型 即实际sessionIdentity字段的类型
            while (!(cls.getGenericSuperclass() instanceof ParameterizedType)
                && cls != SessionParameter.class && cls != Object.class) {
                cls = cls.getSuperclass();
            }

            if (cls == SessionParameter.class) {
                return SessionIdentity.class;
            }

            ParameterizedType parameterizedType = (ParameterizedType) cls.getGenericSuperclass();//获取当前new对象的泛型的父类类型
            Type type = parameterizedType.getActualTypeArguments()[0];
            if (type instanceof Class) {
                sessionIdentityCls = (Class<?>) type;
            }
            else {
                return SessionIdentity.class;
            }
        }
        else {
            try {
                sessionIdentityCls = cls.getDeclaredField("sessionIdentity").getType();
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return sessionIdentityCls;
    }
}
