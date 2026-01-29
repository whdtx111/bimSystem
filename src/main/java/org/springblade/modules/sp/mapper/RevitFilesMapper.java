package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.RevitFiles;

import java.util.List;

@Mapper
public interface RevitFilesMapper extends BaseMapper<RevitFiles> {

    RevitFiles getById(String id);

    RevitFiles getByName(String streamId, String branchId, String commitId,String name);

    List<RevitFiles> searchFilter(String streamId, String branchId, String commitId);

    boolean addRevitFile(RevitFiles revitFiles);

    boolean deleteRevitFileById(String id);

}
