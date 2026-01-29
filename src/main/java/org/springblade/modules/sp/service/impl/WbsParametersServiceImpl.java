package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.WbsParameters;
import org.springblade.modules.sp.mapper.WbsParametersMapper;
import org.springblade.modules.sp.service.WbsParametersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Wbs接口实现类
 * @author dengtx
 */
@Service
@DS("postgresql")
public class WbsParametersServiceImpl extends BaseServiceImpl<WbsParametersMapper, WbsParameters> implements WbsParametersService {

    @Autowired
    private WbsParametersMapper wbsParametersMapper;

    @Override
    public WbsParameters getById(String id) {
        return wbsParametersMapper.getById(id);
    }

    @Override
    public List<WbsParameters> getAllWbsParameters() {
        return wbsParametersMapper.getAllWbsParameters();
    }

    @Override
    public List<WbsParameters> getAllWbsParametersByWbsId(String wbsId) {
        return wbsParametersMapper.getAllWbsParametersByWbsId(wbsId);
    }

    @Override
    public List<WbsParameters> getChildrenByPid(String pid) {
        return wbsParametersMapper.getChildrenByPid(pid);
    }

    @Override
    public WbsParameters getWbsParametersByWbsCode(String wbsCode) {
        return wbsParametersMapper.getWbsParametersByWbsCode(wbsCode);
    }

    @Override
    public List<WbsParameters> reorderWbsParameters(String wbsId) {
        return wbsParametersMapper.reorderWbsParameters(wbsId);
    }

    @Override
    public boolean addWbsParameters(WbsParameters wbsParameters) {
        return wbsParametersMapper.addWbsParameters(wbsParameters);
    }

    @Override
    public boolean insertWbsParametersList(List<WbsParameters> wbsParametersList) {
        return wbsParametersMapper.insertWbsParametersList(wbsParametersList);
    }

    @Override
    public boolean updateWbsParameters(WbsParameters wbsParameters) {
        return wbsParametersMapper.updateWbsParameters(wbsParameters);
    }

    @Override
    public boolean updateWbsStatus(String wbsId) {
        return wbsParametersMapper.updateWbsStatus(wbsId);
    }

    @Override
    public boolean deleteWbsParameters(String id) {
        try {
            return  wbsParametersMapper.deleteWbsParameters(id);
        }catch (Exception e){
            return false;
        }
    }

}
