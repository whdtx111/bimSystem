package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.WbsParameters;

import java.util.List;

@Mapper
public interface WbsParametersMapper extends BaseMapper<WbsParameters> {

    WbsParameters getById(String id);

    List<WbsParameters> getAllWbsParameters();

    List<WbsParameters> getAllWbsParametersByWbsId(String wbsId);

    List<WbsParameters> reorderWbsParameters(String wbsId);

    List<WbsParameters> getChildrenByPid(String pid);

    WbsParameters getWbsParametersByWbsCode(String wbsCode);

    boolean addWbsParameters(WbsParameters wbsParameters);

    boolean insertWbsParametersList(List<WbsParameters> wbsParametersList);

    boolean updateWbsParameters(WbsParameters wbsParameters);

    boolean updateWbsStatus(String wbsId);

    boolean deleteWbsParameters(String id);


}
