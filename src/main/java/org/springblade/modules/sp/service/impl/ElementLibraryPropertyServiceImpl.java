package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.ElementLibraryProperty;
import org.springblade.modules.sp.mapper.ElementLibraryPropertyMapper;
import org.springblade.modules.sp.service.ElementLibraryPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class ElementLibraryPropertyServiceImpl extends BaseServiceImpl<ElementLibraryPropertyMapper, ElementLibraryProperty> implements ElementLibraryPropertyService {

    @Autowired
    private ElementLibraryPropertyMapper elementLibraryPropertyMapper;

    @Override
    public ElementLibraryProperty getById(String id) {
        try {
            return elementLibraryPropertyMapper.getById(id);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<ElementLibraryProperty> getAll(String pid) {
        try {
            return elementLibraryPropertyMapper.getAll(pid);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean addElementLibraryProperty(ElementLibraryProperty elementLibraryProperty) {
        try {
            return elementLibraryPropertyMapper.addElementLibraryProperty(elementLibraryProperty);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateElementLibraryProperty(ElementLibraryProperty elementLibraryProperty) {
        try {
            return elementLibraryPropertyMapper.updateElementLibraryProperty(elementLibraryProperty);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteElementLibraryProperty(String id) {
        try {
            return elementLibraryPropertyMapper.deleteElementLibraryProperty(id);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean insertElementLibraryPropertyList(List<ElementLibraryProperty> list) {
        try {
            return elementLibraryPropertyMapper.insertElementLibraryPropertyList(list);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
