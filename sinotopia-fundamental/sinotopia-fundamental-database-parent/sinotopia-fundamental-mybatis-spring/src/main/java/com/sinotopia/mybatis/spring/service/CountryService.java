package com.sinotopia.mybatis.spring.service;

import com.sinotopia.mybatis.spring.model.Country;

import java.util.List;

/**
 * @author cacotopia
 * @since 2015-09-19 17:17
 */
public interface CountryService extends IService<Country> {

    /**
     * 根据条件分页查询
     *
     * @param country
     * @param page
     * @param rows
     * @return
     */
    List<Country> selectByCountry(Country country, int page, int rows);

}
