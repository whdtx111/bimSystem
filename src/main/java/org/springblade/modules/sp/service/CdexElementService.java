package org.springblade.modules.sp.service;

import com.github.pagehelper.Page;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.CdexElement;

import java.util.List;

public interface CdexElementService extends BaseService<CdexElement> {

    CdexElement getById(String id);

    CdexElement getByName(String name);

    boolean addCdexElement(CdexElement cdexElement);

    boolean updateCdexElement(CdexElement cdexElement);

    boolean deleteCdexElement(String id);
}
