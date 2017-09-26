package com.sinotopia.fundamental.database;

import com.sinotopia.fundamental.database.page.PageList;

/**
 * 分页查询Dao
 */
public interface PageDaoBase<T, K> extends DaoBase<T, K> {
    /**
     * 分页查询
     *
     * @param t
     * @return
     */
    PageList<T> pageQuery(T t);
}
