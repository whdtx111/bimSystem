package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.EncodingLibrary;

import java.util.List;
@Mapper
public interface EncodingLibraryMapper extends BaseMapper<EncodingLibrary> {

        EncodingLibrary getById(String id);

        List<EncodingLibrary> getAll();

        List<EncodingLibrary> getAllEncodingLibraries(String name, String code, String lv, Integer status);

        Page<EncodingLibrary> filterEncodingLibrary(String name, String code, String lv, Integer status);

        boolean addEncodingLibrary(EncodingLibrary encodingLibrary);

        boolean addEncodingLibraryList(List<EncodingLibrary> encodingLibrary);

        boolean updateEncodingLibrary(EncodingLibrary encodingLibrary);

        boolean deleteEncodingLibrary(String id);
}
