package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.StreamAcl;
import org.springblade.modules.sp.mapper.StreamAclMapper;
import org.springblade.modules.sp.service.StreamAclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class StreamAclServiceImpl extends BaseServiceImpl<StreamAclMapper, StreamAcl> implements StreamAclService {

    @Autowired
    private StreamAclMapper streamAclMapper;

    @Override
    public List<StreamAcl> selectStreamAclByStreamId(String streamId) {
        try {
            return streamAclMapper.selectStreamAclByStreamId(streamId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public StreamAcl selectSpRole(String userId,String streamId) {
        try {
            return streamAclMapper.selectSpRole(userId,streamId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> getResourceIdsByUserId(String userId) {
        return baseMapper.getResourceIdsByUserId(userId);
    }

    @Override
    public boolean addStreamAcl(String userId, String resourceId, String role) {
        try {
            StreamAcl streamAcl = new StreamAcl(userId, resourceId, role);
            int result = streamAclMapper.insertStreamAcl(streamAcl);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
