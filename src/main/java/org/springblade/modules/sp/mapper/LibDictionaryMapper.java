package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.LibDictionary;
import java.util.List;

@Mapper
public interface LibDictionaryMapper extends BaseMapper<LibDictionary> {

    List<LibDictionary> getLibDictionaryList();

    LibDictionary getLibDictionaryById(String id);

    boolean addLibDictionary(LibDictionary libDictionary);

    boolean updateLibDictionary(LibDictionary libDictionary);

    boolean updateLibDictionaryStatus(String id,Integer status);

    boolean deleteLibDictionary(String id);

}