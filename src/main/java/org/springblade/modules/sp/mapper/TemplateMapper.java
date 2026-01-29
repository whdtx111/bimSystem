package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.TemplateDO;

import java.util.List;

/**
 * @author huang can
 * @since 2024/3/19 12:23
 */
public interface TemplateMapper extends BaseMapper<TemplateDO> {

    /**
     * 查询所有数据模板文件树节点
     * @param branchId
     * @return
     */
    List<TemplateDO> getTemplateList(String branchId,String streamId);

    TemplateDO getById(String id);

    /**
     * 添加数据模板文件树节点
     * @param templates
     * @return
     */
    boolean addTemplateNode(TemplateDO templates);

    /**
     * 删除数据模板文件树节点
     * @param id
     * @return
     */
    boolean deleteTemplateNode(String id);

    /**
     * 更新数据模板文件树节点
     * @param templates
     * @return
     */
    boolean updateTemplateNode(TemplateDO templates);
}
