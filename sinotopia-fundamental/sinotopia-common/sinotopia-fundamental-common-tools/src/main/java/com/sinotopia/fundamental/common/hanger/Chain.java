package com.hkfs.fundamental.common.hanger;

import com.hkfs.fundamental.common.hanger.exception.ChainException;
import com.hkfs.fundamental.common.hanger.exception.ExceptionHandler;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 执行链条
 * Created by zhoubing on 2016/12/5.
 */
public class Chain implements Executor {
    protected Map<String, Executor> executors = new LinkedHashMap<String, Executor>();
    protected Map<String, Object> params = new HashMap<String, Object>();
    protected ExceptionHandler exceptionHandler;
    protected Chain parent;
    protected String redirectExecutor;
    protected Set<String> ignores = new HashSet<String>();
    protected AtomicBoolean success = new AtomicBoolean(true);
    protected AtomicBoolean previousSuccess = new AtomicBoolean(true);

    public Chain getParent() {
        return parent;
    }

    public void setParent(Chain parent) {
        this.parent = parent;
    }

    private void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void addExecutor(Executor executor) {
        executors.put("executor-"+executors.size(), executor);
    }

    public void addExecutor(String name, Executor executor) {
        executors.put(name, executor);
    }

    public void clearExecutor() {
        executors.clear();
    }

    public void put(String key, Object value) {
        params.put(key, value);
    }

    public boolean containsKey(String key) {
        return params.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return params.containsValue(value);
    }

    public <T> T get(String key) {
        return (T) params.get(key);
    }

    public Object remove(String key) {
        return params.remove(key);
    }

    public void parentPut(String key, Object value) {
        if (parent != null) {
            parent.put(key, value);
        }
    }

    public <T> T parentGet(String key) {
        if (parent != null) {
            return (T) parent.get(key);
        }
        return null;
    }

    public void clearParams() {
        params.clear();
    }

    public boolean isSuccess() {
        return success.get();
    }
    public boolean isPreviousSuccess() {
        return previousSuccess.get();
    }
    public void redirect(String redirectExecutor) {
        this.redirectExecutor = redirectExecutor;
    }

    public void execute() {
        doExecute(null);
    }

    private void doExecute(String key) {
        Iterator<Map.Entry<String, Executor>> it = executors.entrySet().iterator();
        Map.Entry<String, Executor> entry = null;
        Executor executor = null;
        String redirect = null;
        while (it.hasNext()) {
            entry = it.next();
            if (key != null) {
                if (!key.equals(entry.getKey())) {
                    continue;
                }
                key = null;
            }

            if (ignores.contains(entry.getKey())) {
                continue;
            }

            if (redirectExecutor != null) {
                redirect = redirectExecutor;
                redirectExecutor = null;
                break;
            }

            executor = entry.getValue();

            try {
                previousSuccess.set(executor.execute(this));
                success.set(success.get() && previousSuccess.get());
            }
            catch (Exception e) {
                success.set(false);
                previousSuccess.set(false);

                if (exceptionHandler != null) {
                    exceptionHandler.handle(entry.getKey(), e);

                    if (exceptionHandler.isTerminated(entry.getKey(), e)) {
                        break;
                    }
                }
                else {
                    throw new ChainException(e);
                }
            }
        }

        if (redirect != null) {
            doExecute(redirect);
        }
    }

    @Override
    public boolean execute(Chain chain) {
        this.setParent(chain);
        this.execute();
        return isSuccess();
    }

    public void ignore(String executorName) {
        ignores.add(executorName);
    }

    public void resetIgnores() {
        ignores.clear();
    }
}
