package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.BomFile;

import java.util.List;

@Mapper
public interface BomFileMapper extends BaseMapper<BomFile> {

    BomFile getById(String id);

    BomFile getByType(String type);

    List<BomFile> getLatestTemplateFile();

    boolean addBomFile(BomFile bomFile);

    boolean deleteBomFile(String id);


}
