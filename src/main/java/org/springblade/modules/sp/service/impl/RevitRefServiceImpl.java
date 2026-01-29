package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.RevitRef;
import org.springblade.modules.sp.mapper.RevitRefMapper;
import org.springblade.modules.sp.service.RevitRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class RevitRefServiceImpl extends BaseServiceImpl<RevitRefMapper, RevitRef> implements RevitRefService {

    @Autowired
    private RevitRefMapper revitRefMapper;


    @Override
    public RevitRef getById(String id) {
        return revitRefMapper.getById(id);
    }

    @Override
    public List<RevitRef> getAllByGroup(String group) {
        return revitRefMapper.getAllByGroup(group);
    }

    @Override
    public boolean addRevitRef(RevitRef revitRef) {
        return revitRefMapper.addRevitRef(revitRef);
    }

}
