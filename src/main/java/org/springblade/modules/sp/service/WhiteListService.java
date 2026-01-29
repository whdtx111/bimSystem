package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.WhiteList;

import java.util.List;

/**
 * 白名单Service接口
 */
public interface WhiteListService extends BaseService<WhiteList> {

    /**
     * 根据ID查询白名单记录
     */
    WhiteList getById(String id);

    /**
     * 查询所有白名单记录
     */
    List<WhiteList> getAll();

    /**
     * 根据模板ID查询白名单记录列表
     * @param templateId 模板ID
     * @param branchId 分支ID（可选）
     */
    List<WhiteList> getByTemplateId(String templateId,String streamId,String branchId);

    /**
     * 根据文件ID查询白名单记录列表
     */
    List<WhiteList> getByFileId(String fileId);

    /**
     * 根据名称查询白名单记录
     */
    List<WhiteList> getByName(String name);

    /**
     * 新增白名单记录
     */
    boolean addWhiteList(WhiteList whiteList);

    /**
     * 批量新增白名单记录
     */
    boolean batchAddWhiteList(List<WhiteList> whiteListList);

    /**
     * 更新白名单记录
     */
    boolean updateWhiteList(WhiteList whiteList);

    /**
     * 根据ID删除白名单记录
     */
    boolean deleteById(String id);

    /**
     * 根据模板ID删除白名单记录
     */
    boolean deleteByTemplateId(String templateId);

    /**
     * 根据文件ID删除白名单记录
     */
    boolean deleteByFileId(String fileId);
}
