package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.RulesParameters;

import java.util.List;


public interface RulesParametersService extends BaseService<RulesParameters> {

    RulesParameters getRulesParametersById(String id);

//    List<RulesParameters> getRulesParametersByDetailId(String detailId);

    List<RulesParameters> getAll();

    boolean addRulesParameters(RulesParameters rulesParameters);

    boolean insertRulesParametersBatch(List<RulesParameters> list);

    boolean updateRulesParameters(RulesParameters rulesParameters);

    boolean deleteRulesParametersById(String id);

    boolean deleteRulesParametersByDetailId(String detailId);

}
