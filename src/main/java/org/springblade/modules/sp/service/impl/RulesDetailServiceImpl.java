package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.RulesDetail;
import org.springblade.modules.sp.mapper.RulesDetailMapper;
import org.springblade.modules.sp.service.RulesDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Wbs接口实现类
 * @author dengtx
 */
@Service
@DS("postgresql")
public class RulesDetailServiceImpl extends BaseServiceImpl<RulesDetailMapper, RulesDetail> implements RulesDetailService {

    @Autowired
    private RulesDetailMapper rulesDetailMapper;

    @Override
    public RulesDetail getRulesDetailById(String id) {
       try {
           return rulesDetailMapper.getRulesDetailById(id);
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
    }

    @Override
    public List<RulesDetail> getAll() {
        try {
            return rulesDetailMapper.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RulesDetail> getRulesDetailByPid(String pid) {
        try {
            return rulesDetailMapper.getRulesDetailByPid(pid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addRulesDetail(RulesDetail rulesDetail) {
        try {
            return rulesDetailMapper.addRulesDetail(rulesDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertRulesDetailBatch(List<RulesDetail> list) {
        try {
            return rulesDetailMapper.insertRulesDetailBatch(list);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRulesDetail(RulesDetail rulesDetail) {
        try {
            return rulesDetailMapper.updateRulesDetail(rulesDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRulesDetailById(String id) {
        try {
            return rulesDetailMapper.deleteRulesDetailById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRulesDetailByPid(String pid) {
        try {
            return rulesDetailMapper.deleteRulesDetailByPid(pid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
