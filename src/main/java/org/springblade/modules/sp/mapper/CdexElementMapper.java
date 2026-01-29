package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.CdexElement;
import org.springblade.modules.sp.entity.WbsParameters;

import java.util.List;

@Mapper
public interface CdexElementMapper extends BaseMapper<CdexElement> {

    CdexElement getById(String id);

    CdexElement getByName(String name);

    boolean addCdexElement(CdexElement cdexElement);

    boolean updateCdexElement(CdexElement cdexElement);

    boolean deleteCdexElement(String id);

}
