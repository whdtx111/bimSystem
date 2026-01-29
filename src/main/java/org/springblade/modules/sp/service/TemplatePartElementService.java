package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.TemplatePartElement;

import java.util.List;

public interface TemplatePartElementService extends BaseService<TemplatePartElement> {

    TemplatePartElement getByTemplateId(String templateId);

    boolean addTemplatePartElement(TemplatePartElement templatePartElement);

    boolean updateTemplatePartElement(TemplatePartElement templatePartElement);

    boolean deleteTemplatePartElement(String templateId);

    /**
     * 根据模板ID获取最新的两条记录
     * @param templateId 模板ID
     * @return 最新的两条记录列表
     */
    List<TemplatePartElement> getLatestTwoByTemplateId(String templateId);

}
