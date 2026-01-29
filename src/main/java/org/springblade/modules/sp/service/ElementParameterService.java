package org.springblade.modules.sp.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springblade.modules.sp.entity.ElementParameter;
import org.springblade.modules.sp.mapper.ElementParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
@Slf4j
public class ElementParameterService {
    @Autowired
    private ElementParameterMapper elementParameterMapper;

    public ElementParameter getById(String id) {
        return elementParameterMapper.getById(id);
    }

    public List<ElementParameter> getByIds(List<String> ids) {
        try {
            return elementParameterMapper.getByIds(ids);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int insert(ElementParameter elementParameter) {
        return elementParameterMapper.addElementParameter(elementParameter.getId(), elementParameter.getPkey(), elementParameter.getName(), elementParameter.getUnits(), elementParameter.getCategory());
    }

    public int updateById(ElementParameter elementParameter) {
        return elementParameterMapper.updateElementParameter(elementParameter.getId(), elementParameter.getPkey(), elementParameter.getName(), elementParameter.getUnits(), elementParameter.getCategory());
    }

    public int deleteById(String id) {
        return elementParameterMapper.deleteElementParameter(id);
    }
}
