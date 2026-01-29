package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.WbsParameters;

import java.util.List;

public interface WbsParametersService extends BaseService<WbsParameters> {
    WbsParameters getById(String id);

    List<WbsParameters> getAllWbsParameters();

    List<WbsParameters> getAllWbsParametersByWbsId(String wbsId);

    List<WbsParameters> getChildrenByPid(String pid);

    List<WbsParameters> reorderWbsParameters(String wbsId);

    WbsParameters getWbsParametersByWbsCode(String wbsCode);

    boolean addWbsParameters(WbsParameters wbsParameters);

    boolean insertWbsParametersList(List<WbsParameters> wbsParametersList);

    boolean updateWbsParameters(WbsParameters wbsParameters);

    boolean updateWbsStatus(String wbsId);

    boolean deleteWbsParameters(String id);
}
