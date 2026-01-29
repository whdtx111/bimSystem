package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springblade.modules.sp.entity.BranchCommits;
import org.springblade.modules.sp.entity.LibWbsTemp;

import java.util.List;
@Mapper
public interface LibWbsTempMapper extends BaseMapper<LibWbsTemp> {



    LibWbsTemp getById(String id);

    LibWbsTemp getLibWbsTemp(String wbsCode, String tempId);

    List<String> getWbsCodeByTempId(String tempId);

    List<LibWbsTemp> getLibWbsTempByTempId(String tempId);

    boolean addLibWbsTemp(LibWbsTemp libWbsTemp);

    boolean updateLibWbsTemp(LibWbsTemp libWbsTemp);

    boolean deleteLibWbsTemp(String id);
}
