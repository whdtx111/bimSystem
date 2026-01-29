package org.springblade.modules.sp.service;


import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.dto.LibraryRevitDTO;
import org.springblade.modules.sp.entity.Library;
import org.springblade.modules.sp.entity.Librarys;


import java.util.List;
import java.util.Map;

public interface LibraryService extends BaseService<Library> {
    /**
     * 根据id查询
     * @param id
     * @return
     */
    Library getById(String id);

    List<Library> getByIds(List<String> ids);

    LibraryRevitDTO getRevitByName(String name);

    /**
     * 根据name查询（用于校验是否存在）
     * @param name
     * @return
     */
    Library getByName(String name);

    /**
     * 检查属性名是否已存在
     * @param name
     * @return true-已存在，false-不存在
     */
    boolean checkLibraryNameExists(String name);
    /**
     * 获取列表
     * @return
     */
    List<Library> getAllLibrary();

    List<Library> getAllLibraryFilter(String name,String parameters,String tag);

    // 根据name/category/dataType/group参数筛选Library列表（用于导出）
    List<Library> getLibraryForExport(String name, String category, String dataType, String group);

    List<Library> getLibrarysParams();


    List<Library> getLibrarysByTag(String tag);

    // 根据筛选条件获取 Library 列表
    Page<Library> filterLibrary(Map<String, Object> params,Integer pageSize,Integer currentPage);

    boolean updateLibraryTag(String id, String tag);

    /**
     * 新增
     * @param library
     * @return
     */
    boolean addLibrary(Library library);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean deleteLibrary(String id);

    /**
     * 修改
     * @param library
     * @return
     */
    boolean updateLibrary(Library library);

    boolean updateLibraryByKey(Librarys librarys);

    boolean deleteLibrariesByIds(List<String> libIds);

    boolean insertBatchSomeColumn(List<Library> librarys);
}
