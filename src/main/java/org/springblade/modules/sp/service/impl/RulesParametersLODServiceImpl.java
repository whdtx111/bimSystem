package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.RulesParametersLOD;
import org.springblade.modules.sp.mapper.RulesParametersLODMapper;
import org.springblade.modules.sp.service.RulesParametersLODService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Wbs接口实现类
 * @author dengtx
 */
@Service
@DS("postgresql")
public class RulesParametersLODServiceImpl extends BaseServiceImpl<RulesParametersLODMapper, RulesParametersLOD> implements RulesParametersLODService {

    @Autowired
    private RulesParametersLODMapper rulesParametersMapper;

    @Override
    public List<RulesParametersLOD> selectRulesParametersLODList() {
        return rulesParametersMapper.selectRulesParametersLODList();
    }

    @Override
    public List<RulesParametersLOD> getRulesParametersLODByPid(String pid) {
        return rulesParametersMapper.getRulesParametersLODByPid(pid);
    }

    @Override
    public RulesParametersLOD getRulesParametersLODById(String id) {
        return rulesParametersMapper.getRulesParametersLODById(id);
    }

    @Override
    public boolean insertRulesParametersLOD(RulesParametersLOD rulesParametersLOD) {
        return rulesParametersMapper.insertRulesParametersLOD(rulesParametersLOD);
    }

    @Override
    public boolean updateRulesParametersLOD(RulesParametersLOD rulesParametersLOD) {
        return rulesParametersMapper.updateRulesParametersLOD(rulesParametersLOD);
    }

    @Override
    public boolean deleteRulesParametersById(String id) {
        try{
            return rulesParametersMapper.deleteRulesParametersById(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRulesParametersByPid(String pid) {
        try {
            return rulesParametersMapper.deleteRulesParametersByPid(pid);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
