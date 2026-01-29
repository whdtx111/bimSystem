package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.XiaSanPic;
import org.springblade.modules.sp.mapper.XiaSanPicMapper;
import org.springblade.modules.sp.service.XiaSanPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class XiaSanPicServiceImpl extends BaseServiceImpl<XiaSanPicMapper, XiaSanPic> implements XiaSanPicService {

    @Autowired
    private XiaSanPicMapper xiaSanPicMapper;


    @Override
    public List<XiaSanPic> searchFilter(String streamId,String commitId,String objectId,String elementId,String checkStatus) {
        try {
            return xiaSanPicMapper.searchFilter(streamId,commitId,objectId,elementId,checkStatus);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public XiaSanPic getByName(String name, String streamId, String elementId) {
        try {
            return xiaSanPicMapper.getByName(name,streamId,elementId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public XiaSanPic getById(String id) {
        try {
            return xiaSanPicMapper.getById(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addXiaSanPic(XiaSanPic xiaSanPic) {
        try {
            return xiaSanPicMapper.addXiaSanPic(xiaSanPic);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteXiaSanPicById(String id) {
        try {
            return xiaSanPicMapper.deleteXiaSanPicById(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateXiaSanPic(XiaSanPic xiaSanPic) {
        try {
            return xiaSanPicMapper.updateXiaSanPic(xiaSanPic);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
