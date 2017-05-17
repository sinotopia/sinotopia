package com.sinotopia.cms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.cms.dao.mapper.CmsCommentMapper;
import com.sinotopia.cms.dao.model.CmsComment;
import com.sinotopia.cms.dao.model.CmsCommentExample;

/**
* 降级实现CmsCommentService接口
* Created by shuzheng on 2017/4/5.
*/
public class CmsCommentServiceMock extends BaseServiceMock<CmsCommentMapper, CmsComment, CmsCommentExample> implements CmsCommentService {

}
