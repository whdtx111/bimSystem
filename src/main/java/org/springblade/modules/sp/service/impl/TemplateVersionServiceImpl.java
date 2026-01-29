package org.springblade.modules.sp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springblade.modules.sp.entity.TemplateVersion;
import org.springblade.modules.sp.mapper.TemplateVersionMapper;
import org.springblade.modules.sp.service.TemplateVersionService;

import java.util.List;

/**
 * 模板版本服务实现类
 */
@Service
@RequiredArgsConstructor
public class TemplateVersionServiceImpl implements TemplateVersionService {

    private final TemplateVersionMapper templateVersionMapper;

    @Override
    public List<TemplateVersion> getByTemplateId(String templateId) {
        try {
            return templateVersionMapper.getByTemplateId(templateId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addTemplateVersion(TemplateVersion templateVersion) {
        try {
            return templateVersionMapper.addTemplateVersion(templateVersion);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public TemplateVersion getLatestByTemplateId(String templateId) {
        return templateVersionMapper.getLatestByTemplateId(templateId);
    }

    @Override
    public String getLatestNonNullVersionByTemplateId(String templateId) {
        return templateVersionMapper.getLatestNonNullVersionByTemplateId(templateId);
    }
    
    @Override
    public TemplateVersion getById(String id) {
        try {
            return templateVersionMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteByTemplateId(String templateId) {
        try {
            return templateVersionMapper.deleteByTemplateId(templateId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
