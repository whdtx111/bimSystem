package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springblade.modules.sp.entity.Stream;

import java.util.List;
@Mapper
public interface StreamMapper extends BaseMapper<Stream> {

    @Select("SELECT id,\"name\" FROM streams ")
    List<Stream> getAllStreams();

    @Select("SELECT \"name\" FROM streams WHERE id=#{id}")
    String getStreamNameById(String id);

}
