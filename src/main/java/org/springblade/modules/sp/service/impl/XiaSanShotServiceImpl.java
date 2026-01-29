package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.XiaSanShot;
import org.springblade.modules.sp.mapper.XiaSanShotMapper;
import org.springblade.modules.sp.service.XiaSanShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class XiaSanShotServiceImpl extends BaseServiceImpl<XiaSanShotMapper, XiaSanShot> implements XiaSanShotService {

    @Autowired
    private XiaSanShotMapper xiaSanShotMapper;

    @Override
    public XiaSanShot getById(String id) {
        try {
            return xiaSanShotMapper.getById(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public XiaSanShot getByElementId(String streamId, String elementId) {
        try {
            return xiaSanShotMapper.getByElementId(streamId,elementId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> getcheckStatus(String streamId, String commitId) {
        try {
            return xiaSanShotMapper.getcheckStatus(streamId,commitId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public int countByCommitId(String commitId, String streamId,String checkStatus) {
        try {
            return xiaSanShotMapper.countByCommitId(commitId,streamId,checkStatus);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<XiaSanShot> searchFilter(String checkStatus, String streamId, String commitId, String elementId, String objectId) {
        try {
            return xiaSanShotMapper.searchFilter(checkStatus,streamId,commitId,elementId,objectId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addXiaSanShot(XiaSanShot xiaSanShot) {
        try {
            return xiaSanShotMapper.addXiaSanShot(xiaSanShot);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean deleteXiaSanShotById(String id) {
        try {
            return xiaSanShotMapper.deleteXiaSanShotById(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateXiaSanShot(XiaSanShot xiaSanShot) {
        try {
            return xiaSanShotMapper.updateXiaSanShot(xiaSanShot);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
