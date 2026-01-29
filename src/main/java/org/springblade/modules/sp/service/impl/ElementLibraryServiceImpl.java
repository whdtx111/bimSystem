package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.ElementLibrary;
import org.springblade.modules.sp.entity.ElementLibraryMin;
import org.springblade.modules.sp.entity.ElementLibraryWithFiles;
import org.springblade.modules.sp.mapper.ElementLibraryMapper;
import org.springblade.modules.sp.service.ElementLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@DS("postgresql")
public class ElementLibraryServiceImpl extends BaseServiceImpl<ElementLibraryMapper, ElementLibrary> implements ElementLibraryService {

    @Autowired
    private ElementLibraryMapper elementLibraryMapper;

    @Override
    public ElementLibrary getById(String id) {
        try {
            return elementLibraryMapper.getById(id);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public ElementLibraryWithFiles getElementLibraryWithFiles(String id) {
        try {
            return elementLibraryMapper.getElementLibraryWithFiles(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ElementLibraryWithFiles> getElementLibraryWithFilesByProject(String projectId) {
        try {
            return elementLibraryMapper.getElementLibraryWithFilesByProject(projectId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ElementLibrary  getElementLibraryByFileId(String fileId) {
        try {
            return elementLibraryMapper.getElementLibraryByFileId(fileId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ElementLibrary> getAll() {
        try {
            return elementLibraryMapper.getAll();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ElementLibraryMin> getElementLibraryList(String projectId) {
        try {
            return elementLibraryMapper.getElementLibraryList(projectId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ElementLibraryWithFiles> filterElementLibraryList(String name,String[] tags,String[] project,String propertyList,String system,Integer status,String[] library,String lodNow) {
        try {
            return elementLibraryMapper.filterElementLibraryList(name,tags,project,propertyList,system,status,library,lodNow);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addElementLibrary(ElementLibrary elementLibrary) {
        try {
            return elementLibraryMapper.addElementLibrary(elementLibrary);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateElementLibrary(ElementLibrary elementLibrary) {
        try {
            return elementLibraryMapper.updateElementLibrary(elementLibrary);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteElementLibrary(String id) {
        try {
            return elementLibraryMapper.deleteElementLibrary(id);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean insertElementLibraryList(List<ElementLibrary> list) {
        try {
            return elementLibraryMapper.insertElementLibraryList(list);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



}
