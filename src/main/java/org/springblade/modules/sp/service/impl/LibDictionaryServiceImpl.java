package org.springblade.modules.sp.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.LibDictionary;
import org.springblade.modules.sp.mapper.LibDictionaryMapper;
import org.springblade.modules.sp.service.LibDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class LibDictionaryServiceImpl extends BaseServiceImpl<LibDictionaryMapper, LibDictionary> implements LibDictionaryService {

    @Autowired
    private LibDictionaryMapper libDictionaryMapper;

    @Override
    public LibDictionary getLibDictionaryById(String id) {
        try {
            return libDictionaryMapper.getLibDictionaryById(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LibDictionary> getLibDictionaryList() {
        try {
            return libDictionaryMapper.getLibDictionaryList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addLibDictionary(LibDictionary libDictionary) {
        try {
            libDictionaryMapper.addLibDictionary(libDictionary);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateLibDictionary(LibDictionary libDictionary) {
        try {
            libDictionaryMapper.updateLibDictionary(libDictionary);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateLibDictionaryStatus(String id, Integer status) {
        try {
            libDictionaryMapper.updateLibDictionaryStatus(id, status);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteLibDictionary(String id) {
        try {
            libDictionaryMapper.deleteLibDictionary(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
