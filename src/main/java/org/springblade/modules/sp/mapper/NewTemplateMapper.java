package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.NewTemplate;
import org.springblade.modules.sp.vo.StreamListTemplatesVO;

import java.util.List;

/**
 * 新模板Mapper接口
 */
@Mapper
public interface NewTemplateMapper extends BaseMapper<NewTemplate> {
    /**
     * 获取所有模板
     * @return 模板列表
     */
    List<NewTemplate> getTemplateList();

    /**
     * 查询所有流水线及其关联的模板列表
     * @return 所有流水线与模板的关联数据
     */
    List<StreamListTemplatesVO> getAllStreamListTemplates();
    
    /**
     * 根据streamListId查询流水线及其关联的模板列表
     * @param streamListId 流水线列表ID
     * @return 指定流水线与模板的关联数据
     */
    List<StreamListTemplatesVO> getStreamListTemplatesById(String streamListId);
    
    /**
     * 根据流水线ID获取模板列表
     * @param streamId 流水线ID
     * @return 模板列表
     */
    List<NewTemplate> getTemplateListByStreamId(String streamId,String branchId);

    List<NewTemplate> getTemplateListDefault();

    List<NewTemplate> getNewTemplateByStreamListId(String streamListId);
    
    /**
     * 根据父ID获取模板列表
     * @param pid 父ID
     * @return 模板列表
     */
    List<NewTemplate> getTemplateListByPid(String pid);
    
    /**
     * 根据ID获取模板
     * @param id 模板ID
     * @return 模板
     */
    NewTemplate getTemplateById(String id);

    List<NewTemplate> getBomTemplate(String streamId,String branchId);
    
    /**
     * 过滤模板列表
     * @param pid 父ID
     * @param fileId 文件ID
     * @param streamId 流水线ID
     * @param branchId 分支ID
     * @param name 名称
     * @param type 类型
     * @param source 来源
     * @param version 版本
     * @param detail 详情
     * @param modifyUser 修改人
     * @param auth 权限
     * @return 模板列表
     */
    List<NewTemplate> filterTemplateList(String pid, String fileId, String streamId, String branchId, String name, String type, String source, String version, String detail, String modifyUser, String auth);
    
    /**
     * 新增模板
     * @param newTemplate 模板
     * @return 影响的行数
     */
    boolean addTemplate(NewTemplate newTemplate);
    
    /**
     * 更新模板
     * @param newTemplate 模板
     * @return 影响的行数
     */
    boolean updateTemplate(NewTemplate newTemplate);
    
    /**
     * 删除模板
     * @param id 模板ID
     * @return 影响的行数
     */
    boolean deleteTemplate(String id);

    boolean deleteByStream(String streamId);

    /**
     * 根据streamListId获取streamId为null的模板列表
     * @param streamListId 流水线列表ID
     * @return 模板列表
     */
    List<NewTemplate> getTemplatesByStreamListIdWithNullStreamId(String streamListId);

    /**
     * 根据streamId获取模板列表（用于删除检查）
     * @param streamId 流水线ID
     * @return 模板列表
     */
    List<NewTemplate> getTemplatesByStreamId(String streamId);
}
