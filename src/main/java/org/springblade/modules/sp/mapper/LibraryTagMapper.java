package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.DwgFile;
import org.springblade.modules.sp.entity.LibraryTag;

import java.util.List;
@Mapper
public interface LibraryTagMapper extends BaseMapper<LibraryTag> {

    LibraryTag getById(String id);

    LibraryTag getByName(String tag);

    List<LibraryTag> getAllLibraryTag();

    boolean addLibraryTag(LibraryTag libraryTag);

    boolean updateLibraryTag(LibraryTag libraryTag);

    boolean deleteLibraryTag(String id);

}
