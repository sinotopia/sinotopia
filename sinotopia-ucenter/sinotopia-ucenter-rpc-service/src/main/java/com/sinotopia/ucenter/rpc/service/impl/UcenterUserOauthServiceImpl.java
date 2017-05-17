package com.sinotopia.ucenter.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.ucenter.dao.mapper.UcenterUserOauthMapper;
import com.sinotopia.ucenter.dao.model.UcenterUserOauth;
import com.sinotopia.ucenter.dao.model.UcenterUserOauthExample;
import com.sinotopia.ucenter.rpc.api.UcenterUserOauthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UcenterUserOauthService实现
 * Created by shuzheng on 2017/4/27.
 */
@Service
@Transactional
@BaseService
public class UcenterUserOauthServiceImpl extends BaseServiceImpl<UcenterUserOauthMapper, UcenterUserOauth, UcenterUserOauthExample> implements UcenterUserOauthService {

    private static Logger _log = LoggerFactory.getLogger(UcenterUserOauthServiceImpl.class);

    @Autowired
    UcenterUserOauthMapper ucenterUserOauthMapper;

}