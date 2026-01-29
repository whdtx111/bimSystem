package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.RulesParameters;
import org.springblade.modules.sp.entity.RulesParametersLOD;

import java.util.List;


public interface RulesParametersLODService extends BaseService<RulesParametersLOD> {

    RulesParametersLOD getRulesParametersLODById(String id);

    List<RulesParametersLOD> selectRulesParametersLODList();

    List<RulesParametersLOD> getRulesParametersLODByPid(String pid);

    boolean insertRulesParametersLOD(RulesParametersLOD rulesParametersLOD);

    boolean updateRulesParametersLOD(RulesParametersLOD rulesParametersLOD);

    boolean deleteRulesParametersById(String id);

    boolean deleteRulesParametersByPid(String pid);
}
