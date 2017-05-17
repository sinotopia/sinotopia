package com.sinotopia.ucenter.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.ucenter.dao.mapper.UcenterUserMapper;
import com.sinotopia.ucenter.dao.model.UcenterUser;
import com.sinotopia.ucenter.dao.model.UcenterUserExample;
import com.sinotopia.ucenter.rpc.api.UcenterUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UcenterUserService实现
 * Created by shuzheng on 2017/4/27.
 */
@Service
@Transactional
@BaseService
public class UcenterUserServiceImpl extends BaseServiceImpl<UcenterUserMapper, UcenterUser, UcenterUserExample> implements UcenterUserService {

    private static Logger _log = LoggerFactory.getLogger(UcenterUserServiceImpl.class);

    @Autowired
    UcenterUserMapper ucenterUserMapper;

}