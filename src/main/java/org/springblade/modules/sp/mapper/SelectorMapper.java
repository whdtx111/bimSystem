package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.Selector;

import java.util.List;
@Mapper
public interface SelectorMapper extends BaseMapper<Selector> {

    Selector getById(String id);

    List<Selector> searchFilter(String[] commitIds, String streamId);

    boolean addSelector(Selector selector);

    boolean updateSelector(Selector selector);

    boolean updateName(String id, String name);

    boolean deleteSelectorById(String id);
}
