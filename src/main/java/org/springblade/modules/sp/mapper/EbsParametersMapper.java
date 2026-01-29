package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.EbsParameters;

import java.util.List;
@Mapper
public interface EbsParametersMapper  extends BaseMapper<EbsParameters> {

    EbsParameters getById(String id);

    List<EbsParameters> getAllEbsParameters();

    List<EbsParameters> getAllEbsParametersByEbsId(String ebsId);

    List<EbsParameters> reorderEbsParameters(String ebsId);

    boolean addEbsParameters(EbsParameters ebsParameters);

    boolean insertEbsParametersList(List<EbsParameters> list);

    boolean updateEbsParameters(EbsParameters ebsParameters);

    boolean updateEbsStatus(String ebsId);

    boolean deleteEbsParameters(String id);


}
