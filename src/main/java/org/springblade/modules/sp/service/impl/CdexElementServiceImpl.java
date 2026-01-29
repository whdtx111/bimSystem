package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.CdexElement;
import org.springblade.modules.sp.mapper.CdexElementMapper;
import org.springblade.modules.sp.service.CdexElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DS("postgresql")
public class CdexElementServiceImpl extends BaseServiceImpl<CdexElementMapper, CdexElement> implements CdexElementService {

    @Autowired
    private CdexElementMapper cdexElementMapper;

    @Override
    public CdexElement getById(String id) {
        try {
            return cdexElementMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CdexElement getByName(String name) {
        try {
            return cdexElementMapper.getByName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateCdexElement(CdexElement cdexElement) {
        try {
            return cdexElementMapper.updateCdexElement(cdexElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addCdexElement(CdexElement cdexElement) {
        try {
            return cdexElementMapper.addCdexElement(cdexElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCdexElement(String id) {
        try {
            return cdexElementMapper.deleteCdexElement(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
