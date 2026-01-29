package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.BomFile;
import org.springblade.modules.sp.mapper.BomFileMapper;
import org.springblade.modules.sp.service.BomFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@DS("postgresql")
public class BomFileServiceImpl extends BaseServiceImpl<BomFileMapper, BomFile> implements BomFileService {

    @Autowired
    private BomFileMapper bomFileMapper;

    @Override
    public BomFile getById(String id) {
        try {
            return bomFileMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BomFile getByType(String type) {
        try {
            return bomFileMapper.getByType(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BomFile> getLatestTemplateFile() {
        try {
            return bomFileMapper.getLatestTemplateFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addBomFile(BomFile bomFile) {
        try {
            bomFile.setModifyTime(new Date());
            return bomFileMapper.addBomFile(bomFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteBomFile(String id) {
        try {
            return bomFileMapper.deleteBomFile(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
