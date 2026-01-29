package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springblade.modules.sp.entity.MaterialListConversion;
import org.springblade.modules.sp.mapper.MaterialListConversionMapper;
import org.springblade.modules.sp.service.MaterialListConversionService;

import java.util.List;

/**
 * 材料清单转换Service实现类
 */
@Service
@DS("postgresql")
@Slf4j
public class MaterialListConversionServiceImpl implements MaterialListConversionService {
    
    @Autowired
    private MaterialListConversionMapper materialListConversionMapper;
    
    @Override
    public List<MaterialListConversion> getByTemplateStreamBranch(String templateId, String streamId, String branchId) {
        try {
            return materialListConversionMapper.getByTemplateStreamBranch(templateId, streamId, branchId);
        } catch (Exception e) {
            log.error("根据templateId、streamId和branchId查询MaterialListConversion失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public List<MaterialListConversion> getBatchByConditions(List<MaterialListConversion> conditions) {
        try {
            if (conditions == null || conditions.isEmpty()) {
                return null;
            }
            return materialListConversionMapper.getBatchByConditions(conditions);
        } catch (Exception e) {
            log.error("批量查询MaterialListConversion失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public boolean updateStatus(Integer id, String status) {
        try {
            if (id == null || status == null) {
                log.error("更新status失败: id或status为空");
                return false;
            }
            int result = materialListConversionMapper.updateStatus(id, status);
            if (result > 0) {
                log.info("成功更新MaterialListConversion状态: id={}, status={}", id, status);
                return true;
            } else {
                log.warn("更新MaterialListConversion状态失败: id={}, status={}, 影响行数=0", id, status);
                return false;
            }
        } catch (Exception e) {
            log.error("更新MaterialListConversion状态失败: id={}, status={}, error={}", id, status, e.getMessage(), e);
            return false;
        }
    }
}
