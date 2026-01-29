package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.dto.RevitAllUPDTO;
import org.springframework.stereotype.Service;
import org.springblade.modules.sp.entity.TemplateCustomData;
import org.springblade.modules.sp.mapper.TemplateCustomDataMapper;
import org.springblade.modules.sp.service.ITemplateCustomDataService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 自定义模板属性回传Service实现类
 */
@Service
@DS("postgresql")
@AllArgsConstructor
public class TemplateCustomDataServiceImpl extends BaseServiceImpl<TemplateCustomDataMapper, TemplateCustomData> 
        implements ITemplateCustomDataService {

    private final TemplateCustomDataMapper templateCustomDataMapper;

    @Override
    public TemplateCustomData getById(String id) {
        return templateCustomDataMapper.getById(id);
    }

    @Override
    public List<TemplateCustomData> searchFilter(String elementId, String streamId, String branchId, String templateId) {
        return templateCustomDataMapper.searchFilter(elementId, streamId, branchId, templateId);
    }

    @Override
    public boolean addTemplateCustomData(TemplateCustomData templateCustomData) {
        return templateCustomDataMapper.addTemplateCustomData(templateCustomData);
    }

    @Override
    public boolean updateTemplateCustomData(TemplateCustomData templateCustomData) {
        return templateCustomDataMapper.updateTemplateCustomData(templateCustomData);
    }

    @Override
    public boolean deleteById(String id) {
        return templateCustomDataMapper.deleteById(id);
    }

    @Override
    public boolean batchInsert(List<TemplateCustomData> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        return templateCustomDataMapper.batchInsert(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFinished(RevitAllUPDTO revitAllUPDTO) {
        return templateCustomDataMapper.updateFinished(revitAllUPDTO);
    }

    @Override
    public TemplateCustomData getByStreamBranchCommitElementId(String streamId, String branchId, String commitId, String elementId) {
        return templateCustomDataMapper.getByStreamBranchCommitElementId(streamId, branchId, commitId, elementId);
    }

    @Override
    public boolean deleteByTemplateId(String templateId) {
        return templateCustomDataMapper.deleteByTemplateId(templateId);
    }
}
