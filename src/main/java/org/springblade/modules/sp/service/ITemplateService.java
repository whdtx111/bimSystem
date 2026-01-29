package org.springblade.modules.sp.service;

import org.springblade.modules.sp.entity.TemplateDO;

import java.util.List;

/**
 * 数据模板类
 * @author huang can/dengtx
 * @since 2024/3/19 12:05
 */

public interface ITemplateService {

    public List<TemplateDO> getTemplateList(String branchId,String streamId);

    TemplateDO getById(String id);

    public boolean addTemplateNode(TemplateDO templates);

    public boolean deleteTemplateNode(String id);

    public boolean updateTemplateNode(TemplateDO templates);
}
