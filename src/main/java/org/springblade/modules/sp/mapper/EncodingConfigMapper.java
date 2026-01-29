package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.EncodingConfig;

import java.util.List;
@Mapper
public interface EncodingConfigMapper extends BaseMapper<EncodingConfig> {

    EncodingConfig getById(@Param("id") String id);

    List<EncodingConfig> searchFilter(@Param("encodingParametersId") String encodingParametersId, @Param("lv") String lv, @Param("lvCode") String lvCode);

    boolean addEncodingConfig(EncodingConfig encodingConfig);

    boolean updateEncodingConfig(EncodingConfig encodingConfig);

    boolean deleteEncodingConfigById(@Param("id") String id);

}
