package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.StreamUsers;


import java.util.List;


public interface StreamUsersService extends BaseService<StreamUsers> {

    StreamUsers getByStreamId(String streamId);

    StreamUsers getByUserId(String userId);

    boolean addstreamUsers(StreamUsers streamUsers);

    boolean updateStreamUsers(StreamUsers streamUsers);

    boolean deleteStreamUsers(String id);
}
