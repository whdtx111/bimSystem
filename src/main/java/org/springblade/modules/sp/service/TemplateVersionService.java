package org.springblade.modules.sp.service;

import org.springblade.modules.sp.entity.TemplateVersion;

import java.util.List;

/**
 * 模板版本服务接口
 */
public interface TemplateVersionService {

    /**
     * 根据模板ID获取版本列表
     * @param templateId 模板ID
     * @return 版本列表
     */
    List<TemplateVersion> getByTemplateId(String templateId);

    /**
     * 新增模板版本记录
     * @param templateVersion 模板版本对象
     * @return 是否成功
     */
    boolean addTemplateVersion(TemplateVersion templateVersion);

    /**
     * 根据模板ID获取最新的版本记录
     * @param templateId 模板ID
     * @return 最新的版本记录
     */
    TemplateVersion getLatestByTemplateId(String templateId);

    /**
     * 根据模板ID获取最新的非空版本号
     * @param templateId 模板ID
     * @return 最新的非空版本号，如果没有则返回null
     */
    String getLatestNonNullVersionByTemplateId(String templateId);
    
    /**
     * 根据ID获取模板版本
     * @param id 版本ID
     * @return 模板版本对象
     */
    TemplateVersion getById(String id);

    /**
     * 根据模板ID删除所有版本记录
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean deleteByTemplateId(String templateId);

}
