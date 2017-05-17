package com.sinotopia.cms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.cms.dao.mapper.CmsTopicMapper;
import com.sinotopia.cms.dao.model.CmsTopic;
import com.sinotopia.cms.dao.model.CmsTopicExample;

/**
* 降级实现CmsTopicService接口
* Created by shuzheng on 2017/4/5.
*/
public class CmsTopicServiceMock extends BaseServiceMock<CmsTopicMapper, CmsTopic, CmsTopicExample> implements CmsTopicService {

}
