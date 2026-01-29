package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.EncodingConfig;
import org.springblade.modules.sp.mapper.EncodingConfigMapper;
import org.springblade.modules.sp.service.EncodingConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@DS("postgresql")
public class EncodingConfigServiceImpl  extends BaseServiceImpl<EncodingConfigMapper, EncodingConfig> implements EncodingConfigService {

    @Autowired
    private EncodingConfigMapper encodingConfigMapper;

    @Override
    public EncodingConfig getById(String id) {
        return encodingConfigMapper.getById(id);
    }

    @Override
    public List<EncodingConfig> searchFilter(String encodingParametersId, String lv, String lvCode) {
        return encodingConfigMapper.searchFilter(encodingParametersId, lv, lvCode);
    }

    @Override
    public boolean addEncodingConfig(EncodingConfig encodingConfig) {
        return encodingConfigMapper.addEncodingConfig(encodingConfig);
    }

    @Override
    public boolean updateEncodingConfig(EncodingConfig encodingConfig) {
        return encodingConfigMapper.updateEncodingConfig(encodingConfig);
    }

    @Override
    public boolean deleteEncodingConfigById(String id) {
        return encodingConfigMapper.deleteEncodingConfigById(id);
    }
}
