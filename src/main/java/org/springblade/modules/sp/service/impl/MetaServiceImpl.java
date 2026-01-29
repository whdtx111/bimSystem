package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.MetaData;
import org.springblade.modules.sp.mapper.MetaDataMapper;
import org.springblade.modules.sp.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@DS("postgresql")
public class MetaServiceImpl extends BaseServiceImpl<MetaDataMapper, MetaData> implements MetaService {

    @Autowired
    private MetaDataMapper metaDataMapper;

    @Override
    public MetaData getById(String id){
        try {
            MetaData metaData = metaDataMapper.getById(id);
            return metaData;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MetaData> getAllMetaData(){
        try {
            List<MetaData> allMetaData = metaDataMapper.getAllMetaData();
            return allMetaData;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addMetaData(MetaData metaData){
        try {
            if (ObjectUtils.isEmpty(metaData)){
                return false;
            }
            return metaDataMapper.addMetaData(metaData);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMetaData(String id){
        try {
            if (StringUtils.isEmpty(id)){
                return false;
            }
            return metaDataMapper.deleteMetaData(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateMetaData(MetaData metaData){
        try {
            if (ObjectUtils.isEmpty(metaData)){
                return false;
            }
            metaData.setModifyTime(new Date());
            return metaDataMapper.updateMetaData(metaData);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
