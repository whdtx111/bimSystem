package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.WhiteList;

import java.util.List;

/**
 * 白名单Mapper接口
 */
@Mapper
public interface WhiteListMapper extends BaseMapper<WhiteList> {

    /**
     * 根据ID查询白名单记录
     */
    WhiteList getById(@Param("id") String id);

    /**
     * 查询所有白名单记录
     */
    List<WhiteList> getAll();

    /**
     * 根据模板ID查询白名单记录列表
     * @param templateId 模板ID
     * @param branchId 分支ID（可选）
     */
    List<WhiteList> getByTemplateId(@Param("templateId") String templateId,@Param("streamId") String streamId,@Param("branchId") String branchId);

    /**
     * 根据文件ID查询白名单记录列表
     */
    List<WhiteList> getByFileId(@Param("fileId") String fileId);

    /**
     * 根据名称查询白名单记录
     */
    List<WhiteList> getByName(@Param("name") String name);

    /**
     * 批量新增白名单记录
     */
    boolean batchInsert(@Param("list") List<WhiteList> whiteListList);

    /**
     * 更新白名单记录
     */
    boolean updateWhiteList(WhiteList whiteList);

    /**
     * 根据ID删除白名单记录
     */
    boolean deleteById(@Param("id") String id);

    /**
     * 根据模板ID删除白名单记录
     */
    boolean deleteByTemplateId(@Param("templateId") String templateId);

    /**
     * 根据文件ID删除白名单记录
     */
    boolean deleteByFileId(@Param("fileId") String fileId);
}
