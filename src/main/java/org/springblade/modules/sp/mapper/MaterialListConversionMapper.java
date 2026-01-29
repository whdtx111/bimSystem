package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.MaterialListConversion;

import java.util.List;

/**
 * 材料清单转换表Mapper
 */
@Mapper
public interface MaterialListConversionMapper extends BaseMapper<MaterialListConversion> {
    
    /**
     * 根据templateId、streamId和branchId查询
     */
    List<MaterialListConversion> getByTemplateStreamBranch(@Param("templateId") String templateId, 
                                                            @Param("streamId") String streamId, 
                                                            @Param("branchId") String branchId);
    
    /**
     * 根据多个条件批量查询
     */
    List<MaterialListConversion> getBatchByConditions(@Param("list") List<MaterialListConversion> conditions);
    
    /**
     * 更新status状态
     */
    int updateStatus(@Param("id") Integer id, @Param("status") String status);
}
