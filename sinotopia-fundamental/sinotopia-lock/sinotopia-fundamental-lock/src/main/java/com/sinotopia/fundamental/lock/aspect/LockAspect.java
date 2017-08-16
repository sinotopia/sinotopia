package com.sinotopia.fundamental.lock.aspect;

import com.sinotopia.fundamental.lock.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;

/**
 * @author dzr 分布式锁-redis
 */
//@Component
//@Aspect
public class LockAspect {

	private static final String CLASS_NAME = LockAspect.class.getName();

	private static final Logger LOGGER = LoggerFactory.getLogger(CLASS_NAME);

	/**
	 * 拦截点
	 */
	@Pointcut(value = "execution(public * *(..))")
	public void anyPublicMethod() {
	}

	/**
	 * 拦截
	 * 
	 * @param pjp
	 * @param distributeLock
	 * @return
	 * @throws Throwable
	 */
	@Around("anyPublicMethod() && @annotation(distributeLock)")
	public Object processAction(ProceedingJoinPoint pjp, DistributeLock distributeLock) throws Throwable {
		Object result = null;

		String lockKey = getKey(distributeLock, pjp);
		if (lockKey == null || lockKey.length() == 0) {
			result = pjp.proceed();
			logMethod("lockKey is null", pjp);
			return result;
		}

		LOGGER.info("{}.{}({}) lock with key : {}", pjp.getTarget().getClass().getName(), pjp.getSignature().getName(), pjp.getArgs(), lockKey);

		// return pjp.proceed();
		Lock lock = new RedisLock(lockKey);
		try {
			lock.lock();
			result = pjp.proceed();
		} catch (Exception e) {
			LOGGER.error("Lock Aspect invoke error:", e);
			throw new RuntimeException("Lock Aspect invoke error");
		} finally {
			lock.unlock();
		}

		return result;
	}

	/**
	 * 获取lockKey
	 * 
	 * @param distributeLock
	 * @param pjp
	 * @return
	 */
	private String getKey(DistributeLock distributeLock, ProceedingJoinPoint pjp) {
		String keyField = distributeLock.keyField();
		String prefix = distributeLock.prefix();

		String[] fields = keyField.split(";");
		String lockKey = prefix;
		for (String field : fields) {
			lockKey += this.getSplitOperator(distributeLock) + getParamValue(pjp, field);
		}
		return lockKey;
	}

	/**
	 * 获取对象某个方法的值
	 * 
	 * @param method
	 * @param obj
	 * @return
	 */
	public Object getFieldValueByMethod(String method, Object obj) {
		try {
			Method methodObj = obj.getClass().getMethod(method, new Class[] {});
			Object value = methodObj.invoke(obj, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取参数的值
	 * 
	 * @param pjp
	 * @param field
	 * @return
	 */
	private String getParamValue(ProceedingJoinPoint pjp, String field) {
		if (field == null || field.length() == 0)
			return null;
		Object[] params = pjp.getArgs();
		if (!field.contains(".")) {
			for (Object param : params) {
				StringBuilder sb = new StringBuilder();
				if (param.getClass().getSimpleName().equals(field)) {
					if (param instanceof String) {
						sb.append(String.valueOf(param));
					} else if (param instanceof Number || param instanceof Boolean) {
						sb.append(param);
					} else {
						return null;
					}
					return sb.toString();
				}
			}
		} else {
			String[] keyInfo = field.split("\\.");
			for (Object param : params) {
				if (param.getClass().getSimpleName().equals(keyInfo[0])) {
					Object result = getFieldValue(keyInfo[1], param);
					if (result != null) {
						StringBuilder sb = new StringBuilder();
						if (result instanceof String) {
							sb.append(String.valueOf(result));
						} else if (result instanceof Number || result instanceof Boolean) {
							sb.append(result);
						} else {
							return null;
						}
						return sb.toString();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 日志输出
	 * 
	 * @param message
	 * @param pjp
	 */
	private void logMethod(String message, ProceedingJoinPoint pjp) {
		// 方法参数类型，转换成简单类型
		LOGGER.error("{}.{}({}) lock fail for: {}", pjp.getTarget().getClass().getName(), pjp.getSignature().getName(), pjp.getArgs(), message);
	}

	/**
	 * 获取对象某个属性的值
	 * 
	 * @param data
	 * @param fieldName
	 * @return
	 */
	private String getFieldValue(String fieldName, Object data) {
		if (data == null) {
			return null;
		}
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(data.getClass());
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			if (pds != null) {
				for (PropertyDescriptor pd : pds) {
					Method method = pd.getReadMethod();
					if (method == null) {
						continue;
					}
					if ("getClass".equals(method.getName())) {
						continue;
					}

					if (pd.getName().equals(fieldName)) {
						Object result = method.invoke(data);
						if (result != null) {
							StringBuilder sb = new StringBuilder();
							if (result instanceof String) {
								sb.append(String.valueOf(result));
							} else if (result instanceof Number || result instanceof Boolean) {
								sb.append(result);
							} else {
								return null;
							}
							return sb.toString();
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("get field value error " + data, e);
			return null;
		}
		return null;
	}

	/**
	 * 获取分隔符
	 * @param distributeLock
	 * @return
	 */
	public String getSplitOperator(DistributeLock distributeLock){
		if(distributeLock.lockType().equals(LockType.ZOOKEEPER)){
			return ":";
		}else if(distributeLock.lockType().equals(LockType.REDIS)){
			return "/";
		}
		return "/";
	}

	/**
	 * 分布式锁
	 * @author dzr
	 *
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface DistributeLock {

		/**
		 * key
		 * @return
		 */
		public String prefix();

		/**
		 * params
		 * @return
		 */
		public String keyField();

		/**
		 * 锁的类型
		 * @return
		 */
		public LockType lockType() default LockType.ZOOKEEPER;
	}

	public enum  LockType{
		REDIS,
		ZOOKEEPER;
	}
}
