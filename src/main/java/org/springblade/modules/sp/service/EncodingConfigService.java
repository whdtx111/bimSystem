package org.springblade.modules.sp.service;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.EncodingConfig;

import java.util.List;


public interface EncodingConfigService extends BaseService<EncodingConfig> {

    EncodingConfig getById(@Param("id") String id);

    List<EncodingConfig> searchFilter(@Param("encodingParametersId") String encodingParametersId, @Param("lv") String lv, @Param("lvCode") String lvCode);

    boolean addEncodingConfig(EncodingConfig encodingConfig);

    boolean updateEncodingConfig(EncodingConfig encodingConfig);

    boolean deleteEncodingConfigById(@Param("id") String id);
}
