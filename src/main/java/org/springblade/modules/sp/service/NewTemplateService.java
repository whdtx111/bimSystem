package org.springblade.modules.sp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.sp.entity.NewTemplate;
import org.springblade.modules.sp.vo.StreamListTemplatesVO;

import java.util.List;

/**
 * 新模板服务接口
 */
public interface NewTemplateService extends IService<NewTemplate> {

    /**
     * 获取模板列表
     * @return 模板列表
     */
    List<NewTemplate> getTemplateList();

    /**
     * 获取所有流水线及其关联的模板列表
     * @return 流水线和模板关联列表
     */
    List<StreamListTemplatesVO> getTemplateWithStreamByStreamId();

    /**
     * 根据流水线ID获取流水线及其关联的模板列表
     * @param streamListId 流水线ID
     * @return 流水线和模板关联列表
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
     * 根据模板ID获取模板
     * @param id 模板ID
     * @return 模板
     */
    NewTemplate getTemplateById(String id);

    List<NewTemplate> getBomTemplate(String streamId,String branchId);

    /**
     * 根据父级ID获取模板
     * @param pid 父级ID
     * @return 模板列表
     */
    List<NewTemplate> getTemplateListByPid(String pid);

    /**
     * 过滤模板列表
     * @param pid 父级ID
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
     * 保存模板
     * @param newTemplate 模板
     * @return 是否成功
     */
    boolean saveTemplate(NewTemplate newTemplate);

    /**
     * 更新模板
     * @param newTemplate 模板
     * @return 是否成功
     */
    boolean updateTemplate(NewTemplate newTemplate);

    /**
     * 删除模板
     * @param id 模板ID
     * @return 是否成功
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
     * 批量复制模板，保持父子级关系
     * @param templatesToCopy 待复制的模板列表
     * @param newStreamId 新的streamId
     * @param streamListId streamListId
     * @return 是否成功
     */
    boolean batchCopyTemplatesWithRelations(List<NewTemplate> templatesToCopy, String newStreamId, String streamListId);

    /**
     * 根据streamId获取模板列表（用于删除检查）
     * @param streamId 流水线ID
     * @return 模板列表
     */
    List<NewTemplate> getTemplatesByStreamId(String streamId);
}
