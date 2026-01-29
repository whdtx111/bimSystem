package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.EncodingParameters;
import org.springblade.modules.sp.mapper.EncodingParametersMapper;
import org.springblade.modules.sp.service.EncodingParametersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.terracotta.modules.ehcache.async.DefaultAsyncConfig.BATCH_SIZE;

@Service
@DS("postgresql")
public class EncodingParametersServiceImpl extends BaseServiceImpl<EncodingParametersMapper, EncodingParameters> implements EncodingParametersService {

    @Autowired
    private EncodingParametersMapper encodingParametersMapper;

    @Override
    public EncodingParameters getById(String id) {
        try {
            return encodingParametersMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public EncodingParameters getSheetByParametersId(String encodingParametersId) {
        try {
            return encodingParametersMapper.getSheetByParametersId(encodingParametersId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public EncodingParameters getEncodingParametersByCode(String encodingId, String code) {
        try {
            return encodingParametersMapper.getEncodingParametersByCode(encodingId, code);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EncodingParameters> getAllEncodingParameters() {
        try {
            return encodingParametersMapper.getAllEncodingParameters();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EncodingParameters> getAllSubRecordsByLv0Id(String id) {
        try {
            return encodingParametersMapper.getAllSubRecordsByLv0Id(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EncodingParameters> getAllEncodingParametersByEncodingId(String encodingId) {
        try {
            return encodingParametersMapper.getAllEncodingParametersByEncodingId(encodingId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    @Override
//    public Page<EncodingParameters> filterEncodingParameters(String name, String code, String encodingId, String lv, String encodingStatus, Integer pageSize, Integer currentPage) {
//        try {
//            PageHelper.startPage(currentPage, pageSize);
//            return encodingParametersMapper.filterEncodingParameters(name, code, encodingId, lv, encodingStatus);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @Override
    public boolean deleteEncodingParameters(String id) {
        try {
            return encodingParametersMapper.deleteEncodingParameters(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateEncodingParameters(EncodingParameters encodingParameters) {
        try {
            return encodingParametersMapper.updateEncodingParameters(encodingParameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateEncodingStatus(String encodingId) {
        try {
            return encodingParametersMapper.updateEncodingStatus(encodingId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addEncodingParametersList(List<EncodingParameters> encodingParametersList) {
        try {
            boolean res = false;
            if (encodingParametersList == null || encodingParametersList.isEmpty()) {
                return res;
            }

            int totalSize = encodingParametersList.size();
            for (int i = 0; i < totalSize; i += BATCH_SIZE) {
                int endIndex = Math.min(i + BATCH_SIZE, totalSize);
                List<EncodingParameters> batchList = encodingParametersList.subList(i, endIndex);
                this.encodingParametersMapper.addEncodingParametersList(batchList); // 调用分批插入的 MyBatis 方法
                res = true;
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addEncodingParameters(EncodingParameters encodingParameters) {
        try {
            return encodingParametersMapper.addEncodingParameters(encodingParameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEncodingParametersByEncodingId(String encodingId) {
        try {
            return encodingParametersMapper.deleteEncodingParametersByEncodingId(encodingId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
