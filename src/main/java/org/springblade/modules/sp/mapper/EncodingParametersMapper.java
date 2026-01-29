package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.EncodingParameters;

import java.util.List;

@Mapper
public interface EncodingParametersMapper extends BaseMapper<EncodingParameters> {


    EncodingParameters getById(String id);

    List<EncodingParameters> getAllEncodingParameters();

    EncodingParameters getSheetByParametersId(String encodingParametersId);

    EncodingParameters getEncodingParametersByCode(String encodingId, String code);

    List<EncodingParameters> getAllSubRecordsByLv0Id(String id);

//    Page<EncodingParameters> filterEncodingParameters(String name, String code, String encodingId, String lv, String encodingStatus);

    List<EncodingParameters> getAllEncodingParametersByEncodingId(String encodingId);

    boolean deleteEncodingParameters(String id);

    boolean updateEncodingParameters(EncodingParameters encodingParameters);

    boolean updateEncodingStatus(String encodingId);

    boolean addEncodingParametersList(List<EncodingParameters> encodingParametersList);

    boolean addEncodingParameters(EncodingParameters encodingParameters);

    boolean deleteEncodingParametersByEncodingId(String encodingId);
}
