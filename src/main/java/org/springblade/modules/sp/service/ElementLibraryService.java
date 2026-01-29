package org.springblade.modules.sp.service;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.ElementLibrary;
import org.springblade.modules.sp.entity.ElementLibraryMin;
import org.springblade.modules.sp.entity.ElementLibraryWithFiles;

import java.util.List;

public interface ElementLibraryService extends BaseService<ElementLibrary> {

    ElementLibrary getById(String id);

    List<ElementLibrary> getAll();

    List<ElementLibraryMin> getElementLibraryList(String projectId);

    ElementLibrary getElementLibraryByFileId(String fileId);

    boolean addElementLibrary(ElementLibrary elementLibrary);

    boolean updateElementLibrary(ElementLibrary elementLibrary);

    boolean deleteElementLibrary(String id);

    boolean insertElementLibraryList(List<ElementLibrary> list);

    ElementLibraryWithFiles getElementLibraryWithFiles(@Param("id") String id);

    List<ElementLibraryWithFiles> getElementLibraryWithFilesByProject(String projectId);

    List<ElementLibraryWithFiles> filterElementLibraryList(String name,String[] tags,String[] project,String propertyList,String system,Integer status,String[] library,String lodNow);
}
