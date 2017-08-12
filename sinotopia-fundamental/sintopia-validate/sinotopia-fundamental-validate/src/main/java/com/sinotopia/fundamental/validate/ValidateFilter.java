package com.hkfs.fundamental.validate;

import com.sinotopia.fundamental.api.data.ResultEx;
import com.hkfs.fundamental.exception.HkfsBizException;
import com.hkfs.fundamental.validate.utils.ValidateUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2016/4/14.
 * 验证过滤器
 */
public abstract class ValidateFilter<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ValidateFilter.class);

    private Class clazz;

    /**
     * 参数校验
     *
     * @return
     */
    private T resolveParams(final Map<String, Object> paramsMap) {
        Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        this.clazz = (Class<T>) params[0];
        T param = null;
        try {
            param = (T) this.clazz.getConstructor().newInstance();
            //遍历所有的字段，判断字段是否存在map中
            final List<Field> fieldList = new ArrayList<Field>();
            ReflectionUtils.doWithFields(this.clazz, new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    if (!paramsMap.containsKey(field.getName())) {
                        throw new HkfsBizException(ValidateRetCode.FILTER_PARAM_ERROR.getCode(), ValidateRetCode.FILTER_PARAM_ERROR.getDescription() + "[" + ValidateFilter.this.clazz.getName()+ "->" + field.getName() + "]");
                    }
                }
            });
            BeanUtils.populate(param, paramsMap);
            ValidateUtils.validOrThrowException(param);
        } catch (InstantiationException e) {
            LOGGER.error("过滤器出现异常",e);
            throw new HkfsBizException(ValidateRetCode.FILTER_ERROR.getCode(), ValidateRetCode.FILTER_ERROR.getDescription());
        } catch (IllegalAccessException e) {
            LOGGER.error("过滤器出现异常",e);
            throw new HkfsBizException(ValidateRetCode.FILTER_ERROR.getCode(), ValidateRetCode.FILTER_ERROR.getDescription());
        } catch (InvocationTargetException e) {
            LOGGER.error("过滤器出现异常",e);
            throw new HkfsBizException(ValidateRetCode.FILTER_ERROR.getCode(), ValidateRetCode.FILTER_ERROR.getDescription());
        } catch (NoSuchMethodException e) {
            LOGGER.error("过滤器出现异常",e);
            throw new HkfsBizException(ValidateRetCode.FILTER_ERROR.getCode(), ValidateRetCode.FILTER_ERROR.getDescription());
        }
        return param;
    }


    /**
     * 执行操作
     *
     * @param parammeterMap
     * @return
     */
    public ResultEx doFilter(Map<String, Object> parammeterMap) {

        ResultEx resultEx = new ResultEx();
        T param = this.resolveParams(parammeterMap);
        this.prcoess(param, resultEx);
        return resultEx;
    }

    /**
     * 业务数据判断
     *
     * @param param
     * @param result
     */
    protected abstract void prcoess(T param, ResultEx result);
}
