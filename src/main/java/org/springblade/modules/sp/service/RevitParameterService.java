package org.springblade.modules.sp.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springblade.modules.sp.entity.RevitParameter;
import org.springblade.modules.sp.mapper.RevitParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
@Slf4j
public class RevitParameterService {

    @Autowired
    private RevitParameterMapper revitParameterMapper;

    public boolean addRevitParameter(RevitParameter revitParameter) {
        return revitParameterMapper.addRevitParameter(revitParameter);
    }

    public boolean updateRevitParameter(RevitParameter revitParameter) {
        return revitParameterMapper.updateById(revitParameter) > 0;
    }

    public boolean deleteRevitParameter(String guid) {
        return revitParameterMapper.deleteById(guid) > 0;
    }

    public List<RevitParameter> getAllRevitParameter() {
        return revitParameterMapper.selectList(null);
    }

    public RevitParameter getById(String guid) {
        return revitParameterMapper.selectById(guid);
    }

}
