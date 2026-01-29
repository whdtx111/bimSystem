package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.WbsParametersCopy;

import java.util.List;

@Mapper
public interface WbsParametersCopyMapper extends BaseMapper<WbsParametersCopy> {

    WbsParametersCopy getById(String id);

    List<WbsParametersCopy> getAllWbsParametersCopy();

    List<WbsParametersCopy> getAllWbsParametersCopyByWbsId(String wbsId,String tempId);

    WbsParametersCopy getWbsParametersCopyByWbsCodeTempId(String wbsCode,String tempId);

    List<WbsParametersCopy> getWbsParametersCopyByTempId(String tempId);

    boolean addWbsParametersCopy(WbsParametersCopy wbsParameters);

    boolean insertWbsParametersCopyList(List<WbsParametersCopy> wbsParametersList);

    boolean updateWbsParametersCopy(WbsParametersCopy wbsParameters);

    boolean updateWbsStatus(String wbsId);

    boolean deleteWbsParametersCopy(String id);

}
