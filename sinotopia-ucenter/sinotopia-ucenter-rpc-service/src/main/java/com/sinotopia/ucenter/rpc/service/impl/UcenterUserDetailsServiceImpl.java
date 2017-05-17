package com.sinotopia.ucenter.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.ucenter.dao.mapper.UcenterUserDetailsMapper;
import com.sinotopia.ucenter.dao.model.UcenterUserDetails;
import com.sinotopia.ucenter.dao.model.UcenterUserDetailsExample;
import com.sinotopia.ucenter.rpc.api.UcenterUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UcenterUserDetailsService实现
 * Created by shuzheng on 2017/4/27.
 */
@Service
@Transactional
@BaseService
public class UcenterUserDetailsServiceImpl extends BaseServiceImpl<UcenterUserDetailsMapper, UcenterUserDetails, UcenterUserDetailsExample> implements UcenterUserDetailsService {

    private static Logger _log = LoggerFactory.getLogger(UcenterUserDetailsServiceImpl.class);

    @Autowired
    UcenterUserDetailsMapper ucenterUserDetailsMapper;

}