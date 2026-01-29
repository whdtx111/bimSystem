package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.Ebs;
import org.springblade.modules.sp.mapper.EbsMapper;
import org.springblade.modules.sp.service.EbsService;
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
public class EbsServiceImpl extends BaseServiceImpl<EbsMapper, Ebs> implements EbsService {

    @Autowired
    private EbsMapper ebsMapper;



    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Ebs getById(String id){
        try {
          Ebs ebs = ebsMapper.getById(id);
          return ebs;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Ebs> getAllEbs(){
        List<Ebs> allEbs = ebsMapper.getAllEbs();
        return allEbs;
    }


    @Override
    public Page<Ebs> filterEbs(String ebsCode, String ebsName, String type, String source, String auth, Integer ebsStatus, Integer pageSize, Integer currentPage){
        PageHelper.startPage(currentPage, pageSize);
        Page<Ebs> filterEbs = ebsMapper.filterEbs(ebsCode,ebsName,type,source,auth,ebsStatus);
        return filterEbs;
    }

    /**
     * 新增EBS
     * @param ebs
     * @return
     */
    @Override
    public boolean addEbs(Ebs ebs){
        try {
            if (ObjectUtils.isEmpty(ebs)){
                return false;
            }
            return ebsMapper.addEbs(ebs);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 删除EBS
     * @param id
     * @return
     */
    @Override
    public boolean deleteEbs(String id){
        try {
            if (StringUtils.isEmpty(id)){
                return false;
            }
            return ebsMapper.deleteEbs(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 修改EBS
     * @param ebs
     * @return
     */
    @Override
    public boolean updateEbs(Ebs ebs){
        try {
            if (ObjectUtils.isEmpty(ebs)){
                return false;
            }
            ebs.setModifyTime(new Date());
            return ebsMapper.updateEbs(ebs);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateEbsStatus(String id){
        try {
            if (StringUtils.isEmpty(id)){
                return false;
            }
            return ebsMapper.updateEbsStatus(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
