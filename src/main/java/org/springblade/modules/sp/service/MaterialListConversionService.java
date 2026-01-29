package org.springblade.modules.sp.service;

import org.springblade.modules.sp.entity.MaterialListConversion;
import java.util.List;

/**
 * 材料清单转换Service
 */
public interface MaterialListConversionService {
    
    /**
     * 根据templateId、streamId和branchId查询
     */
    List<MaterialListConversion> getByTemplateStreamBranch(String templateId, String streamId, String branchId);
    
    /**
     * 批量查询
     */
    List<MaterialListConversion> getBatchByConditions(List<MaterialListConversion> conditions);
    
    /**
     * 更新status状态
     */
    boolean updateStatus(Integer id, String status);
}
