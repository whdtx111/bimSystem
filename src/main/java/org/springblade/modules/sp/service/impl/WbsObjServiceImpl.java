package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.WbsObj;
import org.springblade.modules.sp.mapper.WbsObjMapper;
import org.springblade.modules.sp.service.WbsObjService;
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
public class WbsObjServiceImpl extends BaseServiceImpl<WbsObjMapper, WbsObj> implements WbsObjService {

    @Autowired
    private WbsObjMapper wbsObjMapper;



    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public WbsObj getById(String id){
        try {
            WbsObj wbsObj = wbsObjMapper.getById(id);
            return wbsObj;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public WbsObj getLink(String wbsId,String objectId){
        try {
            WbsObj wbsObj = wbsObjMapper.getLink(wbsId,objectId);
            return wbsObj;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<WbsObj> getAllWbsObj(){
        List<WbsObj> allWbs = wbsObjMapper.getAllWbsObj();
        return allWbs;
    }
    /**
     * 新增EBS
     * @param
     * @return
     */
    @Override
    public boolean addWbsObj(WbsObj wbsObj){
        try {
            if (ObjectUtils.isEmpty(wbsObj)){
                return false;
            }
            return wbsObjMapper.addWbsObj(wbsObj);
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
    public boolean deleteWbsObj(String id,String modifyUser){
        try {
            if (StringUtils.isEmpty(id)){
                return false;
            }
            return wbsObjMapper.deleteWbsObj(id,modifyUser);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 修改EBS
     * @param
     * @return
     */
    @Override
    public boolean updateWbsObj(WbsObj wbsObj){
        try {
            if (ObjectUtils.isEmpty(wbsObj)){
                return false;
            }
            wbsObj.setModifyTime(new Date());
            return wbsObjMapper.updateWbsObj(wbsObj);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
