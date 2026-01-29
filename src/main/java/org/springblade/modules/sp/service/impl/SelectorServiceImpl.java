package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.Selector;
import org.springblade.modules.sp.mapper.SelectorMapper;
import org.springblade.modules.sp.service.SelectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class SelectorServiceImpl extends BaseServiceImpl<SelectorMapper, Selector> implements SelectorService {

    @Autowired
    private SelectorMapper selectorMapper;

    @Override
    public Selector getById(String id) {
        return selectorMapper.getById(id);
    }

   @Override
    public List<Selector> searchFilter(String[] commitIds, String streamId) {
        return selectorMapper.searchFilter(commitIds, streamId);
    }

    @Override
    public boolean addSelector(Selector selector) {
        return selectorMapper.addSelector(selector);
    }

    @Override
    public boolean updateSelector(Selector selector) {
        return selectorMapper.updateSelector(selector);
    }

    @Override
    public boolean updateName(String id, String name) {
        return selectorMapper.updateName(id, name);
    }

    @Override
    public boolean deleteSelectorById(String id) {
        return selectorMapper.deleteSelectorById(id);
    }

}
