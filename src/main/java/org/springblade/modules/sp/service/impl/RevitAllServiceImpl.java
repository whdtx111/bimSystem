package org.springblade.modules.sp.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.RevitAllUPDTO;
import org.springblade.modules.sp.entity.RevitAll;
import org.springblade.modules.sp.mapper.RevitAllMapper;
import org.springblade.modules.sp.service.RevitAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@DS("postgresql")
public class RevitAllServiceImpl extends BaseServiceImpl<RevitAllMapper, RevitAll> implements RevitAllService {

    @Autowired
    private RevitAllMapper revitAllMapper;

    @Override
    public RevitAll getById(String id) {
        try {
            RevitAll revitAll = revitAllMapper.getById(id);
            return revitAll;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RevitAll getRevitAllByCommitId(String streamId,String elementId, String commitId) {
        try {
            RevitAll revitAll = revitAllMapper.getRevitAllByCommitId(streamId,elementId, commitId);
            return revitAll;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RevitAll> searchFilter(String elementId, String streamId, String branchId,Integer isfinished) {
        try {
            if (StringUtils.isEmpty(elementId)) {
                elementId = null;
            }
            if (StringUtils.isEmpty(streamId)) {
                streamId = null;
            }
            if (StringUtils.isEmpty(branchId)) {
                branchId = null;
            }
            if (isfinished == null) {
                isfinished = 0;
            }
            List<RevitAll> revitAll = revitAllMapper.searchFilter(elementId, streamId, branchId,isfinished);
            return revitAll;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addRevitAll(RevitAll param) {
        try {
            RevitAll revitAll = new RevitAll();
            revitAll.setId(UUID.randomUUID().toString());
            revitAll.setObjId(param.getObjId());
            revitAll.setElementId(param.getElementId());
            revitAll.setStreamId(param.getStreamId());
            revitAll.setBranchId(param.getBranchId());
            revitAll.setCommitId(param.getCommitId());
            revitAll.setCategory(param.getCategory());
            revitAll.setType(param.getType());
            revitAll.setDetail(param.getDetail());
            revitAll.setIsfinished(param.getIsfinished());
            revitAll.setModifyTime(new DateTime());
            revitAll.setModifyUser(param.getModifyUser());
            revitAll.setStatus(0);
            return revitAllMapper.addRevitAll(revitAll);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRevitAll(RevitAll revitAll) {
        try {
            return revitAllMapper.updateRevitAll(revitAll);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateIsFinished(RevitAllUPDTO revitAllUPDTO) {
        try {
            return revitAllMapper.updateIsFinished(revitAllUPDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRevitAllById(String id) {
        try {
            return revitAllMapper.deleteRevitAllById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
