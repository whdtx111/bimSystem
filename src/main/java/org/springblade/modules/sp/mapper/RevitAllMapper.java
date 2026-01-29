package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.dto.RevitAllUPDTO;
import org.springblade.modules.sp.entity.RevitAll;

import java.util.List;

@Mapper
public interface RevitAllMapper extends BaseMapper<RevitAll> {

    RevitAll getById(@Param("id") String id);

    RevitAll getRevitAllByCommitId(String streamId,String elementId, String commitId);

    List<RevitAll> searchFilter(@Param("elementId") String elementId, @Param("streamId") String streamId, @Param("branchId") String branchId,@Param("isfinished") Integer isfinished);

    boolean addRevitAll(RevitAll revitAll);

    boolean updateRevitAll(RevitAll revitAll);

    boolean updateIsFinished(RevitAllUPDTO revitAllUPDTO);

    boolean deleteRevitAllById(@Param("id") String id);

}
