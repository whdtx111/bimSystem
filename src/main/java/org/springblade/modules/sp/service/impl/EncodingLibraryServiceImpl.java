package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.EncodingLibrary;
import org.springblade.modules.sp.mapper.EncodingLibraryMapper;
import org.springblade.modules.sp.service.EncodingLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class EncodingLibraryServiceImpl extends BaseServiceImpl<EncodingLibraryMapper, EncodingLibrary> implements EncodingLibraryService {

    @Autowired
    private EncodingLibraryMapper encodingLibraryMapper;

    @Override
    public EncodingLibrary getById(String id) {
        try {
            return encodingLibraryMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EncodingLibrary> getAll() {
        try {
            return encodingLibraryMapper.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EncodingLibrary> getAllEncodingLibraries(String name, String code, String lv, Integer status) {
        try {
            return encodingLibraryMapper.getAllEncodingLibraries(name, code, lv, status);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page<EncodingLibrary> filterEncodingLibrary(String name, String code, String lv, Integer status,Integer pageSize, Integer currentPage) {
        try {
            PageHelper.startPage(currentPage, pageSize);
            return encodingLibraryMapper.filterEncodingLibrary(name, code, lv, status);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addEncodingLibrary(EncodingLibrary encodingLibrary) {
        try {
            return encodingLibraryMapper.addEncodingLibrary(encodingLibrary);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addEncodingLibraryList(List<EncodingLibrary> encodingLibrary) {
        try {
            return encodingLibraryMapper.addEncodingLibraryList(encodingLibrary);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateEncodingLibrary(EncodingLibrary encodingLibrary) {
        try {
            return encodingLibraryMapper.updateEncodingLibrary(encodingLibrary);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEncodingLibrary(String id) {
        try {
            return encodingLibraryMapper.deleteEncodingLibrary(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
