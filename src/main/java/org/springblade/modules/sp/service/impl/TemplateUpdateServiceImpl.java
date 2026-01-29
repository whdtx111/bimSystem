package org.springblade.modules.sp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springblade.modules.sp.dto.RevitAllUPDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springblade.modules.sp.entity.TemplateUpdate;
import org.springblade.modules.sp.mapper.TemplateUpdateMapper;
import org.springblade.modules.sp.service.ITemplateUpdateService;

import java.util.List;

/**
 * 模板修改表 服务实现类
 */
@Service
@AllArgsConstructor
public class TemplateUpdateServiceImpl extends ServiceImpl<TemplateUpdateMapper, TemplateUpdate> implements ITemplateUpdateService {

    private final TemplateUpdateMapper templateUpdateMapper;

    @Override
    public TemplateUpdate getById(String id) {
        return templateUpdateMapper.getById(id);
    }

    @Override
    public List<TemplateUpdate> getByTemplateId(String templateId) {
        return templateUpdateMapper.getByTemplateId(templateId);
    }

    @Override
    public TemplateUpdate getByStreamAndBranchAndTemplateId(String streamId, String branchId, String templateId) {
        return templateUpdateMapper.getByStreamAndBranchAndTemplateId(streamId, branchId, templateId);
    }

    @Override
    public List<TemplateUpdate> getAllByStreamAndBranchAndTemplateId(String streamId, String branchId, String templateId) {
        return templateUpdateMapper.getAllByStreamAndBranchAndTemplateId(streamId, branchId, templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTemplateUpdate(TemplateUpdate templateUpdate) {
        return templateUpdateMapper.addTemplateUpdate(templateUpdate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTemplateUpdate(TemplateUpdate templateUpdate) {
        return templateUpdateMapper.updateTemplateUpdate(templateUpdate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTemplateUpdate(String id) {
        return templateUpdateMapper.deleteTemplateUpdate(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByTemplateId(String templateId) {
        return templateUpdateMapper.deleteByTemplateId(templateId);
    }

}
