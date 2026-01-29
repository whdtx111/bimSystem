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
package org.springblade.modules.sp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.Users;
import org.springblade.modules.sp.vo.SearchUserVO;
import org.springblade.modules.sp.vo.SubscriptionUserVO;
import org.springblade.modules.sp.vo.UserWithRoleNameVO;

import java.util.List;

/**
 * 服务类
 *
 * @author wangc
 * @since 2023-09-13
 */

public interface IUsersService extends IService<Users> {

    Users getById(String id);

    // sml 登录
    R spAuthLogin(String account, String password,String challenge);

    R<String> getOwnerByTokenId(String tokenId);

//    插件端登录
    R pluginAuthLogin(String accessCode,String challenge);
    // sml 退出
    R spLogOut(String token);

    // sml 注册
    R<String> spRegister(String email,String name, String password,String company,String phone,String challenge);

    // 初始化4a数据
    R spRegisterAll();

    // 随机头像
    R RandomAvatarAll();

    R<List<SearchUserVO>> userByPhoneName (String str);

    R importUser();

    List<Users> getUserByStreamId(String streamId);

    List<UserWithRoleNameVO> getUserByStreamIdWithRoleName(String streamId);

    boolean updateRole(String id,String[] roleIds);

    Users getUserByToken(String token);

    IPage<SubscriptionUserVO> filterSubscriptionUsers(String filterInput, Integer isOrder, String startTime, String endTime,
                                                      Integer pageSize, Integer currentPage);

    SubscriptionUserVO getSubscriptionUserById(@Param("id") String id);

    boolean updateSubscriptionUser(Users user);

    boolean batchDeleteSubscriptionUsers(@Param("ids") List<String> ids);

    boolean updateIsOrder(String id,Integer isOrder);

    Users getUserByPhone(String phone);
}
