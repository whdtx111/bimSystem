package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springblade.modules.sp.entity.BranchCommits;
import org.springblade.modules.sp.entity.RevitRef;

import java.util.List;
@Mapper
public interface RevitRefMapper extends BaseMapper<RevitRef> {

    RevitRef getById(@Param("id") String id);

    List<RevitRef> getAllByGroup(@Param("group") String group);

    boolean addRevitRef(RevitRef revitRef);

}
