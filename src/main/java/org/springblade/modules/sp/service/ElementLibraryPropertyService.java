package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.ElementLibraryProperty;

import java.util.List;

public interface ElementLibraryPropertyService extends BaseService<ElementLibraryProperty> {

    ElementLibraryProperty getById(String id);

    List<ElementLibraryProperty> getAll(String pid);

    boolean addElementLibraryProperty(ElementLibraryProperty elementLibraryProperty);

    boolean updateElementLibraryProperty(ElementLibraryProperty elementLibraryProperty);

    boolean deleteElementLibraryProperty(String id);

    boolean insertElementLibraryPropertyList(List<ElementLibraryProperty> list);
}
