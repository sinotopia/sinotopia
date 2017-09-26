package com.sinotopia.fundamental.database;

import com.sinotopia.fundamental.api.data.ResultEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;


/**
 * 自定义数据库事物管理器，根据执行方法后的返回结果进行回滚
 */
@SuppressWarnings("serial")
public class TransactionManager extends DataSourceTransactionManager implements CallbackPreferringPlatformTransactionManager {

    private final Logger logger = LoggerFactory.getLogger(TransactionManager.class);

    @SuppressWarnings("unchecked")
    public <T> T execute(TransactionDefinition definition, TransactionCallback<T> callback) throws TransactionException {
        long tid = Thread.currentThread().getId();
        logger.debug("start transaction.[" + tid + "]");
        TransactionStatus status = getTransaction(definition);//获得事务的状态
        try {
            Object result = callback.doInTransaction(status);//执行逻辑处理
            if (result == null) {
                logger.debug("check returned value null.[" + tid + "]");
            } else {
                logger.debug("check returned value " + result.toString() + ".[" + tid + "]");
            }
            if (!status.isCompleted()) {//事务还没有完成
                if (status.isRollbackOnly()) {
                    logger.debug("rollbacking transaction due to code requested.[" + tid + "]");//代码中手动回滚
                    rollback(status);
                } else {
                    if ((result != null) && ((result instanceof ResultEx))) {//判断返回结果是否为空，及其类型是否正确
                        if (((ResultEx) result).isFailed()) {//根据返回对象的isFailed方法判断逻辑处理结果是否成功
                            logger.error("rollbacking transaction due to failed result.[" + tid + "]");
                            rollback(status);//返回失败的结果执行事务回滚
                        } else {
                            logger.debug("committing transaction due to succeeded result.[" + tid + "]");
                            commit(status);//返回成功的结果执行事务提交
                        }
                    } else {
                        logger.debug("committing transaction on unknown result.[" + tid + "]");
                        commit(status);//未知结果类型提交事务
                    }
                }
            }
            return (T) result;//将底层的返回值作为代理的返回值
        } catch (RuntimeException e) {
            if (!status.isCompleted()) {//发生异常执行事务回滚
                logger.error("rollbacking transaction due to runtime exception.[" + tid + "]");
                rollback(status);
            }
            throw e;
        }
    }
}
