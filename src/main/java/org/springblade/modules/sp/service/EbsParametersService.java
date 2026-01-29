package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.EbsParameters;

import java.util.List;

public interface EbsParametersService extends BaseService<EbsParameters> {
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
