package org.springblade.modules.sp.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.dto.LibraryRevitDTO;
import org.springblade.modules.sp.entity.Library;
import org.springblade.modules.sp.entity.Librarys;
import org.springblade.modules.sp.mapper.LibraryMapper;
import org.springblade.modules.sp.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@DS("postgresql")
public class LibraryServiceImpl extends BaseServiceImpl<LibraryMapper, Library> implements LibraryService {

    @Autowired
    private LibraryMapper libraryMapper;

    @Override
    public Library getById(String id){
        try {
            Library library = libraryMapper.getById(id);
            return library;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Library> getByIds(List<String> ids){
        try {
            List<Library> library = libraryMapper.getByIds(ids);
            return library;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LibraryRevitDTO getRevitByName(String name){
        try {
            LibraryRevitDTO library = libraryMapper.getRevitByName(name);
            return library;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Library getByName(String name){
        try {
            Library library = libraryMapper.getByName(name);
            return library;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean checkLibraryNameExists(String name){
        try {
            if (StringUtils.isEmpty(name)){
                return false;
            }
            Library library = libraryMapper.getByName(name);
            return library != null;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Library> getAllLibrary(){
        try {
            List<Library> allLibrary = libraryMapper.getAllLibrary();
            return allLibrary;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Library> getAllLibraryFilter(String name,String parameters,String tag){
        try {
            List<Library> allLibrary = libraryMapper.getAllLibraryFilter(name,parameters,tag);
            return allLibrary;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Library> getLibraryForExport(String name, String category, String dataType, String group) {
        try {
            // 构建查询参数Map
            Map<String, Object> params = new HashMap<>();
            if (name != null && !name.trim().isEmpty()) {
                params.put("name", name);
            }
            if (category != null && !category.trim().isEmpty()) {
                params.put("category", category);
            }
            if (dataType != null && !dataType.trim().isEmpty()) {
                params.put("dataType", dataType);
            }
            if (group != null && !group.trim().isEmpty()) {
                params.put("parameters", group);
            }
            
            // 使用现有的filterLibrary方法但不分页
            List<Library> libraries = libraryMapper.filterLibrary(params);
            return libraries;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Library> getLibrarysParams(){
        try {
            List<Library> allLibrary = libraryMapper.getLibrarysParams();
            return allLibrary;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Library> getLibrarysByTag(String tag){
        try {
            List<Library> allLibrary = libraryMapper.getLibrarysByTag(tag);
            return allLibrary;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page<Library> filterLibrary(Map<String, Object> params, Integer pageSize, Integer currentPage){
        try {
            PageHelper.startPage(currentPage, pageSize);
            Page<Library> libraries = libraryMapper.filterLibrary(params);
            return libraries;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateLibraryTag(String id, String tag){
        try {
            if (StringUtils.isEmpty(id)){
                return false;
            }
            return libraryMapper.updateLibraryTag(id,tag);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addLibrary(Library library){
        try {
            if (ObjectUtils.isEmpty(library)){
                return false;
            }
            library.setVersion("V1.0");
            return libraryMapper.addLibrary(library);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteLibrary(String id){
        try {
            if (StringUtils.isEmpty(id)){
                return false;
            }
            return libraryMapper.deleteLibrary(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateLibrary(Library library){
        try {
            if (ObjectUtils.isEmpty(library)){
                return false;
            }
            library.setModifyTime(new Date());
            return libraryMapper.updateLibrary(library);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateLibraryByKey(Librarys librarys){
        try {
            if (ObjectUtils.isEmpty(librarys)){
                return false;
            }
            return libraryMapper.updateLibraryByKey(librarys);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional // 添加事务管理
    public boolean insertBatchSomeColumn(List<Library> libraryList) {
        if (libraryList != null && !libraryList.isEmpty()) {
            // 使用 MyBatis 批量插入数据
            boolean b = libraryMapper.insertBatchSomeColumn(libraryList);
            return b;
        }
        return false;
    }

    @Override
    public boolean deleteLibrariesByIds(List<String> libIds) {
        if (libIds != null && !libIds.isEmpty()) {
            // 使用 MyBatis 批量删除数据
            boolean b = libraryMapper.deleteLibrariesByIds(libIds);
            return b;
        }
        return false;
    }

}
