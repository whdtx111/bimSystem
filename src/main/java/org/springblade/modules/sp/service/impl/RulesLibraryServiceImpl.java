package org.springblade.modules.sp.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aliyun.oss.ServiceException;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.RulesDetail;
import org.springblade.modules.sp.entity.RulesLibrary;
import org.springblade.modules.sp.entity.RulesParameters;
import org.springblade.modules.sp.mapper.RulesDetailMapper;
import org.springblade.modules.sp.mapper.RulesLibraryMapper;
import org.springblade.modules.sp.mapper.RulesParametersMapper;
import org.springblade.modules.sp.service.RulesLibraryService;
import org.springblade.modules.sp.vo.RulesDetailVO;
import org.springblade.modules.sp.vo.RulesLibraryDetailVO;
import org.springblade.modules.sp.vo.RulesLibraryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Wbs接口实现类
 * @author dengtx
 */
@Service
@DS("postgresql")
public class RulesLibraryServiceImpl extends BaseServiceImpl<RulesLibraryMapper, RulesLibrary> implements RulesLibraryService {

    @Autowired
    private RulesLibraryMapper rulesLibraryMapper;

    @Autowired
    private RulesDetailMapper rulesDetailMapper;

    @Autowired
    private RulesParametersMapper rulesParametersMapper;

    @Override
    public RulesLibrary getRulesLibraryById(String id) {
        try {
            return rulesLibraryMapper.getRulesLibraryById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RulesLibrary> getAll() {
        try {
            return rulesLibraryMapper.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page<RulesLibrary> filterRulesLibrary(String name, String auth, String source, Integer status, Integer pageSize, Integer currentPage) {
        try {
            PageHelper.startPage(currentPage, pageSize);
            Page<RulesLibrary> filterRulesLibrary = rulesLibraryMapper.filterRulesLibrary(name, auth, source, status);
            return filterRulesLibrary;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addRulesLibrary(RulesLibrary rulesLibrary) {
        try {
            return rulesLibraryMapper.addRulesLibrary(rulesLibrary);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertRulesLibraryBatch(List<RulesLibrary> list) {
        try {
            return rulesLibraryMapper.insertRulesLibraryBatch(list);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRulesLibrary(RulesLibrary rulesLibrary) {
        try {
            return rulesLibraryMapper.updateRulesLibrary(rulesLibrary);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRulesLibraryById(String id) {
        try {
            return rulesLibraryMapper.deleteRulesLibraryById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public RulesLibraryDetailVO getRulesLibraryWithDetails(String id) {
        try {
            return rulesLibraryMapper.getRulesLibraryWithDetails(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RulesLibraryDetailVO> getAllRulesLibraryWithDetails(String streamId) {
        try {
            return rulesLibraryMapper.getAllRulesLibraryWithDetails(streamId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRulesDetails(RulesLibraryVO vo) {
        try {
            if (CollUtil.isNotEmpty(vo.getRulesDetails())) {
                for (RulesDetailVO detail : vo.getRulesDetails()) {
                    // 设置关联ID
                    detail.setPid(vo.getId());
                    detail.setId(UUID.randomUUID().toString());
                    rulesDetailMapper.addRulesDetail(detail);

                    // 保存规则参数
                    if (CollUtil.isNotEmpty(detail.getParameters())) {
                        for (RulesParameters param : detail.getParameters()) {
                            param.setId(UUID.randomUUID().toString());
                            param.setDetailId(detail.getId());
                            param.setStatus(0);
                            rulesParametersMapper.addRulesParameters(param);
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
           e.printStackTrace();
            return false;
        }
    }

}
