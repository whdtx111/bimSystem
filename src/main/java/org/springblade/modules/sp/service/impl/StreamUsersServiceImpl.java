package org.springblade.modules.sp.service.impl;





import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.StreamUsers;
import org.springblade.modules.sp.mapper.StreamUsersMapper;
import org.springblade.modules.sp.service.StreamUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@DS("postgresql")
@Slf4j
public class StreamUsersServiceImpl extends BaseServiceImpl<StreamUsersMapper, StreamUsers> implements StreamUsersService {

    @Autowired
    private StreamUsersMapper streamUsersMapper;

    @Override
    public StreamUsers getByStreamId(String streamId) {
        try {
            return streamUsersMapper.getByStreamId(streamId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public StreamUsers getByUserId(String userId) {
        try {
            return streamUsersMapper.getByUserId(userId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addstreamUsers(StreamUsers streamUsers) {
        try {
            return streamUsersMapper.addstreamUsers(streamUsers);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStreamUsers(StreamUsers streamUsers) {
        try {
            return streamUsersMapper.updateStreamUsers(streamUsers);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteStreamUsers(String id) {
        try {
            return streamUsersMapper.deleteStreamUsers(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
