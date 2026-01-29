package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.dto.RevitAllUPDTO;
import org.springblade.modules.sp.entity.TemplateCustomData;

import java.util.List;

/**
 * 自定义模板属性回传Mapper接口
 */
@Mapper
public interface TemplateCustomDataMapper extends BaseMapper<TemplateCustomData> {

    /**
     * 根据ID查询
     */
    TemplateCustomData getById(@Param("id") String id);

    /**
     * 根据条件查询列表
     */
    List<TemplateCustomData> searchFilter(@Param("elementId") String elementId, 
                                           @Param("streamId") String streamId, 
                                           @Param("branchId") String branchId,
                                           @Param("templateId") String templateId);

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
    boolean deleteById(@Param("id") String id);
    
    /**
     * 批量新增
     */
    boolean batchInsert(@Param("list") List<TemplateCustomData> list);

    boolean updateFinished(RevitAllUPDTO revitAllUPDTO);

    /**
     * 根据streamId/branchId/commitId/elementId查询记录
     */
    TemplateCustomData getByStreamBranchCommitElementId(@Param("streamId") String streamId,
                                                         @Param("branchId") String branchId,
                                                         @Param("commitId") String commitId,
                                                         @Param("elementId") String elementId);

    /**
     * 根据模板ID删除所有记录
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean deleteByTemplateId(@Param("templateId") String templateId);
}
