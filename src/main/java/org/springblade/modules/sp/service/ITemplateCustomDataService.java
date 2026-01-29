package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.dto.RevitAllUPDTO;
import org.springblade.modules.sp.entity.TemplateCustomData;

import java.util.List;

/**
 * 自定义模板属性回传Service接口
 */
public interface ITemplateCustomDataService extends BaseService<TemplateCustomData> {

    /**
     * 根据ID查询
     */
    TemplateCustomData getById(String id);

    /**
     * 根据条件查询列表
     */
    List<TemplateCustomData> searchFilter(String elementId, String streamId, String branchId, String templateId);

    /**
     * 新增记录
     */
    boolean addTemplateCustomData(TemplateCustomData templateCustomData);

    /**
     * 更新记录
     */
    boolean updateTemplateCustomData(TemplateCustomData templateCustomData);

    /**
     * 根据ID删除记录
     */
    boolean deleteById(String id);
    
    /**
     * 批量新增
     */
    boolean batchInsert(List<TemplateCustomData> list);

    boolean updateFinished(RevitAllUPDTO revitAllUPDTO);

    /**
     * 根据streamId/branchId/commitId/elementId查询记录
     */
    TemplateCustomData getByStreamBranchCommitElementId(String streamId, String branchId, String commitId, String elementId);

    /**
     * 根据模板ID删除所有记录
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean deleteByTemplateId(String templateId);
}
