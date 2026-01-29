package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.LibWbsTemp;
import org.springblade.modules.sp.mapper.LibWbsTempMapper;
import org.springblade.modules.sp.service.LibWbsTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class LibWbsTempServiceImpl extends BaseServiceImpl<LibWbsTempMapper, LibWbsTemp> implements LibWbsTempService {

    @Autowired
    private LibWbsTempMapper libWbsTempMapper;

    @Override
    public LibWbsTemp getById(String id) {
        try {
            return libWbsTempMapper.getById(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LibWbsTemp getLibWbsTemp(String wbsCode, String tempId) {
        return libWbsTempMapper.getLibWbsTemp(wbsCode, tempId);
    }

    @Override
    public List<LibWbsTemp> getLibWbsTempByTempId(String tempId) {
        return libWbsTempMapper.getLibWbsTempByTempId(tempId);
    }

    @Override
    public List<String> getWbsCodeByTempId(String tempId) {
        return libWbsTempMapper.getWbsCodeByTempId(tempId);
    }

    @Override
    public boolean addLibWbsTemp(LibWbsTemp libWbsTemp) {
        return libWbsTempMapper.addLibWbsTemp(libWbsTemp);
    }

    @Override
    public boolean updateLibWbsTemp(LibWbsTemp libWbsTemp) {
        return libWbsTempMapper.updateLibWbsTemp(libWbsTemp);
    }

    @Override
    public boolean deleteLibWbsTemp(String id) {
        return libWbsTempMapper.deleteLibWbsTemp(id);
    }
}
