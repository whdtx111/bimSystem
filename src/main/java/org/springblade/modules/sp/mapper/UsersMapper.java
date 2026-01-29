/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.Users;
import org.springblade.modules.sp.vo.SearchUserVO;
import org.springblade.modules.sp.vo.SubscriptionUserVO;
import org.springblade.modules.sp.vo.UserWithRoleNameVO;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author wangc
 * @since 2023-09-13
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

    Users getById(String id);

    List<SearchUserVO> getUserByPhonOrEmailOrName(String str);

    List<Users> getUserByStreamId(String streamId);

    List<UserWithRoleNameVO> getUserByStreamIdWithRoleName(String streamId);

    boolean updateRole(String id,String[] roleIds);

    boolean removeRoleIdsByStreamId(String streamId);

    String getOwnerByTokenId(String tokenId);

    IPage<SubscriptionUserVO> filterSubscriptionUsers(IPage<SubscriptionUserVO> page,
                                                     @Param("filterInput") String filterInput,
                                                      @Param("isOrder") Integer isOrder,
                                                      @Param("startTime") String startTime,
                                                      @Param("endTime") String endTime);

    SubscriptionUserVO getSubscriptionUserById(@Param("id") String id);

    boolean updateSubscriptionUser(Users user);

    boolean batchDeleteSubscriptionUsers(@Param("ids") List<String> ids);

    boolean updateIsOrder(String id,Integer isOrder);

    Users getUserByPhone(String phone);
}
