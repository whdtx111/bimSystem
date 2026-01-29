package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.ElementWbs;
import org.springblade.modules.sp.mapper.ElementWbsMapper;
import org.springblade.modules.sp.service.ElementWbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * ElementWbs接口实现类
 * @author dengtx
 */
@Service
@DS("postgresql")
public class ElementWbsServiceImpl extends BaseServiceImpl<ElementWbsMapper, ElementWbs> implements ElementWbsService {

    @Autowired
    private ElementWbsMapper elementWbsMapper;



    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public ElementWbs getById(String id){
        try {
            ElementWbs elementWbs = elementWbsMapper.getById(id);
            return elementWbs;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 新增
     * @param
     * @return
     */
    @Override
    public boolean linkElementWbs(ElementWbs elementWbs){
        try {
            if (ObjectUtils.isEmpty(elementWbs)){
                return false;
            }
            return elementWbsMapper.linkElementWbs(elementWbs);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 删除
     * @param id
     * @return
     */
//    @Override
//    public boolean unlinkElementWbs(String id, String[] elementId){
//        try {
//            if (StringUtils.isEmpty(id)){
//                return false;
//            }
//            if ( Arrays.toString((elementId)).equals("[[]]")){
//                elementId = new String[0];
//            }
//            for (String s : elementId) {
//
//            }
//            return elementWbsMapper.unlinkElementWbs(id);
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }

    @Override
    public boolean updateElementWbs(String id, String[] elementId){
        try {
            if (StringUtils.isEmpty(id)){
                return false;
            }
            if ( Arrays.toString((elementId)).equals("[[]]")){
                elementId = new String[0];
            }
            return elementWbsMapper.updateElementWbs(id,elementId);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ElementWbs getElementWbs(String wbsCode,String branchId,String tempId){
        try {
            ElementWbs elementWbs = elementWbsMapper.getElementWbs(wbsCode,branchId,tempId);
            return elementWbs;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ElementWbs> getAllElementWbs(String branchId,String tempId){
        try {
            List<ElementWbs> allElementWbs = elementWbsMapper.getAllElementWbs(branchId, tempId);
            return allElementWbs;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
