package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.RulesParameters;
import org.springblade.modules.sp.mapper.RulesParametersMapper;
import org.springblade.modules.sp.service.RulesParametersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Wbs接口实现类
 * @author dengtx
 */
@Service
@DS("postgresql")
public class RulesParametersServiceImpl extends BaseServiceImpl<RulesParametersMapper, RulesParameters> implements RulesParametersService {

    @Autowired
    private RulesParametersMapper rulesParametersMapper;

    @Override
    public RulesParameters getRulesParametersById(String id) {
       try {
           return rulesParametersMapper.getRulesParametersById(id);
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
    }

    @Override
    public List<RulesParameters> getAll(){
        try {
            return rulesParametersMapper.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addRulesParameters(RulesParameters rulesParameters) {
        try {
            return rulesParametersMapper.addRulesParameters(rulesParameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertRulesParametersBatch(List<RulesParameters> list) {
        try {
            return rulesParametersMapper.insertRulesParametersBatch(list);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRulesParameters(RulesParameters rulesParameters) {
        try {
            return rulesParametersMapper.updateRulesParameters(rulesParameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRulesParametersById(String id) {
        try {
            return rulesParametersMapper.deleteRulesParametersById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRulesParametersByDetailId(String detailId) {
        try {
            return rulesParametersMapper.deleteRulesParametersByDetailId(detailId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
