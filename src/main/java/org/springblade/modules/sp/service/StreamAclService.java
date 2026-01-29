package org.springblade.modules.sp.service;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.StreamAcl;

import java.util.List;

public interface StreamAclService extends BaseService<StreamAcl> {

    List<StreamAcl> selectStreamAclByStreamId(@Param("resourceId") String streamId);

    StreamAcl selectSpRole(@Param("userId") String userId,@Param("resourceId") String streamId);

    List<String> getResourceIdsByUserId(String userId);

    boolean addStreamAcl(String userId, String resourceId, String role);
}
