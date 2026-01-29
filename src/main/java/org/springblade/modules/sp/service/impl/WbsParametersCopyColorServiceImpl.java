package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.WbsParametersCopyColor;
import org.springblade.modules.sp.mapper.WbsParametersCopyColorMapper;
import org.springblade.modules.sp.service.WbsParametersCopyColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class WbsParametersCopyColorServiceImpl extends BaseServiceImpl<WbsParametersCopyColorMapper, WbsParametersCopyColor> implements WbsParametersCopyColorService {

    @Autowired
    private WbsParametersCopyColorMapper wbsParametersCopyColorMapper;

    @Override
    public List<WbsParametersCopyColor> getAllWbsParametersCopyColorByWbsId(String wbsId) {
       try {
           List<WbsParametersCopyColor> res = wbsParametersCopyColorMapper.getAllWbsParametersCopyColorByWbsId(wbsId);
           return res;
       }catch (Exception e){
           e.printStackTrace();
           return null;
       }
    }

    @Override
    public WbsParametersCopyColor getByWbsCode(String wbsCode,String wbsId) {
        try {
            WbsParametersCopyColor res = wbsParametersCopyColorMapper.getByWbsCode(wbsCode,wbsId);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addWbsParametersCopyColor(WbsParametersCopyColor wbsParametersCopyColor) {
        try {
            boolean res = wbsParametersCopyColorMapper.addWbsParametersCopyColor(wbsParametersCopyColor);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addWbsParametersCopyColorList(List<WbsParametersCopyColor> list) {
        try {
            boolean res = wbsParametersCopyColorMapper.addWbsParametersCopyColorList(list);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateWbsParametersCopyColor(WbsParametersCopyColor wbsParametersCopyColor) {
        try {
            boolean res = wbsParametersCopyColorMapper.updateWbsParametersCopyColor(wbsParametersCopyColor);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteWbsParametersCopyColor(String id) {
        try {
            boolean res = wbsParametersCopyColorMapper.deleteWbsParametersCopyColor(id);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
