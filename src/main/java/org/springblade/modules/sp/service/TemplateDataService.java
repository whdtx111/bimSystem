package org.springblade.modules.sp.service;

import com.alibaba.fastjson.JSONObject;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.TemplateData;

import java.util.List;

/**
 * 模板数据服务接口
 */
public interface TemplateDataService extends BaseService<TemplateData> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    TemplateData getById(String id);

    /**
     * 根据templateId和type获取data数据
     * @param templateId 模板ID
     * @param type 类型
     * @param branchId 分支ID
     * @return data的JSON数据
     */
    JSONObject getDataByTemplateIdAndType(String templateId, String type, String branchId);

    /**
     * 根据templateId和type获取模板数据实体
     * @param templateId 模板ID
     * @param type 类型
     * @param branchId 分支ID
     * @return 模板数据实体
     */
    TemplateData getByTemplateIdAndType(String templateId, String type, String branchId);

    /**
     * 根据templateId查询列表
     * @param templateId
     * @return
     */
    List<TemplateData> getByTemplateId(String templateId);

    /**
     * 根据type查询列表
     * @param type
     * @return
     */
    List<TemplateData> getByType(String type);

    /**
     * 新增
     * @param templateData
     * @return
     */
    boolean addTemplateData(TemplateData templateData);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    boolean deleteById(String id);

    /**
     * 根据templateId和type删除
     * @param templateId 模板ID
     * @param type 类型
     * @param branchId 分支ID
     * @return
     */
    boolean deleteByTemplateIdAndType(String templateId, String type, String branchId);

    /**
     * 更新
     * @param templateData
     * @return
     */
    boolean updateTemplateData(TemplateData templateData);

    /**
     * 获取所有数据
     * @return
     */
    List<TemplateData> getAllTemplateData();

    /**
     * 检查模板是否有数据
     * @param templateId 模板ID
     * @return 是否有数据
     */
    boolean hasDataByTemplateId(String templateId);

    /**
     * 保存合并后的数据到数据库
     * @param templateId 模板ID
     * @param type 类型
     * @param branchId 分支ID
     * @param blockName 地块号
     * @return 是否保存成功
     */
    boolean saveMergedData(String templateId, String type, String branchId, String blockName);

    /**
     * 根据模板ID删除所有记录
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean deleteByTemplateId(String templateId);
}
