package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.ElementLibraryFile;
import org.springblade.modules.sp.mapper.ElementLibraryFileMapper;
import org.springblade.modules.sp.service.ElementLibraryFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@DS("postgresql")
public class ElementLibraryFileServiceImpl extends BaseServiceImpl<ElementLibraryFileMapper, ElementLibraryFile> implements ElementLibraryFileService {

    @Autowired
    private ElementLibraryFileMapper elementLibraryFileMapper;


    @Override
    public ElementLibraryFile getById(String id) {
        try {
            return elementLibraryFileMapper.getById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ElementLibraryFile> getAll(String pid) {
        try {
            return elementLibraryFileMapper.getAll(pid);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addElementLibraryFile(ElementLibraryFile elementLibraryFile) {
        try {
            return elementLibraryFileMapper.addElementLibraryFile(elementLibraryFile);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateElementLibraryFile(ElementLibraryFile elementLibraryFile) {
        try {
            return elementLibraryFileMapper.updateElementLibraryFile(elementLibraryFile);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteElementLibraryFile(String id) {
        try {
            return elementLibraryFileMapper.deleteElementLibraryFile(id);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
