package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.WbsParametersCopyColor;

import java.util.List;

@Mapper
public interface WbsParametersCopyColorMapper extends BaseMapper<WbsParametersCopyColor> {

    WbsParametersCopyColor getByWbsCode(String wbsCode,String wbsId);

    List<WbsParametersCopyColor> getAllWbsParametersCopyColorByWbsId(String wbsId);

    boolean addWbsParametersCopyColor(WbsParametersCopyColor wbsParametersCopyColor);

    boolean addWbsParametersCopyColorList(List<WbsParametersCopyColor> list);

    boolean updateWbsParametersCopyColor(WbsParametersCopyColor wbsParametersCopyColor);

    boolean deleteWbsParametersCopyColor(String id);
}
