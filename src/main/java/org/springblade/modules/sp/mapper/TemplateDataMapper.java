package org.springblade.modules.sp.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.TemplateData;

import java.util.List;

@Mapper
public interface TemplateDataMapper extends BaseMapper<TemplateData> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    TemplateData getById(@Param("id") String id);

    /**
     * 根据templateId和type获取data数据
     * @param templateId 模板ID
     * @param type 类型
     * @param branchId 分支ID
     * @return data的JSON数据
     */
    JSONObject getDataByTemplateIdAndType(@Param("templateId") String templateId, @Param("type") String type, @Param("branchId") String branchId);

    /**
     * 根据templateId和type获取模板数据
     * @param templateId 模板ID
     * @param type 类型
     * @param branchId 分支ID
     * @return 模板数据实体
     */
    TemplateData getByTemplateIdAndType(@Param("templateId") String templateId, @Param("type") String type, @Param("branchId") String branchId);

    /**
     * 根据templateId查询列表
     * @param templateId
     * @return
     */
    List<TemplateData> getByTemplateId(@Param("templateId") String templateId);

    /**
     * 根据type查询列表
     * @param type
     * @return
     */
    List<TemplateData> getByType(@Param("type") String type);

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
    boolean deleteById(@Param("id") String id);

    /**
     * 根据templateId和type删除
     * @param templateId 模板ID
     * @param type 类型
     * @param branchId 分支ID
     * @return
     */
    boolean deleteByTemplateIdAndType(@Param("templateId") String templateId, @Param("type") String type, @Param("branchId") String branchId);

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
     * @return 记录数量
     */
    int countByTemplateId(@Param("templateId") String templateId);

    /**
     * 根据模板ID删除所有记录
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean deleteByTemplateId(@Param("templateId") String templateId);
}
