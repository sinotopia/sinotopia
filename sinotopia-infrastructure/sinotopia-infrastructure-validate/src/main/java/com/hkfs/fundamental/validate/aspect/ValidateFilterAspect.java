package com.hkfs.fundamental.validate.aspect;

import com.hkfs.fundamental.api.data.ListResultEx;
import com.hkfs.fundamental.api.data.ObjectResultEx;
import com.hkfs.fundamental.api.data.Result;
import com.hkfs.fundamental.api.data.ResultEx;
import com.hkfs.fundamental.validate.ValidateFilter;
import com.hkfs.fundamental.validate.ValidateRetCode;
import com.hkfs.fundamental.validate.annotaion.ValidateFilters;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证切面
 */
//@Component
//@Aspect
public class ValidateFilterAspect implements ApplicationContextAware{

    private final Map<Class<? extends ValidateFilter>, ValidateFilter> filterCache = new ConcurrentHashMap<Class<? extends ValidateFilter>, ValidateFilter>();
    private final Map<Method, List<ValidateFilter>> methodCache = new ConcurrentHashMap<Method, List<ValidateFilter>>();

    private ApplicationContext applicationContext;
    /**
     * 逻辑校验
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
//    @Around("@annotation(com.hkfs.fundamental.common.validate.annotaion.VaildateFilters)")
    public Object process(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Class userClass = pjp.getTarget().getClass();
        Method method = methodSignature.getMethod();
        Class<?> returnType = method.getReturnType();
        if (Result.class.isAssignableFrom(returnType)) {
            if (args != null && args.length == 1) {
                Object param = args[0];
                Map<String, Object> paramsMap = new HashMap<String, Object>();
                Class targetClass = param.getClass();
                do {
                    Field[] fields = targetClass.getDeclaredFields();
                    for(Field field : fields) {
                        field.setAccessible(true);
                        Object fieldValue = field.get(param);
                        paramsMap.put(field.getName(), fieldValue);
                    }
                    targetClass = targetClass.getSuperclass();
                } while(targetClass != null && targetClass != Object.class);

                List<ValidateFilter> validateFilterList = methodCache.get(method);
                if( validateFilterList == null){
                    validateFilterList = new ArrayList<ValidateFilter>();
                    ValidateFilters vaildateFilters = method.getDeclaredAnnotation(ValidateFilters.class);
                    if(vaildateFilters == null){
                        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
                        vaildateFilters = specificMethod.getDeclaredAnnotation(ValidateFilters.class);
                    }
                    if (vaildateFilters != null) {
                        Class<? extends ValidateFilter>[] filterClasses = vaildateFilters.filterClasses();
                        for (Class<? extends ValidateFilter> filterClz : filterClasses) {
                            ValidateFilter filter = filterCache.get(filterClz);
                            if (filter == null) {
                                filter = this.applicationContext.getBean(filterClz);
                                if( filter == null) {
                                    filter = filterClz.getConstructor().newInstance();
                                }
                            }
                            validateFilterList.add(filter);
                        }
                    }
                }

                for (ValidateFilter filter : validateFilterList) {
                    ResultEx result = filter.doFilter(paramsMap);
                    if (result.isFailed()) {
                        return this.handleResultType(returnType, result);
                    }
                }
                return pjp.proceed();
            } else {
                return new ResultEx().makeResult(ValidateRetCode.FILTER_PARAM_MUST_ONLY_ONE.getCode(),
                        ValidateRetCode.FILTER_PARAM_MUST_ONLY_ONE.getDescription());
            }
        } else {
            throw new Exception("该方法返回类型必须为Result类型！");
        }
    }

    /**
     * 处理异常
     * @param returnType
     */
    private Object handleResultType(Class returnType,ResultEx filterResult){
        if(returnType == Result.class){
            Result result = new Result();
            result.setRetCode(filterResult.getRetCode());
            result.setRetMsg(filterResult.getRetMsg());
            return result;
        }else if(returnType == ResultEx.class){
            return filterResult;
        }else if(returnType == ListResultEx.class){
            return new ListResultEx().makeResult(filterResult.getRetCode(),filterResult.getRetMsg());
        }else if(returnType == ObjectResultEx.class){
            return new ObjectResultEx().makeResult(filterResult.getRetCode(),filterResult.getRetMsg());
        }else{
            return filterResult;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
