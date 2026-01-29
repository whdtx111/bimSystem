package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.WbsParametersCopy;
import org.springblade.modules.sp.mapper.WbsParametersCopyMapper;
import org.springblade.modules.sp.service.WbsParametersCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class WbsParametersCopyServiceImpl extends BaseServiceImpl<WbsParametersCopyMapper, WbsParametersCopy> implements WbsParametersCopyService {

    @Autowired
    private WbsParametersCopyMapper wbsParametersCopyMapper;

    @Override
    public WbsParametersCopy getById(String id) {
        return wbsParametersCopyMapper.getById(id);
    }

    @Override
    public List<WbsParametersCopy> getAllWbsParametersCopy() {
        return wbsParametersCopyMapper.getAllWbsParametersCopy();
    }

    @Override
    public List<WbsParametersCopy> getAllWbsParametersCopyByWbsId(String wbsId,String tempId) {
        return wbsParametersCopyMapper.getAllWbsParametersCopyByWbsId(wbsId,tempId);
    }

    @Override
    public List<WbsParametersCopy> getWbsParametersCopyByTempId(String tempId) {
        return wbsParametersCopyMapper.getWbsParametersCopyByTempId(tempId);
    }

    @Override
    public WbsParametersCopy getWbsParametersCopyByWbsCodeTempId(String wbsCode,String tempId) {
        return wbsParametersCopyMapper.getWbsParametersCopyByWbsCodeTempId(wbsCode,tempId);
    }

    @Override
    public boolean addWbsParametersCopy(WbsParametersCopy wbsParameters) {
        return wbsParametersCopyMapper.addWbsParametersCopy(wbsParameters);
    }

    @Override
    public boolean insertWbsParametersCopyList(List<WbsParametersCopy> wbsParametersList) {
        return wbsParametersCopyMapper.insertWbsParametersCopyList(wbsParametersList);
    }

    @Override
    public boolean updateWbsParametersCopy(WbsParametersCopy wbsParameters) {
        return wbsParametersCopyMapper.updateWbsParametersCopy(wbsParameters);
    }

    @Override
    public boolean updateWbsStatus(String wbsId) {
        return wbsParametersCopyMapper.updateWbsStatus(wbsId);
    }

    @Override
    public boolean deleteWbsParametersCopy(String id) {
        try {
            return  wbsParametersCopyMapper.deleteWbsParametersCopy(id);
        }catch (Exception e){
            return false;
        }
    }

}
