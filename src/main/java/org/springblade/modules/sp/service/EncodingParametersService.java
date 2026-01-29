package org.springblade.modules.sp.service;

import com.github.pagehelper.Page;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.EncodingParameters;

import java.util.List;

public interface EncodingParametersService extends BaseService<EncodingParameters> {

    EncodingParameters getById(String id);

    List<EncodingParameters> getAllEncodingParameters();

    EncodingParameters getSheetByParametersId(String encodingParametersId);

    EncodingParameters getEncodingParametersByCode(String encodingId, String code);

    List<EncodingParameters> getAllEncodingParametersByEncodingId(String encodingId);

    List<EncodingParameters> getAllSubRecordsByLv0Id(String id);

//    Page<EncodingParameters> filterEncodingParameters(String name, String code, String encodingId, String lv, String encodingStatus,Integer pageSize, Integer currentPage);

    boolean deleteEncodingParameters(String id);

    boolean updateEncodingParameters(EncodingParameters encodingParameters);

    boolean updateEncodingStatus(String encodingId);

    boolean addEncodingParametersList(List<EncodingParameters> encodingParametersList);

    boolean addEncodingParameters(EncodingParameters encodingParameters);

    boolean deleteEncodingParametersByEncodingId(String encodingId);
}
