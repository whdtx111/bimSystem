package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.dto.LibraryRevitDTO;
import org.springblade.modules.sp.entity.Library;
import org.springblade.modules.sp.entity.Librarys;

import java.util.List;
import java.util.Map;

public interface LibraryMapper extends BaseMapper<Library> {

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
     * 获取列表
     * @return
     */
    List<Library> getAllLibrary();

    List<Library> getAllLibraryFilter(String name,String parameters,String tag);

    List<Library> getLibrarysParams();

    List<Library> getLibrarysByTag(String tag);

    // 动态筛选 Library
    Page<Library> filterLibrary(Map<String, Object> params);

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

    boolean insertBatchSomeColumn(@Param("list") List<Library> librarys);

    boolean deleteLibrariesByIds( List<String> libIds);

//    @Insert("INSERT INTO sp_lib (name, units, version,category,parameter_type,data_type,parameters,detail,modify_user) " +
//            "VALUES (#{name}, #{units}, #{version},#{category},#{parameter_type},#{data_type},#{parameters},#{detail},#{modify_user})")
//    int insert(Library library);
}
