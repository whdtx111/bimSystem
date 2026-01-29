package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.ElementLibraryFile;

import java.util.List;

public interface ElementLibraryFileService extends BaseService<ElementLibraryFile> {

    ElementLibraryFile getById(String id);

    List<ElementLibraryFile> getAll(String pid);

    boolean addElementLibraryFile(ElementLibraryFile elementLibraryFile);

    boolean updateElementLibraryFile(ElementLibraryFile elementLibraryFile);

    boolean deleteElementLibraryFile(String id);

}
