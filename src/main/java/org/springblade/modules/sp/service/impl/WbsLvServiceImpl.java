package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.WbsLv;
import org.springblade.modules.sp.mapper.WbsLvMapper;
import org.springblade.modules.sp.service.WbsLvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class WbsLvServiceImpl extends BaseServiceImpl<WbsLvMapper, WbsLv> implements WbsLvService {

    @Autowired
    private WbsLvMapper wbsLvMapper;

    @Override
    public WbsLv getById(String id){
        return wbsLvMapper.getById(id);
    }

    @Override
    public List<WbsLv> getWbsLvByWbsId(String wbsId){
        return wbsLvMapper.getWbsLvByWbsId(wbsId);
    }

    @Override
    public  List<WbsLv> filterWbsLv(String code,String nameCn,String wbsId){
        return wbsLvMapper.filterWbsLv(code,nameCn,wbsId);
    }

    @Override
    public boolean addWbsLv(WbsLv wbsLv){
        return wbsLvMapper.addWbsLv(wbsLv);
    }

    @Override
    public boolean updateWbsLv(WbsLv wbsLv){
        return wbsLvMapper.updateWbsLv(wbsLv);
    }

    @Override
    public boolean deleteWbsLv(String id){
        return wbsLvMapper.deleteWbsLv(id);
    }
}
