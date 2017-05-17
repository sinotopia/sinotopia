package com.sinotopia.cms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.cms.dao.mapper.CmsSettingMapper;
import com.sinotopia.cms.dao.model.CmsSetting;
import com.sinotopia.cms.dao.model.CmsSettingExample;

/**
* 降级实现CmsSettingService接口
* Created by shuzheng on 2017/4/5.
*/
public class CmsSettingServiceMock extends BaseServiceMock<CmsSettingMapper, CmsSetting, CmsSettingExample> implements CmsSettingService {

}
