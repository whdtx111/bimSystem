package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.SLRole;
import org.springblade.modules.sp.entity.StreamUsers;
@Mapper
public interface StreamUsersMapper extends BaseMapper<StreamUsers> {

    StreamUsers getByStreamId(String streamId);

    StreamUsers getByUserId(String userId);

    boolean addstreamUsers(StreamUsers streamUsers);

    boolean updateStreamUsers(StreamUsers streamUsers);

    boolean deleteStreamUsers(String id);

}
