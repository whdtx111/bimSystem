package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.ElementLibrary;
import org.springblade.modules.sp.entity.ElementLibraryMin;
import org.springblade.modules.sp.entity.ElementLibraryWithFiles;

import java.util.List;
@Mapper
public interface ElementLibraryMapper extends BaseMapper<ElementLibrary> {

    ElementLibrary getById(String id);

    List<ElementLibrary> getAll();

    List<ElementLibraryMin> getElementLibraryList(String projectId);

    ElementLibrary getElementLibraryByFileId(String fileId);

    boolean addElementLibrary(ElementLibrary elementLibrary);

    boolean updateElementLibrary(ElementLibrary elementLibrary);

    boolean deleteElementLibrary(String id);

    boolean insertElementLibraryList(List<ElementLibrary> list);

    // 联表查询方法
    ElementLibraryWithFiles getElementLibraryWithFiles(@Param("id") String id);

    List<ElementLibraryWithFiles> getElementLibraryWithFilesByProject(String projectId);

    List<ElementLibraryWithFiles> filterElementLibraryList(String name,String[] tags,String[] project,String propertyList,String system,Integer status,String[] library,String lodNow);
}
