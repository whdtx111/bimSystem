package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.Encoding;

import java.util.List;
@Mapper
public interface EncodingMapper extends BaseMapper<Encoding> {

    Encoding getById(String id);

    List<Encoding> getAllEncoding();

    Page<Encoding> filterEncoding(@Param("name") String name,
                                  @Param("version") String version,
                                  @Param("auth") String auth,
                                  @Param("user") String user,
                                  @Param("source") String source,
                                  @Param("isfinished") String isfinished);

    boolean addEncoding(Encoding encoding);

    boolean deleteEncoding(String id);

    boolean updateEncoding(Encoding encoding);


}
