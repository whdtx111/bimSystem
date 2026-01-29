package org.springblade.modules.sp.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.modules.sp.entity.DwgFile;
import org.springblade.modules.sp.mapper.DwgFileMapper;
import org.springblade.modules.sp.service.DwgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * DWG接口实现类
 * @author dengtx
 */
@Service
@DS("postgresql")
public class DwgServiceImpl extends BaseServiceImpl<DwgFileMapper, DwgFile> implements DwgService {


    @Autowired
    private DwgFileMapper dwgFileMapper;

    @Override
    public DwgFile getById(String id) {
        try {
            DwgFile dwgFile = dwgFileMapper.getById(id);
            return dwgFile;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DwgFile getByMapId(String mapId,String projectId) {
        try {
           DwgFile dwgFile = dwgFileMapper.getByMapId(mapId,projectId);
           return dwgFile;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<DwgFile> getAllDwg(String projectId) {
       try {
           List<DwgFile> dwgFiles = dwgFileMapper.getAllDwg(projectId);
           return dwgFiles;
       }catch (Exception e){
           e.printStackTrace();
           return null;
       }
    }

    @Override
    public boolean addDwgFile(DwgFile dwgFile) {
        try {
            if (ObjectUtils.isEmpty(dwgFile)){
                return false;
            }
            return dwgFileMapper.addDwgFile(dwgFile);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteDwgFile(String mapId,String projectId) {
        try {
            if (StringUtils.isEmpty(mapId)){
                return false;
            }
            return dwgFileMapper.deleteDwgFile(mapId,projectId);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateDwgFile(DwgFile dwgFile) {
        try {
            if (ObjectUtils.isEmpty(dwgFile)){
                return false;
            }
            dwgFile.setModifyTime(new DateTime());
            return dwgFileMapper.updateDwgFile(dwgFile);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 文件关联模型
     * @param id
     * @param objectId
     * @return
     */
    @Override
    public boolean linkObjects(String id,String[] objectId){
        try {
            if (StringUtils.isEmpty(id)){
                return false;
            }
            if (objectId.toString().equals("[]")){
                objectId = new String[0];
            }
            return dwgFileMapper.linkObjects(id,objectId);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
