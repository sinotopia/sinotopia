package com.sinotopia.mybatis.spring.util;

import com.sinotopia.mybatis.mapper.common.Mapper;
import com.sinotopia.mybatis.mapper.common.MySqlMapper;

/**
 * 继承自己的MyMapper
 *
 * @author cacotopia
 * @since 2015-09-06 21:53
 */
public interface SimpleMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
