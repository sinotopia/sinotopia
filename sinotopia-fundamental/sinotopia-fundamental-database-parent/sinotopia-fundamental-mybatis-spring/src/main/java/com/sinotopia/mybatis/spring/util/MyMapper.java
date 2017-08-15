package com.sinotopia.mybatis.spring.util;

import com.sinotopia.mybatis.mapper.common.Mapper;
import com.sinotopia.mybatis.mapper.common.MySqlMapper;

/**
 * 继承自己的MyMapper
 *
 * @author liuzh_3nofxnp
 * @since 2015-09-06 21:53
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
