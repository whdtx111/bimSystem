package org.springblade.modules.sp.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.Wbs;
import org.springblade.modules.sp.mapper.WbsMapper;
import org.springblade.modules.sp.service.WbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * EBS接口实现类
 * @author dengtx
 */
@Service
@DS("postgresql")
public class WbsServiceImpl extends BaseServiceImpl<WbsMapper, Wbs> implements WbsService {

    @Autowired
    private WbsMapper wbsMapper;

    @Override
    public Wbs getById(String id){
        try {
          Wbs wbs = wbsMapper.getById(id);
          return wbs;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Wbs> getAllWbs(){
        List<Wbs> allWbs = wbsMapper.getAllWbs();
        return allWbs;
    }

    @Override
    public Page<Wbs> filterWbs(String wbsCode, String wbsName, String type, String source, String auth, Integer wbsStatus, Integer pageSize, Integer currentPage){
        PageHelper.startPage(currentPage, pageSize);
        Page<Wbs> filterWbs = wbsMapper.filterWbs(wbsCode,wbsName,type,source,auth,wbsStatus);
        return filterWbs;
    }

    @Override
    public boolean addWbs(Wbs wbs){
        return wbsMapper.addWbs(wbs);
    }

    @Override
    public boolean insertWbsList(List<Wbs> wbsList){
        return wbsMapper.insertWbsList(wbsList);
    }

    @Override
    public boolean deleteWbs(String id){
        return wbsMapper.deleteWbs(id);
    }

    @Override
    public boolean updateWbs(Wbs wbs){
        return wbsMapper.updateWbs(wbs);
    }

    @Override
    public boolean updateWbsStatus(String id){
        return wbsMapper.updateWbsStatus(id);
    }
}
