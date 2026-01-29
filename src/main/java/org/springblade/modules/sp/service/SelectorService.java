package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.Selector;

import java.util.List;


public interface SelectorService extends BaseService<Selector> {

    Selector getById(String id);

    List<Selector> searchFilter(String[] commitIds, String streamId);

    boolean addSelector(Selector selector);

    boolean updateSelector(Selector selector);

    boolean updateName(String id, String name);

    boolean deleteSelectorById(String id);
}
