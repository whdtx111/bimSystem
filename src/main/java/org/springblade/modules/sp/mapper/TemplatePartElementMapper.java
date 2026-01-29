package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.NewTemplate;
import org.springblade.modules.sp.entity.TemplatePartElement;
import org.springblade.modules.sp.vo.StreamListTemplatesVO;

import java.util.List;

/**
 * 新模板Mapper接口
 */
@Mapper
public interface TemplatePartElementMapper extends BaseMapper<TemplatePartElement> {

    TemplatePartElement getByTemplateId(String templateId);

    boolean addTemplatePartElement(TemplatePartElement templatePartElement);

    boolean updateTemplatePartElement(TemplatePartElement templatePartElement);

    boolean deleteTemplatePartElement(String templateId);

    /**
     * 根据模板ID获取最新的两条记录，按创建时间倒序排列
     * @param templateId 模板ID
     * @return 最新的两条记录列表
     */
    List<TemplatePartElement> getLatestTwoByTemplateId(String templateId);

}
