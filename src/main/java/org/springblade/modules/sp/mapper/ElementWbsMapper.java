package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.ElementWbs;

import java.util.List;

public interface ElementWbsMapper extends BaseMapper<ElementWbs> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
     ElementWbs getById(String id);

//    ElementWbs getElementByWbs(String wbsCode);
//
//    ElementWbs getWbsByElement(String elementId);

      ElementWbs getElementWbs(String wbsCode,String branchId,String tempId);

      List<ElementWbs> getAllElementWbs(String branchId,String tempId);

    /**
     * 新增
     * @param
     * @return
     */
    boolean linkElementWbs(ElementWbs elementWbs);

    boolean updateElementWbs(String id, String[] elementId);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean unlinkElementWbs(String id);
}
