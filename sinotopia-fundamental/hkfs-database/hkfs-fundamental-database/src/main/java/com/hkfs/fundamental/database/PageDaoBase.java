package com.hkfs.fundamental.database;

import com.hkfs.fundamental.database.page.PageList;

/**
 * 分页查询Dao
 * Created by brucezee on 2017/2/9.
 */
public interface PageDaoBase<T, K> extends DaoBase<T, K> {
    /**
     * 分页查询
     * @param t
     * @return
     */
    public PageList<T> pageQuery(T t);
}
