package com.sinotopia.mybatis.spring.service.impl;

import com.sinotopia.mybatis.pagehelper.PageHelper;
import com.sinotopia.mybatis.spring.model.Country;
import com.sinotopia.mybatis.spring.service.CountryService;
import org.springframework.stereotype.Service;
import com.sinotopia.mybatis.mapper.entity.Example;
import com.sinotopia.mybatis.mapper.util.StringUtil;

import java.util.List;

/**
 * @author cacotopia
 * @since 2015-09-19 17:17
 */
@Service("countryService")
public class CountryServiceImpl extends BaseService<Country> implements CountryService {

    @Override
    public List<Country> selectByCountry(Country country, int page, int rows) {

        Example example = new Example(Country.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtil.isNotEmpty(country.getCountryName())) {
            criteria.andLike("countryname", "%" + country.getCountryName() + "%");
        }
        if (StringUtil.isNotEmpty(country.getCountryCode())) {
            criteria.andLike("countrycode", "%" + country.getCountryCode() + "%");
        }
        if (country.getId() != null) {
            criteria.andEqualTo("id", country.getId());
        }
        //分页查询
        PageHelper.startPage(page, rows);
        return selectByExample(example);
    }

}
