package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;

import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.EbsParameters;
import org.springblade.modules.sp.mapper.EbsParametersMapper;
import org.springblade.modules.sp.service.EbsParametersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * EBS接口实现类
 * @author dengtx
 */
@Service
@DS("postgresql")
public class EbsParametersServiceImpl extends BaseServiceImpl<EbsParametersMapper, EbsParameters> implements EbsParametersService {

    @Autowired
    private EbsParametersMapper ebsParametersMapper;

    @Override
    public EbsParameters getById(String id) {
        return ebsParametersMapper.getById(id);
    }

    @Override
    public List<EbsParameters> getAllEbsParameters() {
        return ebsParametersMapper.getAllEbsParameters();
    }

    @Override
    public List<EbsParameters> getAllEbsParametersByEbsId(String ebsId) {
        return ebsParametersMapper.getAllEbsParametersByEbsId(ebsId);
    }

    @Override
    public List<EbsParameters> reorderEbsParameters(String ebsId) {
        return ebsParametersMapper.reorderEbsParameters(ebsId);
    }

    @Override
    public boolean addEbsParameters(EbsParameters ebsParameters) {
        return ebsParametersMapper.addEbsParameters(ebsParameters);
    }

    @Override
    public boolean insertEbsParametersList(List<EbsParameters> list) {
        return ebsParametersMapper.insertEbsParametersList(list);
    }
    @Override
    public boolean updateEbsParameters(EbsParameters ebsParameters) {
        return ebsParametersMapper.updateEbsParameters(ebsParameters);
    }

    @Override
    public boolean updateEbsStatus(String ebsId) {
        return ebsParametersMapper.updateEbsStatus(ebsId);
    }

    @Override
    public boolean deleteEbsParameters(String id) {
        try {
            return  ebsParametersMapper.deleteEbsParameters(id);
        }catch (Exception e){
            return false;
        }
    }

}
