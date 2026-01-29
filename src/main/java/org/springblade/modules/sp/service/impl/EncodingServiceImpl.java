package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.Encoding;
import org.springblade.modules.sp.mapper.EncodingMapper;
import org.springblade.modules.sp.service.EncodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class EncodingServiceImpl extends BaseServiceImpl<EncodingMapper, Encoding> implements EncodingService {

    @Autowired
    private EncodingMapper encodingMapper;

    @Override
    public Encoding getById(String id) {
        try {
            return encodingMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Encoding> getAllEncoding() {
        try {
            return encodingMapper.getAllEncoding();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page<Encoding> filterEncoding(String name, String version, String auth, String user, String source, String isfinished, Integer pageSize, Integer currentPage) {
        try {
            PageHelper.startPage(currentPage, pageSize);
            return encodingMapper.filterEncoding(name, version, auth, user, source,isfinished);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addEncoding(Encoding encoding) {
        try {
            return encodingMapper.addEncoding(encoding);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEncoding(String id) {
        try {
            return encodingMapper.deleteEncoding(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateEncoding(Encoding encoding) {
        try {
            return encodingMapper.updateEncoding(encoding);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
