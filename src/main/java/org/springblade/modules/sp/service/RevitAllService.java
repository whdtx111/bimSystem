package org.springblade.modules.sp.service;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.RevitAllUPDTO;
import org.springblade.modules.sp.entity.RevitAll;

import java.util.List;

public interface RevitAllService extends BaseService<RevitAll> {

    RevitAll getById(@Param("id") String id);

    RevitAll getRevitAllByCommitId(String streamId,String elementId, String commitId);

    List<RevitAll> searchFilter(@Param("elementId") String elementId, @Param("streamId") String streamId, @Param("branchId") String branchId,@Param("isfinished") Integer isfinished);

    boolean addRevitAll(RevitAll revitAll);

    boolean updateRevitAll(RevitAll revitAll);

    boolean updateIsFinished(RevitAllUPDTO revitAllUPDTO);

    boolean deleteRevitAllById(@Param("id") String id);
}
