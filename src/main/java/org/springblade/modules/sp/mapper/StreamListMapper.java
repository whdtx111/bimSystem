package org.springblade.modules.sp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.StreamFiles;
import org.springblade.modules.sp.entity.StreamList;

import java.util.List;

@Mapper
public interface StreamListMapper extends BaseMapper<StreamList> {

    List<StreamList> getStreamList();

    StreamList getStreamListById(String id);

    boolean addStreamList(StreamList streamList);

    boolean updateStreamList(StreamList streamList);

    boolean deleteStreamListById(String id);


}
