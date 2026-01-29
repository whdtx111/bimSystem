package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.dto.RevitAllUPDTO;
import org.springblade.modules.sp.entity.TemplateUpdate;

import java.util.List;

/**
 * 模板修改表 Mapper 接口
 */
@Mapper
public interface TemplateUpdateMapper extends BaseMapper<TemplateUpdate> {

    /**
     * 根据ID获取模板修改记录
     * @param id 主键ID
     * @return 模板修改记录
     */
    TemplateUpdate getById(@Param("id") String id);

    /**
     * 根据模板ID获取模板修改记录列表
     * @param templateId 模板ID
     * @return 模板修改记录列表
     */
    List<TemplateUpdate> getByTemplateId(@Param("templateId") String templateId);

    /**
     * 根据流水线ID和分支ID获取模板修改记录列表
     * @param streamId 流水线ID
     * @param branchId 分支ID
     * @return 模板修改记录列表
     */
    TemplateUpdate getByStreamAndBranchAndTemplateId(@Param("streamId") String streamId, @Param("branchId") String branchId, @Param("templateId") String templateId);

    /**
     * 根据流水线ID、分支ID、模板ID获取所有记录，按修改时间倒序
     * @param streamId 流水线ID
     * @param branchId 分支ID
     * @param templateId 模板ID
     * @return 模板修改记录列表
     */
    List<TemplateUpdate> getAllByStreamAndBranchAndTemplateId(@Param("streamId") String streamId, @Param("branchId") String branchId, @Param("templateId") String templateId);

    /**
     * 新增模板修改记录
     * @param templateUpdate 模板修改记录
     * @return 是否成功
     */
    boolean addTemplateUpdate(TemplateUpdate templateUpdate);

    /**
     * 更新模板修改记录
     * @param templateUpdate 模板修改记录
     * @return 是否成功
     */
    boolean updateTemplateUpdate(TemplateUpdate templateUpdate);

    /**
     * 删除模板修改记录
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteTemplateUpdate(@Param("id") String id);

    /**
     * 根据模板ID删除所有记录
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean deleteByTemplateId(@Param("templateId") String templateId);
}
