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
package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.modules.sp.entity.A4User;
import org.springblade.modules.sp.entity.ServerAcl;
import org.springblade.modules.sp.entity.Users;
import org.springblade.modules.sp.service.IA4UserService;
import org.springblade.modules.sp.service.IServerAclService;
import org.springblade.modules.sp.service.IUsersService;
import org.springblade.modules.sp.service.UserEdgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Wbs任务表 服务实现类
 *
 * @author wangc
 * @since 2023-04-23
 */
@Service
@DS("postgresql")
public class UserEdgeServiceImpl implements UserEdgeService {
    @Autowired
    private IUsersService usersService;
    @Autowired
    private IServerAclService serverAclService;
    @Autowired
    private IA4UserService ia4UserService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean sync4aUser2Users() {
        /**
         * 4a用户列表
         * */
        List<A4User> a4UserList = ia4UserService.list();
        /**
         * 初始化插入对象
         * */
        List<Users> users = a4UserList.stream().map(A4User::transform).collect(Collectors.toList());
        /**
         * 需要进行对象对比，比对是否需要新增或更新
         * */
        List<ServerAcl> serverAcls = users.stream().map(Users::transform).collect(Collectors.toList());
        return usersService.saveBatch(users) && serverAclService.saveBatch(serverAcls);
    }
}

